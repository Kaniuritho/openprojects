package com.ritho.hadoop.hive.udf;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

import com.ritho.hadoop.lucene.analyze.AnalyzerUtil;

public final class CompareStreetNames extends UDF {
	
  public Boolean evaluate(final Text street1, final Text street2 ) throws IOException {
	  
	  String str1 = notEmpty(street1)?street1.toString():"";
	  String str2 = notEmpty(street2)?street2.toString():"";
  
    return AnalyzerUtil.analyzedString("street",str1).equalsIgnoreCase(AnalyzerUtil.analyzedString("street",str2));
  }
  
  private boolean notEmpty(Text t){
	  return (t!=null && !StringUtils.isEmpty(t.toString()));
  }
  
}

