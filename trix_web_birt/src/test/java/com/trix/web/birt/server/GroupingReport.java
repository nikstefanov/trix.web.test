package com.trix.web.birt.server;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.IScalarParameterDefn;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;

public class GroupingReport extends Test {
  protected Map<String,String[]> groupingsMap;
  protected ElementFactory elementFactory;
  protected TableHandle table;

  public GroupingReport() throws BirtException {
    super();
    groupingsMap = new HashMap<String,String[]>();
  }

  /**
   * Generates the report in HTML format. But first fills the map with grouping
   * information and adds groups in the table of the report. 
   */
  public void RunAndRender()
      throws EngineException, SemanticException {
    renderOptions.setOutputFormat("HTML");
    renderOptions.setOutputFileName(OUTPUT_FILE);
    IReportRunnable design = reportEngine.openReportDesign(DESIGN_FILE);
    
    //fills the map with grouping information
    getReportGropings(design, groupingsMap);
    //adds groups in the table of the report
    addGroups( (ReportDesignHandle) design.getDesignHandle( ), 
        Arrays.asList("ClinicGroupItem","DepartmentGroupItem","DoctorGroupItem"));    
    
    IRunAndRenderTask task = reportEngine.createRunAndRenderTask(design);    
    task.setRenderOption(renderOptions);
    task.run();
    task.close();  
  }
  
  /**
   * This method iterates the list of group items. It crates groups,
   * group header rows with needed data elements and design and adds them in the 
   * report table. Or sets the detail row data element for the last grouping
   * item. It also adds a group clause to the report sql query.
   * 
   * @param designHandle
   * @param groupingsList
   * @throws SemanticException
   */
  public void addGroups(ReportDesignHandle designHandle,
       List<String> groupingsList) throws SemanticException {
    elementFactory = designHandle.getElementFactory( );
    table = (TableHandle) designHandle.getBody().get(0);
    StringBuilder groupClause = new StringBuilder();
    String groupingName;
    TableGroupHandle tableGroup;    
    for(int index = 0; index < groupingsList.size(); index++) {
      groupingName = groupingsList.get(index);
      groupClause.append(',').append(groupingsMap.get(groupingName)[0]);
      if (index < groupingsList.size() - 1) {        
        tableGroup = elementFactory.newTableGroup( );
        designTableGroup(tableGroup,groupingName);
        table.getGroups().add(tableGroup);
        
        RowHandle grpHdrRow = elementFactory.newTableRow(table.getColumnCount());        
        designGroupHeaderRow(grpHdrRow, groupingName);
        tableGroup.getHeader().add(grpHdrRow);        
        copyFooterRowTotals(grpHdrRow, groupingName);
      } else {
        setFirstColumn(groupingName);       
      }
    }
    groupClause.deleteCharAt(0);
    addGroupClause(designHandle, groupClause.toString());
  }
  
  /**
   * Sets the properties of the new table group.
   * 
   * @param tableGroup
   * @param groupingName
   * @throws SemanticException
   */
  public void designTableGroup(TableGroupHandle tableGroup, String groupingName)
      throws SemanticException {
    tableGroup.setKeyExpr(
        "row[\"" + groupingsMap.get(groupingName)[1] + "\"]");
    tableGroup.setName(groupingName);
    tableGroup.setInterval("none");
    tableGroup.setSortDirection(DesignChoiceConstants.SORT_DIRECTION_ASC);
    tableGroup.setRepeatHeader(true);
    tableGroup.setHideDetail(false);
  }
  
  /**
   * Sets the color of the group header row and a data-element,
   * representing the value of the grouping item.
   *  
   * @param grpHdrRow
   * @param groupingName
   * @throws SemanticException
   */
  public void designGroupHeaderRow(RowHandle grpHdrRow, String groupingName)
      throws SemanticException {
    grpHdrRow.setProperty("backgroundColor", "#F0A0A0");
    CellHandle tblCellHandle = (CellHandle)   grpHdrRow.getCells().get(0);
    DataItemHandle grpHdrDataItem = elementFactory.newDataItem(null);
    grpHdrDataItem.setResultSetColumn(groupingsMap.get(groupingName)[2]);
    tblCellHandle.getContent().add(grpHdrDataItem);
  }
  
