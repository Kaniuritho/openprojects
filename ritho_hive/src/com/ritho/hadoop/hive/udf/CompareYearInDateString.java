package com.ritho.hadoop.hive.udf;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

public final class CompareYearInDateString extends UDF {
  public Boolean evaluate(final Text year1,  final Text year2) {
	  Boolean ret = false;
	  if(notEmpty(year1) && notEmpty(year2) ){
		  ret = getYear(year1.toString()).equals(getYear(year2.toString()));
	  }else
		  ret=false;
    return ret;
  }
  
  private boolean notEmpty(Text t){
	  return (t!=null && !StringUtils.isEmpty(t.toString()));
  }
  
  private String getYear(final String dateStr){
	  return getYearFromDate(getDatePattern(dateStr),dateStr);
  }
  
  public static Pattern getDatePattern(final String dateStr){
	  
	  Pattern p = null;
	  if(dateStr.matches("\\d{4}-\\d{2}-\\d{2}")){
		  p = Pattern.compile("(?<year>\\d{4,4})-(?<month>\\d{2,2})");
	  }else if(dateStr.matches("\\d{4}/\\d{2}/\\d{2}")){
		  p = Pattern.compile("(?<year>\\d{4,4})/(?<month>\\d{2,2})");
	  }else if(dateStr.matches("\\d{4}\\d{2}\\d{2}")){
		  p = Pattern.compile("(?<year>\\d{4,4})(?<month>\\d{2,2})"); 
	  }else if(dateStr.matches("\\d{4}")){
		  p = Pattern.compile("(?<year>\\d{4,4})"); 
	  }
	  
	  return p;
  }
  public static String getYearFromDate( final Pattern p, final String dateStr ){
	 
	  if(p==null || StringUtils.isEmpty(dateStr))return "NULL";
	  Matcher m = p.matcher(dateStr);
	  String year ="";
	  if (m.find())
	  {
	      year = m.group("year");
	      //String month = m.group("month");
	      //System.out.println(year + "    " + month);
	  }
	  return year;
  }
}

