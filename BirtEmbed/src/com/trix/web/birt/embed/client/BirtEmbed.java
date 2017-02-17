package com.trix.web.birt.embed.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class BirtEmbed implements EntryPoint {
  /**
   * The message displayed to the user when the server cannot be reached or
   * returns an error.
   
  private static final String SERVER_ERROR = "An error occurred while "
      + "attempting to contact the server. Please check your network "
      + "connection and try again.";
   */
  /**
   * Create a remote service proxy to talk to the server-side Greeting service.
  
  private final GreetingServiceAsync greetingService = GWT
      .create(GreetingService.class);
 */
  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
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
    RootPanel.get("root").add(form);
  }
  
//  /**
//   * This is the entry point method.
//   */
//  public void onModuleLoad() {
//    final Button sendButton = new Button("Send");
//    final TextBox nameField = new TextBox();
//    nameField.setText("GWT User");
//    final Label errorLabel = new Label();
//
//    // We can add style names to widgets
//    sendButton.addStyleName("sendButton");
//
//    // Add the nameField and sendButton to the RootPanel
//    // Use RootPanel.get() to get the entire body element
//    RootPanel.get("nameFieldContainer").add(nameField);
//    RootPanel.get("sendButtonContainer").add(sendButton);
//    RootPanel.get("errorLabelContainer").add(errorLabel);
//
//    // Focus the cursor on the name field when the app loads
//    nameField.setFocus(true);
//    nameField.selectAll();
//
//    // Create the popup dialog box
//    final DialogBox dialogBox = new DialogBox();
//    dialogBox.setText("Remote Procedure Call");
//    dialogBox.setAnimationEnabled(true);
//    final Button closeButton = new Button("Close");
//    // We can set the id of a widget by accessing its Element
//    closeButton.getElement().setId("closeButton");
//    final Label textToServerLabel = new Label();
//    final HTML serverResponseLabel = new HTML();
//    VerticalPanel dialogVPanel = new VerticalPanel();
//    dialogVPanel.addStyleName("dialogVPanel");
//    dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
//    dialogVPanel.add(textToServerLabel);
//    dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
//    dialogVPanel.add(serverResponseLabel);
//    dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
//    dialogVPanel.add(closeButton);
//    dialogBox.setWidget(dialogVPanel);
//
//    // Add a handler to close the DialogBox
//    closeButton.addClickHandler(new ClickHandler() {
//      public void onClick(ClickEvent event) {
//        dialogBox.hide();
//        sendButton.setEnabled(true);
//        sendButton.setFocus(true);
//      }
//    });
//
//    // Create a handler for the sendButton and nameField
//    class MyHandler implements ClickHandler, KeyUpHandler {
//      /**
//       * Fired when the user clicks on the sendButton.
//       */
//      public void onClick(ClickEvent event) {
//        sendNameToServer();
//      }
//
//      /**
//       * Fired when the user types in the nameField.
//       */
//      public void onKeyUp(KeyUpEvent event) {
//        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
//          sendNameToServer();
//        }
//      }
//
//      /**
//       * Send the name from the nameField to the server and wait for a response.
//       */
//      private void sendNameToServer() {
//        // First, we validate the input.
//        errorLabel.setText("");
//        String textToServer = nameField.getText();
//        if (!FieldVerifier.isValidName(textToServer)) {
//          errorLabel.setText("Please enter at least four characters");
//          return;
//        }
//
//        // Then, we send the input to the server.
//        sendButton.setEnabled(false);
//        textToServerLabel.setText(textToServer);
//        serverResponseLabel.setText("");
//        greetingService.greetServer(textToServer, new AsyncCallback<String>() {
//          public void onFailure(Throwable caught) {
//            // Show the RPC error message to the user
//            dialogBox.setText("Remote Procedure Call - Failure");
//            serverResponseLabel.addStyleName("serverResponseLabelError");
//            serverResponseLabel.setHTML(SERVER_ERROR);
//            dialogBox.center();
//            closeButton.setFocus(true);
//          }
//
//          public void onSuccess(String result) {
//            dialogBox.setText("Remote Procedure Call");
//            serverResponseLabel.removeStyleName("serverResponseLabelError");
//            serverResponseLabel.setHTML(result);
//            dialogBox.center();
//            closeButton.setFocus(true);
//          }
//        });
//      }
//    }
//
//    // Add a handler to send the name to the server
//    MyHandler handler = new MyHandler();
//    sendButton.addClickHandler(handler);
//    nameField.addKeyUpHandler(handler);
//  }
}
