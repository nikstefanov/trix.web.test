package com.trix.report;

import java.util.HashMap;
import java.util.Map;

public class ReportConfiguration {  
  private Map<String, ReportElementConfiguration> elementConfigurationMap;
  private String pageRange;
  
  public ReportConfiguration() {
    elementConfigurationMap = new HashMap<String, ReportElementConfiguration>();
  }
  
  public Map<String, ReportElementConfiguration> getElementConfigurationMap() {
    return elementConfigurationMap;
  }
  
  public void addElementConfiguration(
      String elementName, ReportElementConfiguration reportElementConfiguration)
  {
    elementConfigurationMap.put(elementName, reportElementConfiguration);
  }
  
  public ReportElementConfiguration getReportElementConfiguration(
      String elementName) {
    return elementConfigurationMap.get(elementName);
  }
  
  public boolean hasConfiguration(String elemnetName) {
    return elementConfigurationMap.containsKey(elemnetName);
  }

  public String getPageRange() {
    return pageRange;
  }

  public void setPageRange(String pageRange) {
    this.pageRange = pageRange;
  }  
}
