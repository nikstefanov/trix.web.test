package com.trix.web.birt.client.report;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.VLayout;
import com.trix.birt.share.dto.ReportContentDto;
import com.trix.web.birt.client.service.ReportServiceAsync;

public class TableReport extends VLayout {
  public TableReport() {
    setWidth100();
    setHeight100();
  }
  
  public void load() {
    // load report from server
    ReportServiceAsync.Util.getInstance().getPojoReport("SimpleTable",
        new AsyncCallback<ReportContentDto>() {
          public void onSuccess(ReportContentDto result) {
            SC.warn("Report loaded");
          }
          
          public void onFailure(Throwable caught) {
            caught.printStackTrace();
            SC.warn("Report loading ERROR");
          }
        });
  }
  
  /**
   * TODO: pass ReportDto as param
   */
  protected void build(ReportContentDto reportContentDto) {
    final ListGrid reportGrid = new ListGrid();  
    reportGrid.setWidth100();  
    reportGrid.setHeight100();  
    reportGrid.setShowAllRecords(true);
    
    ListGridField nameField = new ListGridField("countryName", "Country");
    
    reportGrid.setFields(nameField);
    
    addMember(reportGrid);
  }
}
