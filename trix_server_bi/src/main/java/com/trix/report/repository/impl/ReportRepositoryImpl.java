package com.trix.report.repository.impl;

import java.util.HashMap;
import java.util.Map;

import com.trix.report.repository.ReportRepository;

public class ReportRepositoryImpl implements ReportRepository {
  private Map<String, String> reports;
  
  public ReportRepositoryImpl() {
    reports = new HashMap<String, String>();
  }

  @Override
  public String getFileName(String reportName) {    
    return reports.get(reportName);
  }
  
  public void addFileName(String reportName, String fileName) {
    reports.put(reportName, fileName);
  }

}
