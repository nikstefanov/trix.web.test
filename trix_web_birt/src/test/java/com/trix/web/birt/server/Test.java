package com.trix.web.birt.server;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IParameterDefn;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.api.IScalarParameterDefn;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;

import com.trix.birt.share.dto.FilterItemDto;
import com.trix.birt.share.dto.ReportDto;

public class Test {  
 
  protected static String TEMP_LOCATION =
      "D:/Users/User/Development/Temp/ReportHandler";
  protected static String DESIGN_FILE =
      "D:/Users/User/Development/Workspaces/trix.birt/"
          + "test_birt_report/SimpleTableMySQL.rptdesign";
//    "D:/Users/User/Development/Workspaces/trix.birt/"
//    + "test_birt_report/HillClinic/PatientsGrouped.rptdesign";
  protected static String DOCUMNET_FILE =
      "D:/Users/User/Development/Workspaces/trix.birt/"
          + "test_birt_report/SimpleTable.rptdocument";
  protected static String OUTPUT_FILE =
//      "D:/Users/User/Development/Workspaces/trix.birt/"
//          + "test_birt_report/SimpleTable.html";
      "D:/Users/User/Development/Projects/HillClinicReports/"
      + "Generated Reports/PatientsGrouped_test.html";
  protected static Map<Integer,String> paramDataTypeMap;
  protected IReportEngine reportEngine;
  protected IRenderOption renderOptions;
  
  static{
    paramDataTypeMap = new HashMap<Integer,String>(9);
    paramDataTypeMap.put(IParameterDefn.TYPE_ANY, "Any");
    paramDataTypeMap.put(IParameterDefn.TYPE_STRING, "String");
    paramDataTypeMap.put(IParameterDefn.TYPE_FLOAT, "Float");
    paramDataTypeMap.put(IParameterDefn.TYPE_DECIMAL, "Decimal");
    paramDataTypeMap.put(IParameterDefn.TYPE_DATE_TIME, "Date Time");    
    paramDataTypeMap.put(IParameterDefn.TYPE_BOOLEAN, "Boolean");
    paramDataTypeMap.put(IParameterDefn.TYPE_INTEGER, "Integer");
    paramDataTypeMap.put(IParameterDefn.TYPE_DATE, "Date");
    paramDataTypeMap.put(IParameterDefn.TYPE_TIME, "Time");    
  }
  
  public Test() throws BirtException {
    init();
    renderOptions = new RenderOption();    
  }
  
  public void init() throws BirtException {
    final EngineConfig config = new EngineConfig( );
    config.setLogConfig(TEMP_LOCATION, Level.OFF);    
    Platform.startup( config );
    IReportEngineFactory factory =
        (IReportEngineFactory) Platform.createFactoryObject(
            IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );
    reportEngine = factory.createReportEngine( config );
  }
  
  public void reportRun() throws Exception {    
    IReportRunnable design = reportEngine.openReportDesign(DESIGN_FILE);    
    IRunTask task = reportEngine.createRunTask(design);
    task.run(DOCUMNET_FILE);
    task.close();
  }
  
  public void reportRender() throws Exception {    
    IReportDocument reportDocument =
        reportEngine.openReportDocument(DOCUMNET_FILE);
    IRenderTask task = reportEngine.createRenderTask(reportDocument);   
    task.setRenderOption(renderOptions);
    task.render();
    task.close();
    reportDocument.close();
  }
  
  public void reportRunAndRender() throws Exception {
    IReportRunnable runnable = reportEngine.openReportDesign(DESIGN_FILE);
    IRunAndRenderTask task = reportEngine.createRunAndRenderTask(runnable);    
    task.setRenderOption(renderOptions);
    task.run();
    task.close();
  }
  
  public void setRenderOption(String key, Object value) {
    renderOptions.setOption(key, value);
  }
  
  public void exit() {
    reportEngine.destroy();
    Platform.shutdown();  
  }

  public void printReportDto(ReportDto reportDto) {    
    System.out.println("Title:"+reportDto.getTitle());
    System.out.println();
  }
  
  public void runPojoEmitterTest() throws Exception {
    ReportDto report = new ReportDto();
    renderOptions.setOutputFormat("POJO");
    renderOptions.setOption("pojo", report);
    reportRun();
    reportRender();
    printReportDto(report);    
  }
  
  public void runHtmlEmitter() throws Exception{
    renderOptions.setOutputFormat("HTML");
    renderOptions.setOutputFileName(OUTPUT_FILE);
    reportRun();
    reportRender();
  }
  
