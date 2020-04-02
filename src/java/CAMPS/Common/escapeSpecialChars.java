package CAMPS.Common;
public class escapeSpecialChars {
 
    public String escapeSpecialChar(String value){
        String val1=value.replaceAll("\\\\","\\\\\\\\");
        String val=val1.replaceAll("'","\\\\'").replaceAll("#","").replaceAll("\"","&#34;");
        return val;
     }
    public String escapeSQLInjuction(String value){
         return value.replaceAll("\"","&#34;");
     }
}
