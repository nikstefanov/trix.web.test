package com.trix.web.birt.client.dto;

import java.io.Serializable;

public class ReportElementGwtDto implements Serializable {
  private static final long serialVersionUID = 7422338759809859817L;
  private String name;
  private String filtersJson;
  private String dimensionsJson;
  private String summariesJson;
  
  public ReportElementGwtDto() {
    name = null;
    filtersJson = null;
    dimensionsJson = null;
    summariesJson = null;
  }
  
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getFiltersJson() {
    return filtersJson;
  }
  public void setFiltersJson(String filtersJson) {
    this.filtersJson = filtersJson;
  }
  public String getDimensionsJson() {
    return dimensionsJson;
  }
  public void setDimensionsJson(String dimensionsJson) {
    this.dimensionsJson = dimensionsJson;
  }
  public String getSummariesJson() {
    return summariesJson;
  }
  public void setSummariesJson(String summariesJson) {
    this.summariesJson = summariesJson;
  }

}
