package com.trix.web.birt.server.dfo;

import java.io.Serializable;
import java.util.List;

public class DataFetchRes<T> implements Serializable {
  private static final long serialVersionUID = 3799655485623378938L;

  protected List<T> list;
  protected int startRow;
  protected int endRow;

  protected int rowsCount;

  public DataFetchRes() {

  }

  public DataFetchRes(List<T> list) {
    this.list=list;
  }

  public void setList(List<T> list) {
    this.list = list;
  }

  public List<T> getList() {
    return list;
  }

  public int getRowsCount() {
    return rowsCount;
  }

  public void setRowsCount(int rowsCount) {
    this.rowsCount = rowsCount;
  }

  public int getStartRow() {
    return startRow;
  }

  public void setStartRow(int startRow) {
    this.startRow = startRow;
  }

  public int getEndRow() {
    return endRow;
  }

  public void setEndRow(int endRow) {
    this.endRow = endRow;
  }
}
