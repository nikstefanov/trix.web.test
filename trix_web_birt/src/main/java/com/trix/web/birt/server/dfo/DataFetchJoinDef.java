package com.trix.web.birt.server.dfo;

public class DataFetchJoinDef {
  private String objectClass;
  private String joinExpression;
  private String alias;
  
  public DataFetchJoinDef() {
  }

  public DataFetchJoinDef(String objectClass, String alias,  
      String joinExpression) {
    this.objectClass = objectClass;
    this.alias = alias;
    this.joinExpression = joinExpression;
  }

  public String getObjectClass() {
    return objectClass;
  }
  public void setObjectClass(String objectClass) {
    this.objectClass = objectClass;
  }
  public String getJoinExpression() {
    return joinExpression;
  }
  public void setJoinExpression(String joinExpression) {
    this.joinExpression = joinExpression;
  }
  public void setAlias(String alias) {
    this.alias = alias;
  }
  public String getAlias() {
    return(alias);
  }
}
