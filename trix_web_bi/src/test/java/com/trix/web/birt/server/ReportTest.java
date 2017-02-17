package com.trix.web.birt.server;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;

import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.model.api.ColumnHintHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.trix.report.Report;
import com.trix.report.ReportConfiguration;
import com.trix.report.ReportElementConfiguration;
import com.trix.report.ReportEngine;
import com.trix.report.ReportFactory;
import com.trix.report.cache.RptDocumentCache;
import com.trix.report.dto.ReportDto;
import com.trix.report.dto.ReportElementDto;
import com.trix.report.repository.ReportLocator;
import com.trix.report.repository.ResourceLocator;
import com.trix.report.repository.impl.ReportRepositoryImpl;
import com.trix.report.repository.source.SourceStreamLocator;
import com.trix.report.repository.source.impl.SourceStreamLocatorFileImpl;

public class ReportTest {
  private static String jsonStringCriteria =
      " {"+
    " \"_constructor\":\"AdvancedCriteria\", "+
    " \"operator\":\"or\", "+
    " \"criteria\":["+
    "     {"+
    "         \"fieldName\":\"officeCode\","+ 
    "         \"operator\":\"iContains\", "+
    "         \"value\":\"1\""+
    "     }, "+
    "     {"+
    "         \"fieldName\":\"officeCode\", "+
    "         \"operator\":\"iContains\", "+
    "         \"value\":\"3\""+
    "     }, "+
    "     {"+
    "         \"fieldName\":\"officeCode\", "+
    "         \"operator\":\"iEquals\", "+
    "         \"value\":\"2\""+
    "     }, "+
    "     {"+
    "        \"_constructor\":\"AdvancedCriteria\", "+
    "        \"operator\":\"or\", "+
    "        \"criteria\":["+
    "           {"+
    "                 \"fieldName\":\"officeCode\", "+
    "                 \"operator\":\"greaterThan\","+ 
    "                 \"value\":\"5\""+
    "             }"+
    "         ]"+
    "     }"+
    "]"+
    "}";
  private static String jsonStringCrossTab =
    " {"+
    " \"_constructor\":\"AdvancedCriteria\", "+
    " \"operator\":\"and\", "+
    " \"criteria\":[]}";

