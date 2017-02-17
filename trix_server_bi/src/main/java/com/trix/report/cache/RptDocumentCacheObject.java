package com.trix.report.cache;

import java.util.Date;

public class RptDocumentCacheObject {
  private Date createTime;
  private long lastAccessTime;
  private String fileName;
  private boolean locked;
  
  public Date getCreateTime() {
    return createTime;
  }
  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }
  public long getLastAccessTime() {
    return lastAccessTime;
  }
  public void setLastAccessTime(long lastAccessTime) {
    this.lastAccessTime = lastAccessTime;
  }
  public String getFileName() {
    return fileName;
  }
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
  
  public boolean isLocked() {
    return locked;
  }
  
  public void lock() {
    locked = true;    
  }
  
  public void unlock() {
    locked = false;
  }

}
