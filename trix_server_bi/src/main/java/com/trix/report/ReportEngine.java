package com.trix.report;

import java.util.logging.Level;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;

public class ReportEngine {
  private static IReportEngine reportEngine;
  
  public static void start() throws Exception{
    try{
      final EngineConfig config = new EngineConfig( );
      config.setLogConfig("D://Users//User//Development//Temp//BIRT Log", Level.OFF);    
      Platform.startup(config);
      IReportEngineFactory factory =
          (IReportEngineFactory) Platform.createFactoryObject(
              IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
      reportEngine = factory.createReportEngine(config);
    } catch (BirtException be) {      
      throw new Exception("Can not start BIRT platform.");
    }
  }
  
  public static void stop() {
    if (reportEngine != null){
      reportEngine.destroy();
      Platform.shutdown();
    }
  }

  public static IReportEngine getReportEngine() {
    return reportEngine;
  }

  public static void setReportEngine(IReportEngine reportEngine) {
    ReportEngine.reportEngine = reportEngine;
  }

}
