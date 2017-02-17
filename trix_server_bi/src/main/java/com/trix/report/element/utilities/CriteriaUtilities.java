package com.trix.report.element.utilities;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class CriteriaUtilities {
  
  private enum Type{
    ANY, BOOLEAN, DATE, DATETIME, ENUM, FLOAT, INTEGER, STRING, TIME}
//  private enum LogicalOperation {NOT,AND,OR}
  
  private Map<String,Type> fieldTypeMap;
  private String jsonString;

  public CriteriaUtilities(String jsonString) {
    this.jsonString = jsonString;
    fieldTypeMap = new HashMap<String,Type>();
//    System.out.println("json: " + jsonString);
  }
  
//  public  CriteriaUtilities(String jsonString,
//      Map<String,Type> fieldTypeMap) {
//    this.jsonString = jsonString;
//    this.fieldTypeMap = fieldTypeMap;
//  }  

  public String linearizeCriteria() {
    return linearizeCriteria(new JSONObject(jsonString));    
  }
  
  private String linearizeCriteria(JSONObject jsonObj) {
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
  
  /*
  //MySQL case
  private String simpleCriteria(JSONObject jsonObj) throws CriteriaException {
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
      throw new CriteriaException("Not implemented");
    }
    if (operator.equals("notInSet")) {
//      return jsonObj.getString("fieldName") + " not in (" +
//          jsonObj.getString("value") + ')';
      throw new CriteriaException("Not implemented");
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
      throw new CriteriaException("Not implemented");
    }
    if (operator.equals("not")) {
      throw new CriteriaException("Not implemented");
    }
    if (operator.equals("or")) {
      throw new CriteriaException("Not implemented");
    }
    if (operator.equals("regexp") ||
        operator.equals("iregexp")) {
      throw new CriteriaException("Not implemented");
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
      throw new CriteriaException("Not implemented");
    }
    return null;
  }
  
  private String getValueForQuery(String fieldName, String value) {
    String returnValue;
    switch (fieldTypeMap.get(fieldName)) {
    case ANY:
    case STRING:
    default:
      value = value.replaceAll("\\\\", "\\\\\\\\");
      value = value.replaceAll("\"", "\\\"");
      returnValue = '\"' + value + '\"'; break;
    case FLOAT:   returnValue = value; break;
    case DATETIME:returnValue = '\"' + value + '\"'; break;
    case BOOLEAN: returnValue = value.toUpperCase(); break;
    case INTEGER: returnValue = value; break;
    case DATE:    returnValue = '\"' + value + '\"'; break;
    case TIME:    returnValue = '\"' + value + '\"'; break;
    }
    return returnValue;
  }*/
  
  private String simpleCriteria(JSONObject jsonObj) {
    final StringBuilder criteriaBuilder =
        new StringBuilder(jsonObj.getString("fieldName") + ' ');
    final String operator = jsonObj.getString("operator");
    final String operatorSql = getOperatorSql(operator);
    criteriaBuilder.append(operatorSql);
    //equalsField, notEqualField, greaterThanField, lessThanField, greaterOrEqualField, lessOrEqualField
    if (operator.indexOf("Field") >= 0) {
      criteriaBuilder.append(' ').append(jsonObj.getString("value"));
    } else if (operator.equals("iContains") || operator.equals("iNotContains")) {
      criteriaBuilder.append(" '%" + jsonObj.get("value").toString() + "%'");
    } else if (operator.equals("iStartsWith") || operator.equals("iNotStartsWith")) {
      criteriaBuilder.append(" '" + jsonObj.get("value").toString() + "%'");
    } else if (operator.equals("iEndsWith") || operator.equals("iNotEndsWith")) {
      criteriaBuilder.append(" '%" + jsonObj.get("value").toString() + '\'');
    } else if (operator.equals("iEquals") || operator.equals("iNotEqual")
        || operator.equals("equals") || operator.equals("notEqual")
        || operator.equals("greaterThan") || operator.equals("lessThan")
        || operator.equals("greaterOrEqual") || operator.equals("lessOrEqual")) {
      criteriaBuilder.append(' ').append(getValueSql("value", jsonObj));
    } else if (operator.equals("between")) {
      criteriaBuilder.append(' ');
      criteriaBuilder.append(getValueSql("start", jsonObj));
      criteriaBuilder.append(" and ");
      criteriaBuilder.append(getValueSql("end", jsonObj));
    }
    return criteriaBuilder.toString();
  }
  
  private String getValueSql(String key, JSONObject jsonObj) {
    switch (fieldTypeMap.get(jsonObj.getString("fieldName"))) {
    case ANY:
    case DATE:
    case DATETIME:
    case STRING:
    case TIME:
    default:
      return '\'' + jsonObj.getString(key) + '\'';
    case FLOAT:   return String.valueOf(jsonObj.getDouble(key));    
    case BOOLEAN: return String.valueOf(jsonObj.getBoolean(key));
    case INTEGER: return String.valueOf(jsonObj.getInt(key));    
    }
  }
  
  private String getOperatorSql(String operator) {
    switch(operator) {
      case "iEquals":
      case "equals":
      case "equalsField": return "=";
      case "iNotEqual":
      case "notEqual":
      case "notEqualField": return "!=";
      case "greaterThan":
      case "greaterThanField": return ">";
      case "lessThan":
      case "lessThanField": return "<";
      case "greaterOrEqual":
      case "greaterOrEqualField": return ">=";
      case "lessOrEqual":
      case "lessOrEqualField": return "<=";
      case "iContains":
      case "iStartsWith":
      case "iEndsWith": return "like";
      case "iNotContains":     
      case "iNotStartsWith":      
      case "iNotEndsWith": return "not like";
      case "between": return "between";
      case "isNull": return "is null";
      case "notNull": return "is not null";
      default: return null;
    }
  }
  
//  private void putFieldType(String field, Type type) {
//    fieldTypeMap.put(field, type);
//  }
  
  public void putFieldType(String field, String typeStr) {
    String typeLowerCase = typeStr.toLowerCase();
    if (typeLowerCase.equals("string")) {
      fieldTypeMap.put(field, Type.STRING);
    } else if (typeLowerCase.equals("ineger")) {
      fieldTypeMap.put(field, Type.INTEGER);
    } else if (typeLowerCase.equals("time")) {
      fieldTypeMap.put(field, Type.TIME);
    } else if (typeLowerCase.equals("boolean")) {
      fieldTypeMap.put(field, Type.BOOLEAN);
    } else if (typeLowerCase.equals("datetime")) {
      fieldTypeMap.put(field, Type.DATETIME);
    } else if (typeLowerCase.equals("date")) {
      fieldTypeMap.put(field, Type.DATE);
    } else if (typeLowerCase.equals("float")) {
      fieldTypeMap.put(field, Type.FLOAT);
    } else if (typeLowerCase.equals("enum")) {
      fieldTypeMap.put(field, Type.ENUM);
    } else {
      fieldTypeMap.put(field, Type.ANY);
    }
    return;
  }
  
//  public Map<String, Type> getFieldTypeMap() {
//    return fieldTypeMap;
//  }  

//  public String getJsonString() {
//    return jsonString;
//  }

//  public void setJsonString(String jsonString) {
//    this.jsonString = jsonString;
//  }
  
//  public static class CriteriaException extends java.lang.Exception {   
//    private static final long serialVersionUID = 6586657770124161587L;
//
//    CriteriaException(String message) {
//      super(message);
//    }
//  }
}