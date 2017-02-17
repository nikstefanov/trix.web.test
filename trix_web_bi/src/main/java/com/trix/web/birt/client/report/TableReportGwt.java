package com.trix.web.birt.client.report;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.layout.VLayout;
import com.trix.web.birt.client.ReportServiceAsync;
import com.trix.web.birt.shared.dto.ReportConfigurationGwtDto;
import com.trix.web.birt.shared.dto.ReportElementGwtDto;
import com.trix.web.birt.shared.dto.ReportGwtDto;

public class TableReportGwt extends VLayout
    implements AsyncCallback<ReportGwtDto>, ClickHandler {
  private String reportName;
  private List<ConfigurationPanel> configurationPanelList;
  private final HTML htmlWidget;
  private final Button getReportButton;

  public TableReportGwt(String repName) {
    setWidth100();
    setHeight100();
    reportName = repName;
    configurationPanelList = new ArrayList<ConfigurationPanel>();
    htmlWidget = new HTML();
    getReportButton = new Button("Get Report", this);
  }
  
  public void onFailure(Throwable caught) {
    SC.warn("Report loading ERROR");        
  }
  
  public void onSuccess(ReportGwtDto reportGwtDto) {
    for (ReportElementGwtDto reportElementGwtDto :
      reportGwtDto.getElementDtoList()) {
      ConfigurationPanel configurationPanel =
          new ConfigurationPanel(reportElementGwtDto);
      configurationPanel.setHeight(100);
      configurationPanelList.add(configurationPanel);
      addMember(configurationPanel);
    }
    addMember(getReportButton);
    addMember(htmlWidget);
  }
  
  public void loadDefinition() {
    ReportServiceAsync.Util.getInstance().getReportDefinition(reportName,this);
  }
  
  public void onClick(ClickEvent event) {
    ReportConfigurationGwtDto reportConfigurationGwtDto =
        new ReportConfigurationGwtDto();
    for (ConfigurationPanel configurationPanel : configurationPanelList) {
      reportConfigurationGwtDto.addElementConfiguration(
          configurationPanel.getElementName(),
          configurationPanel.getElementConfiguration());
    }
    ReportServiceAsync.Util.getInstance().getReport(reportName,
        reportConfigurationGwtDto,
        new AsyncCallback<String>() {
      public void onFailure(Throwable caught) {
        SC.warn("Report loading ERROR");        
      }
      public void onSuccess(String result) {
        htmlWidget.setHTML(result);        
      }
    });
  }
  
  
}
