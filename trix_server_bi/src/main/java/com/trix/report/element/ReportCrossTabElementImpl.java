package com.trix.report.element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.model.api.ColumnHintHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;
import org.eclipse.birt.report.model.api.olap.TabularLevelHandle;

import com.trix.report.ReportElement;
import com.trix.report.ReportElementConfiguration;
import com.trix.report.element.utilities.FilterUtilities;

public class ReportCrossTabElementImpl extends ReportElement {
  private ElementFactory designFactory;
  private CrosstabReportItemHandle xtabHandle; 
  private TabularCubeHandle cubeHandle;
  private OdaDataSetHandle odaDataSetHandle;
  private List<String> xtColumns;
  private List<String> xtRows;
  private List<String> xtSummaries;
  private List<String> rowLevelLabels;
  private List<String> columnLevelLabels;
  private String lastColumnLevelQualifiedName,lastRowLevelQualifiedName;
  private StringBuilder groupClauseBuilder;
  
  public ReportCrossTabElementImpl() {
    groupClauseBuilder = new StringBuilder();
    rowLevelLabels = new ArrayList<String>();
    columnLevelLabels = new ArrayList<String>();
  }
  
  @Override
  public void configure(ReportElementConfiguration reportElementConfiguration) 
  throws Exception {
//    System.out.println(elementHandle.getName());
    ExtendedItemHandle xtab = (ExtendedItemHandle) elementHandle;
    designFactory = elementHandle.getElementFactory();
    IReportItem reportItem = xtab.getReportItem();
    xtabHandle = (CrosstabReportItemHandle) reportItem;
    cubeHandle = (TabularCubeHandle) xtab.getCube();
    odaDataSetHandle = (OdaDataSetHandle) cubeHandle.getDataSet();
    xtColumns = reportElementConfiguration.getXtColumns();
    xtRows = reportElementConfiguration.getXtRows();
    xtSummaries = reportElementConfiguration.getXtSummaries();    
    cubeHandle.setAutoPrimaryKey(true);
    dimensions();
    measures();
    headers();
    if (groupClauseBuilder.length() > 0) {
      setGroupClause(groupClauseBuilder.deleteCharAt(0).toString());
    }    
    // filter conditions
    FilterUtilities filterUtilities = new FilterUtilities(odaDataSetHandle,
        reportElementConfiguration.getCriteriaJSON());
    filterUtilities.applyJSONFilterConditions();
  }
  
  private void dimensions() throws Exception {      
    for(int i = 0; i < xtRows.size(); i++) {
      String rowDimentionName = xtRows.get(i);
//      System.out.println("Row:"+rowDimentionName);
      insertDimension(cubeHandle.getDimension(rowDimentionName),
          ICrosstabConstants.ROW_AXIS_TYPE, i);
    }
    for(int i = 0; i < xtColumns.size(); i++) {
      String colDimentionName = xtColumns.get(i);
//      System.out.println("Column:"+colDimentionName);
      insertDimension(cubeHandle.getDimension(colDimentionName),
          ICrosstabConstants.COLUMN_AXIS_TYPE, i);
    }    
  }
  
  private void insertDimension(DimensionHandle dimension, int axisType,
      int index) throws Exception {
//    System.out.println(dimension.getName());
    DimensionViewHandle dvh =
        xtabHandle.insertDimension(dimension, axisType, index);
    HierarchyHandle hierarchy = dimension.getDefaultHierarchy();
    @SuppressWarnings("rawtypes")
    List levelList =
        hierarchy.getPropertyHandle(TabularHierarchyHandle.LEVELS_PROP)
        .getContents();
    for(Object obj:levelList) {
      TabularLevelHandle levelHandle = (TabularLevelHandle) obj;
      if (ICrosstabConstants.ROW_AXIS_TYPE == axisType) {
        rowLevelLabels.add(levelHandle.getName());
        lastRowLevelQualifiedName = levelHandle.getQualifiedName();
      } else {
        columnLevelLabels.add(levelHandle.getName());
        lastColumnLevelQualifiedName = levelHandle.getQualifiedName();
      }
      LevelViewHandle levelViewHandle = dvh.insertLevel(levelHandle, -1);  
      
      CrosstabCellHandle cellHandle = levelViewHandle.getCell();

      DesignElementHandle eii = xtabHandle.getModelHandle();

      //level computed column
      ComputedColumn bindingColumn =
          StructureFactory.newComputedColumn(eii, levelHandle.getName());
      ComputedColumnHandle bindingHandle =
          ((ReportItemHandle) eii).addColumnBinding( bindingColumn,false );
      bindingColumn.setDataType(levelHandle.getDataType());
      String exp ="dimension[\"" + dimension.getName()+"\"]"
          + "[\""+levelHandle.getName()+"\"]";
      bindingColumn.setExpression(exp);

      //add data element for level
      DataItemHandle dataHandle = designFactory.newDataItem(
          levelHandle.getName());
      dataHandle.setResultSetColumn(bindingHandle.getName());
      cellHandle.addContent(dataHandle);
      
      groupClauseBuilder.append(',');
      groupClauseBuilder.append(findColumnAlias(levelHandle.getColumnName()));
    }
  }
  
