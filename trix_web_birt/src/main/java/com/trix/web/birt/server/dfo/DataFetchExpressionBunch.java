package com.trix.web.birt.server.dfo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Criteria is a bunch of expressions, grouped with a single operation - 
 * AND or OR
 * 
 * @author paco
 *
 */
public class DataFetchExpressionBunch implements Serializable {
  private static final long serialVersionUID = 1L;

  public static enum LogicalOperation {
    AND, OR
  };

  private LogicalOperation operation = LogicalOperation.AND;
  private List<DataFetchExpressionBunch> expressionBunch;
  private List<DataFetchExpression> expressions;
  
  public DataFetchExpressionBunch() {
  }
  
  public DataFetchExpressionBunch(LogicalOperation operation) {
    this.operation = operation;
  }
  
  public void addExpression(DataFetchExpression dataFetchExpression) {
    if (null == expressions) {
      expressions = new ArrayList<DataFetchExpression>();
    }

    expressions.add(dataFetchExpression);
  }

  public void addBigDecimalExpression(String fieldName, BigDecimal fieldValue, 
      DataFetchExpression.Operation operation) {
    if (null == expressions) {
      expressions = new ArrayList<DataFetchExpression>();
    }

    expressions.add(new DataFetchExpression(fieldName, fieldValue, operation));
  }
  
  public void addBigIntegerExpression(String fieldName, BigInteger fieldValue, 
      DataFetchExpression.Operation operation) {
    if (null == expressions) {
      expressions = new ArrayList<DataFetchExpression>();
    }

    expressions.add(new DataFetchExpression(fieldName, fieldValue, operation));
  }
  
  public void addBooleanExpression(String fieldName, Boolean fieldValue, 
      DataFetchExpression.Operation operation) {
    if (null == expressions) {
      expressions = new ArrayList<DataFetchExpression>();
    }

    expressions.add(new DataFetchExpression(fieldName, fieldValue, operation));
  }
  
  public void addDateExpression(String fieldName, Date fieldValue, 
      DataFetchExpression.Operation operation) {
    if (null == expressions) {
      expressions = new ArrayList<DataFetchExpression>();
    }

    expressions.add(new DataFetchExpression(fieldName, fieldValue, operation));
  }
  
  public void addDoubleExpression(String fieldName, Double fieldValue, 
      DataFetchExpression.Operation operation) {
    if (null == expressions) {
      expressions = new ArrayList<DataFetchExpression>();
    }

    expressions.add(new DataFetchExpression(fieldName, fieldValue, operation));
  }
  
  public void addIntegerExpression(String fieldName, Integer fieldValue, 
      DataFetchExpression.Operation operation) {
    if (null == expressions) {
      expressions = new ArrayList<DataFetchExpression>();
    }

    expressions.add(new DataFetchExpression(fieldName, fieldValue, operation));
  }
  
  public void addLongExpression(String fieldName, Long fieldValue, 
      DataFetchExpression.Operation operation) {
    if (null == expressions) {
      expressions = new ArrayList<DataFetchExpression>();
    }

    expressions.add(new DataFetchExpression(fieldName, fieldValue, operation));
  }
  
  public void addShortExpression(String fieldName, Short fieldValue, 
      DataFetchExpression.Operation operation) {
    if (null == expressions) {
      expressions = new ArrayList<DataFetchExpression>();
    }

    expressions.add(new DataFetchExpression(fieldName, fieldValue, operation));
  }

  public void addStringExpression(String fieldName, String fieldValue, 
      DataFetchExpression.Operation operation) {
    if (null == expressions) {
      expressions = new ArrayList<DataFetchExpression>();
    }

    expressions.add(new DataFetchExpression(fieldName, fieldValue, operation));
  }
  
  public void addCollectionExpression(String fieldName, Object[] fieldValue, 
      DataFetchExpression.Operation operation) {
    if (null == expressions) {
      expressions = new ArrayList<DataFetchExpression>();
    }

    expressions.add(new DataFetchExpression(fieldName, fieldValue, operation));
  }

  public void addExpressionBunch(DataFetchExpressionBunch 
      dataFetchExpressionBunch) {
    if (null == expressionBunch) {
      expressionBunch = new ArrayList<DataFetchExpressionBunch>();
    }
    
    expressionBunch.add(dataFetchExpressionBunch);
  }

  public LogicalOperation getOperation() {
    return operation;
  }

  public void setOperation(LogicalOperation operation) {
    this.operation = operation;
  }

  public List<DataFetchExpressionBunch> getExpressionBunch() {
    return expressionBunch;
  }

  public void setExpressionBunch(List<DataFetchExpressionBunch> expressionBunch) {
    this.expressionBunch = expressionBunch;
  }

  public List<DataFetchExpression> getExpressions() {
    return expressions;
  }

  public void setExpressions(List<DataFetchExpression> expressions) {
    this.expressions = expressions;
  }
}
