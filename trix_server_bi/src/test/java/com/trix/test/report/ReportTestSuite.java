package com.trix.test.report;

//import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
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

public class ReportTestSuite {
  
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
    String baseDir =
        ReportTestSuite.class.getResource("/com/trix/report/test/design").getFile();
    SourceStreamLocator sourceStreamLocator =
        new SourceStreamLocatorFileImpl(baseDir);
    SourceStreamLocator resourcesSourceStreamLocator =
        new SourceStreamLocatorFileImpl(baseDir + File.separator + "resources");
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

  // Tests whether the generation of Cross-tab is correct.
  @Test
  public void testCrossTab() throws Exception {
    ReportElementConfiguration elementCfg = new ReportElementConfiguration();
    elementCfg.setCriteriaJSON(jsonStringCrossTab);
    elementCfg.addXtColumn("ClinicGroup");
    elementCfg.addXtColumn("DepartmentGroup");
    elementCfg.addXtRow("YearGroup");
    elementCfg.addXtSummary("Summary Patients Count");
    ReportConfiguration reportCfg = new ReportConfiguration();
    reportCfg.addElementConfiguration("CrossTabTest", elementCfg);
    compareReportToFile("CrossTabExample", reportCfg,
        "/com/trix/report/test/output/CrossTabExample.html");
  }
  
  // Tests whether the generation of table report with filters is correct.
  @Test
  public void testFilters() throws Exception {
    ReportElementConfiguration elementCfg = new ReportElementConfiguration();
    elementCfg.setCriteriaJSON(jsonStringCriteria);
    ReportConfiguration reportCfg = new ReportConfiguration();
    reportCfg.addElementConfiguration("SimpleTable", elementCfg);
    compareReportToFile("SimpleTableFilters", reportCfg,
        "/com/trix/report/test/output/SimpleTableFilters.html");
  }
  
  /*
  @Test
  public void testXlsFormat() throws Exception {
    ReportElementConfiguration elementCfg = new ReportElementConfiguration();
    elementCfg.setCriteriaJSON(jsonStringCrossTab);
    ReportConfiguration reportCfg = new ReportConfiguration();
    reportCfg.addElementConfiguration("", elementCfg);
    compareReportToFile("PatientsCrossTabDoctorsTable", reportCfg,
        "/com/trix/report/test/output/SimpleTableFilters.html");
  }*/
  
  // Tests whether the method getReportDefinition() collects
  // all the info about report definition.
  @Test
  public void testReportDefinition() throws Exception {
    Report report = reportFactory.create("CrossTabExample");
    ReportDto reportDto = report.getReportDefinition();
    for (ReportElementDto elementDto : reportDto.getElementDtoList()) {      
      assertThat(elementDto.getName(), is("CrossTabTest"));
      assertThat(elementDto.getFiltersJson(), is("[]"));
      assertThat(elementDto.getDimensionsJson(), is("[{\"name\":\"AgeIntervalGroup\",\"title\":\"Възрастов интервал\"},{\"name\":\"ClinicGroup\",\"title\":\"Клиника\"},{\"name\":\"DoctorGroup\",\"title\":\"Доктор\"},{\"name\":\"SexGroup\",\"title\":\"Код пол\"},{\"name\":\"DepartmentGroup\",\"title\":\"Отделение\"},{\"name\":\"SexNameGroup\",\"title\":\"Пол\"},{\"name\":\"YearGroup\",\"title\":\"Година\"}]"));
      assertThat(elementDto.getSummariesJson(), is("[{\"name\":\"Summary Patients Count\",\"title\":\"Пациенти\"},{\"name\":\"Summary PatientsMaxCount\",\"title\":\"Max брой пациенти\"}]"));
    }
  }
  
