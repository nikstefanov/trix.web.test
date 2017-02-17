package com.trix.report.repository;

import java.io.InputStream;

import com.trix.report.repository.source.SourceStreamLocator;

public class ReportLocator {
  private SourceStreamLocator sourceStreamLocator;
  
  public ReportLocator(SourceStreamLocator sourceStreamLocator) {
    this.sourceStreamLocator = sourceStreamLocator;
  }
  public InputStream getReportStream(String reportFileName) throws Exception {
    return sourceStreamLocator.findSource(reportFileName);
  }
}
