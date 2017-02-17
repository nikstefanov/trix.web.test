package com.trix.web.birt.client.report;

import java.util.Arrays;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.util.JSON;
import com.smartgwt.client.widgets.form.FilterBuilder;
import com.smartgwt.client.widgets.layout.VLayout;
import com.trix.web.birt.shared.dto.ReportElementConfigurationGwtDto;
import com.trix.web.birt.shared.dto.ReportElementGwtDto;

public class ConfigurationPanel extends VLayout {
  private final ReportElementGwtDto reportElement;
  private final TextBox columnsTextBox, rowsTextBox, summariesTextBox;
  private final FilterBuilder filterBuilder;
  
  public ConfigurationPanel(ReportElementGwtDto reportElement) {
    this.reportElement = reportElement;
    setBackgroundColor("red");

    Label l = new Label(reportElement.getName());
    addMember(l);

    filterBuilder = new FilterBuilder();
    DataSource filterDataSource = new DataSource();
    JSONArray filtersJsonArray = (JSONArray)JSONParser.parseLenient(
        reportElement.getFiltersJson());
    for (int i = 0; i < filtersJsonArray.size(); i++) {
      JSONObject filterObject = (JSONObject)filtersJsonArray.get(i);
      DataSourceField dataSourceField = new DataSourceField(
          ((JSONString)filterObject.get("column")).stringValue(),
          getFieldType(((JSONString)filterObject.get("type")).stringValue()),
          ((JSONString)filterObject.get("title")).stringValue());
      filterDataSource.addField(dataSourceField);
    }
    filterBuilder.setDataSource(filterDataSource);
    addMember(filterBuilder);

    StringBuilder dimensionsFiledsStrBuilder = new StringBuilder();
    JSONArray dimensionsJsonArray = (JSONArray)JSONParser.parseLenient(
        reportElement.getDimensionsJson());
    for (int i = 0; i < dimensionsJsonArray.size(); i++) {
      JSONObject dimensionObject = (JSONObject)dimensionsJsonArray.get(i);
      dimensionsFiledsStrBuilder.append(',').
        append(((JSONString)dimensionObject.get("name")).stringValue());
    }
    if (dimensionsFiledsStrBuilder.length() != 0) {
      dimensionsFiledsStrBuilder.deleteCharAt(0);
    }
    
    columnsTextBox = new TextBox();
    columnsTextBox.setValue("YearGroup");
    addMember(columnsTextBox);
    
    rowsTextBox = new TextBox();
    rowsTextBox.setValue("ClinicGroup");
    addMember(rowsTextBox);
    
    summariesTextBox = new TextBox();
    summariesTextBox.setValue("Summary Patients Count");
    addMember(summariesTextBox);
    
    //this.setText(2, 0, "Dimension Fields:");
    //this.setText(2, 1, dimensionsFiledsStrBuilder.toString());
    //this.setText(3,0,"Column Dimensions:");
    //this.setWidget(3, 1, columnsTextBox);
    //this.setText(4,0,"Rows Dimensions:");
    //this.setWidget(4,1, rowsTextBox);
    
    StringBuilder summariesFiledsStrBuilder = new StringBuilder();
    JSONArray summariesJsonArray =
        (JSONArray)JSONParser.parseLenient(reportElement.getSummariesJson());
    for (int i = 0; i < summariesJsonArray.size(); i++) {
      JSONObject summaryObject = (JSONObject)summariesJsonArray.get(i);
      summariesFiledsStrBuilder.append(',').
        append(((JSONString)summaryObject.get("name")).stringValue());
    }
    if (summariesFiledsStrBuilder.length() != 0) {
      summariesFiledsStrBuilder.deleteCharAt(0);
    }
    //this.setText(5, 0, "Summary Fields: ");
    //this.setText(5, 1, summariesFiledsStrBuilder.toString());
    //this.setText(6,0,"Summary Fields:");
    //this.setWidget(6, 1, summariesTextBox);
  }
  
  private FieldType getFieldType(String type) {
    if (type.equals("string")) return FieldType.TEXT;
    if (type.equals("integer")) return FieldType.INTEGER;
    if (type.equals("float")) return FieldType.FLOAT;
    if (type.equals("date")) return FieldType.DATE;
    if (type.equals("datetime")) return FieldType.DATETIME;
    if (type.equals("time")) return FieldType.TIME;
    if (type.equals("boolean")) return FieldType.BOOLEAN;
    if (type.equals("enum")) return FieldType.ENUM;
    else return FieldType.ANY;
  }
  
  public ReportElementConfigurationGwtDto getElementConfiguration() {
    ReportElementConfigurationGwtDto elementConfiguration =
        new ReportElementConfigurationGwtDto();
    elementConfiguration.setCriteriaJSON(
        JSON.encode(filterBuilder.getCriteria().getJsObj()));
    elementConfiguration.setXtColumns(
        Arrays.asList(columnsTextBox.getValue().split(",")));
    elementConfiguration.setXtRows(
        Arrays.asList(rowsTextBox.getValue().split(",")));
    elementConfiguration.setXtSummaries(
        Arrays.asList(summariesTextBox.getValue().split(",")));
//    System.out.println(elementConfiguration.getCriteriaJSON());
//    System.out.println(elementConfiguration.getXtColumns());
//    System.out.println(elementConfiguration.getXtRows());
//    System.out.println(elementConfiguration.getXtSummaries());
    return elementConfiguration;
  }
  
  public String getElementName() {
    return reportElement.getName();
  }
}
