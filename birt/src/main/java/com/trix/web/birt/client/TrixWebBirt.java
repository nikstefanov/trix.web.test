package com.trix.web.birt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.trix.birt.share.dto.ReportContentDto;
import com.trix.web.birt.client.report.TableReport;
import com.trix.web.birt.client.service.ReportServiceAsync;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TrixWebBirt implements EntryPoint {
  /*
  public void onModuleLoad() {
    Canvas layout = new Canvas();
    layout.setWidth100();
    layout.setHeight100();
    layout.setMargin(5);
    
    TableReport report = new TableReport();
    layout.addChild(report);
    
    layout.draw();

    report.load();
  }
  */
  
  
  
 
  public void onModuleLoad() {
    final HTML htmlWidget = new HTML();
    final TextArea textWidget = new TextArea();
    textWidget.setVisibleLines(150);
    textWidget.setCharacterWidth(150);    
//    final HtmlReportServiceAsync htmlReportSvc = GWT.create(HtmlReportService.class);    
//    final AsyncCallback<String> callback = new AsyncCallback<String>() {
//      public void onFailure(Throwable caught) {
//        Window.alert("Error occured during communication with server!");
//      }
//      public void onSuccess(String result) {
//        htmlWidget.setHTML(result);
//        textWidget.setValue(result);
//      }
//    };    
    
    Grid firstLineGrid = new Grid(1,5);
    firstLineGrid.setWidget(0,0,new InlineLabel("Report:"));
    
    final ListBox reportListBox = new ListBox(false);
    reportListBox.addItem("CustomersMySQL", "CustomersMySQLSource");
    reportListBox.addItem("CustomersAggregationMySQL",
        "CustomersAggregationMySQL");
    reportListBox.addItem("CustomersAggregation",        
        "CustomersAggregation");
    reportListBox.addItem("Customers", "Customers");
    reportListBox.addItem("SalesByStateAndProductLine",        
        "SalesByStateAndProductLine");
    reportListBox.addItem("SimpleTable", "SimpleTable");
//    reportListBox.addItem("test", "test");
//    reportListBox.addItem("test1", "test1");
    firstLineGrid.setWidget(0,1,reportListBox);
    
    firstLineGrid.setWidget(0,2,new InlineLabel("Format:"));
    
    final ListBox formatListBox = new ListBox(false);    
    formatListBox.addItem("html","html");
    formatListBox.addItem("pojo","POJO");
    firstLineGrid.setWidget(0,3,formatListBox);
    
    final AsyncCallback<String> callbackHtml = new AsyncCallback<String>() {
      public void onFailure(Throwable caught) {
        Window.alert("Error occured during communication with server!");
      }
      public void onSuccess(String result) {        
          htmlWidget.setHTML (result);
          textWidget.setValue(result);    
      }
    };
    
    final AsyncCallback<ReportContentDto> callbackPojo =
        new AsyncCallback<ReportContentDto>() {
      public void onFailure(Throwable caught) {
        Window.alert("Error occured during communication with server!");
        caught.printStackTrace();
      }
      public void onSuccess(ReportContentDto result) {        
        htmlWidget.setHTML(
            "<table>"+
                "<tr>"+
                  "<td>"+
                    result.getPagesList().get(0).getTablesList().get(0)
                      .getRowsList().get(0).getCellsList().get(0)
                      .getLabelContentValue().getLabelText() +
                  "</td>"+
                  "<td>"+
                    result.getPagesList().get(0).getTablesList().get(0)
                    .getRowsList().get(0).getCellsList().get(1)
                    .getLabelContentValue().getLabelText() +
                  "</td>"+
                  "<td>"+
                    result.getPagesList().get(0).getTablesList().get(0)
                    .getRowsList().get(0).getCellsList().get(2)
                    .getLabelContentValue().getLabelText() +
                  "</td>"+
                "</tr>"+
                "<tr>"+
                  "<td>"+
                    result.getPagesList().get(0).getTablesList().get(0)
                    .getRowsList().get(1).getCellsList().get(0)
                    .getDataContentValue().getStringValue() +                   
                  "</td>"+
                  "<td>"+
                    result.getPagesList().get(0).getTablesList().get(0)
                    .getRowsList().get(1).getCellsList().get(1)
                    .getDataContentValue().getStringValue() +
                  "</td>"+
                  "<td>"+
                    result.getPagesList().get(0).getTablesList().get(0)
                    .getRowsList().get(1).getCellsList().get(2)
                    .getDataContentValue().getStringValue() +
                  "</td>"+
                "</tr>"+
                "<tr>"+
                  "<td>"+
                  "</td>"+
                  "<td>"+
                  "</td>"+
                  "<td>"+
                  "</td>"+
                "</tr>"+
            "</table>"    
            );
        textWidget.setValue("");  
      }
    };
   
    Button submitButton = new Button("Go for it!", new ClickHandler() {
      public void onClick(ClickEvent event) {        
//        htmlReportSvc.getHtmlReport(
//            reportListBox.getValue(reportListBox.getSelectedIndex()),
//            callback);
        if (formatListBox.getSelectedIndex() == 0) {
        ReportServiceAsync.Util.getInstance().getHtmlReport(
            reportListBox.getValue(reportListBox.getSelectedIndex()),
            callbackHtml);
        }else if (formatListBox.getSelectedIndex() == 1) {
          ReportServiceAsync.Util.getInstance().getPojoReport(
              reportListBox.getValue(reportListBox.getSelectedIndex()),
              callbackPojo);
        }
      }
    });
    firstLineGrid.setWidget(0,4,submitButton);
    
    
    
    RootPanel.get("rootPanel").add(firstLineGrid);
    RootPanel.get("rootPanel").add(htmlWidget);
    RootPanel.get("rootPanel").add(textWidget);
  }
   
  /**
   * 
   * This is the entry point method.
   */
  public void onModuleLoadOld() {
    final FormPanel form = new FormPanel((String)null);
    form.setAction("/preview");
    form.setEncoding("UTF-8");
    form.setMethod("GET");
    Grid gr = new Grid(1,14);
    
    ListBox reportListBox = new ListBox(false);   
    reportListBox.setName("__report");
    reportListBox.addItem("CustomersMySQL",
        "C:\\Users\\User\\Documents\\Development\\Workspaces\\trix.test\\" +
        "test_birt_report\\CustomersMySQLSource.rptdesign");
    reportListBox.addItem("CustomersAggregationMySQL",
        "C:\\Users\\User\\Documents\\Development\\Workspaces\\trix.test\\" +
        "test_birt_report\\CustomersAggregationMySQL.rptdesign");
    reportListBox.addItem("CustomersAggregation",
        "C:\\Users\\User\\Documents\\Development\\Workspaces\\trix.test\\" +
        "test_birt_report\\CustomersAggregation.rptdesign");
    reportListBox.addItem("Customers",
        "C:\\Users\\User\\Documents\\Development\\Workspaces\\trix.test\\" +
        "test_birt_report\\Customers.rptdesign");
    reportListBox.addItem("SalesByStateAndProductLine",
        "C:\\Users\\User\\Documents\\Development\\Workspaces\\trix.test\\" +
        "test_birt_report\\SalesByStateAndProductLine.rptdesign");
    reportListBox.addItem("test", "test.rptdesign");
    reportListBox.addItem("test1", "test1.rptdesign");
    reportListBox.addItem("SalesByStateAndProductLine",
        "SalesByStateAndProductLine.rptdesign");
    
    ListBox formatListBox = new ListBox(false);
    formatListBox.setName("__format");
    formatListBox.addItem("html","html");
    formatListBox.addItem("pdf","pdf");
    formatListBox.addItem("doc","doc");
    formatListBox.addItem("xls","xls");
    
    Hidden svgHid = new Hidden();
    svgHid.setName("__svg");
    svgHid.setValue("false");
    
    Hidden localeHid = new Hidden();
    localeHid.setName("__locale");
    localeHid.setValue("en_US");
    
    Hidden timezoneHid = new Hidden();
    timezoneHid.setName("__timezone");
    timezoneHid.setValue("Turkey");
    
    Hidden masterpageHid = new Hidden();
    masterpageHid.setName("__masterpage");
    masterpageHid.setValue("true");
    
    Hidden rtlHid = new Hidden();
    rtlHid.setName("__rtl");
    rtlHid.setValue("false");
    
    Hidden cubememsizeHid = new Hidden();
    cubememsizeHid.setName("__cubememsize");
    cubememsizeHid.setValue("10");
    
    Hidden resourceFolderHid = new Hidden();
    resourceFolderHid.setName("__resourceFolder");
    resourceFolderHid.setValue("C:\\Users\\User\\Documents\\Development\\" +
        "Workspaces\\trix.birt\\test_birt_report");
    
    Hidden dpiHid = new Hidden();
    dpiHid.setName("__dpi");
    dpiHid.setValue("96");
    
    Hidden toolbarHid = new Hidden();
    toolbarHid.setName("__toolbar");
    toolbarHid.setValue("false");   
    
    Hidden navigationbarHid = new Hidden();
    navigationbarHid.setName("__navigationbar");
    navigationbarHid.setValue("false");
    
    Hidden showtitleHid = new Hidden();
    showtitleHid.setName("__showtitle");
    showtitleHid.setValue("false");
    
    Button submitBut = new Button("Submit", new ClickHandler() {
        public void onClick(ClickEvent event) {
            form.submit();
          }
        });
    
    gr.setWidget(0, 0, reportListBox);
    gr.setWidget(0, 1, formatListBox);
    gr.setWidget(0, 2, svgHid);
    gr.setWidget(0, 3, localeHid);
    gr.setWidget(0, 4, timezoneHid);
    gr.setWidget(0, 5, masterpageHid);
    gr.setWidget(0, 6, rtlHid);
    gr.setWidget(0, 7, cubememsizeHid);
    gr.setWidget(0, 8, resourceFolderHid);
    gr.setWidget(0, 9, dpiHid);
    gr.setWidget(0,10, toolbarHid);
    gr.setWidget(0,11, navigationbarHid);
    gr.setWidget(0,12, showtitleHid);
    gr.setWidget(0,13, submitBut);
    
    form.add(gr);
    RootPanel.get("rootPanel").add(form);
  }
}
