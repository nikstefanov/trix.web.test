package com.trix.report.dto;

import java.util.ArrayList;
import java.util.List;

public class ReportDto {
  private List<ReportElementDto> elementDtoList;
  
  public ReportDto() {
    elementDtoList = new ArrayList<ReportElementDto>();
  }
  
  public int size() {
    return elementDtoList.size();
  }
  
  public void addElementDto(ReportElementDto reportElementDto) {
    elementDtoList.add(reportElementDto);
  }
  
  public ReportElementDto getElementDto(int index) {
    return elementDtoList.get(index);
  }
  
  public void clear() {
    elementDtoList.clear();
  }
  
  public List<ReportElementDto> getElementDtoList() {
    return elementDtoList;
  }
}