  public void addFilterCondition(ReportDesignHandle designHandle)
      throws SemanticException {
    DataSetHandle dataSetHandle =
        (DataSetHandle)designHandle.getDataSets().get(0);
    FilterCondition filterCondition = StructureFactory.createFilterCond();
    filterCondition.setExpr(
        "row['officeCode']=='1'||row['officeCode']=='2'||row['city']=='Paris'"); 
    filterCondition.setOperator(DesignChoiceConstants.FILTER_OPERATOR_TRUE);
//    filterCondition.setValue1("4");
    dataSetHandle.addFilter(filterCondition);
//    designHandle.saveAs( "c:/temp/sample.rptdesign" ); //$NON-NLS-1$
  }
  
  public void addFilterRunAndRender()
      throws EngineException, SemanticException {
    renderOptions.setOutputFormat("HTML");
    renderOptions.setOutputFileName(OUTPUT_FILE);
    IReportRunnable design = reportEngine.openReportDesign(DESIGN_FILE);     
    addFilterCondition( (ReportDesignHandle) design.getDesignHandle( ) );
    IRunAndRenderTask task = reportEngine.createRunAndRenderTask(design);    
    task.setRenderOption(renderOptions);
    task.run();
    task.close();  
  }
  
  public void changeQueryText(ReportDesignHandle designHandle)
      throws SemanticException {
    OdaDataSetHandle odaDataSetHandle =
        (OdaDataSetHandle)designHandle.getDataSets().get(1);
    String query = odaDataSetHandle.getQueryText();
    query = query.concat(" WHERE officeCode=1 OR officeCode=2 OR city='Paris'");
    odaDataSetHandle.setQueryText(query);
  }
  
  public void changeQueryRunAndRender()
      throws EngineException, SemanticException {
    renderOptions.setOutputFormat("HTML");
    renderOptions.setOutputFileName(OUTPUT_FILE);
    IReportRunnable design = reportEngine.openReportDesign(DESIGN_FILE);
    changeQueryText( (ReportDesignHandle) design.getDesignHandle( ) );
    IRunAndRenderTask task = reportEngine.createRunAndRenderTask(design);
    task.setRenderOption(renderOptions);
    task.run();
    task.close();
  }
  
  public void getAllFilterProperties(ReportDesignHandle designHandle,
      ReportDto report) throws SemanticException {
//    for (Object object:designHandle.getAllParameters()) {
//      ScalarParameterHandle param = (ScalarParameterHandle) object;
     
//    }
  }
  
  @SuppressWarnings("rawtypes")
  public void extractParameterProperties(Collection collection,
      ReportDto report) {    
    Iterator iter = collection.iterator( );
    FilterItemDto filterItemDto;
    while ( iter.hasNext( ) )
    {
      IScalarParameterDefn param = (IScalarParameterDefn) iter.next( );     
      if (param.getUserPropertyValue("IsFilter").equals("true")) {
        filterItemDto = new FilterItemDto();
        filterItemDto.setColumnName(param.getUserPropertyValue("ColumnName"));
        filterItemDto.setTrixId(param.getUserPropertyValue("TrixId"));
        filterItemDto.setName(param.getName());
        filterItemDto.setTitle(param.getPromptText());
        filterItemDto.setType(paramDataTypeMap.get(param.getDataType()));
        report.addFilterItem(filterItemDto);
      }
    }
  }
  
  public void pojoGetParametersRunAndRender() throws EngineException {
    ReportDto report = new ReportDto();
    renderOptions.setOutputFormat("POJO");
    renderOptions.setOption("pojo", report);    
    IReportRunnable design = reportEngine.openReportDesign(DESIGN_FILE);
    IGetParameterDefinitionTask taskPr =
        reportEngine.createGetParameterDefinitionTask( design );   
    extractParameterProperties(taskPr.getParameterDefns( false ), report);
    IRunAndRenderTask task = reportEngine.createRunAndRenderTask(design);    
    task.setRenderOption(renderOptions);
    task.run();
    task.close();
  }
  public void runXlsxEmitter() throws EngineException, SemanticException {
    renderOptions.setOutputFormat("XLSX");
    renderOptions.setOutputFileName(OUTPUT_FILE);
    renderOptions.setOption("ExcelEmitter.SingleSheetWithPageBreaks", true);
    IReportRunnable runnable = reportEngine.openReportDesign(DESIGN_FILE);
    ReportDesignHandle designHandle = (ReportDesignHandle) runnable.getDesignHandle();
    DesignElementHandle designElement = designHandle.getBody().get(0);
    if( designElement instanceof TableHandle ) {
      ((TableHandle)designElement).setPageBreakInterval(0);
      ((TableHandle)designElement).setRepeatHeader(false);
    }
    IRunAndRenderTask task = reportEngine.createRunAndRenderTask(runnable);    
    task.setRenderOption(renderOptions);
    task.run();
    task.close();
  }
  