  private void measures()
      throws SemanticException {
    for(int i = 0; i < xtSummaries.size(); i++) {
      String measureGroupName = xtSummaries.get(i);
      insertMeasureGroup(measureGroupName);          
    }
  }
  
  @SuppressWarnings("rawtypes")
  private void insertMeasureGroup(String measureGroupName)
          throws SemanticException {    
    List measureGroups = cubeHandle.getContents(CubeHandle.MEASURE_GROUPS_PROP);
    for ( int i = 0; i < measureGroups.size(); i++ ) {
      MeasureGroupHandle mgh = (MeasureGroupHandle) measureGroups.get(i);
      if (mgh.getName().equals(measureGroupName)) {
        List measureGroup = mgh.getContents(MeasureGroupHandle.MEASURES_PROP);
        for (int j = 0; j < measureGroup.size(); j++)
        {
          MeasureHandle measure = (MeasureHandle) measureGroup.get(j);
          MeasureViewHandle mvh =
              xtabHandle.insertMeasure(measure, -1);
          mvh.addHeader();
          LabelHandle labelHandle = designFactory.newLabel( null );
          labelHandle.setText(measure.getDisplayName());
          mvh.getHeader().addContent(labelHandle);
          
          AggregationCellHandle cellHandle = mvh.getCell();
          ExtendedItemHandle xtModelHandle =
              (ExtendedItemHandle) xtabHandle.getModelHandle();
          
        //level computed column
          ComputedColumn bindingColumn =
              StructureFactory.newComputedColumn(
                  xtModelHandle, measure.getName()+"cc");
          ComputedColumnHandle bindingHandle =
              ((ReportItemHandle) xtModelHandle)
              .addColumnBinding(bindingColumn, false);
          bindingColumn.setDataType( measure.getDataType());
          bindingColumn.setAggregateFunction(measure.getFunction());
          String exp = "measure[\"" + measure.getName()+"\"]";
          bindingColumn.setExpression(exp);
          bindingColumn.addAggregateOn(lastRowLevelQualifiedName);
          bindingColumn.addAggregateOn(lastColumnLevelQualifiedName);
          
        //add data element for level
          DataItemHandle dataHandle = designFactory.newDataItem(
              measure.getName()+"data");
          dataHandle.setResultSetColumn( bindingHandle.getName());
          cellHandle.addContent(dataHandle);
        }
      }
    }
  }
  
  private void headers() throws SemanticException {
    LabelHandle labelHandle;
    int rowLevelCount = rowLevelLabels.size();
    int columnLevelCount = columnLevelLabels.size();
    for (int i = 0; i < rowLevelCount; i++) {
      labelHandle = designFactory.newLabel(rowLevelLabels.get(i)+"Label");
      labelHandle.setText(rowLevelLabels.get(i));
      xtabHandle.getHeader(rowLevelCount * columnLevelCount + i)
      .addContent(labelHandle);
    }
    for (int i = 0; i < columnLevelCount; i++) {
      labelHandle = designFactory.newLabel(columnLevelLabels.get(i)+"Label");
      labelHandle.setText(columnLevelLabels.get(i));
      xtabHandle.getHeader(rowLevelCount * (i + 1) - 1).addContent(labelHandle);
    }
  }
  
  private void setGroupClause(String groupClauseString)
      throws SemanticException {
//    System.out.println("Group by "+groupClauseString);    
    String query = odaDataSetHandle.getQueryText();
    query = query.replace("#groupClause", "GROUP BY " + groupClauseString);
    odaDataSetHandle.setQueryText(query);    
  }
  
  private String findColumnAlias(String colName) throws Exception {    
    @SuppressWarnings("rawtypes")
    Iterator columnHintIter = odaDataSetHandle.columnHintsIterator();     
    if (columnHintIter != null) {
      while (columnHintIter.hasNext()) {     
        ColumnHintHandle columnHintHandle = (ColumnHintHandle) columnHintIter.next();
        if (columnHintHandle.getColumnName().equals(colName)) {
          return columnHintHandle.getHeading();
        }
      }
    }
    throw new Exception(
        "Cann't find proper column alias for result set column: " + colName);
  }
  
}
