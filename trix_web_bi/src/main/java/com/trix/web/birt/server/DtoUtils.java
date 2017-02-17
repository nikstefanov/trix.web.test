package com.trix.web.birt.server;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import com.trix.report.ReportConfiguration;
import com.trix.report.ReportContent;
import com.trix.report.ReportElementConfiguration;
import com.trix.report.dto.ReportDto;
import com.trix.report.dto.ReportElementDto;
import com.trix.web.birt.shared.dto.ReportConfigurationGwtDto;
import com.trix.web.birt.shared.dto.ReportContentGwtDto;
import com.trix.web.birt.shared.dto.ReportElementConfigurationGwtDto;
import com.trix.web.birt.shared.dto.ReportElementGwtDto;
import com.trix.web.birt.shared.dto.ReportGwtDto;

public class DtoUtils {
  
  public static ReportGwtDto createReportGwtDto(ReportDto reportDto) {
    ReportGwtDto reportGwtDto = new ReportGwtDto();
    for (int i = 0; i < reportDto.size(); i++) {
      reportGwtDto.addElementGwtDto(
          createReportElementGwtDto(reportDto.getElementDto(i)));
    }
    return reportGwtDto;
  }
  
  private static ReportElementGwtDto createReportElementGwtDto(
      ReportElementDto reportElementDto) {
    ReportElementGwtDto reportElementGwtDto = new ReportElementGwtDto();
    reportElementGwtDto.setName(          reportElementDto.getName());
    reportElementGwtDto.setFiltersJson(   reportElementDto.getFiltersJson());
    reportElementGwtDto.setDimensionsJson(reportElementDto.getDimensionsJson());
    reportElementGwtDto.setSummariesJson( reportElementDto.getSummariesJson());
    return reportElementGwtDto;
  }
  
  public static ReportConfiguration createReportConfiguration(
      ReportConfigurationGwtDto reportConfigurationGwtDto) {
    ReportConfiguration reportConfiguration = new ReportConfiguration();
    for (Map.Entry<String, ReportElementConfigurationGwtDto> entry :
      reportConfigurationGwtDto.getElementConfigurationMap().entrySet()) {
      reportConfiguration.addElementConfiguration(entry.getKey(),
          createReportElementConfiguration(entry.getValue()));
    }
    reportConfiguration.setPageRange(reportConfigurationGwtDto.getPageRange());
    return reportConfiguration;
  }
  
  private static ReportElementConfiguration createReportElementConfiguration(
      ReportElementConfigurationGwtDto reportElementConfigurationGwtDto) {
    ReportElementConfiguration reportElementConfiguration =
        new ReportElementConfiguration();
    reportElementConfiguration.setCriteriaJSON(
        reportElementConfigurationGwtDto.getCriteriaJSON());
    reportElementConfiguration.setXtColumns(
        reportElementConfigurationGwtDto.getXtColumns());
    reportElementConfiguration.setXtRows(
        reportElementConfigurationGwtDto.getXtRows());
    reportElementConfiguration.setXtSummaries(
        reportElementConfigurationGwtDto.getXtSummaries());
    return reportElementConfiguration;
  }
  
  public static ReportContentGwtDto createReportContentGwtDto(
      ReportContent reportContent) {
    ReportContentGwtDto reportContentGwtDto = new ReportContentGwtDto();
    reportContentGwtDto.setContent(reportContent.getContent());
    reportContentGwtDto.setFormat(reportContent.getFormat());
    reportContentGwtDto.setPageRange(reportContent.getPageRange());
    reportContentGwtDto.setRowsPerPage(reportContent.getRowsPerPage());
    reportContentGwtDto.setTotalPages(reportContent.getTotalPages());
    return reportContentGwtDto;
  }
  
  public static void printReportConfiguration(
      ReportConfiguration reportConfiguration) {
    for (Entry<String, ReportElementConfiguration> entry :
      reportConfiguration.getElementConfigurationMap().entrySet()) {
      System.out.println(entry.getKey());
      printReportElementConfiguration(entry.getValue());
      System.out.println();
    }
  }
  
  private static void printReportElementConfiguration(
      ReportElementConfiguration reportElementConfiguration) {
    System.out.println(reportElementConfiguration.getCriteriaJSON());
    System.out.println(Arrays.toString(reportElementConfiguration.getXtColumns().toArray()));
    System.out.println(Arrays.toString(reportElementConfiguration.getXtRows().toArray()));
    System.out.println(Arrays.toString(reportElementConfiguration.getXtSummaries().toArray()));
  }
}
