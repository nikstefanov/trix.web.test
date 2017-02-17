package com.trix.web.birt.client.report;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.TextBox;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.layout.VLayout;
import com.trix.web.birt.client.ReportServiceAsync;
import com.trix.web.birt.shared.dto.ReportConfigurationGwtDto;
import com.trix.web.birt.shared.dto.ReportContentGwtDto;
import com.trix.web.birt.shared.dto.ReportElementGwtDto;
import com.trix.web.birt.shared.dto.ReportGwtDto;

public class TableReport extends VLayout
    implements AsyncCallback<ReportGwtDto>,  ClickHandler {
  private String reportName;
  private List<ConfigurationPanel> configurationPanelList;
  private final HTMLPane htmlPane;
  private final Button getReportButton;
  private final Grid pagesGrid;
  private final TextBox pageTextBox;

  public TableReport(String repName) {
    setWidth100();
    setHeight100();
    reportName = repName;
    configurationPanelList = new ArrayList<ConfigurationPanel>();
    htmlPane = new HTMLPane();
    htmlPane.setWidth100();  
    htmlPane.setHeight100();  
    htmlPane.setShowEdges(true);  
    htmlPane.setStyleName("exampleTextBlock");  
    getReportButton = new Button("Get Report", this);
    pageTextBox = new TextBox();
    pageTextBox.setVisibleLength(10);
    pagesGrid = new Grid (1,5);
    pagesGrid.setText(0, 0, "Available Pages:");
    pagesGrid.setText(0, 2, "Page Range:");
    pagesGrid.setWidget(0, 3, pageTextBox);
   
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
    addMember(pagesGrid);
    addMember(getReportButton);
    addMember(htmlPane);
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
    reportConfigurationGwtDto.setPageRange(pageTextBox.getValue().trim());
    ReportServiceAsync.Util.getInstance().getReport(reportName,
        reportConfigurationGwtDto,
        new AsyncCallback<ReportContentGwtDto>() {
      public void onFailure(Throwable caught) {
        SC.warn("Report loading ERROR");        
      }
      public void onSuccess(ReportContentGwtDto reportContent) {
        pagesGrid.setText(0, 1, "" + reportContent.getTotalPages());
        htmlPane.setContents(reportContent.getContent());        
      }
    });
  }
  
  
}
