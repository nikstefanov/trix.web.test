package com.trix.web.birt.client.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReportGwtDto implements Serializable {
  private static final long serialVersionUID = 3410554828867373341L;
  private List<ReportElementGwtDto> elementDtoList;
  
  public ReportGwtDto() {
    elementDtoList = new ArrayList<ReportElementGwtDto>();
  }
  
  public int size() {
    return elementDtoList.size();
  }
  
  public void addElementGwtDto(ReportElementGwtDto reportElementGwtDto) {
    elementDtoList.add(reportElementGwtDto);
  }
  
  public ReportElementGwtDto getElementDto(int index) {
    return elementDtoList.get(index);
  }
  
//  public void clear() {
//    elementDtoList.clear();
//  }
  
  public List<ReportElementGwtDto> getElementDtoList() {
    return elementDtoList;
  }
}
