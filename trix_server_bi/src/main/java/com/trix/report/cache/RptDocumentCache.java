package com.trix.report.cache;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import com.trix.report.ReportConfiguration;
import com.trix.report.ReportElementConfiguration;

public class RptDocumentCache {
  private String directoryName;
  private Map<String, RptDocumentCacheObject> cacheMap;
  // times in ms
  private long cacheTTL;
  private long lastPurgeCallTime;
  private long purgeCallInterval;
  
  public RptDocumentCache(
      String directoryName, long cacheTTL, long purgeCallInterval) {
    if (directoryName.charAt(directoryName.length()-1) != File.separatorChar) {
      directoryName = directoryName +File.separatorChar;
    }
    this.directoryName = directoryName;
    this.cacheTTL = cacheTTL;
    this.purgeCallInterval = purgeCallInterval;
    this.cacheMap = new HashMap<String, RptDocumentCacheObject>();
  } 
  /*
  public String store(String reportName,
      ReportConfiguration reportConfiguration) {
    String cacheKey = reportName + getStringPresentation(reportConfiguration);
//    String cacheKey = reportName + getHashCode(reportConfiguration);
    RptDocumentCacheObject rptDocumentCacheObject =
        new RptDocumentCacheObject();
    rptDocumentCacheObject.setFileName(
        directoryName + reportName + (new Random()).nextInt() + ".rptdocument");
    Date currentTime = new Date();
    rptDocumentCacheObject.setLastAccessTime(currentTime.getTime());
    rptDocumentCacheObject.setCreateTime(currentTime);
    cacheMap.put(cacheKey, rptDocumentCacheObject);
    return rptDocumentCacheObject.getFileName();
  }
  
  public String retrive(String reportName,
      ReportConfiguration reportConfiguration) {
    String rptDocumentFileName;
    String cacheKey = reportName + getStringPresentation(reportConfiguration);
//    String cacheKey = reportName + getHashCode(reportConfiguration);
    RptDocumentCacheObject rptDocumentCacheObject = cacheMap.get(cacheKey);
    if (rptDocumentCacheObject != null) {
      rptDocumentCacheObject.setLastAccessTime(new Date().getTime());
      rptDocumentFileName = rptDocumentCacheObject.getFileName();
    } else {
      rptDocumentFileName = null;
    }
    purge();
    return rptDocumentFileName;
  }
  */
  
  public String store(String reportName, String cacheKey) {
    RptDocumentCacheObject rptDocumentCacheObject = cacheMap.get(cacheKey);
    rptDocumentCacheObject.setFileName(
        directoryName + reportName + (new Random()).nextInt() + ".rptdocument");
    Date currentTime = new Date();
    rptDocumentCacheObject.setLastAccessTime(currentTime.getTime());
    rptDocumentCacheObject.setCreateTime(currentTime);    
    return rptDocumentCacheObject.getFileName();
  }
  
  public String retrive(String cacheKey) {    
    RptDocumentCacheObject rptDocumentCacheObject = cacheMap.get(cacheKey);    
    String rptDocumentFileName = rptDocumentCacheObject.getFileName();
    if (rptDocumentFileName != null) {
      rptDocumentCacheObject.setLastAccessTime(new Date().getTime());
      purge();
    }    
    return rptDocumentFileName;
  }
  
  public String lock(String reportName, ReportConfiguration reportConfiguration)
      throws InterruptedException {    
    String cacheKey = reportName + getStringPresentation(reportConfiguration);
    RptDocumentCacheObject rptDocumentCacheObject = null;
    synchronized(this) {
      if (cacheMap.containsKey(cacheKey)) {
        rptDocumentCacheObject = cacheMap.get(cacheKey);        
      } else {
        rptDocumentCacheObject = new RptDocumentCacheObject();
        cacheMap.put(cacheKey, rptDocumentCacheObject);
      }
    }
    while(true) {
      synchronized(rptDocumentCacheObject) {
        if (!rptDocumentCacheObject.isLocked()) {
          rptDocumentCacheObject.lock();
          break;
        }
        Thread.sleep(3000);
      }
    }
    return cacheKey;
  }
  
  public void unlock(String cacheKey) {
    cacheMap.get(cacheKey).unlock();
  }
  
  public String getDirectoryName() {
    return directoryName;
  }
  
