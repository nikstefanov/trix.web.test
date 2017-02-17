package com.trix.web.birt.server;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.trix.report.Report;
import com.trix.report.ReportConfiguration;
import com.trix.report.ReportEngine;
import com.trix.report.ReportFactory;
import com.trix.report.cache.RptDocumentCache;
import com.trix.report.repository.ReportLocator;
import com.trix.report.repository.ResourceLocator;
import com.trix.report.repository.source.SourceStreamLocator;
import com.trix.report.repository.source.impl.SourceStreamLocatorFileImpl;
import com.trix.web.birt.client.ReportService;
import com.trix.web.birt.shared.dto.ReportConfigurationGwtDto;
import com.trix.web.birt.shared.dto.ReportContentGwtDto;
import com.trix.web.birt.shared.dto.ReportGwtDto;

/**
 * The server side implementation of the RPC service.
 */
public class ReportServiceImpl extends RemoteServiceServlet implements
    ReportService {
  private static final long serialVersionUID = -183841221764247929L;  
  
  private ReportFactory reportFactory;  
 
  public void init(ServletConfig sc) throws ServletException {
    super.init(sc);    
    try {
      ReportEngine.start();
      String cacheDirectory = "D:/Users/User/Development/Temp/rptdocumentCache/";    
      String baseDir =
          getClass().getResource("/com/trix/web/birt/reports").getFile();
      SourceStreamLocator sourceStreamLocator =
          new SourceStreamLocatorFileImpl(baseDir);
      ReportLocator reportLocator = new ReportLocator(sourceStreamLocator);
      ResourceLocator resourceLocator = new ResourceLocator(sourceStreamLocator);
      RptDocumentCache rptDocumentCache =
          new RptDocumentCache(cacheDirectory, 600000, 600000);
      reportFactory =
          new ReportFactory(new BIReportRepository(), rptDocumentCache,
              reportLocator, resourceLocator);
    } catch (Exception e) {
      throw new ServletException(e.getMessage());
    }
  }
    
  public ReportGwtDto getReportDefinition(String reportName) throws Exception {
    Report report = reportFactory.create(reportName);
    return DtoUtils.createReportGwtDto(report.getReportDefinition());
  }
  
  public ReportContentGwtDto getReport(String reportName,
      ReportConfigurationGwtDto reportConfigurationGwtDto) throws Exception {    
    ReportConfiguration reportConfiguration =
        DtoUtils.createReportConfiguration(reportConfigurationGwtDto);
//    DtoUtils.printReportConfiguration(reportConfiguration);
//    try {
      Report report = reportFactory.create(reportName);
      return DtoUtils.createReportContentGwtDto(
          report.execute(reportConfiguration, "HTML"));      
//    } catch (Exception e) {
//      e.printStackTrace();
//      return "";
//    }
  }
  
  public void destroy() {
    super.destroy();    
    reportFactory.getRptDocumentCache().emptyCache();
    ReportEngine.stop();
  } 
}
