package com.trix.report;

import org.eclipse.birt.report.model.api.DesignElementHandle;

public abstract class ReportElement {
  protected DesignElementHandle elementHandle;
  
  public DesignElementHandle getElementhandle() {
    return elementHandle;
  }

  public void setElementhandle(DesignElementHandle elementHandle) {
    this.elementHandle = elementHandle;
  }

  public abstract void configure (
      ReportElementConfiguration reportElementConfiguration) throws Exception;
}
