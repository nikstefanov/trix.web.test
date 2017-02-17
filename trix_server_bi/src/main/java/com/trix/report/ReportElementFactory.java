package com.trix.report;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.TableHandle;

import com.trix.report.element.ReportCrossTabElementImpl;
import com.trix.report.element.ReportTableElementImpl;

public class ReportElementFactory{
  public ReportElement create(DesignElementHandle elementHandle) 
      throws Exception {
    ReportElement reportElement;
    if (elementHandle instanceof TableHandle) {
      reportElement = new ReportTableElementImpl();
    } else {
      if (elementHandle instanceof ExtendedItemHandle) {
        reportElement = new ReportCrossTabElementImpl(); 
      } else {
        throw new Exception("Unsuported Element");
      }
    }
    reportElement.setElementhandle(elementHandle);
    return reportElement;
  }

}
