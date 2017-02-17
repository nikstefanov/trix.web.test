package com.trix.web.birt.server.dfo;

public class DataFetchFieldSpec {
  private String fieldName;
  private String expression;
  private String[] objectsToJoin;
  private boolean inUse = false;
  
  public DataFetchFieldSpec() {
  }
  
  public DataFetchFieldSpec(String fieldName, String expression, 
      String[] objectsToJoin) {
    this.fieldName = fieldName;
    this.expression = expression;
    this.objectsToJoin = objectsToJoin;
  }

  public String getFieldName() {
    return fieldName;
  }
  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }
  public String getExpression() {
    return expression;
  }
  public void setExpression(String expression) {
    this.expression = expression;
  }
  public String[] getObjectsToJoin() {
    return objectsToJoin;
  }
  public void setObjectsToJoin(String[] objectsToJoin) {
    this.objectsToJoin = objectsToJoin;
  }
  
  public void setInUse(boolean inUse) {
    this.inUse = inUse;
  }
  public boolean isInUse() {
    return inUse;
  }
}
