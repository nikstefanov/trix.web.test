package com.trix.web.birt.client;

import com.google.gwt.core.client.EntryPoint;
import com.smartgwt.client.widgets.Canvas;
import com.trix.web.birt.client.report.TableReport;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TrixWebBirt implements EntryPoint {
  public void onModuleLoad() {
    Canvas layout = new Canvas();
    layout.setWidth100();
    layout.setHeight100();
    layout.setMargin(5);
    
    TableReport report = new TableReport("SimpleTableMySQL");
    layout.addChild(report);
    
    layout.draw();

    report.loadReport(null);
  }
}