  public void runPdfEmitter() throws EngineException {
    renderOptions.setOutputFormat("PDF");
    renderOptions.setOutputFileName(OUTPUT_FILE);
    IReportRunnable runnable = reportEngine.openReportDesign(DESIGN_FILE);
    IRunAndRenderTask task = reportEngine.createRunAndRenderTask(runnable);    
    task.setRenderOption(renderOptions);
    task.run();
    task.close();
  }
  
  public void addGroupInTable(ReportDesignHandle designHandle)
      throws SemanticException {
    ElementFactory elementFactory = designHandle.getElementFactory( );
    TableHandle table = (TableHandle) designHandle.getBody().get(0);
    TableGroupHandle group = elementFactory.newTableGroup( );
    group.setKeyExpr("row[\"clinicId Binding\"]");
    group.setName("DinamicGroup");
    group.setInterval("none");
    group.setSortDirection(DesignChoiceConstants.SORT_DIRECTION_ASC);
    group.setRepeatHeader(true);
    group.setHideDetail(false);
    table.getGroups().add(group);
    
    RowHandle grpHdrHandle = elementFactory.newTableRow(table.getColumnCount());
    grpHdrHandle.setProperty("backgroundColor", "orange");
    CellHandle tblCellHandle = (CellHandle)   grpHdrHandle.getCells().get(0);
//    tblCellHandle.setDrop(DesignChoiceConstants.DROP_TYPE_DETAIL);
    DataItemHandle data0 = elementFactory.newDataItem(null);
    data0.setResultSetColumn("clinicId Binding"); 
    tblCellHandle.getContent().add(data0);
    group.getHeader().add(grpHdrHandle);
  }
  
  public void RunAndRender() throws  Exception {
    renderOptions.setOutputFormat("HTML");
    renderOptions.setOutputFileName(OUTPUT_FILE);
    IReportRunnable design = reportEngine.openReportDesign(DESIGN_FILE);
    
//    addFilterCondition( (ReportDesignHandle) design.getDesignHandle( ) );
//    addGroupInTable((ReportDesignHandle) design.getDesignHandle( ));
    HashMap<String,String[]> groupingsMap = new HashMap<String,String[]>();
    getReportGropings(design, groupingsMap);
    addGroups( (ReportDesignHandle) design.getDesignHandle( ), groupingsMap,
        Arrays.asList("ClinicGroupItem","DepartmentGroupItem","DoctorGroupItem"));    
    
    IRunAndRenderTask task = reportEngine.createRunAndRenderTask(design);    
    task.setRenderOption(renderOptions);
    task.run();
    task.close();  
  }
  
