package com.trix.web.birt.server.dfo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.trix.web.birt.server.dfo.DataFetchExpression.ExpressionType;
import com.trix.web.birt.server.dfo.DataFetchExpression.Operation;
import com.trix.web.birt.server.dfo.DataFetchExpressionBunch.LogicalOperation;

public class DataFetchDefUtils {
  
  @SuppressWarnings("rawtypes")
  private static DataFetchExpression buildDataFetchExpression(Map values) 
      throws Exception {
    String operator = (String)values.get("operator");
    String fieldName = (String)values.get("fieldName");
    Object value = values.get("value");

    DataFetchExpression dataFetchExpression = new DataFetchExpression();
 
    // set name 
    dataFetchExpression.setFieldName(fieldName);
    
    // set operation
    if (operator.equalsIgnoreCase("iEquals") || 
        operator.equalsIgnoreCase("equals")) {
      dataFetchExpression.setOperation(Operation.OP_EQ);
    }
    if (operator.equals("iNotEqual") ||
        operator.equals("notEqual")) {
      dataFetchExpression.setOperation(Operation.OP_NE);
    }
    
    if (operator.equals("greaterThan")) {
      dataFetchExpression.setOperation(Operation.OP_GT);
    }
    if (operator.equals("lessThan")) {
      dataFetchExpression.setOperation(Operation.OP_LT);
    }
    
    if (operator.equals("greaterOrEqual")) {
      dataFetchExpression.setOperation(Operation.OP_GE);
    }
    if (operator.equals("lessOrEqual")) {
      dataFetchExpression.setOperation(Operation.OP_LE);
    }
    
    if (operator.equals("iContains")) {
      value = "%" + value + "%";
      dataFetchExpression.setOperation(Operation.OP_LIKE);
    }
    
    if (operator.equals("startsWith")) {
      value = value + "%";
      dataFetchExpression.setOperation(Operation.OP_LIKE);
    }
    if (operator.equals("endsWith")) {
      value = "%" + value;
      dataFetchExpression.setOperation(Operation.OP_LIKE);
    }
    if (operator.equals("contains")) {
      value = "%" + value + "%";
      dataFetchExpression.setOperation(Operation.OP_LIKE);
    }

    if (operator.equals("iStartsWith")) {
      value = value + "%";
      dataFetchExpression.setOperation(Operation.OP_LIKE);
    }
    if (operator.equals("iEndsWith")) {
      value = "%" + value;
      dataFetchExpression.setOperation(Operation.OP_LIKE);
    }

    if (operator.equals("notContains")) {
      throw new Exception("Not implemented");
    }
    
    if (operator.equals("notContains")) {
      throw new Exception("Not implemented");
    }
    if (operator.equals("notStartsWith")) {
      throw new Exception("Not implemented");
    }
    if (operator.equals("notEndsWith")) {
      throw new Exception("Not implemented");
    }
    if (operator.equals("iNotContains")) {
      throw new Exception("Not implemented");
    }
    if (operator.equals("iNotStartsWith")) {
      throw new Exception("Not implemented");
    }
    if (operator.equals("iNotEndsWith")) {
      throw new Exception("Not implemented");
    }
    if (operator.equals("regexp")) {
      throw new Exception("Not implemented");
    }
    if (operator.equals("iregexp")) {
      throw new Exception("Not implemented");
    }
    if (operator.equals("isNull")) {
      dataFetchExpression.setOperation(Operation.OP_IS_NULL);
    }
    if (operator.equals("notNull")) {
      dataFetchExpression.setOperation(Operation.OP_NOT_NULL);
    }
    if (operator.equals("inSet")) {
      dataFetchExpression.setOperation(Operation.OP_IN);
    }
    if (operator.equals("notInSet")) {
      throw new Exception("Not implemented");
    }
    if (operator.equals("equalsField")) {
      throw new Exception("Not implemented");
    }
    if (operator.equals("notEqualField")) {
      throw new Exception("Not implemented");
    }
    if (operator.equals("greaterThanField")) {
      throw new Exception("Not implemented");
    }
    if (operator.equals("lessThanField")) {
      throw new Exception("Not implemented");
    }
    if (operator.equals("greaterOrEqualField")) {
      throw new Exception("Not implemented");
    }
    if (operator.equals("lessOrEqualField")) {
      throw new Exception("Not implemented");
    }
    if (operator.equals("containsField")) {
      throw new Exception("Not implemented");
    }
    if (operator.equals("startsWithField")) {
      throw new Exception("Not implemented");
    }
    if (operator.equals("endsWithField")) {
      throw new Exception("Not implemented");
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
    if (operator.equals("between")) {
      throw new Exception("Not implemented");
    }
    if (operator.equals("betweenInclusive")) {
      throw new Exception("Not implemented");
    }
    
    // set value
    if (value instanceof BigDecimal) {
      dataFetchExpression.setFieldType(ExpressionType.T_BIG_DECIMAL);
      dataFetchExpression.setBigDecimalValue((BigDecimal)value);
    }
    if (value instanceof BigInteger) {
      dataFetchExpression.setFieldType(ExpressionType.T_BIG_INTEGER);
      dataFetchExpression.setBigIntegerValue((BigInteger)value);
    }
    if (value instanceof Boolean) {
      dataFetchExpression.setFieldType(ExpressionType.T_BOOLEAN);
      dataFetchExpression.setBooleanValue((Boolean)value);
    }
    if (value instanceof Date) {
      dataFetchExpression.setFieldType(ExpressionType.T_DATE);
      dataFetchExpression.setDateValue((Date)value);
    }
    if (value instanceof Integer) {
      dataFetchExpression.setFieldType(ExpressionType.T_INTEGER);
      dataFetchExpression.setIntegerValue((Integer)value);
    }
    if (value instanceof Long) {
      dataFetchExpression.setFieldType(ExpressionType.T_LONG);
      dataFetchExpression.setLongValue((Long)value);
    }
    if (value instanceof Short) {
      dataFetchExpression.setFieldType(ExpressionType.T_SHORT);
      dataFetchExpression.setShortValue((Short)value);
    }
    if (value instanceof String) {
      dataFetchExpression.setFieldType(ExpressionType.T_STRING);
      dataFetchExpression.setStringValue((String)value);
    }

    return(dataFetchExpression);
  }
  
  @SuppressWarnings({ "rawtypes" })
  public static DataFetchExpressionBunch buildDataFetchExpressionBunch(Map values, 
      LogicalOperation op) throws Exception {
    if (null == values) return null;//nss
    DataFetchExpressionBunch dataFetchExpressionBunch = 
        new DataFetchExpressionBunch();
    dataFetchExpressionBunch.setOperation(op);

    if (values.containsKey("criteria")) {
      String operator = (String)values.get("operator");
      Object value = values.get("criteria");

      LogicalOperation bop = LogicalOperation.AND;
      if (operator.equalsIgnoreCase("and")) {
        bop = LogicalOperation.AND;
      }
      if (operator.equalsIgnoreCase("or")) {
        bop = LogicalOperation.OR;
      }
      dataFetchExpressionBunch.setOperation(bop);

      if (value instanceof List) {
        for (int j=0; j<((List)value).size(); j++) {
          if (((List)value).get(j) instanceof Map) {
            Map criteriaValues = (Map)((List)value).get(j);
            if (criteriaValues.containsKey("criteria")) {
              DataFetchExpressionBunch criteriaDataFetchExpressionBunch = 
                  buildDataFetchExpressionBunch(criteriaValues, bop);
              dataFetchExpressionBunch.addExpressionBunch(
                  criteriaDataFetchExpressionBunch);
            }
            
            if (criteriaValues.containsKey("fieldName")) {
              dataFetchExpressionBunch.addExpression(
                  buildDataFetchExpression(criteriaValues));
            }
          }
        }
      }
    }

    if (values.containsKey("fieldName")) {
      dataFetchExpressionBunch.addExpression(buildDataFetchExpression(values));
    }

    return(dataFetchExpressionBunch);
  }
  
  public static StringBuilder generateQueryWhere(DataFetchDef dataFetchDef, 
      DataFetchExpressionBunch dataFetchExpressionBunch, int itr) {
    StringBuilder whereStatement = new StringBuilder();

    if (null != dataFetchExpressionBunch && 
        null != dataFetchExpressionBunch.getExpressionBunch()) {
      for (DataFetchExpressionBunch d : 
          dataFetchExpressionBunch.getExpressionBunch()) {
        StringBuilder whereDefinition = generateQueryWhere(dataFetchDef, 
            d, itr+1);
        if (! whereDefinition.toString().equals("")) {
          if (! whereStatement.toString().equals("")) {
            whereStatement.append(" and ");
          }

          whereStatement.append(whereDefinition);
        }
      }
    }

    if (null != dataFetchExpressionBunch && 
        null != dataFetchExpressionBunch.getExpressions() && 
        dataFetchExpressionBunch.getExpressions().size() > 0) {
      if (! whereStatement.toString().equals("")) {
        whereStatement.append(" and ");
      }
      whereStatement.append("(");

      int i = 0;
      for (DataFetchExpression dataFetchExpression : 
          dataFetchExpressionBunch.getExpressions()) {
//        if (null == alias) {
//          alias = "";
//        }
        
        if (i++ > 0) {
          if (dataFetchExpressionBunch.getOperation() == 
              DataFetchExpressionBunch.LogicalOperation.AND) {
            whereStatement.append(" and ");
          }
          
          if (dataFetchExpressionBunch.getOperation() == 
              DataFetchExpressionBunch.LogicalOperation.OR) {
            whereStatement.append(" or ");
          }
        }
        
        String fieldName = dataFetchExpression.getFieldName();
        DataFetchFieldSpec dataFetchFieldSpec = getDataFetchFieldSpec(
            dataFetchDef, fieldName);
        if (null != dataFetchFieldSpec) {
          dataFetchFieldSpec.setInUse(true);
          fieldName = dataFetchFieldSpec.getExpression();
        }        
       

        //String fieldValueParam = getFieldValueName(fieldName, itr, i);  
        /*if (
            //null != dataFetchDef.getAlias() &&
            //(! dataFetchDef.getAlias().equals("")) &&
            (! fieldName.contains("."))) {
          fieldName = dataFetchDef.getAlias() + "." + fieldName;
        } else {
          fieldName = fieldName.replaceAll("\\$alias", dataFetchDef.getAlias());
        }*/
        //fieldName.replaceAll("\\.", "_") + "_" + itr + "_" + i;
        
        if (dataFetchExpression.getOperation() == 
            DataFetchExpression.Operation.OP_EQ) {
          whereStatement.append(" ");
          whereStatement.append(fieldName);
          whereStatement.append(" = ");
          whereStatement.append(getDataFetchExpressionValue(dataFetchExpression));
          whereStatement.append(" ");
        }
        
        if (dataFetchExpression.getOperation() == 
            DataFetchExpression.Operation.OP_NE) {
          whereStatement.append(" ");
          whereStatement.append(fieldName);
          whereStatement.append(" <> ");
          whereStatement.append(getDataFetchExpressionValue(dataFetchExpression));
          whereStatement.append(" ");
        }
        
        if (dataFetchExpression.getOperation() == 
            DataFetchExpression.Operation.OP_GT) {
          whereStatement.append(" ");
          whereStatement.append(fieldName);
          whereStatement.append(" > ");
          whereStatement.append(getDataFetchExpressionValue(dataFetchExpression));
          whereStatement.append(" ");
        }
        
        if (dataFetchExpression.getOperation() == 
            DataFetchExpression.Operation.OP_LT) {
          whereStatement.append(" ");
          whereStatement.append(fieldName);
          whereStatement.append(" < ");
          whereStatement.append(getDataFetchExpressionValue(dataFetchExpression));
          whereStatement.append(" ");
        }

        if (dataFetchExpression.getOperation() == 
            DataFetchExpression.Operation.OP_LE) {
          whereStatement.append(" ");
          whereStatement.append(fieldName);
          whereStatement.append(" <= ");
          whereStatement.append(getDataFetchExpressionValue(dataFetchExpression));
          whereStatement.append(" ");
        }

        if (dataFetchExpression.getOperation() == 
            DataFetchExpression.Operation.OP_GE) {
          whereStatement.append(" ");
          whereStatement.append(fieldName);
          whereStatement.append(" >= ");
          whereStatement.append(getDataFetchExpressionValue(dataFetchExpression));
          whereStatement.append(" ");
        }

        if (dataFetchExpression.getOperation() == 
            DataFetchExpression.Operation.OP_LIKE) {
          whereStatement.append(" ");
          whereStatement.append(fieldName);
          whereStatement.append(" like ");
          whereStatement.append(getDataFetchExpressionValue(dataFetchExpression));
          whereStatement.append(" ");
        }
        
        if (dataFetchExpression.getOperation() == 
            DataFetchExpression.Operation.OP_IN) {
          whereStatement.append(" ");
          whereStatement.append(fieldName);
          whereStatement.append(" in (");
          whereStatement.append(getDataFetchExpressionValue(dataFetchExpression));
          whereStatement.append(") ");
        }
        
        if (dataFetchExpression.getOperation() == 
            DataFetchExpression.Operation.OP_IS_NULL) {
          whereStatement.append(" ");
          whereStatement.append(fieldName);
          whereStatement.append(" IS NULL ");
        }
        
        if (dataFetchExpression.getOperation() == 
            DataFetchExpression.Operation.OP_NOT_NULL) {
          whereStatement.append(" ");
          whereStatement.append(fieldName);
          whereStatement.append(" IS NOT NULL ");
        }
      }

      whereStatement.append(")");
    }

    return(whereStatement);
  }
  
  public static String getDataFetchExpressionValue(
      DataFetchExpression dataFetchExpression) {
        
    if (dataFetchExpression.getFieldType() == 
        DataFetchExpression.ExpressionType.T_BIG_DECIMAL) {
      return dataFetchExpression.getBigDecimalValue().toString();
    }

    if (dataFetchExpression.getFieldType() == 
        DataFetchExpression.ExpressionType.T_BIG_INTEGER) {
      return dataFetchExpression.getBigIntegerValue().toString();
    }
    
    if (dataFetchExpression.getFieldType() == 
        DataFetchExpression.ExpressionType.T_BOOLEAN) {
      return (dataFetchExpression.getBooleanValue()) ? "true" : "false";
    }
    
    if (dataFetchExpression.getFieldType() == 
        DataFetchExpression.ExpressionType.T_DATE) {
      return "\"" + dataFetchExpression.getDateValue() + "\"";
    }
    
    if (dataFetchExpression.getFieldType() == 
        DataFetchExpression.ExpressionType.T_DOUBLE) {
      return dataFetchExpression.getDoubleValue().toString();
    }
    
    if (dataFetchExpression.getFieldType() == 
        DataFetchExpression.ExpressionType.T_INTEGER) {
      return dataFetchExpression.getIntegerValue().toString();
    }
    
    if (dataFetchExpression.getFieldType() == 
        DataFetchExpression.ExpressionType.T_LONG) {
      return dataFetchExpression.getLongValue().toString();
    } 
    
    if (dataFetchExpression.getFieldType() == 
        DataFetchExpression.ExpressionType.T_SHORT) {
      return dataFetchExpression.getShortValue().toString();
    }
    
    if (dataFetchExpression.getFieldType() == 
        DataFetchExpression.ExpressionType.T_STRING) {
      String value = dataFetchExpression.getStringValue();
      value = value.replaceAll("\\\\", "\\\\\\\\");
      value = value.replaceAll("\"", "\\\"");
      return "\"" + value + "\"";
    }
    
//    if (dataFetchExpression.getFieldType() == 
//        DataFetchExpression.ExpressionType.T_COLLECTION) {
//      query.setParameterList(fieldValueParam, 
//          dataFetchExpression.getCollectionValue());
//    }
        
     return(null);
  }

/* 
  private static String buildSqlQueryWhereStatement(DataFetchDef dataFetchDef) {
    if (null != dataFetchDef.getQueryWhereStatement()) {
      return dataFetchDef.getQueryWhereStatement();
    }
    
    StringBuilder whereStatement = generateQueryWhere(dataFetchDef,
        dataFetchDef.getDataFetchExpressionBunch(), 0);
    dataFetchDef.setQueryWhereStatement(whereStatement.toString());

    return(dataFetchDef.getQueryWhereStatement());
  }
  
  private static String buildHbmQueryWhereStatement(DataFetchDef dataFetchDef) {
    if (null != dataFetchDef.getQueryWhereStatement()) {
      return dataFetchDef.getQueryWhereStatement();
    }

    StringBuilder whereStatement = generateQueryWhere(dataFetchDef,
        dataFetchDef.getDataFetchExpressionBunch(), 0);
    dataFetchDef.setQueryWhereStatement(whereStatement.toString());

    return(dataFetchDef.getQueryWhereStatement());
  }
*/
  private static DataFetchFieldSpec getDataFetchFieldSpec(
      DataFetchDef dataFetchDef, String fieldName) {
    if (null == dataFetchDef.getDataFetchFieldsSpec()) {
      return(null);
    }

    for (DataFetchFieldSpec dataFetchFieldSpec : 
        dataFetchDef.getDataFetchFieldsSpec()) {
      if (dataFetchFieldSpec.getFieldName().equals(fieldName)) {
        return(dataFetchFieldSpec);
      }
    }

    return(null);
  }
  
//  private static String getFieldValueName(String fieldName, int itr, int i) {
//    return fieldName.replaceAll("[\\.\\(\\) \\'\\,\\$\\<\\>]", "_") + 
//        "_" + itr + "_" + i;
//  }
}
