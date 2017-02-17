package com.trix.web.birt.server;

/*
 * These JSON classes are in gwt-dev.jar
 * The library url is http://www.json.org/java/index.html
 */
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.engine.api.IParameterDefn;
import org.json.JSONArray;
import org.json.JSONObject;

public class CriteriaUtilities {
  
//  public enum Type{
//    BOOLEAN, DATE, DATETIME, DECIMAL, FLOAT, INTEGER, STRING, TIME}
//  private enum LogicalOperation {NOT,AND,OR}
  
  private Map<String,Integer> fieldTypeMap;
  private String jsonString;

  public CriteriaUtilities(String jsonString) {
    this.jsonString = jsonString;
    fieldTypeMap = new HashMap<String,Integer>();
  }
  
  public  CriteriaUtilities(String jsonString,
      Map<String,Integer> fieldTypeMap) {
    this.jsonString = jsonString;
    this.fieldTypeMap = fieldTypeMap;
  }  

  public String linearizeCriteria() throws Exception {
    return linearizeCriteria(new JSONObject(jsonString));
  }
  
  private String linearizeCriteria(JSONObject jsonObj) throws Exception {
    if (jsonObj.has("fieldName")) {
      return '(' + simpleCriteria(jsonObj) + ')';
    } else {      
      StringBuilder strBuild = new StringBuilder();
      JSONArray criteriasJSONArray = jsonObj.getJSONArray("criteria");
      if (criteriasJSONArray.length()>0) {
        if(jsonObj.getString("operator").equalsIgnoreCase("not")) {
          strBuild.append("not(");
        } else {
          strBuild.append('(');
        }
        String criteriaString;
        for(int i = 0; i < criteriasJSONArray.length(); i++){
          criteriaString = linearizeCriteria(criteriasJSONArray.getJSONObject(i));
          if (i > 0 && criteriaString.length() > 0) {
            if(jsonObj.getString("operator").equalsIgnoreCase("and")) {
            strBuild.append("and");
            } else {
              strBuild.append("or");
            }
          }
          strBuild.append(criteriaString);        
        }
        strBuild.append(')');
      }
      return strBuild.toString();
    }
  }
  
//MySQL case
  private String simpleCriteria(JSONObject jsonObj) throws Exception {
    String operator = jsonObj.getString("operator");
    if (operator.equalsIgnoreCase("iEquals") ||
        operator.equalsIgnoreCase("equals")) {
      return jsonObj.getString("fieldName") + " = " +
        getValueForQuery(jsonObj.getString("fieldName"),
            jsonObj.getString("value"));
//    '\'' + jsonObj.getString("value") + '\'';
    }
    if (operator.equals("iNotEqual") ||
        operator.equals("notEqual")) {
      return jsonObj.getString("fieldName") + " != " +
        getValueForQuery(jsonObj.getString("fieldName"),
             jsonObj.getString("value"));
//    '\'' + jsonObj.getString("value") + '\'';
    }
    if (operator.equals("greaterThan")) {
      return jsonObj.getString("fieldName") + " > " +
        getValueForQuery(jsonObj.getString("fieldName"),
            jsonObj.getString("value"));
//    '\'' + jsonObj.getString("value") + '\'';
    }
    if (operator.equals("lessThan")) {
      return jsonObj.getString("fieldName") + " < " +
          getValueForQuery(jsonObj.getString("fieldName"),
              jsonObj.getString("value"));
//          '\'' + jsonObj.getString("value") + '\'';
    }    
    if (operator.equals("greaterOrEqual")) {
      return jsonObj.getString("fieldName") + " >= " +
          getValueForQuery(jsonObj.getString("fieldName"),
              jsonObj.getString("value"));
//          '\'' + jsonObj.getString("value") + '\'';
    }
    if (operator.equals("lessOrEqual")) {
      return jsonObj.getString("fieldName") + " <= " +
          getValueForQuery(jsonObj.getString("fieldName"),
              jsonObj.getString("value"));
//          '\'' + jsonObj.getString("value") + '\'';
    }    
    if (operator.equals("iContains") ||
        operator.equals("contains")) {      
      return jsonObj.getString("fieldName") + " like " +
          '\"' + '%' + jsonObj.getString("value") + '%' + '\"';
    }    
    if (operator.equals("startsWith") ||
        operator.equals("iStartsWith")) {
      return jsonObj.getString("fieldName") + " like " +
          '\"' + jsonObj.getString("value") + '%' + '\"';
    }
    if (operator.equals("endsWith") ||
        operator.equals("iEndsWith")) {
      return jsonObj.getString("fieldName") + " like " +
          '\"' + '%' + jsonObj.getString("value") + '\"';
    }    
    if (operator.equals("notContains") ||
        operator.equals("iNotContains")) {
      return jsonObj.getString("fieldName") + " not like " +
          '\"' + '%' + jsonObj.getString("value") + '%' + '\"';
    }
    if (operator.equals("notStartsWith") ||
        operator.equals("iNotStartsWith")) {
      return jsonObj.getString("fieldName") + " not like " +
          '\"' + jsonObj.getString("value") + '%' + '\"';
    }
    if (operator.equals("notEndsWith") ||
        operator.equals("iNotEndsWith")) {
      return jsonObj.getString("fieldName") + " not like " +
          '\"' + '%' + jsonObj.getString("value") + '\"';
    }
    if (operator.equals("isNull")) {
      return jsonObj.getString("fieldName") + " is null ";
    }
    if (operator.equals("notNull")) {
      return jsonObj.getString("fieldName") + " is not null ";
    }
    if (operator.equals("inSet")) {
//      return jsonObj.getString("fieldName") + " in (" +
//          jsonObj.getString("value") + ')';
      throw new Exception("Not implemented");
    }
    if (operator.equals("notInSet")) {
//      return jsonObj.getString("fieldName") + " not in (" +
//          jsonObj.getString("value") + ')';
      throw new Exception("Not implemented");
    }
    if (operator.equals("equalsField")) {
      return jsonObj.getString("fieldName") + " = " +
          jsonObj.getString("value");
    }
    if (operator.equals("notEqualField")) {
      return jsonObj.getString("fieldName") + " != " +
          jsonObj.getString("value");
    }
    if (operator.equals("greaterThanField")) {
      return jsonObj.getString("fieldName") + " > " +
          jsonObj.getString("value");
    }
    if (operator.equals("lessThanField")) {
      return jsonObj.getString("fieldName") + " < " +
          jsonObj.getString("value");
    }
    if (operator.equals("greaterOrEqualField")) {
      return jsonObj.getString("fieldName") + " >= " +
          jsonObj.getString("value");
    }
    if (operator.equals("lessOrEqualField")) {
      return jsonObj.getString("fieldName") + " <= " +
          jsonObj.getString("value");
    }    
    if (operator.equals("containsField")) {
      return jsonObj.getString("fieldName") + " like CONCAT(" +
          '\'' + '%' + '\'' + ',' + jsonObj.getString("value") + ',' +
          '\'' + '%' + '\'' + ')';
    }
    if (operator.equals("startsWithField")) {
      return jsonObj.getString("fieldName") + " like CONCAT(" +
           jsonObj.getString("value") + ',' + '\'' + '%' + '\'' + ')';
    }
    if (operator.equals("endsWithField")) {
      return jsonObj.getString("fieldName") + " like CONCAT(" +
          '\'' + '%' + '\'' + ',' + jsonObj.getString("value") + ')';
    }
    if (operator.equals("and")) {
      throw new Exception("Not implemented");
    }
    if (operator.equals("not")) {
      throw new Exception("Not implemented");
    }
    if (operator.equals("or")) {
      throw new Exception("Not implemented");
    }
    if (operator.equals("regexp") ||
        operator.equals("iregexp")) {
      throw new Exception("Not implemented");
    }
    if (operator.equals("between")) {
      return jsonObj.getString("fieldName") + " between " +
          getValueForQuery(jsonObj.getString("fieldName"),
              jsonObj.getString("start")) + " and " +
              getValueForQuery(jsonObj.getString("fieldName"),
                  jsonObj.getString("end"));
//          '\'' + jsonObj.getString("start") + '\'' + " and " +
//          '\'' + jsonObj.getString("end") + '\'';
    }
    if (operator.equals("betweenInclusive")) {
      throw new Exception("Not implemented");
    }
    return null;
  }
  
