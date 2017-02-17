package com.trix.web.birt.server;

public class TestJSONCriteria {
  
  private static String jsonStringCriteria =
     " {"+
   " \"_constructor\":\"AdvancedCriteria\", "+
   " \"operator\":\"or\", "+
   " \"criteria\":["+
   "     {"+
   "         \"fieldName\":\"o.officeCode\","+ 
   "         \"operator\":\"iContains\", "+
   "         \"value\":\"1\""+
   "     }, "+
   "     {"+
   "         \"fieldName\":\"o.officeCode\", "+
   "         \"operator\":\"iContains\", "+
   "         \"value\":\"3\""+
   "     }, "+
   "     {"+
   "         \"fieldName\":\"o.officeCode\", "+
   "         \"operator\":\"iEquals\", "+
   "         \"value\":\"2\""+
   "     }, "+
   "     {"+
   "        \"_constructor\":\"AdvancedCriteria\", "+
   "        \"operator\":\"or\", "+
   "        \"criteria\":["+
   "           {"+
   "                 \"fieldName\":\"o.officeCode\", "+
   "                 \"operator\":\"greaterThan\","+ 
   "                 \"value\":\"5\""+
   "             }"+
   "         ]"+
   "     }"+
   "]"+
   "}";
  
  private static String jsonString = 
//      "{'_constructor'='AdvancedCriteria', 'operator'='and', 'criteria'=[" +
//            "{'fieldName'='documentNo', operator='iContains', 'value'='111'}, " +
//            "{'_constructor'='AdvancedCriteria', 'operator'='or', 'criteria'=[" +
//                  "{'fieldName'='documentNo', operator='iContains', 'value'='222'}]}]}";

      "{_constructor=AdvancedCriteria, operator=and, value=123 456 asd}";
//      "{_constructor=AdvancedCriteria, operator=and, criteria=[{fieldName=documentId, operator=equals, value=54, _constructor=AdvancedCriteria}, {_constructor=AdvancedCriteria, operator=and, criteria=[]}]}";
      
//      "{_constructor=AdvancedCriteria, operator=and, " +
//          "criteria=[{fieldName=documentClassId, operator=equals, value=17, _constructor=AdvancedCriteria}, " +
//                    "{_constructor=AdvancedCriteria, operator=and, criteria=[] }]}";

//      "{'_constructor'='AdvancedCriteria', 'operator'='and', 'criteria'=[{'operator'='and', 'criteria'=[{fieldName='name', 'operator'='iContains', value='45'}]}]}";
//      "{'_constructor'='AdvancedCriteria', 'operator'='and', 'criteria'=[{fieldName='name', 'operator'='iContains', value='45'}]}";
      
//      "{ '_constructor'='AdvancedCriteria' ,  'operator'='and' ,  'criteria'=[{ 'operator'='and' ,  'criteria'=[{ 'fieldName'='businessSwitch.businessSwitchId' ,  'operator'='equals' ,  'value'='1' }, { 'fieldName'='consumer' ,  'operator'='equals' ,  'value'='true' }] }]  }";


  public static void main(String[] args) throws Exception{
   System.out.println(
       (new CriteriaUtilities(jsonStringCriteria)).linearizeCriteria()
       );

  }

}