  /**
   * Makes and adds an aggregation data elements in the group header row.
   * It takes the aggregation data elements in the table footer row and
   * sets to be aggregated on the newly created group, referenced by its name.
   *  
   * @param grpHdrRow
   * @param groupingName
   * @throws SemanticException
   */
  public void copyFooterRowTotals(RowHandle grpHdrRow, String groupingName)
      throws SemanticException {
    RowHandle footerRow = (RowHandle) table.getFooter( ).get( 0 );       
    for (int cellNumber = 1; cellNumber < table.getColumnCount();
        cellNumber++) {          
      CellHandle footerCell =
          (CellHandle) footerRow.getCells().get(cellNumber);
      SlotHandle footerCellSlotHandle = footerCell.getContent();
      if (footerCellSlotHandle.getCount() == 1) {
        DataItemHandle footerDataItem =
            (DataItemHandle) footerCellSlotHandle.get(0);
        String footerDataColumnBinding = footerDataItem.getResultSetColumn();
        ComputedColumnHandle footerCCHandle =
            table.findColumnBinding(footerDataColumnBinding);
        ComputedColumn footerCC =
            (ComputedColumn) footerCCHandle.getStructure();
        ComputedColumn grpHdrCC = (ComputedColumn) footerCC.copy();
        grpHdrCC.setName(footerCCHandle.getName()+'_'+groupingName);
        grpHdrCC.setAggregateOn(groupingName);
        table.addColumnBinding(grpHdrCC, false);
        DataItemHandle grpHdrDataItem = elementFactory.newDataItem(null);
        grpHdrDataItem.setResultSetColumn(grpHdrCC.getName());
        CellHandle tblCellHandle =
            (CellHandle) grpHdrRow.getCells().get(cellNumber);
        tblCellHandle.getContent().add(grpHdrDataItem);
      }
    }
  }
  
  /**
   * Sets the values of the label the header row, column 0 and the data-element
   * in the detail row, column 0.
   * These are filled according to the last grouping item.
   * 
   * @param table         table design
   * @param groupingName  the name of the grouping
   * @throws SemanticException
   */
  public void setFirstColumn(String groupingName)
      throws SemanticException{
    RowHandle tablerow = (RowHandle) table.getHeader( ).get( 0 );
    CellHandle cell = (CellHandle) tablerow.getCells( ).get( 0 );
    LabelHandle  label = (LabelHandle) cell.getContent( ).get( 0 );
    label.setText(groupingsMap.get(groupingName)[3]);
    
    tablerow = (RowHandle) table.getDetail( ).get( 0 );
    cell = (CellHandle) tablerow.getCells( ).get( 0 );
    DataItemHandle data = (DataItemHandle) cell.getContent( ).get( 0 );
    data.setResultSetColumn(groupingsMap.get(groupingName)[2]);
  }
  
  /**
   * Adds a group clause in the query, replacing a place-holder.
   * 
   * @param designHandle
   * @param groupClause
   * @throws SemanticException
   */
  public void addGroupClause(ReportDesignHandle designHandle,
      String groupClause) throws SemanticException {
    OdaDataSetHandle odaDataSetHandle =
        (OdaDataSetHandle)designHandle.getDataSets().get(0);
    String query = odaDataSetHandle.getQueryText();
    query = query.replace("#groupClause", "GROUP BY " + groupClause.toString());
    odaDataSetHandle.setQueryText(query);
  }
  
  /** 
   * Fills the supplied Map with the key property values from
   * grouping parameters.
   * 
   * @param design       the design of the report
   * @param groupingsMap the map that is filled.
   *                      The format is Map<String,String[4]> 
   */
  public void getReportGropings(IReportRunnable design,
      Map<String,String[]> groupingsMap) {
    IGetParameterDefinitionTask taskPr =
        reportEngine.createGetParameterDefinitionTask( design );
    @SuppressWarnings("rawtypes")
    Iterator iter = taskPr.getParameterDefns( false ).iterator( );
    String[] columns;
    while ( iter.hasNext( ) )
    {
      IScalarParameterDefn param = (IScalarParameterDefn) iter.next( );     
      if (param.getUserPropertyValue("IsGroup") != null &&
          param.getUserPropertyValue("IsGroup").equals("true")) {
        columns = new String[4];
        columns[0] = param.getUserPropertyValue("Column");
        columns[1] = param.getUserPropertyValue("ColumnBinding");
        columns[2] = param.getUserPropertyValue("ColumnName");
        columns[3] = param.getPromptText();        
        groupingsMap.put(param.getName(), columns);
      }
    }
  }
}
