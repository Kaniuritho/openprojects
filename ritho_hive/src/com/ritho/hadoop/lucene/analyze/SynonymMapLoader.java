package com.ritho.hadoop.lucene.analyze;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.synonym.SolrSynonymParser;
import org.apache.lucene.analysis.synonym.SynonymMap;

public class SynonymMapLoader {

	private static SynonymMapLoader loader =null;
	public String LOCAL_SYNONYM_FILE = "resources/lucene_solr_synonyms";
	
	private SynonymMapLoader() throws IOException, ParseException{
			System.out.println("Initializing SynonymMapLoader");
			Analyzer analyzer = new SimpleAnalyzer();
			SolrSynonymParser parser = new SolrSynonymParser(true,true,analyzer);
			InputStream stream = this.getClass().getClassLoader().getResourceAsStream(LOCAL_SYNONYM_FILE);
			InputStreamReader reader = new InputStreamReader(stream);
			parser.parse(reader);
			synonymMap = parser.build();
			analyzer.close();
			reader.close();
	}
	
	static SynonymMapLoader singleton() throws IOException, ParseException{
		if(loader==null){
			loader = new SynonymMapLoader();
		}
		return loader;
	}
	
	private SynonymMap synonymMap = null;
	
	public SynonymMap getSynonymMap(){
		return synonymMap;
	}
	
}
