package com.trix.web.birt.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.trix.birt.share.dto.ReportContentDto;

@RemoteServiceRelativePath("reportService")
public interface ReportService extends RemoteService {
  public String getHtmlReport(String key);
  public ReportContentDto getPojoReport(String key);
  
}