  private ReportFactory reportFactory;
  private ReportRepositoryImpl reportRepository;
  
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    ReportEngine.start();
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
    ReportEngine.stop();
  }
  
  @Before
  public void setUpBeforeTest() {   
    String tempDir = System.getProperty("java.io.tmpdir");
    if (tempDir.charAt(tempDir.length()-1) != File.separatorChar) {
      tempDir = tempDir +File.separatorChar;
    }
    String cacheDirectory = tempDir + "rptCacheDirectory";
    File cacheDirFile = new File(cacheDirectory);
    cacheDirFile.mkdir();
    RptDocumentCache rptDocumentCache =
        new RptDocumentCache(cacheDirectory, 1000,1000);
    reportRepository = new ReportRepositoryImpl();
    reportRepository.addFileName("CrossTabExample",
        "CrossTabExample.rptdesign");
    reportRepository.addFileName("PatientsCrossTab",
        "PatientsCrossTab.rptdesign");
    reportRepository.addFileName("SimpleTableFilters",        
        "SimpleTableFilters.rptdesign");
    reportRepository.addFileName("AppointmentRequests",       
        "AppointmentRequests.rptdesign");
    String baseDir =
        ReportTest.class.getResource("/com/trix/report/test/reports").getFile();
    SourceStreamLocator sourceStreamLocator =
        new SourceStreamLocatorFileImpl(baseDir);
    ReportLocator reportLocator = new ReportLocator(sourceStreamLocator);
    ResourceLocator resourceLocator = new ResourceLocator(sourceStreamLocator);    
    reportFactory = new ReportFactory(reportRepository, rptDocumentCache,
        reportLocator, resourceLocator);
  }

  @Test
  public void testCrossTab() throws Exception{
    ReportElementConfiguration elementCfg = new ReportElementConfiguration();
    elementCfg.setCriteriaJSON(jsonStringCrossTab);
    elementCfg.addXtColumn("ClinicGroup");
    elementCfg.addXtColumn("DepartmentGroup");
    elementCfg.addXtRow("YearGroup");
    elementCfg.addXtSummary("Summary Patients Count");
    ReportConfiguration reportCfg = new ReportConfiguration();
    reportCfg.addElementConfiguration("PatientsCrossTab", elementCfg);
    
    Report report = reportFactory.create("PatientsCrossTab");
    report.execute(reportCfg, "HTML", new FileOutputStream(
        "D:\\Users\\User\\Development\\Temp\\PatientsCrossTab.html"));
  }
  
  @Test
  public void testFilters() throws Exception {
    ReportElementConfiguration elementCfg = new ReportElementConfiguration();
    elementCfg.setCriteriaJSON(jsonStringCriteria);
    ReportConfiguration reportCfg = new ReportConfiguration();
    reportCfg.addElementConfiguration("SimpleTable", elementCfg);
    
    Report report = reportFactory.create("SimpleTableFilters");
    report.execute(reportCfg, "HTML", new FileOutputStream(
        "D:\\Users\\User\\Development\\Temp\\SimpleTableFilters.html"));
  }
  
  @Test
  @SuppressWarnings("deprecation")
  public void testColumns() throws Exception {
    String fileName =  "D:\\Users\\User\\Development\\Workspaces\\trix.birt"
        + "\\test_birt_report\\SimpleTableMySQL.rptdesign";
    IReportRunnable runnable = ReportEngine.getReportEngine().openReportDesign(
        fileName);
    ReportDesignHandle reportDesignHandle =
        (ReportDesignHandle) runnable.getDesignHandle();
    OdaDataSetHandle dataSetHandle =
        (OdaDataSetHandle) reportDesignHandle.getDataSets().get(0);
//    String query = "SELECT addressLine1 FROM offices;";
//    dataSetHandle.setQueryText(query);
    @SuppressWarnings("rawtypes")
    Iterator columnIterator = dataSetHandle.columnHintsIterator();
    while (columnIterator.hasNext()) {
      ColumnHintHandle columnHintHandle =
          (ColumnHintHandle) columnIterator.next();
      System.out.println("Alias: [" + columnHintHandle.getAlias() + "]");
      System.out.println("Analysis: [" + columnHintHandle.getAnalysis() + "]");
      System.out.println("AnalysisColumn: [" + columnHintHandle.getAnalysisColumn() + "]");
      System.out.println("ColumnName: [" + columnHintHandle.getColumnName() + "]");
      System.out.println("Description: [" + columnHintHandle.getDescription() + "]");
      System.out.println("DescriptionKey: [" + columnHintHandle.getDescriptionKey() + "]");
      System.out.println("DisplayName: [" + columnHintHandle.getDisplayName() + "]");
      System.out.println("DisplayNameKey: [" + columnHintHandle.getDisplayNameKey() + "]");
      System.out.println("Heading: [" + columnHintHandle.getHeading() + "]");
      System.out.println("HelpText: [" + columnHintHandle.getHelpText() + "]");
      System.out.println("ParentLevel: [" + columnHintHandle.getParentLevel() + "]");
      System.out.println("Searching: [" + columnHintHandle.getSearching() + "]");
      System.out.println("TextFormat: [" + columnHintHandle.getTextFormat() + "]");
      System.out.println();
    }
    PropertyHandle handle = dataSetHandle.getPropertyHandle( OdaDataSetHandle.RESULT_SET_PROP );
    for (Object obj : handle.getListValue( )) {
      ResultSetColumn resultSetColumn = (ResultSetColumn) obj;
      System.out.println("ColumnName: ["+ resultSetColumn.getColumnName()+"]");
      System.out.println("DataType: ["+ resultSetColumn.getDataType()+"]");
      System.out.println("Position: ["+ resultSetColumn.getPosition()+"]");
      System.out.println("ReferencableProperty: ["+ resultSetColumn.getReferencableProperty()+"]");
      System.out.println("StructName: ["+ resultSetColumn.getStructName()+"]");
      System.out.println();
    }
  }
  
  @Test
  public void testReportDefinition() throws Exception {
    Report report = reportFactory.create("CrossTabExample");
    ReportDto reportDto = report.getReportDefinition();
    for (ReportElementDto elementDto : reportDto.getElementDtoList()) {      
      System.out.println("Name:" + elementDto.getName());
      System.out.println("Filters:" + elementDto.getFiltersJson());
      System.out.println("Dimentions:" + elementDto.getDimensionsJson());
      System.out.println("Summaries:" + elementDto.getSummariesJson());
      System.out.println();
    }
  }
 /* 
  @Test
  public void testReportDataDto() throws Exception {
    ReportDataDto reportDataDto = new ReportDataDto();
    IRenderOption renderOptions = new RenderOption();  
    renderOptions.setOutputFormat("POJO");
    renderOptions.setOption("pojo", reportDataDto);
    IReportRunnable design = ReportEngine.getReportEngine().openReportDesign(
        reportRepository.getFileName("PatientsCrossTab"));
    IRunAndRenderTask task =
        ReportEngine.getReportEngine().createRunAndRenderTask(design);    
    task.setRenderOption(renderOptions);
    task.run();
    task.close();
    System.out.println(reportDataDto.getDataJson());    
  }*/
}
