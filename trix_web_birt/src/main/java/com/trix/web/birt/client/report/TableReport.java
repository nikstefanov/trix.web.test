package com.trix.web.birt.client.report;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.ListBox;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.util.JSON;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.FilterBuilder;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.VLayout;
import com.trix.birt.share.dto.ColumnDto;
import com.trix.birt.share.dto.FilterItemDto;
import com.trix.birt.share.dto.ReportDto;
import com.trix.web.birt.client.ReportServiceAsync;

public class TableReport extends VLayout {
  private String reportName;
  private ListGrid reportGrid;  

  public TableReport(String repName) {
    setWidth100();
    setHeight100();
    reportName = repName;
  }
  
  public void loadReport(String criteriaJSONString) {    
    ReportServiceAsync.Util.getInstance().getPojoFilteredReport(reportName,
        criteriaJSONString, new AsyncCallback<ReportDto>() {
      public void onFailure(Throwable caught) {
        SC.warn("Report loading ERROR");        
      }
      public void onSuccess(ReportDto result) {
        if (null != result){
          if (null == reportGrid){
            build(result);
          }
          loadData(result);
        } else {
          Window.alert("Emitter problems!");
        }
      }
    });
  }  
  
  protected void build(ReportDto reportDto) {
    
    // build report filter
    final FilterBuilder filterBuilder =  new FilterBuilder();
    
    DataSource filterDataSource = new DataSource();
    filterDataSource.setClientOnly(true);
    
    List<DataSourceField> dataSourceFields = new ArrayList<DataSourceField>();
    if (null != reportDto.getFilterItems()) {
      for (FilterItemDto filterItem : reportDto.getFilterItems()) {
        DataSourceTextField f = new DataSourceTextField(
            filterItem.getColumnName(), filterItem.getTitle());
        dataSourceFields.add(f);
      }
      filterDataSource.setFields(dataSourceFields.toArray(new DataSourceField[0]));
      
      filterBuilder.setDataSource(filterDataSource);
      addMember(filterBuilder);
      
      IButton btnFind = new IButton("Find");
      btnFind.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {                    
          loadReport(JSON.encode(filterBuilder.getCriteria().getJsObj()));          
        }
      });
      addMember(btnFind);
    }
    
    final ListBox exportListBox = new ListBox(false);
    exportListBox.setName("reportFormat");
    exportListBox.addItem("Export to..." ,"");
    exportListBox.addItem("pdf" ,"pdf");
    exportListBox.addItem("docx" ,"docx");
    exportListBox.addItem("xlsx" ,"xlsx");
    exportListBox.addItem("pptx" ,"pptx");
    exportListBox.addItem("doc" ,"doc");
    exportListBox.addItem("xls" ,"xls");
    exportListBox.addItem("ppt" ,"ppt");
    exportListBox.addItem("html" ,"html");/*
    exportListBox.addChangeHandler(new ChangeHandler() {
      public void onChange(ChangeEvent event) {
        RequestBuilder request = new RequestBuilder(
            RequestBuilder.POST, "/TrixWebBirt/formattedReport");
        request.setHeader("Accept-Charset", "utf-8");
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");
        request.setRequestData("reportName=" + reportName + "&reportFormat=" +
            exportListBox.getItemText(exportListBox.getSelectedIndex()));
        request.setCallback(new RequestCallback() {
          public void onResponseReceived(Request request, Response response) {
            System.out.println("Response: "+ response.getStatusCode());
            System.out.println(response.getStatusText());
            System.out.println(response.getText());
            System.out.println(response.getHeadersAsString());
          }
          public void onError(Request request, Throwable exception) {
            Window.alert("The server did not respond correctly.");
            exception.printStackTrace();
          }          
        });
        try{
          request.send();
        }catch (RequestException re) {
          Window.alert("Problems sending a requst to the server.");
        }finally{
          exportListBox.setSelectedIndex(0);
        }
      }
    });    
    addMember(exportListBox);
    */
    final FormPanel exportFormPanel = new FormPanel((String)null);
    exportFormPanel.setAction("/TrixWebBirt/formattedReport");
    exportFormPanel.setMethod(FormPanel.METHOD_POST);
    exportFormPanel.setEncoding(FormPanel.ENCODING_URLENCODED);
    final Hidden reportNameHidden = new Hidden("reportName", reportName);
    final Hidden criteriaJSONStringHidden = new Hidden("cirteriaJSONString");
    Grid formGrid = new Grid(1,3);    
    formGrid.setWidget(0,0,exportListBox);
    formGrid.setWidget(0,1,reportNameHidden);
    formGrid.setWidget(0,2,criteriaJSONStringHidden);
    exportFormPanel.setWidget(formGrid);
    exportListBox.addChangeHandler(new ChangeHandler() {
      public void onChange(ChangeEvent event) {
        criteriaJSONStringHidden.setValue(
            JSON.encode(filterBuilder.getCriteria().getJsObj()));
        exportFormPanel.submit();
        exportListBox.setSelectedIndex(0);
      }
    });
    addMember(exportFormPanel);
    
    // build report grid
    reportGrid = new ListGrid();  
    reportGrid.setWidth100();  
    reportGrid.setHeight100();  
    reportGrid.setShowAllRecords(true);
    
    List<ListGridField> listGridFields = new ArrayList<ListGridField>();
    for (ColumnDto column : reportDto.getColumns()) {
      ListGridField field = new ListGridField(column.getName(),
          column.getTitle().getLabelText());
      if (column.getWidth() > 0) {
        field.setWidth((int)column.getWidth());
      }
      listGridFields.add(field);
    }
    
    reportGrid.setFields(listGridFields.toArray(new ListGridField[0]));
    addMember(reportGrid);
  }
  
  public void loadData(ReportDto reportDto) {
    if (null == reportGrid) {
      return;
    }

    JavaScriptObject jsObj = JSON.decode(reportDto.getJsonData());    
    RecordList recordList = new RecordList(jsObj);
    
    reportGrid.setData(recordList);    
  }
}
