package com.trix.web.birt.shared.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ReportConfigurationGwtDto implements Serializable {
  private static final long serialVersionUID = -7340122336979481200L;
  private Map<String, ReportElementConfigurationGwtDto> elementConfigurationMap;
  private String pageRange;
  
  public ReportConfigurationGwtDto() {
    elementConfigurationMap =
        new HashMap<String, ReportElementConfigurationGwtDto>();
  }
  
  public Map<String, ReportElementConfigurationGwtDto>
      getElementConfigurationMap() {
    return elementConfigurationMap;
  }
  
  public void addElementConfiguration(String elementName,
      ReportElementConfigurationGwtDto reportElementConfigurationGwtDto) {
    elementConfigurationMap.put(elementName, reportElementConfigurationGwtDto);
  }
  
  public ReportElementConfigurationGwtDto getReportElementConfiguration(
      String elementName) {
    return elementConfigurationMap.get(elementName);
  }

  public String getPageRange() {
    return pageRange;
  }

  public void setPageRange(String pageRange) {
    this.pageRange = pageRange;
  }
  
}