  // Tests whether the pojo emitter generates the proper ReportDataDto object.
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
    assertEquals("Fails - Wrong Data Json.", 1951605185,
        reportDataDto.getDataJson().hashCode());
  }
  
  // Tests the page one of AppointmentRequests report.
  @Test
  public void testExecutePage() throws Exception {    
    ReportElementConfiguration elementCfg = new ReportElementConfiguration();
    elementCfg.setCriteriaJSON(jsonStringCrossTab);
    ReportConfiguration reportCfg = new ReportConfiguration();
    reportCfg.addElementConfiguration("AppointmentRequestsTable", elementCfg);
    reportCfg.setPageRange("1");
    compareReportToFile("AppointmentRequests", reportCfg,
        "/com/trix/report/test/output/AppointmentRequests_1.html");
  }
  
  /*
  @Ignore  @Test
  public void testExcelFormat() throws Exception {
    ReportConfiguration reportCfg = new ReportConfiguration();
    Report report = reportFactory.create("PatientsCrossTabDoctorsTable");
    
    ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
    report.execute(reportCfg, "XLS", byteOutStream);
//    File goldenFile = new File(ReportTestSuite.class.getResource(
//        "/com/trix/report/test/output/PatientsCrossTabDoctorsTable.xlsx").getFile());
//    FileInputStream goldenFileInputStream = new FileInputStream(goldenFile);
//    byte[] goldenByteArray = new byte[(int)goldenFile.length()];
//    goldenFileInputStream.read(goldenByteArray);
//    goldenFileInputStream.close();    
//    assertArrayEquals("Failure - The result does not match the test file.",
//        goldenByteArray, byteOutStream.toByteArray());
    System.out.println(Arrays.hashCode(byteOutStream.toByteArray()));
  }*/
  
  // Tests whether the method execute(reportCfg, format)
  // generates the proper ReportContent object.
  @Test
  public void testExecuteReportContent() throws Exception {
    ReportElementConfiguration elementCfg = new ReportElementConfiguration();
    elementCfg.setCriteriaJSON(jsonStringCrossTab);
    ReportConfiguration reportCfg = new ReportConfiguration();
    reportCfg.addElementConfiguration("AppointmentRequestsTable", elementCfg);
    reportCfg.setPageRange("2");
    
    Report report = reportFactory.create("AppointmentRequests");
    ReportContent reportContent = report.execute(reportCfg, "HTML");
    assertEquals("Fail - Content differs.", 810417653,
        removeAutoIds(reportContent.getContent()).hashCode());
    assertEquals("Fail - ContentLength differs.", 33718,
        reportContent.getContent().length());
    assertEquals("Fail - Format differs", "HTML", reportContent.getFormat());
    assertEquals("Fails - Page range differs", "2", reportContent.getPageRange());
    assertEquals("Fail - Rows per page differ", 40, reportContent.getRowsPerPage());
    assertEquals("Fails - TotalPages differs", 10, reportContent.getTotalPages());
  }
  
  // Test whether one rptdocument is generated
  // when rendering two different pages of a report.
  @Test
  public void testRptdocumnetCreation() throws Exception {
    ReportElementConfiguration elementCfg = new ReportElementConfiguration();
    elementCfg.setCriteriaJSON(jsonStringCrossTab);
    ReportConfiguration reportCfg = new ReportConfiguration();
    reportCfg.addElementConfiguration("AppointmentRequestsTable", elementCfg);
    
    reportCfg.setPageRange("1");   
    Report report = reportFactory.create("AppointmentRequests");
    report.execute(reportCfg, "HTML");    
    
    reportCfg.setPageRange("2");
    report = reportFactory.create("AppointmentRequests");
    report.execute(reportCfg, "HTML");
    
    assertEquals("Fails - One rptdocument should be in the cache directory.", 1,
        (new File(cacheDirectory)).list().length);
   
  }
  
  // Test whether purge method of RptDocumentCache deletes a time-outed file.
  @Test
  public void testCachePurge() throws Exception {
    ReportElementConfiguration elementCfg = new ReportElementConfiguration();
    elementCfg.setCriteriaJSON(jsonStringCrossTab);
    ReportConfiguration reportCfg = new ReportConfiguration();
    reportCfg.addElementConfiguration("AppointmentRequestsTable", elementCfg);
    
    reportCfg.setPageRange("1");
    Report report = reportFactory.create("AppointmentRequests");
    report.execute(reportCfg, "HTML");
    reportCfg.setPageRange("2");
    report = reportFactory.create("AppointmentRequests");
    report.execute(reportCfg, "HTML");
    
    Thread.sleep(1000);
    
    reportCfg.setPageRange("1");
    report = reportFactory.create("PatientsCrossTab");
    report.execute(reportCfg, "HTML");
    reportCfg.setPageRange("1");
    report = reportFactory.create("PatientsCrossTab");
    report.execute(reportCfg, "HTML");
    
    File cacheDirectoryFile = new File(cacheDirectory);
    assertThat("AppointmentRequests rptdocument should be deleted." +
        "purge() method fail.", Arrays.asList(cacheDirectoryFile.list()),
      not(hasItem(startsWith("AppointmentRequests"))));
  }
  
  private static void compareReportToFile(String reportName,
      ReportConfiguration reportCfg, String filePath) throws Exception {
    Report report = reportFactory.create(reportName);
    ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
    report.execute(reportCfg, "HTML", byteOutStream);
    File goldenFile =
        new File(ReportTestSuite.class.getResource(filePath).getFile());
    FileInputStream goldenFileInputStream = new FileInputStream(goldenFile);
    byte[] goldenByteArray = new byte[(int)goldenFile.length()];
    goldenFileInputStream.read(goldenByteArray);
    goldenFileInputStream.close();    
    assertEquals("Failure - The result does not match the test file.",
        removeAutoIds(new String(goldenByteArray, "UTF-8")),
        removeAutoIds(byteOutStream.toString("UTF-8")));
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
    if (dir.isDirectory()) {
      String[] children = dir.list();
      for (int i = 0; i < children.length; i++) {
        boolean success = deleteDir(new File(dir, children[i]));
        if (!success) {
          return false;
        }
      }
    }
    // The directory is now empty so delete it
    return dir.delete( );
  }
  
  private static String removeAutoIds(String reportHtmlString) {
    return reportHtmlString.replaceAll(
        "id=\"AUTOGENBOOKMARK_\\d+?_[\\da-f]{8}+-[\\da-f]{4}+-[\\da-f]{4}+-[\\da-f]{4}+-[\\da-f]{12}+\"", "");
  }
}