  private String getValueForQuery(String fieldName, String value) {
    String returnValue;
    switch (fieldTypeMap.get(fieldName)) {
    case IParameterDefn.TYPE_ANY:      
    case IParameterDefn.TYPE_STRING:
    default:
      value = value.replaceAll("\\\\", "\\\\\\\\");
      value = value.replaceAll("\"", "\\\"");
      returnValue = '\"' + value + '\"'; break;
    case IParameterDefn.TYPE_FLOAT:   returnValue = value; break;
    case IParameterDefn.TYPE_DECIMAL: returnValue = value; break;
    case IParameterDefn.TYPE_DATE_TIME:returnValue = '\"' + value + '\"'; break;    
    case IParameterDefn.TYPE_BOOLEAN: returnValue = value.toUpperCase(); break;
    case IParameterDefn.TYPE_INTEGER: returnValue = value; break;
    case IParameterDefn.TYPE_DATE:    returnValue = '\"' + value + '\"'; break;
    case IParameterDefn.TYPE_TIME:    returnValue = '\"' + value + '\"'; break;                             
    }
    return returnValue;
  }
  
  public void putFieldType(String field, Integer integerType) {
    fieldTypeMap.put(field, integerType);
  }
  
  public Map<String, Integer> getFieldTypeMap() {
    return fieldTypeMap;
  }  

  public String getJsonString() {
    return jsonString;
  }

  public void setJsonString(String jsonString) {
    this.jsonString = jsonString;
  }
}