  public void addGroups(ReportDesignHandle designHandle,
      Map<String,String[]> groupingsMap, List<String> groupingsList)
          throws SemanticException {
    StringBuilder groupClause = new StringBuilder();
    String groupingName;
    TableGroupHandle tableGroup;
    ElementFactory elementFactory = designHandle.getElementFactory( );
    TableHandle table = (TableHandle) designHandle.getBody().get(0);
    for(int index = 0; index < groupingsList.size(); index++) {
      groupingName = groupingsList.get(index);
      groupClause.append(',').append(groupingsMap.get(groupingName)[0]);
      if (index < groupingsList.size() - 1) {        
        tableGroup = elementFactory.newTableGroup( );
        tableGroup.setKeyExpr(
            "row[\"" + groupingsMap.get(groupingName)[1] + "\"]");
        tableGroup.setName(groupingName);
        tableGroup.setInterval("none");
        tableGroup.setSortDirection(DesignChoiceConstants.SORT_DIRECTION_ASC);
        tableGroup.setRepeatHeader(true);
        tableGroup.setHideDetail(false);
        table.getGroups().add(tableGroup);
        
        RowHandle grpHdrRow = elementFactory.newTableRow(table.getColumnCount());
        switch(index){
        case 0:grpHdrRow.setProperty("backgroundColor", "#F0A0A0");break;
        case 1:grpHdrRow.setProperty("backgroundColor", "#F0C0C0");break;
        case 2:grpHdrRow.setProperty("backgroundColor", "#F0E0E0");break;
        case 3:grpHdrRow.setProperty("backgroundColor", "#F0FFFF");break;
        }
        CellHandle tblCellHandle = (CellHandle)   grpHdrRow.getCells().get(0);
//        tblCellHandle.setDrop(DesignChoiceConstants.DROP_TYPE_DETAIL);
        DataItemHandle grpHdrDataItem = elementFactory.newDataItem(null);
        grpHdrDataItem.setResultSetColumn(groupingsMap.get(groupingName)[2]);
        tblCellHandle.getContent().add(grpHdrDataItem);
        tableGroup.getHeader().add(grpHdrRow);
        
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
            grpHdrDataItem = elementFactory.newDataItem(null);
            grpHdrDataItem.setResultSetColumn(grpHdrCC.getName());
            tblCellHandle = (CellHandle) grpHdrRow.getCells().get(cellNumber);
            tblCellHandle.getContent().add(grpHdrDataItem);            
//            System.out.println(grpHdrCC.getName());
            
//            @SuppressWarnings("rawtypes")
//            Iterator computedSetIterator = table.columnBindingsIterator( ); 
//            while(computedSetIterator.hasNext()) {
//              ComputedColumnHandle cch =
//                  (ComputedColumnHandle) computedSetIterator.next();
//              if (cc.getName().equals(footerDataColumnBinding)) {
//                ComputedColumn newCC = (ComputedColumn) cc.copy();
//                newCC.setName(cc.getName()+'_'+groupingName);
//                newCC.setAggregateOn(groupingName);
//                table.getColumnBindings().addItem(newCC);
//
//              }
//            }
          }
        }
      } else {
        RowHandle tablerow = (RowHandle) table.getHeader( ).get( 0 );
        CellHandle cell = (CellHandle) tablerow.getCells( ).get( 0 );
        LabelHandle  label = (LabelHandle) cell.getContent( ).get( 0 );
        label.setText(groupingsMap.get(groupingName)[3]);
        
        tablerow = (RowHandle) table.getDetail( ).get( 0 );
        cell = (CellHandle) tablerow.getCells( ).get( 0 );
        DataItemHandle data = (DataItemHandle) cell.getContent( ).get( 0 );
        data.setResultSetColumn(groupingsMap.get(groupingName)[2]);
      }
    }
    groupClause.deleteCharAt(0);
    OdaDataSetHandle odaDataSetHandle =
        (OdaDataSetHandle)designHandle.getDataSets().get(0);
    String query = odaDataSetHandle.getQueryText();
    query = query.replace("#groupClause", "GROUP BY " + groupClause.toString());
    odaDataSetHandle.setQueryText(query);
  }
  
  
  
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
  
  public void extractParametrs() throws EngineException{
    IReportRunnable design = reportEngine.openReportDesign(DESIGN_FILE);
    IGetParameterDefinitionTask taskPr =
        reportEngine.createGetParameterDefinitionTask( design );
    @SuppressWarnings("rawtypes")
    Iterator iter = taskPr.getParameterDefns( false ).iterator( );
    while ( iter.hasNext( ) )
    {
      IScalarParameterDefn param = (IScalarParameterDefn) iter.next( );
      System.out.println("parameter: "+param.getName());
      System.out.println(param.getUserPropertyValues().toString());
    }
  }
  
  public void extractReportElementBindings() throws EngineException {
    IReportRunnable design = reportEngine.openReportDesign(DESIGN_FILE);
    ReportDesignHandle designHandle =
        (ReportDesignHandle) design.getDesignHandle( );
    TableHandle table = (TableHandle) designHandle.getBody().get(0);
    @SuppressWarnings("rawtypes")
    Iterator computedSetIterator =
//        table.getAvailableBindings( );
        table.columnBindingsIterator();
    while(computedSetIterator.hasNext()) {
      ComputedColumnHandle cc = (ComputedColumnHandle) computedSetIterator.next();
      System.out.println(cc.getName());
    }
  }
  
  public void printMainElement() throws EngineException {
    IReportRunnable design = reportEngine.openReportDesign(DESIGN_FILE);
    ReportDesignHandle designHandle =
        (ReportDesignHandle) design.getDesignHandle( );
    DesignElementHandle elementHandle = designHandle.getBody().get(0);
    System.out.println("Main class: " + elementHandle.getClass().getName());
  }
  
  public static void main(String[] args) {
    try{
      Test test = new Test();
//      test.runPojoEmitterTest();
//      test.runHtmlEmitter();
//      test.addFilterRunAndRender();
//      test.changeQueryRunAndRender();
//      test.pojoGetParametersRunAndRender();
//      test.runXlsxEmitter();
//      test.runPdfEmitter();
//      test.RunAndRender();--
      test.extractParametrs();
//    test.extractReportElementBindings();
//      test.printMainElement();
      test.exit();     
    }catch(Exception ex){
      ex.printStackTrace();
    }
  }
}
