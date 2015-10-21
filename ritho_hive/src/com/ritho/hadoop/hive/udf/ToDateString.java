package com.ritho.hadoop.hive.udf;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

public final class ToDateString extends UDF {
  public Text evaluate(final Text year, final Text month, final Text day ) {
	  String ret = null;
	  if(notEmpty(year) && notEmpty(month) && notEmpty(day))
		  ret=year.toString()+month.toString()+day.toString();
	  else if(notEmpty(year))
		  ret=year.toString()+"0101";
	  else
		  ret="";
    return new Text(ret);
  }
  private boolean notEmpty(Text t){
	  return (t!=null && !StringUtils.isEmpty(t.toString()));
  }
}

