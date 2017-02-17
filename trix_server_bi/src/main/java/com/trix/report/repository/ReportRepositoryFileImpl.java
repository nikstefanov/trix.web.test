package com.trix.report.repository;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.trix.report.ReportRepository;

public class ReportRepositoryFileImpl implements ReportRepository{
  private Map<String, String> fileNameMap;
  
  public ReportRepositoryFileImpl() {
    fileNameMap = new HashMap<String, String>();
  }

  public InputStream getInputStream(String reportName) throws Exception{
    return new FileInputStream(fileNameMap.get(reportName));
  }

  public String getFileName(String reportName) {    
    return fileNameMap.get(reportName);
  }
  
  public void addFileName(String reportName, String fileName) {
    fileNameMap.put(reportName, fileName);
  }

}
