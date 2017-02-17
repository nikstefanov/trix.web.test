package com.trix.report;

import org.eclipse.birt.report.engine.api.IReportRunnable;

import com.trix.report.cache.RptDocumentCache;
import com.trix.report.dto.ReportElementDtoFactory;
import com.trix.report.repository.ReportLocator;
import com.trix.report.repository.ReportRepository;
import com.trix.report.repository.ResourceLocator;

public class ReportFactory {
  private ReportRepository reportRepository;
  private RptDocumentCache rptDocumentCache;
  private ReportLocator reportLocator;
  private ResourceLocator resourceLocator;  
  
  public ReportFactory(
      ReportRepository reportRepository, RptDocumentCache rptDocumentCache,
      ReportLocator reportLocator, ResourceLocator resourceLocator) {
    this.reportRepository = reportRepository;
    this.rptDocumentCache = rptDocumentCache;
    this.reportLocator = reportLocator;
    this.resourceLocator = resourceLocator;
  }
  
  public Report create(String reportName) throws Exception {
    Report report = new Report();
    report.setName(reportName);
    IReportRunnable runnable = ReportEngine.getReportEngine().openReportDesign(
        null,
        reportLocator.getReportStream(reportRepository.getFileName(reportName)),
        resourceLocator);
    report.setReportRunnable(runnable);
    ReportElementFactory elementFactory = new ReportElementFactory();
    ReportElementDtoFactory elementDtoFactory = new ReportElementDtoFactory();
    report.setReportElementFactory(elementFactory);
    report.setReportElementDtoFactory(elementDtoFactory);
    report.setRptDocumentCache(rptDocumentCache);
    return report;
  }
  public RptDocumentCache getRptDocumentCache() {
    return rptDocumentCache;
  }

  public void setRptDocumentCache(RptDocumentCache rptDocumentCache) {
    this.rptDocumentCache = rptDocumentCache;
  }

  public ReportRepository getReportRepository() {
    return reportRepository;
  }
  
  public void setReportRepository(ReportRepository reportRepository) {
    this.reportRepository = reportRepository;
  }
  
  public void setReportLocator(ReportLocator reportLocator) {
    this.reportLocator = reportLocator;
  }
  
  public void setResourceLocator(ResourceLocator resourceLocator) {
    this.resourceLocator = resourceLocator;
  }

}
