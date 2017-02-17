package com.trix.test.report;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Iterator;

import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.model.api.ColumnHintHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;
import org.junit.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.trix.report.Report;
import com.trix.report.ReportConfiguration;
import com.trix.report.ReportContent;
import com.trix.report.ReportElementConfiguration;
import com.trix.report.ReportEngine;
import com.trix.report.ReportFactory;
import com.trix.report.cache.RptDocumentCache;
import com.trix.report.dto.ReportDataDto;
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
    " \"_constructor\":\"AdvancedCriteria\", " +
    " \"operator\":\"or\", "+
    " \"criteria\":[]}";
  private static String cacheDirectory;
  private static ReportFactory reportFactory;
  
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    ReportEngine.start();
    String tempDir =
        System.getProperty("java.io.tmpdir");
//        "D:\\Users\\User\\Development\\Temp";
    if (tempDir.charAt(tempDir.length()-1) != File.separatorChar) {
      tempDir = tempDir +File.separatorChar;
    }
    cacheDirectory = tempDir + "rptCacheDirectory";
    File cacheDirFile = new File(cacheDirectory);
    cacheDirFile.mkdir();
    RptDocumentCache rptDocumentCache =
        new RptDocumentCache(cacheDirectory, 1000,1000);
    ReportRepositoryImpl reportRepository = new ReportRepositoryImpl();
    reportRepository.addFileName("CrossTabExample",
        "CrossTabExample.rptdesign");
    reportRepository.addFileName("PatientsCrossTab",
        "PatientsCrossTab.rptdesign");
    reportRepository.addFileName("SimpleTableFilters",        
        "SimpleTableFilters.rptdesign");
    reportRepository.addFileName("AppointmentRequests",       
        "AppointmentRequests.rptdesign");
    reportRepository.addFileName("PatientsCrossTabDoctorsTable",       
        "PatientsCrossTabDoctorsTable.rptdesign");
    reportRepository.addFileName("PatientsCrossTabDoctorsTableFont",       
        "PatientsCrossTabDoctorsTableFont.rptdesign");
    String baseDir =
        ReportTest.class.getResource("/com/trix/report/test/design").getFile();
    SourceStreamLocator sourceStreamLocator =
        new SourceStreamLocatorFileImpl(baseDir);
    SourceStreamLocator resourcesSourceStreamLocator =
        new SourceStreamLocatorFileImpl(baseDir);
    ReportLocator reportLocator = new ReportLocator(sourceStreamLocator);
    ResourceLocator resourceLocator =
        new ResourceLocator(resourcesSourceStreamLocator);    
    reportFactory = new ReportFactory(reportRepository, rptDocumentCache,
        reportLocator, resourceLocator);
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
    reportFactory.getRptDocumentCache().emptyCache();
    if (!deleteDir(cacheDirectory)) {
      System.out.println("Failed to delete cache directory  " + cacheDirectory);
      System.out.println();
    }
    ReportEngine.stop();
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
    reportCfg.addElementConfiguration("CrossTabTest", elementCfg);
    
    Report report = reportFactory.create("CrossTabExample");
    report.execute(reportCfg, "HTML", new FileOutputStream(
        "D:\\Users\\User\\Development\\Temp\\CrossTabExample.html"));
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
  public void testXlsFormat() throws Exception {
    ReportConfiguration reportCfg = new ReportConfiguration();
    Report report = reportFactory.create("PatientsCrossTabDoctorsTable");
    report.execute(reportCfg, "XLSX", new FileOutputStream(
        "D:\\Users\\User\\Development\\Temp\\PatientsCrossTabDoctorsTable.xlsx"));
  }
  
  @Test
  public void testPdfFormat() throws Exception {
    ReportConfiguration reportCfg = new ReportConfiguration();
    Report report = reportFactory.create("PatientsCrossTabDoctorsTableFont");
    report.execute(reportCfg, "PDF", new FileOutputStream(
        "D:\\Users\\User\\Development\\Temp\\PatientsCrossTabDoctorsTableFont.pdf"));
  }
  
  @Test
  public void testXlsFormatAR() throws Exception {
    ReportElementConfiguration elementCfg = new ReportElementConfiguration();
    elementCfg.setCriteriaJSON(jsonStringCrossTab);
    ReportConfiguration reportCfg = new ReportConfiguration();
    reportCfg.addElementConfiguration("AppointmentRequestsTable", elementCfg);
    reportCfg.setPageRange("all");
    
    Report report = reportFactory.create("AppointmentRequests");
    report.execute(reportCfg, "XLS", new FileOutputStream(
        "D:\\Users\\User\\Development\\Temp\\AppointmentRequests.xls"));
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
  
  @Test
  public void testReportDataDto() throws Exception {
    ReportDataDto reportDataDto = new ReportDataDto();
    IRenderOption renderOptions = new RenderOption();  
    renderOptions.setOutputFormat("POJO");
    renderOptions.setOption("pojo", reportDataDto);
    IReportRunnable design = ReportEngine.getReportEngine().openReportDesign(
        getClass().getResource(
            "/com/trix/report/test/design/PatientsCrossTab.rptdesign")
            .getFile());
    IRunAndRenderTask task =
        ReportEngine.getReportEngine().createRunAndRenderTask(design);    
    task.setRenderOption(renderOptions);
    task.run();
    task.close();
    System.out.println(reportDataDto.getDataJson());    
  }
  
  @Test
  public void testExecutePage() throws Exception {    
    ReportElementConfiguration elementCfg = new ReportElementConfiguration();
    elementCfg.setCriteriaJSON(jsonStringCrossTab);
    ReportConfiguration reportCfg = new ReportConfiguration();
    reportCfg.addElementConfiguration("AppointmentRequestsTable", elementCfg);
    reportCfg.setPageRange("1");
    
    Report report = reportFactory.create("AppointmentRequests");
    report.execute(reportCfg, "HTML", new FileOutputStream(
        "D:\\Users\\User\\Development\\Temp\\AppointmentRequests.html"));   
  }
  
  @Test
  public void testExecuteReportContent() throws Exception {
    ReportElementConfiguration elementCfg = new ReportElementConfiguration();
    elementCfg.setCriteriaJSON(jsonStringCrossTab);
    ReportConfiguration reportCfg = new ReportConfiguration();
    reportCfg.addElementConfiguration("AppointmentRequestsTable", elementCfg);
    reportCfg.setPageRange("2");
    
    Report report = reportFactory.create("AppointmentRequests");
    ReportContent reportContent = report.execute(reportCfg, "HTML");
    System.out.println("ContentHash: " + reportContent.getContent().hashCode());
    System.out.println("ContentLength: " + reportContent.getContent().length());
    System.out.println("Format: " + reportContent.getFormat());
    System.out.println("Page Range: " + reportContent.getPageRange());
    System.out.println("Rows: " + reportContent.getRowsPerPage());
    System.out.println("TotalPages: " + reportContent.getTotalPages());
  }
  
  @Test
  public void testRptdocumnetCreation() throws Exception {
    ReportElementConfiguration elementCfg = new ReportElementConfiguration();
    elementCfg.setCriteriaJSON(jsonStringCrossTab);
    ReportConfiguration reportCfg = new ReportConfiguration();
    reportCfg.addElementConfiguration("AppointmentRequestsTable", elementCfg);
    
    reportCfg.setPageRange("1");
    
    Report report = reportFactory.create("AppointmentRequests");
    ReportContent reportContent = report.execute(reportCfg, "HTML");
    System.out.println("ContentLength: " + reportContent.getContent().length());
    System.out.println("Format: " + reportContent.getFormat());
    System.out.println("Page Range: " + reportContent.getPageRange());
    System.out.println("Rows: " + reportContent.getRowsPerPage());
    System.out.println("TotalPages: " + reportContent.getTotalPages());
    
    reportCfg.setPageRange("2");
    
    report = reportFactory.create("AppointmentRequests");
    reportContent = report.execute(reportCfg, "HTML");
    System.out.println("ContentLength: " + reportContent.getContent().length());
    System.out.println("Format: " + reportContent.getFormat());
    System.out.println("Page Range: " + reportContent.getPageRange());
    System.out.println("Rows: " + reportContent.getRowsPerPage());
    System.out.println("TotalPages: " + reportContent.getTotalPages());
  }
  
  @Test
  public void testExecuteFourReportContent() throws Exception {
    File cacheDirectoryFile = new File(cacheDirectory);
    deleteDirContent(cacheDirectoryFile);
    
    ReportElementConfiguration elementCfg = new ReportElementConfiguration();
    elementCfg.setCriteriaJSON(jsonStringCrossTab);
    ReportConfiguration reportCfg = new ReportConfiguration();
    reportCfg.addElementConfiguration("AppointmentRequestsTable", elementCfg);
    
    reportCfg.setPageRange("1");  
    Report report = reportFactory.create("AppointmentRequests");
    ReportContent reportContent = report.execute(reportCfg, "HTML");    
    
    System.out.println("ContentLength: " + reportContent.getContent().length());
    System.out.println("Format: " + reportContent.getFormat());
    System.out.println("Page Range: " + reportContent.getPageRange());
    System.out.println("Rows: " + reportContent.getRowsPerPage());
    System.out.println("TotalPages: " + reportContent.getTotalPages());
    
    reportCfg.setPageRange("2");  
    report = reportFactory.create("AppointmentRequests");
    reportContent = report.execute(reportCfg, "HTML");
    System.out.println("ContentLength: " + reportContent.getContent().length());
    System.out.println("Format: " + reportContent.getFormat());
    System.out.println("Page Range: " + reportContent.getPageRange());
    System.out.println("Rows: " + reportContent.getRowsPerPage());
    System.out.println("TotalPages: " + reportContent.getTotalPages());    
    
    reportCfg.setPageRange("1");
    report = reportFactory.create("PatientsCrossTab");
    reportContent = report.execute(reportCfg, "HTML");
    System.out.println("ContentLength: " + reportContent.getContent().length());
    System.out.println("Format: " + reportContent.getFormat());
    System.out.println("Page Range: " + reportContent.getPageRange());
    System.out.println("Rows: " + reportContent.getRowsPerPage());
    System.out.println("TotalPages: " + reportContent.getTotalPages());
    
    reportCfg.setPageRange("1");
    report = reportFactory.create("PatientsCrossTab");
    reportContent = report.execute(reportCfg, "HTML");
    System.out.println("ContentLength: " + reportContent.getContent().length());
    System.out.println("Format: " + reportContent.getFormat());
    System.out.println("Page Range: " + reportContent.getPageRange());
    System.out.println("Rows: " + reportContent.getRowsPerPage());
    System.out.println("TotalPages: " + reportContent.getTotalPages());
    
    String[] children = cacheDirectoryFile.list();
    for (int i=0; i<children.length; i++) {
      Assert.assertFalse("AppointmentRequests rptdocument should be deleted." +
          "purge() method fail.", children[i].startsWith("AppointmentRequests"));
    }
  }
  
  @Test
  public void testPageRange() throws Exception {
    ReportElementConfiguration elementCfg = new ReportElementConfiguration();
    elementCfg.setCriteriaJSON(jsonStringCrossTab);
    ReportConfiguration reportCfg = new ReportConfiguration();
    reportCfg.addElementConfiguration("AppointmentRequestsTable", elementCfg);
    reportCfg.setPageRange(null);
    
    Report report = reportFactory.create("AppointmentRequests");
    report.execute(reportCfg, "HTML", new FileOutputStream(
        "D:\\Users\\User\\Development\\Temp\\AppointmentRequests.html"));  
  }
  
  @Test
  public void URLTest() {
    URL a =
        getClass().getResource("/com/trix/report/test/reports");
//        a = ReportTest.class.getResource("/com/trix/report/test/reports");
    System.out.println("URL" + a.toString());
    System.out.println("Authority " + a.getAuthority());
    System.out.println("File " + a.getFile());
    System.out.println("Host " + a.getHost());
    System.out.println("Path " + a.getPath());
    System.out.println("Protocol " + a.getProtocol());
    System.out.println("Query " + a.getQuery());
    System.out.println("Ref " + a.getRef());
    System.out.println("UserInfo " + a.getUserInfo());
    System.out.println(System.getProperty("java.io.tmpdir"));
  }
  
  private static void deleteDirContent(File dir) {
    if (dir.isDirectory()) {
      String[] children = dir.list( );
      for (int i = 0; i < children.length; i++) {
        (new File(dir, children[i])).delete();        
      }
    }
  }
  
  private static boolean deleteDir(String dirName) {
    return deleteDir(new File(dirName));
  }

  

/**Deletes all files and subdirectories under dir.
 * Returns true if all deletions were successful.
 * If a deletion fails, the method stops attempting to delete and returns false.
 * Parameters:
 *    dir directory
*/


  private static boolean deleteDir(File dir) {
    if ( dir.isDirectory( ) ) {
      String[] children = dir.list( );
      for ( int i = 0; i < children.length; i++ ) {
        boolean success = deleteDir( new File( dir, children[i] ) );
        if ( !success ) {
          return false;
        }
      }
    }
    // The directory is now empty so delete it
    return dir.delete( );
  }
  
}
