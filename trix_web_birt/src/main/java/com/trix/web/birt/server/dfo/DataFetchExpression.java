package com.trix.web.birt.server.dfo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class DataFetchExpression implements Serializable {
  private static final long serialVersionUID = 1L;

  public static enum Operation {
    OP_EQ,
    OP_NE,
    OP_GT,
    OP_LT,
    OP_LE,
    OP_GE,
    OP_LIKE,
    OP_IN,
    OP_IS_NULL,
    OP_NOT_NULL,
  };
  
  public static enum ExpressionType {
    T_BIG_DECIMAL,
    T_BIG_INTEGER,
    T_BOOLEAN,
    T_DATE,
    T_DOUBLE,
    T_INTEGER,
    T_LONG,
    T_SHORT,
    T_STRING,
    T_COLLECTION
  };

  private Operation operation;
  private String fieldName;
  private ExpressionType fieldType;
  
  private BigDecimal bigDecimalValue = null;
  private BigInteger bigIntegerValue = null;
  private Boolean booleanValue = null;
  private Date dateValue = null;
  private Double doubleValue = null;
  private Integer integerValue = null;
  private Long longValue = null;
  private Short shortValue = null;
  private String stringValue = null;
  private Object[] collectionValue = null;
  
  public DataFetchExpression() {
  }
    
  public DataFetchExpression(String fieldName, BigDecimal fieldValue, 
      Operation operation) {
    this.fieldName = fieldName;
    this.bigDecimalValue = fieldValue;
    this.operation = operation;
    
    fieldType = ExpressionType.T_BIG_DECIMAL;
  }
  
  public DataFetchExpression(String fieldName, BigInteger fieldValue, 
      Operation operation) {
    this.fieldName = fieldName;
    this.bigIntegerValue = fieldValue;
    this.operation = operation;
    
    fieldType = ExpressionType.T_BIG_INTEGER;
  }
  
  public DataFetchExpression(String fieldName, Boolean fieldValue, 
      Operation operation) {
    this.fieldName = fieldName;
    this.booleanValue = fieldValue;
    this.operation = operation;
    
    fieldType = ExpressionType.T_BOOLEAN;
  }
  
  public DataFetchExpression(String fieldName, Date fieldValue, 
      Operation operation) {
    this.fieldName = fieldName;
    this.dateValue = fieldValue;
    this.operation = operation;
    
    fieldType = ExpressionType.T_DATE;
  }
  
  public DataFetchExpression(String fieldName, Double fieldValue, 
      Operation operation) {
    this.fieldName = fieldName;
    this.doubleValue = fieldValue;
    this.operation = operation;
    
    fieldType = ExpressionType.T_DOUBLE;
  }

  public DataFetchExpression(String fieldName, Integer fieldValue, 
      Operation operation) {
    this.fieldName = fieldName;
    this.integerValue = fieldValue;
    this.operation = operation;
    
    fieldType = ExpressionType.T_INTEGER;
  }
  
  public DataFetchExpression(String fieldName, Long fieldValue, 
      Operation operation) {
    this.fieldName = fieldName;
    this.longValue = fieldValue;
    this.operation = operation;
    
    fieldType = ExpressionType.T_LONG;
  }
  
  public DataFetchExpression(String fieldName, Short fieldValue, 
      Operation operation) {
    this.fieldName = fieldName;
    this.shortValue = fieldValue;
    this.operation = operation;
    
    fieldType = ExpressionType.T_SHORT;
  }
  
  public DataFetchExpression(String fieldName, String fieldValue, 
      Operation operation) {
    this.fieldName = fieldName;
    this.stringValue = fieldValue;
    this.operation = operation;
    
    fieldType = ExpressionType.T_STRING;
  }
  
  public DataFetchExpression(String fieldName, Object[] fieldValue, 
      Operation operation) {
    this.fieldName = fieldName;
    this.collectionValue = fieldValue;
    this.operation = operation;
    
    fieldType = ExpressionType.T_COLLECTION;
  }

  public Operation getOperation() {
    return operation;
  }
  public void setOperation(Operation operation) {
    this.operation = operation;
  }
  public String getFieldName() {
    return fieldName;
  }
  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  public ExpressionType getFieldType() {
    return fieldType;
  }

  public void setFieldType(ExpressionType fieldType) {
    this.fieldType = fieldType;
  }

  public BigDecimal getBigDecimalValue() {
    return bigDecimalValue;
  }

  public void setBigDecimalValue(BigDecimal bigDecimalValue) {
    this.bigDecimalValue = bigDecimalValue;
  }

  public BigInteger getBigIntegerValue() {
    return bigIntegerValue;
  }

  public void setBigIntegerValue(BigInteger bigIntegerValue) {
    this.bigIntegerValue = bigIntegerValue;
  }

  public Boolean getBooleanValue() {
    return booleanValue;
  }

  public void setBooleanValue(Boolean booleanValue) {
    this.booleanValue = booleanValue;
  }

  public Date getDateValue() {
    return dateValue;
  }

  public void setDateValue(Date dateValue) {
    this.dateValue = dateValue;
  }

  public Double getDoubleValue() {
    return doubleValue;
  }

  public void setDoubleValue(Double doubleValue) {
    this.doubleValue = doubleValue;
  }

  public Integer getIntegerValue() {
    return integerValue;
  }

  public void setIntegerValue(Integer integerValue) {
    this.integerValue = integerValue;
  }

  public Long getLongValue() {
    return longValue;
  }

  public void setLongValue(Long longValue) {
    this.longValue = longValue;
  }

  public Short getShortValue() {
    return shortValue;
  }

  public void setShortValue(Short shortValue) {
    this.shortValue = shortValue;
  }

  public String getStringValue() {
    return stringValue;
  }

  public void setStringValue(String stringValue) {
    this.stringValue = stringValue;
  }
  
  public Object[] getCollectionValue() {
    return collectionValue;
  }

  public void setCollectionValue(Object[] collectionValue) {
    this.collectionValue = collectionValue;
  }
}
