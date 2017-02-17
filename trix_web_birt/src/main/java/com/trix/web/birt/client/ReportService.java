package com.trix.web.birt.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.trix.birt.share.dto.ReportDto;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("report")
public interface ReportService extends RemoteService {
  ReportDto getPojoFilteredReport(String name, String criteriaJSONString)
      throws IllegalArgumentException;
}
