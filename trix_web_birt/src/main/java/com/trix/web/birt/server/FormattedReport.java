package com.trix.web.birt.server;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.IScalarParameterDefn;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

public class FormattedReport extends HttpServlet {

  private static final long serialVersionUID = 1311193913233917960L;
  private static String DESIGN_FOLDER =
      "D:/Users/User/Development/Workspaces/trix.birt/"
          + "test_birt_report/";  
  private static final HashMap<String,String> ReportFileNamesMap,ContentTypeMap;
  private IReportEngine reportEngine;
  
  
  static{    
    ReportFileNamesMap = new HashMap<String,String>(6);
    ReportFileNamesMap.put("Customers",
        "Customers.rptdesign");
    ReportFileNamesMap.put("CustomersAggregation",
        "CustomersAggregation.rptdesign");
    ReportFileNamesMap.put("CustomersAggregationMySQL",
        "CustomersAggregationMySQL.rptdesign");
    ReportFileNamesMap.put("CustomersMySQLSource",
        "CustomersMySQLSource.rptdesign");
    ReportFileNamesMap.put("CustomersTwoTables",
        "CustomersTwoTables.rptdesign");
    ReportFileNamesMap.put("SalesByStateAndProductLine",
        "SalesByStateAndProductLine.rptdesign");
    ReportFileNamesMap.put("SimpleTable",
        "SimpleTable.rptdesign");
    ReportFileNamesMap.put("SimpleGrid",
        "SimpleGrid.rptdesign");
    ReportFileNamesMap.put("SimpleTableMySQL",
        "SimpleTableMySQL.rptdesign");
    
    ContentTypeMap = new HashMap<String,String>(8);
    ContentTypeMap.put("pdf", "application/pdf");
    ContentTypeMap.put("xlsx",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    ContentTypeMap.put("pptx",
        "application/vnd.openxmlformats-officedocument.presentationml."
        + "presentation");
    ContentTypeMap.put("docx",
        "application/vnd.openxmlformats-officedocument.wordprocessingml."
        + "document");
    ContentTypeMap.put("doc", "application/msword");
    ContentTypeMap.put("xls", "application/vnd.ms-excel");
    ContentTypeMap.put("ppt", "application/vnd.ms-powerpoint");
    ContentTypeMap.put("html", "text/html");
  }
 
  public void init(ServletConfig sc) throws ServletException {
    super.init(sc);    
    final EngineConfig config = new EngineConfig( );
    config.setLogConfig(null, Level.OFF);
    try {
      Platform.startup( config );
      IReportEngineFactory factory =
        (IReportEngineFactory) Platform.createFactoryObject(
           IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );
      reportEngine = factory.createReportEngine( config );
    } catch(BirtException be) {
      System.out.println("Platform cannot start.");
      be.printStackTrace();
      reportEngine = null;
    }
  }
  
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    System.out.println("doPost method");
    System.out.println(request.getParameter("reportName") + '.' +
        request.getParameter("reportFormat"));
    response.setHeader("Content-Disposition", "attachment;filename=" + 
        request.getParameter("reportName") + '.' +
        request.getParameter("reportFormat"));
    response.setCharacterEncoding("utf-8");
//    response.setContentLength(size);
    response.setContentType(
        ContentTypeMap.get(request.getParameter("reportFormat")));
    try {         
      IReportRunnable runnable = reportEngine.openReportDesign(
          DESIGN_FOLDER + ReportFileNamesMap.get(
              request.getParameter("reportName")));
      CriteriaUtilities criteriaUtilities =
          new CriteriaUtilities(request.getParameter("cirteriaJSONString"));
      if (criteriaUtilities.getJsonString() != null) {
        IGetParameterDefinitionTask taskPr =
            reportEngine.createGetParameterDefinitionTask(runnable);  
        extractParameterTypes(taskPr.getParameterDefns( false ),
            criteriaUtilities);
      }
      addQueryWhereConstrains((ReportDesignHandle)runnable.getDesignHandle( ),
          criteriaUtilities);
      IRunAndRenderTask task = reportEngine.createRunAndRenderTask(runnable);
      IRenderOption options = new RenderOption();   
      options.setOutputFormat(request.getParameter("reportFormat"));
      options.setOutputStream(response.getOutputStream());
      task.setRenderOption(options);
      response.setStatus(HttpServletResponse.SC_OK);
      task.run();
      task.close();      
    }catch (Exception e){
      e.printStackTrace();
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
  }
  
  @SuppressWarnings("rawtypes")
  public void extractParameterTypes(Collection collection,
      CriteriaUtilities criteriaUtilities) {    
    Iterator iter = collection.iterator( );    
    while ( iter.hasNext( ) )
    {
      IScalarParameterDefn param = (IScalarParameterDefn) iter.next( );     
      if (param.getUserPropertyValue("IsFilter").equals("true")) {
        criteriaUtilities.putFieldType(param.getUserPropertyValue("ColumnName"),
            param.getDataType());        
      }
    }
  }
  
  /*
   * Note: WHERE statement should be the last statement of the query.
   */
  private void addQueryWhereConstrains(ReportDesignHandle designHandle,
      CriteriaUtilities criteriaUtilities) throws Exception {
    System.out.println("CriteriaJSONString: "+
      criteriaUtilities.getJsonString());
    String whereStatement = "";
    if (criteriaUtilities.getJsonString() != null) {
      whereStatement = criteriaUtilities.linearizeCriteria();
    } else {
      whereStatement = "false";
    }
    OdaDataSetHandle odaDataSetHandle =
        (OdaDataSetHandle)designHandle.getDataSets().get(0);
    String query = odaDataSetHandle.getQueryText();
    if (whereStatement.length() != 0) {
      if (query.toLowerCase().indexOf("where") != -1) {
        query = query.concat(" and " + whereStatement); 
      } else {
        query = query.concat(" where " + whereStatement);
      }
      odaDataSetHandle.setQueryText(query);
    }
    System.out.println("Query: "+query);
    System.out.println("where Statement: "+ whereStatement);
  }
  
  public void destroy() {
    super.destroy(); 
    if (reportEngine == null) {
      return;
    }   
    reportEngine.destroy();
    Platform.shutdown();
    reportEngine = null;
  } 

}
