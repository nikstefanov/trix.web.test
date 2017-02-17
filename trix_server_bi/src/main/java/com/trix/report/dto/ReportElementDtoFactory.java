package com.trix.report.dto;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;
import org.eclipse.birt.report.model.api.olap.TabularLevelHandle;
import org.eclipse.birt.report.model.api.olap.TabularMeasureHandle;
import org.json.JSONObject;
import org.json.JSONStringer;

public class ReportElementDtoFactory {
  
  public ReportElementDtoFactory() {    
  }
  
  public ReportElementDto create(DesignElementHandle elementHandle)
      throws Exception {
    ReportElementDto reportElementDto = new ReportElementDto();
    fill(reportElementDto, elementHandle);
    return reportElementDto;
  }
  
  private void fill(ReportElementDto reportElementDto,
      DesignElementHandle elementHandle) throws Exception {
    reportElementDto.setName(elementHandle.getName());
    if (elementHandle instanceof ExtendedItemHandle) {
      ExtendedItemHandle xtab = (ExtendedItemHandle) elementHandle;
      TabularCubeHandle cubeHandle = (TabularCubeHandle) xtab.getCube();
      OdaDataSetHandle dataSetHandle =
          (OdaDataSetHandle) cubeHandle.getDataSet();
      reportElementDto.setFiltersJson(getFiltersJson(dataSetHandle));
      reportElementDto.setDimensionsJson(getDimensionsJson(cubeHandle));
      reportElementDto.setSummariesJson(getSummariesJson(cubeHandle));
    } else {
      if (elementHandle instanceof TableHandle) {
        TableHandle tableHandle = (TableHandle) elementHandle;
        OdaDataSetHandle dataSetHandle =
            (OdaDataSetHandle) tableHandle.getDataSet();
        reportElementDto.setFiltersJson(getFiltersJson(dataSetHandle));
        reportElementDto.setDimensionsJson("[]");
        reportElementDto.setSummariesJson("[]");
      } else {
        throw new Exception ("Unsuported Element");
      }
    }
  }
  
  //could return empty array []
  private String getFiltersJson(OdaDataSetHandle dataSetHandle) {
    JSONStringer filtersJsonStringer = new JSONStringer();
    String query = dataSetHandle.getQueryText();
    Pattern p =
        Pattern.compile("#\"filter\":(\\{[^{}\\[\\]\\r\\n]+?})");
    Matcher m = p.matcher(query);
    filtersJsonStringer.array();
    while (m.find()) {
      JSONObject filterJsonObject =
          new JSONObject(query.substring(m.start(1), m.end(1)));
      filtersJsonStringer.object();
      filtersJsonStringer.key("title");
      filtersJsonStringer.value(filterJsonObject.get("title"));
      filtersJsonStringer.key("column");
      filtersJsonStringer.value(filterJsonObject.get("column"));
      filtersJsonStringer.key("type");
      filtersJsonStringer.value(filterJsonObject.get("type"));
      filtersJsonStringer.key("trixId");
      filtersJsonStringer.value(filterJsonObject.get("trixId"));
      filtersJsonStringer.endObject();
    }
    filtersJsonStringer.endArray();
    return filtersJsonStringer.toString();
  }
  
  //could return empty array []
  private String getDimensionsJson(TabularCubeHandle cubeHandle){
    JSONStringer dimensionsJsonStringer = new JSONStringer();
    dimensionsJsonStringer.array();
    @SuppressWarnings("rawtypes")
    List dimensions = cubeHandle.getContents(CubeHandle.DIMENSIONS_PROP);
    for (int i = 0; i < dimensions.size(); i++) {
      DimensionHandle dHandle = (DimensionHandle) dimensions.get(i);
      dimensionsJsonStringer.object();
      dimensionsJsonStringer.key("name");
      dimensionsJsonStringer.value(dHandle.getName());
      dimensionsJsonStringer.key("title");
//      dimensionsJsonStringer.value(dHandle.getDisplayName());
      TabularHierarchyHandle hierarchyHandle =
          (TabularHierarchyHandle) dHandle.getDefaultHierarchy();
      TabularLevelHandle levelhandle =
          (TabularLevelHandle) hierarchyHandle.getLevel(0);
      dimensionsJsonStringer.value(levelhandle.getName());
      dimensionsJsonStringer.endObject();
    }
    dimensionsJsonStringer.endArray();
    return dimensionsJsonStringer.toString();
  }  
  
  //could return empty array []
  private String getSummariesJson(TabularCubeHandle cubeHandle){
    JSONStringer summariesJsonStringer = new JSONStringer();
    summariesJsonStringer.array();
    @SuppressWarnings("rawtypes")
    List measureGroups = cubeHandle.getContents(CubeHandle.MEASURE_GROUPS_PROP);
    for (int i = 0; i < measureGroups.size(); i++) {
      MeasureGroupHandle mgh = (MeasureGroupHandle) measureGroups.get(i);
      summariesJsonStringer.object();
      summariesJsonStringer.key("name");
      summariesJsonStringer.value(mgh.getName());
      summariesJsonStringer.key("title");
//      summariesJsonStringer.value(mgh.getDisplayName());
      TabularMeasureHandle measureHandle = (TabularMeasureHandle)
          mgh.getContent(MeasureGroupHandle.MEASURES_PROP, 0);
      summariesJsonStringer.value(measureHandle.getDisplayName());
      summariesJsonStringer.endObject();
    }
    summariesJsonStringer.endArray();
    return summariesJsonStringer.toString();
  }

}
