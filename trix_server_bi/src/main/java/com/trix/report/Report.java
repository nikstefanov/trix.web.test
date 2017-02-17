package com.trix.report;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Iterator;

import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

import uk.co.spudsoft.birt.emitters.excel.ExcelEmitter;

import com.trix.report.cache.RptDocumentCache;
import com.trix.report.dto.ReportDto;
import com.trix.report.dto.ReportElementDtoFactory;


public class Report {
  private String name;
  private IReportRunnable reportRunnable;
  private ReportElementFactory reportElementFactory;
  private ReportElementDtoFactory reportElementDtoFactory;
  private RptDocumentCache rptDocumentCache;
  
  public ReportContent execute(ReportConfiguration reportConfiguration,
      String format) throws Exception {
    ReportContent reportContent = new ReportContent();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    reportContent.setTotalPages(execute(reportConfiguration, format, baos));    
    reportContent.setContent(baos.toString("UTF-8"));
    reportContent.setFormat(format);
    reportContent.setRowsPerPage(40);
    reportContent.setPageRange(reportConfiguration.getPageRange());
    return reportContent;
  } 
  
  public long execute(ReportConfiguration reportConfiguration, String format,
      OutputStream outputStream) throws Exception{
    ReportDesignHandle designHandle =
        (ReportDesignHandle) reportRunnable.getDesignHandle();
    @SuppressWarnings("rawtypes")
    Iterator elementIterator = designHandle.getBody().iterator();
    while (elementIterator.hasNext()) {
      DesignElementHandle element = (DesignElementHandle)elementIterator.next();
      if (reportConfiguration.hasConfiguration(element.getName())) {        
        ReportElement reportElement = reportElementFactory.create(element);
        reportElement.configure(
            reportConfiguration.getReportElementConfiguration(
                element.getName()));
      }
    }
    
    String rptDocFileName = null, cacheKey = null;
    try {
      cacheKey = rptDocumentCache.lock(name, reportConfiguration);
      rptDocFileName = rptDocumentCache.retrive(cacheKey);
      if (rptDocFileName == null) {
        rptDocFileName = rptDocumentCache.store(name, cacheKey);
        IRunTask runTask =
            ReportEngine.getReportEngine().createRunTask(reportRunnable);
        runTask.setReportDocument(rptDocFileName);
        runTask.run();
        runTask.close();
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      rptDocumentCache.unlock(cacheKey);
    }
    
    long totalPages = 0;    
    IReportDocument reportDocument =
        ReportEngine.getReportEngine().openReportDocument(rptDocFileName);
    IRenderTask renderTask =
        ReportEngine.getReportEngine().createRenderTask(reportDocument);
    totalPages = renderTask.getTotalPage();    
    IRenderOption options = new RenderOption();   
    options.setOutputFormat(format);
    options.setOutputStream(outputStream);
    
    // Following options apply only to SpudSoft BIRT Excel Emitter.
    // They don't bother other emitters.
    options.setOption(ExcelEmitter.SINGLE_SHEET_PAGE_BREAKS, true);
    options.setOption(ExcelEmitter.BLANK_ROW_AFTER_TOP_LEVEL_TABLE, true);
    
    renderTask.setRenderOption(options);
    renderTask.setPageRange(reportConfiguration.getPageRange());
    renderTask.render();
    renderTask.close();
    reportDocument.close();
    return totalPages;
  }
  
  public ReportDto getReportDefinition() throws Exception{
    ReportDto reportDto = new ReportDto();
    reportDto.clear();
    ReportDesignHandle designHandle =
        (ReportDesignHandle) reportRunnable.getDesignHandle();
    @SuppressWarnings("rawtypes")
    Iterator elementIterator = designHandle.getBody().iterator();
    while (elementIterator.hasNext()) {
      DesignElementHandle element = (DesignElementHandle)elementIterator.next();
      String elementName = element.getName();
      if (elementName != null && elementName.length() > 0 &&
          elementName.toLowerCase().indexOf("ignore") == -1) {
        reportDto.addElementDto(reportElementDtoFactory.create(element));
      }
    }
    return reportDto;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public IReportRunnable getReportRunnable() {
    return reportRunnable;
  }

  public void setReportRunnable(IReportRunnable reportRunnable) {
    this.reportRunnable = reportRunnable;
  }

  public void setReportElementFactory(ReportElementFactory reportElementFactory)
  {
    this.reportElementFactory = reportElementFactory;
  }
  
  public void setReportElementDtoFactory(
      ReportElementDtoFactory reportElementDtoFactory) {
    this.reportElementDtoFactory = reportElementDtoFactory;
  }
  
  public void setRptDocumentCache(RptDocumentCache rptDocumentCache) {
    this.rptDocumentCache = rptDocumentCache;
  }

  /*
  private void removePageBrakes() throws SemanticException {
    ReportDesignHandle designHandle =
        (ReportDesignHandle) reportRunnable.getDesignHandle();    
    @SuppressWarnings("unchecked")
    Iterator<DesignElementHandle> elementIterator =
        designHandle.getBody().iterator();
    while (elementIterator.hasNext()) {
      DesignElementHandle designElement = elementIterator.next();
      if (designElement instanceof ListingHandle) {
        ((ListingHandle)designElement).setPageBreakInterval(0);
//        ((ListingHandle)designElement).setRepeatHeader(false);
      }
    }
  }*/
}