  public void setDirectoryName(String directoryName) {
    this.directoryName = directoryName;
  }

  public long getCacheTTL() {
    return cacheTTL;
  }

  public void setCacheTTL(long cacheTTL) {
    this.cacheTTL = cacheTTL;
  }  
  /*
  private int getHashCode(ReportConfiguration reportConfiguration) {
    Map<String, ReportElementConfiguration> reportConfigurationMap =
        reportConfiguration.getElementConfigurationMap();
    
    LinkedHashMap<String, ReportElementConfiguration> orderedConfigurationMap =
    new LinkedHashMap<String, ReportElementConfiguration>(
        reportConfigurationMap.size());
    TreeSet<String> orderedKeysSet =
      new TreeSet<String>(reportConfigurationMap.keySet());
    
    String mapKey;
    Iterator<String> keyIterator = orderedKeysSet.iterator();    
    while (keyIterator.hasNext()) {
      mapKey = keyIterator.next();
      orderedConfigurationMap.put(mapKey, reportConfigurationMap.get(mapKey));
    }
    return orderedConfigurationMap.hashCode();
  }*/
  
  public long getPurgeCallInterval() {
    return purgeCallInterval;
  }

  public void setPurgeCallInterval(long purgeCallInterval) {
    this.purgeCallInterval = purgeCallInterval;
  }

  private String getStringPresentation(ReportConfiguration reportConfiguration){
    Map<String, ReportElementConfiguration> reportConfigurationMap =
        reportConfiguration.getElementConfigurationMap();
    // TreeSet is ordered set. Here can be used a list also. Colsection.sort(List<T> list);
    TreeSet<String> orderedKeysSet =
      new TreeSet<String>(reportConfigurationMap.keySet());
    
    StringBuilder stringPresentation = new StringBuilder();
    String mapKey;
    Iterator<String> keyIterator = orderedKeysSet.iterator();    
    while (keyIterator.hasNext()) {
      mapKey = keyIterator.next();
      stringPresentation.append(mapKey);
      appendStringPresentation(stringPresentation,
          reportConfigurationMap.get(mapKey));
    }
    return stringPresentation.toString();
  }
    
  private void appendStringPresentation(StringBuilder stringPresentation,
      ReportElementConfiguration reportElementConfiguration) {
    stringPresentation.append(reportElementConfiguration.getCriteriaJSON());
    stringPresentation.append(
        Arrays.toString(reportElementConfiguration.getXtColumns().toArray()));
    stringPresentation.append(
        Arrays.toString(reportElementConfiguration.getXtRows().toArray()));
    stringPresentation.append(
        Arrays.toString(reportElementConfiguration.getXtSummaries().toArray()));
  }
  
  private synchronized void purge() {
    long timeNow = (new Date()).getTime();
    if (timeNow - lastPurgeCallTime >= purgeCallInterval) {
//      System.out.println("purge()");
      lastPurgeCallTime = timeNow;
      Set<Map.Entry<String, RptDocumentCacheObject>> cacheMapEntrySet =
          cacheMap.entrySet();
      Iterator<Map.Entry<String, RptDocumentCacheObject>> cacheMapEntrySetIterator
          = cacheMapEntrySet.iterator();      
      RptDocumentCacheObject rptDocumentCacheObject;      
      while(cacheMapEntrySetIterator.hasNext()) {
        rptDocumentCacheObject = cacheMapEntrySetIterator.next().getValue();
//        System.out.println("LA"+rptDocumentCacheObject.getLastAccessTime());
        if (timeNow - rptDocumentCacheObject.getLastAccessTime() >= cacheTTL) {
//          System.out.println(rptDocumentCacheObject.getFileName());
          (new File(rptDocumentCacheObject.getFileName())).delete();
          cacheMapEntrySetIterator.remove();
        }
      }
    }
  }
  
  public boolean emptyCache() {
    Collection<RptDocumentCacheObject> cacheObjectCollection = cacheMap.values();
    Iterator<RptDocumentCacheObject> cacheObjectIterator =
        cacheObjectCollection.iterator();
    boolean success = true;
    while (cacheObjectIterator.hasNext()) {
      success &=
          (new File(directoryName, cacheObjectIterator.next().getFileName())).delete();
    }
    cacheMap.clear();
    lastPurgeCallTime = (new Date()).getTime();
    return success;
  }
}
