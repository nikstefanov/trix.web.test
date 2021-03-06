package com.trix.report;

import java.util.ArrayList;
import java.util.List;

public class ReportElementConfiguration {  
  private String criteriaJSON;
  private List<String> xtColumns;
  private List<String> xtRows;
  private List<String> xtSummaries;
  
  public ReportElementConfiguration() {
    criteriaJSON = "";
    xtColumns = new ArrayList<String>();
    xtRows = new ArrayList<String>();
    xtSummaries = new ArrayList<String>();
  }
  
  public void setCriteriaJSON(String criteriaJSON) {
    this.criteriaJSON = criteriaJSON;
  }
  public String getCriteriaJSON() {
    return criteriaJSON;
  }
  public List<String> getXtColumns() {
    return xtColumns;
  }
  public List<String> getXtRows() {
    return xtRows;
  }
  public List<String> getXtSummaries() {
    return xtSummaries;
  }
  public void addXtColumn(String columnName) {
    if (xtColumns == null) {
      xtColumns = new ArrayList<String>();
    }
    xtColumns.add(columnName);
  }
  public void addXtRow(String rowName) {
    if (xtRows == null) {
      xtRows = new ArrayList<String>();
    }
    xtRows.add(rowName);
  }
  public void addXtSummary(String summaryName) {
    if (xtSummaries == null) {
      xtSummaries = new ArrayList<String>();
    }
    xtSummaries.add(summaryName);
  }
  public void setXtColumns(List<String> xtColumns) {
    this.xtColumns = xtColumns;
  }
  public void setXtRows(List<String> xtRows) {
    this.xtRows = xtRows;
  }
  public void setXtSummaries(List<String> xtSummaries) {
    this.xtSummaries = xtSummaries;
  }
}
