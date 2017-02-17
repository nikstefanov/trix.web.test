package com.trix.report;

import java.io.InputStream;

public interface ReportRepository {
  public InputStream getInputStream(String reportName) throws Exception;
  public String getFileName(String reportName);
}
