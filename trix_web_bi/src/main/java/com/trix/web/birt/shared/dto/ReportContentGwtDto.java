package com.trix.web.birt.shared.dto;

import java.io.Serializable;

public class ReportContentGwtDto implements Serializable {
  private static final long serialVersionUID = -7340122336979481280L;
  private String content;
  private String pageRange; 
  private int rowsPerPage;
  private long totalPages;
  private String format;
  public String getContent() {
    return content;
  }
  public void setContent(String content) {
    this.content = content;
  }
  public String getPageRange() {
    return pageRange;
  }
  public void setPageRange(String pageRange) {
    this.pageRange = pageRange;
  }
  public long getTotalPages() {
    return totalPages;
  }
  public void setTotalPages(long totalPages) {
    this.totalPages = totalPages;
  }
  public int getRowsPerPage() {
    return rowsPerPage;
  }
  public void setRowsPerPage(int rowsPerPage) {
    this.rowsPerPage = rowsPerPage;
  }
  public String getFormat() {
    return format;
  }
  public void setFormat(String format) {
    this.format = format;
  }
  
}
