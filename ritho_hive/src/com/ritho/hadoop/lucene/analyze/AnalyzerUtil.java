package com.ritho.hadoop.lucene.analyze;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import com.gopdatatrust.hadoop.lucene.analyze.SynonymAnalyzer;

public class AnalyzerUtil {
	
	public static String analyzedString(String fieldname, String s) throws IOException{
		
		Analyzer analyzer = new SynonymAnalyzer(); // or any other analyzer

	    TokenStream ts = analyzer.tokenStream(fieldname, new StringReader(s));
	     
	    CharTermAttribute term = ts.addAttribute(CharTermAttribute.class);
	     
	    String ret = "";
	    String st = null;
	     try {
	    	 ts.reset();
	    	 
	    	 boolean hasToken = false;
	 	    do {
	 	      hasToken = ts.incrementToken( );
	 	      if (hasToken) {st = term.toString( );  ret=ret.concat(st + " " );}
	 	    } while (hasToken); 	    
		       ts.end();   // Perform end-of-stream operations, e.g. set the final offset.
		     } finally {
		       ts.close(); // Release resources associated with this stream.
		     }

	     analyzer.close();
	     return ret.trim();
	}

}
