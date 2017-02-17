package com.trix.web.birt.server.dfo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class DataFetchDef implements Serializable {
  private static final long serialVersionUID = 4743641959569569460L;

  private String objectClass = "";
  private String alias = "";
  
  private int startRow = 0;
  private int fetchRows = 30;

  private DataFetchExpressionBunch dataFetchExpressionBunch;

  private String[] orderBy;
  private String[] groupBy;

  // names of the fields to fetch, only listed fields will be returned 
  // through dynamic DTO
  private String[] fetchFields;
  
  /*
   * Data fetch field specs. Provides a list fields specification, where
   * for each field alias, expression and join objects are set. Fields
   * specifications are used in:
   * - selected fields list
   * - where expression building
   * - what objects to join
   */
  private transient List<DataFetchFieldSpec> dataFetchFieldsSpec;
  
  private transient List<DataFetchJoinDef> dataFetchJoinDefs;
  
  private transient String queryJoinDefinitions = null;
  private transient String queryWhereStatement = null;
  
  public DataFetchDef() {
  }

  public void setDataFetchJoinDefs(List<DataFetchJoinDef> dataFetchJoinDefs) {
    this.dataFetchJoinDefs = dataFetchJoinDefs;
  }
  public List<DataFetchJoinDef> getDataFetchJoinDefs() {
    return(dataFetchJoinDefs);
  }
  
  public void addDataFetchJoinDef(DataFetchJoinDef dataFetchJoinDef) {
    if (null == dataFetchJoinDefs) {
      dataFetchJoinDefs = new ArrayList<DataFetchJoinDef>();
    }
    dataFetchJoinDefs.add(dataFetchJoinDef);
  }
  
  public void renameExpressionField(String fieldName, String newFieldName) {
    renameExpressionField(fieldName, newFieldName, dataFetchExpressionBunch);
  }
  
  public void renameExpressionField(String fieldName, String newFieldName,
      DataFetchExpressionBunch eb) {
    if (null == eb) {
      return;
    }

    if (null != eb.getExpressions()) {
      Iterator<DataFetchExpression> it = eb.getExpressions().iterator();
      while (it.hasNext()) {
        DataFetchExpression e = it.next();
        if (e.getFieldName().equals(fieldName)) {
          e.setFieldName(newFieldName);
        }
      }
    }
    
    if (null != eb.getExpressionBunch()) {
      for (DataFetchExpressionBunch b : eb.getExpressionBunch()) {
        renameExpressionField(fieldName, newFieldName, b);
      }
    }
  }
  
  public DataFetchExpression retrieveAndRemoveExpression(String fieldName) {
    return retrieveAndRemoveExpression(fieldName, dataFetchExpressionBunch);
  }

  public DataFetchExpression retrieveAndRemoveExpression(String fieldName, 
      DataFetchExpressionBunch eb) {
    if (null == eb) {
      return(null);
    }

    if (null != eb.getExpressions()) {
      Iterator<DataFetchExpression> it = eb.getExpressions().iterator();
      while (it.hasNext()) {
        DataFetchExpression e = it.next();
        if (e.getFieldName().equals(fieldName)) {
          it.remove();
          return(e);
        }
      }
    }
    
    if (null != eb.getExpressionBunch()) {
      for (DataFetchExpressionBunch b : eb.getExpressionBunch()) {
        DataFetchExpression e = retrieveAndRemoveExpression(fieldName, b);
        if (null != e) {
          return e;
        }
      }
    }

    return(null);
  }

  public void setFetchFields(String[] fetchFields) {
    this.fetchFields = fetchFields;
  }
  
  public String[] getFetchFields() {
    return fetchFields;
  }

  /**
   * Simple method for adding expression to the data fetch definition.
   * By default all added expressions are grouped in AND criteria.
   * For more flexibility use addExpressionBunch
   * 
   * @param fieldName
   * @param fieldValue
   * @param operation
   */
  public void addExpression(DataFetchExpression dataFetchExpression) {
    if (null == dataFetchExpressionBunch) {
      dataFetchExpressionBunch = new DataFetchExpressionBunch(); 
    }
    
    dataFetchExpressionBunch.addExpression(dataFetchExpression);
  }

  public void addBigDecimalExpression(String fieldName, BigDecimal fieldValue, 
      DataFetchExpression.Operation operation) {
    if (null == dataFetchExpressionBunch) {
      dataFetchExpressionBunch = new DataFetchExpressionBunch(); 
    }
    
    dataFetchExpressionBunch.addBigDecimalExpression(
        fieldName, fieldValue, operation);
  }
  
  public void addBigIntegerExpression(String fieldName, BigInteger fieldValue, 
      DataFetchExpression.Operation operation) {
    if (null == dataFetchExpressionBunch) {
      dataFetchExpressionBunch = new DataFetchExpressionBunch(); 
    }
    
    dataFetchExpressionBunch.addBigIntegerExpression(
        fieldName, fieldValue, operation);
  }
  
  public void addBooleanExpression(String fieldName, Boolean fieldValue, 
      DataFetchExpression.Operation operation) {
    if (null == dataFetchExpressionBunch) {
      dataFetchExpressionBunch = new DataFetchExpressionBunch(); 
    }
    
    dataFetchExpressionBunch.addBooleanExpression(
        fieldName, fieldValue, operation);
  }
  
  public void addDateExpression(String fieldName, Date fieldValue, 
      DataFetchExpression.Operation operation) {
    if (null == dataFetchExpressionBunch) {
      dataFetchExpressionBunch = new DataFetchExpressionBunch(); 
    }
    
    dataFetchExpressionBunch.addDateExpression(
        fieldName, fieldValue, operation);
  }
  
  public void addDoubleExpression(String fieldName, Double fieldValue, 
      DataFetchExpression.Operation operation) {
    if (null == dataFetchExpressionBunch) {
      dataFetchExpressionBunch = new DataFetchExpressionBunch(); 
    }
    
    dataFetchExpressionBunch.addDoubleExpression(
        fieldName, fieldValue, operation);
  }
  
  public void addIntegerExpression(String fieldName, Integer fieldValue, 
      DataFetchExpression.Operation operation) {
    if (null == dataFetchExpressionBunch) {
      dataFetchExpressionBunch = new DataFetchExpressionBunch(); 
    }
    
    dataFetchExpressionBunch.addIntegerExpression(
        fieldName, fieldValue, operation);
  }
  
  public void addLongExpression(String fieldName, Long fieldValue, 
      DataFetchExpression.Operation operation) {
    if (null == dataFetchExpressionBunch) {
      dataFetchExpressionBunch = new DataFetchExpressionBunch(); 
    }
    
    dataFetchExpressionBunch.addLongExpression(
        fieldName, fieldValue, operation);
  }

  public void addShortExpression(String fieldName, Short fieldValue, 
      DataFetchExpression.Operation operation) {
    if (null == dataFetchExpressionBunch) {
      dataFetchExpressionBunch = new DataFetchExpressionBunch(); 
    }
    
    dataFetchExpressionBunch.addShortExpression(
        fieldName, fieldValue, operation);
  }

  public void addStringExpression(String fieldName, String fieldValue, 
      DataFetchExpression.Operation operation) {
    if (null == dataFetchExpressionBunch) {
      dataFetchExpressionBunch = new DataFetchExpressionBunch(); 
    }
    
    dataFetchExpressionBunch.addStringExpression(
        fieldName, fieldValue, operation);
  }
  
  public void addCollectionExpression(String fieldName, Object[] fieldValue, 
      DataFetchExpression.Operation operation) {
    if (null == dataFetchExpressionBunch) {
      dataFetchExpressionBunch = new DataFetchExpressionBunch(); 
    }
    
    dataFetchExpressionBunch.addCollectionExpression(
        fieldName, fieldValue, operation);
  }

  /**
   * Add expression bunch to the data fetch definition. This is more flexible, 
   * but more heavy way to define expressions
   * 
   * @param fieldName
   * @param fieldValue
   * @param operation
   */
  public void addExpressionBunch(DataFetchExpressionBunch 
      dataFetchExpressionBunch) {
    if (null == this.dataFetchExpressionBunch) {
      this.dataFetchExpressionBunch = new DataFetchExpressionBunch(); 
    }

    this.dataFetchExpressionBunch.addExpressionBunch(dataFetchExpressionBunch);
  }

  public void setObjectClass(String objectClass) {
    this.objectClass = objectClass;
  }
  public String getObjectClass() {
    return(objectClass);
  }
  
  public void setAlias(String alias) {
    this.alias = alias;
  }
  public String getAlias() {
    return(alias);
  }

  public int getStartRow() {
    return startRow;
  }

  public void setStartRow(int startRow) {
    this.startRow = startRow;
  }

  public int getFetchRows() {
    return fetchRows;
  }

  public void setFetchRows(int fetchRows) {
    this.fetchRows = fetchRows;
  }

  public DataFetchExpressionBunch getDataFetchExpressionBunch() {
    return dataFetchExpressionBunch;
  }

  public void setDataFetchExpressionBunch(
      DataFetchExpressionBunch dataFetchExpressionBunch) {
    this.dataFetchExpressionBunch = dataFetchExpressionBunch;
  }

  public String[] getOrderBy() {
    return orderBy;
  }

  public void setOrderBy(String[] orderBy) {
    this.orderBy = orderBy;
  }
  
  public String[] getGroupBy() {
    return groupBy;
  }

  public void setGroupBy(String[] groupBy) {
    this.groupBy = groupBy;
  }
  
  public String toString() {
    String s = "";
    
    s += "=== DataFetchDef ===\n";
    s += dataFetchExpressionBunchToString(getDataFetchExpressionBunch(), 1);   
    s += "====================\n";

    return(s);
  }
  
  private String dataFetchExpressionBunchToString(DataFetchExpressionBunch 
      dataFetchExpressionBunch, int il) {
    String r = "";

    String ident = "";
    for (int i=0; i<il; i++) {
      ident += "  ";
    }

    r += ident + " DataFetchExpressionBunch: " + dataFetchExpressionBunch + "\n";
    if (null != dataFetchExpressionBunch) {
      if (null != dataFetchExpressionBunch.getExpressions()) {
        for (int i=0; i<dataFetchExpressionBunch.
            getExpressions().size(); i++) {
          DataFetchExpression ex = dataFetchExpressionBunch.getExpressions().get(0);
          r += ident + " field: " + ex.getFieldName() + "," +
              " int value: " + ex.getIntegerValue() + "," +
              " string value: " + ex.getStringValue() + "\n";
        }
        r += "\n";
      }
      
      if (null != dataFetchExpressionBunch.getExpressionBunch()) {
        for (int i=0; i<dataFetchExpressionBunch.
            getExpressionBunch().size(); i++) {
          r += dataFetchExpressionBunchToString(dataFetchExpressionBunch.
              getExpressionBunch().get(0), il+1);
        }
        r += "\n";
      }
    }

    return(r);
  }
  
  public void addDataFetchFieldSpec(DataFetchFieldSpec dataFetchFieldSpec) {
    if (null == dataFetchFieldsSpec) {
      dataFetchFieldsSpec = new ArrayList<DataFetchFieldSpec>();
    }
    dataFetchFieldsSpec.add(dataFetchFieldSpec);
  }
  public void setDataFetchFieldsSpec(
      List<DataFetchFieldSpec> dataFetchFieldsSpec) {
    this.dataFetchFieldsSpec = dataFetchFieldsSpec;
  }
  public List<DataFetchFieldSpec> getDataFetchFieldsSpec() {
    return dataFetchFieldsSpec;
  }

  public void setQueryJoinDefinitions(String queryJoinDefinitions) {
    this.queryJoinDefinitions = queryJoinDefinitions;
  }
  public String getQueryJoinDefinitions() {
    return(this.queryJoinDefinitions);
  }
  
  public void setQueryWhereStatement(String queryWhereStatement) {
    this.queryWhereStatement = queryWhereStatement;
  }
  public String getQueryWhereStatement() {
    return queryWhereStatement;
  }
}
