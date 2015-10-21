package com.ritho.hadoop.lucene.analyze;

import java.io.IOException;
import java.text.ParseException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.synonym.SynonymFilter;
import org.apache.lucene.analysis.synonym.SynonymMap;

public class SynonymAnalyzer extends Analyzer{
	  
		public SynonymAnalyzer(){ super(); }
		
		
		public SynonymMap initSynonymMap() throws IOException, ParseException{
			System.out.println("Initializing synonym map in SynonymAnalyzer");
			return SynonymMapLoader.singleton().getSynonymMap();
		}

		
		@Override
		protected TokenStreamComponents createComponents(final String fieldName){

			WhitespaceTokenizer source = new WhitespaceTokenizer();
	
		    SynonymFilter filter =null;
			try {
				filter = new SynonymFilter(  source, initSynonymMap(), true );
			} catch (Exception e) {
				e.printStackTrace();
			}
		    
		    return new TokenStreamComponents(source, filter);
		  }
		
		  
	  }
