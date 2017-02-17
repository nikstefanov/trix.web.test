package com.trix.web.birt.server;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trix.report.Report;
import com.trix.report.ReportConfiguration;
import com.trix.report.ReportEngine;
import com.trix.report.ReportFactory;
import com.trix.report.cache.RptDocumentCache;
import com.trix.report.repository.ReportLocator;
import com.trix.report.repository.ResourceLocator;
import com.trix.report.repository.source.SourceStreamLocator;
import com.trix.report.repository.source.impl.SourceStreamLocatorFileImpl;

public class FormattedReport extends HttpServlet {
  private static final long serialVersionUID = 1311193913233917960L;  
  private static final HashMap<String,String> ContentTypeMap;
  private ReportFactory reportFactory;
  
  static{    
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
      Report report = reportFactory.create(request.getParameter("reportName"));            
      report.execute(
          new ReportConfiguration(),
//          DtoUtils.createReportConfiguration(reportConfigurationGwtDto),
          request.getParameter("reportFormat"), response.getOutputStream());  
    }catch (Exception e){
      throw new ServletException(e.getMessage());
    }
  }
      
  public void destroy() {
    super.destroy();
    reportFactory.getRptDocumentCache().emptyCache();
    ReportEngine.stop();
  } 

}
