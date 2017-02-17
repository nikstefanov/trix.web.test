package com.trix.web.birt.server;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.logging.Level;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.RenderOption;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.trix.birt.share.dto.ReportContentDto;
import com.trix.web.birt.client.service.ReportService;

public class ReportServiceImpl extends RemoteServiceServlet implements
    ReportService {

  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -183841221764247929L;  
  private static String DESIGN_FOLDER =
      "C:/Users/User/Documents/Development/Workspaces/trix.birt/"
          + "test_birt_report/";  
  private static final HashMap<String,String> ReportFileNamesMap;
  
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
  }
 
  private IReportEngine reportEngine;
  
  public void init(ServletConfig sc) throws ServletException {
System.out.println("AAAAAAAAAAAAAAAAAAAAA: init servlet !!!!!!!!!!!!!!!");
    final EngineConfig config = new EngineConfig( );
    config.setLogConfig(DESIGN_FOLDER, Level.FINE);
    try{
      Platform.startup( config );
      IReportEngineFactory factory =
          (IReportEngineFactory) Platform.createFactoryObject(
              IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );
      reportEngine = factory.createReportEngine( config );
      reportEngine.changeLogLevel( Level.WARNING );
    } catch(BirtException be) {
      System.out.println("Platform cannot start.");
      be.printStackTrace();
      reportEngine = null;
    }
  }

  public Object getReport(String key, String format) throws EngineException {
    Object resultObject = null;
    
    IReportRunnable runnable = reportEngine.openReportDesign(
        DESIGN_FOLDER + ReportFileNamesMap.get(key));
    IRenderOption options = new RenderOption();   
    options.setOutputFormat(format);
    if (format.equals("html")){
      resultObject = new ByteArrayOutputStream();
      options.setOutputStream((ByteArrayOutputStream)resultObject);
    }
    if (format.equals("POJO")){
      resultObject = new ReportContentDto();
      options.setOption("pojo", resultObject);
    }
    IRunAndRenderTask task = reportEngine.createRunAndRenderTask(runnable);    
    task.setRenderOption(options);
    task.run();
    task.close();   
    return resultObject;
  }
  
  public String getHtmlReport(String key) {
System.out.println("OOOOOOOOOOOOOOOOOOOOOOO: getHtmlReport:[" + key + "] !!!!!!!!!!!!");
    try{
      return ((ByteArrayOutputStream)getReport(key,"html")).toString("UTF-8");
    } catch (Exception ee) {
      ee.printStackTrace();
      return null;
    }
  }
  
  public ReportContentDto getPojoReport(String key) {
    try{
      return (ReportContentDto)getReport(key,"POJO");
    } catch (EngineException ee) {
      ee.printStackTrace();
      return null;
    }
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
