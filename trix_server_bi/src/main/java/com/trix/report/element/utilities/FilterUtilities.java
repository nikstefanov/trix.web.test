package com.trix.report.element.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.json.JSONObject;

public class FilterUtilities {
  private String criteriaJSON;
  private OdaDataSetHandle dataSetHandle;
  
  public FilterUtilities(OdaDataSetHandle dataSetHandle,String json) {
    this.dataSetHandle = dataSetHandle;
    criteriaJSON = json;
  }
  
  public void applyJSONFilterConditions() throws Exception, SemanticException {
    String query = dataSetHandle.getQueryText();
    String whereStatement;
    if (criteriaJSON == null) {
      whereStatement = "false";
    } else {      
      CriteriaUtilities criteriaUtilities = new CriteriaUtilities(criteriaJSON);      
      // Filling the types map.
      Pattern p =
          Pattern.compile("#\"filter\":(\\{[^{}\\[\\]\\r\\n]+?})");
      Matcher m = p.matcher(query);
      while (m.find()) {
        JSONObject filterJsonObject =
            new JSONObject(query.substring(m.start(1), m.end(1)));
        criteriaUtilities.putFieldType(filterJsonObject.getString("column"),
            filterJsonObject.getString("type"));
      }
      whereStatement = criteriaUtilities.linearizeCriteria();
//      System.out.println("where: " + whereStatement);
    }
    if (whereStatement.length() != 0) {      
      query = query.replaceFirst("#criteriaWhere#", " WHERE ");
      query = query.replaceFirst("#criteriaAnd#", " AND ");
      query = query.replaceFirst("#criteriaOr#", " OR ");
      query = query.replaceFirst("#criteria#", whereStatement);      
      dataSetHandle.setQueryText(query);
//      System.out.println("Added query: " + query);
    }
  }
/*
  private void addQueryWhereConstrains(ReportDesignHandle designHandle,
      CriteriaUtilities criteriaUtilities)
          throws CriteriaException, Exception, SemanticException {
    System.out.println("CriteriaJSONString: "+
      criteriaUtilities.getJsonString());
    String whereStatement = "";
    if (criteriaUtilities.getJsonString() != null) {
      whereStatement = criteriaUtilities.linearizeCriteria();
    } else {
      whereStatement = "false";
    }
    OdaDataSetHandle odaDataSetHandle =
        (OdaDataSetHandle)designHandle.getDataSets().get(0);
    String query = odaDataSetHandle.getQueryText();
    if (whereStatement.length() != 0) {      
      query = query.replaceFirst("#criteriaWhere#", " WHERE ");
      query = query.replaceFirst("#criteriaAnd#", " AND ");
      query = query.replaceFirst("#criteriaOr#", " OR ");
      query = query.replaceFirst("#criteria#", whereStatement);      
      odaDataSetHandle.setQueryText(query);
    }
    System.out.println("Query: "+query);
    System.out.println("where Statement: "+ whereStatement);
  }
  */
}
