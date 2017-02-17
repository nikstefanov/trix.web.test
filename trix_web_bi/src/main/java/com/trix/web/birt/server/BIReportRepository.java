package com.trix.web.birt.server;

import com.trix.report.repository.impl.ReportRepositoryImpl;

public class BIReportRepository extends ReportRepositoryImpl {
  public BIReportRepository() {
    super();    
    String DesignFolder = 
//        "D://Users//User//Development//Workspaces//trix.web.test//"
//        + "trix_web_bi//src//main//resources"
//        + "//com//trix//web//birt//reports//";
        "";
    addFileName("AppointmentRequests", DesignFolder + "AppointmentRequests.rptdesign");
    addFileName("CrossTabExample", DesignFolder + "CrossTabExample.rptdesign");
    addFileName("SimpleTableFilters", DesignFolder + "SimpleTableFilters.rptdesign");
    addFileName("PatientsCrossTab", DesignFolder + "PatientsCrossTab.rptdesign");
  }
}
