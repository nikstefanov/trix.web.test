package com.trix.web.birt.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.trix.web.birt.shared.dto.ReportContentGwtDto;
import com.trix.web.birt.shared.dto.ReportConfigurationGwtDto;
import com.trix.web.birt.shared.dto.ReportGwtDto;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("report")
public interface ReportService extends RemoteService {
  ReportGwtDto getReportDefinition(String reportName) throws Exception;
  ReportContentGwtDto getReport(String reportName,
      ReportConfigurationGwtDto reportConfigurationGwtDto) throws Exception;
}
