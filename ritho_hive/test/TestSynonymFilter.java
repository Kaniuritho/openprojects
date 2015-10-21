
import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.apache.lucene.analysis.core.LowerCaseTokenizer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.synonym.SynonymFilter;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.CharsRef;
import org.apache.lucene.util.CharsRefBuilder;
import org.junit.Before;
import org.junit.Test;

public class TestSynonymFilter {

	SynonymMap synonymMap = null;
	@Before
	public void init() throws IOException{
		SynonymMap.Builder builder = new SynonymMap.Builder(true);
		
		CharsRefBuilder multiWordCharsRef = new CharsRefBuilder();
		String[] words = new String[] { "income tax", "tax refund", "property tax"};
		SynonymMap.Builder.join(words, multiWordCharsRef);
		builder.add(new CharsRef("tax"), multiWordCharsRef.get(), true);
		
		multiWordCharsRef = new CharsRefBuilder();
		SynonymMap.Builder.join(new String[] { "alone", "indubitably", "single", "only" }, multiWordCharsRef);
	    builder.add(new CharsRef("one"), multiWordCharsRef.get(), true);
	    
	  //SynonymMap.Builder.join(words, multiWordCharsRef)(new FileInputStream("samples/fulltext/wn_s.pl"));

		synonymMap =  builder.build();
	}
	
	
	@Test
	  public void testShowTokens( ) throws Exception {

	    final String input = "tax in California are only high";//"what is my income tax refund this year now that my property tax is so high";
	    WhitespaceTokenizer wt = new WhitespaceTokenizer( );
	    wt.setReader(new StringReader(input));
	    
	    SynonymFilter aptf = new SynonymFilter(  wt, synonymMap, false );
	    CharTermAttribute term = aptf.addAttribute(CharTermAttribute.class);
	    aptf.reset();

	    boolean hasToken = false;
	    do {
	      hasToken = aptf.incrementToken( );
	      if (hasToken) System.out.println( "token:'" + term.toString( ) + "'" );
	    } while (hasToken);
	    
	    aptf.close();
	  }
	
	
	@Test
	  public void testAnalyzerClass( ) throws Exception {
		
	     Analyzer analyzer = new MySynonymAnalyzer(); // or any other analyzer
	     /* TokenStream ts = analyzer.tokenStream("state", new StringReader("for one, tax in California is high"));
	     // The Analyzer class will construct the Tokenizer, TokenFilter(s), and CharFilter(s),
	     //   and pass the resulting Reader to the Tokenizer.
	     OffsetAttribute offsetAtt = ts.addAttribute(OffsetAttribute.class);
	    
	     try {
	       ts.reset(); // Resets this stream to the beginning. (Required)
	       while (ts.incrementToken()) {
	         // Use AttributeSource.reflectAsString(boolean)
	         // for token stream debugging.
	         System.out.println("token: " + ts.reflectAsString(false));
	 
	         System.out.println("token start offset: " + offsetAtt.startOffset());
	         System.out.println("token end offset: " + offsetAtt.endOffset());

	       }
	       ts.end();   // Perform end-of-stream operations, e.g. set the final offset.
	     } finally {
	       ts.close(); // Release resources associated with this stream.
	     }
	     */
	     
	     
	     TokenStream ts = analyzer.tokenStream("state", new StringReader("climate in Cali is dry"));
	     CharTermAttribute term = ts.addAttribute(CharTermAttribute.class);
	     try {
	    	 ts.reset();
	    	 
	    	 boolean hasToken = false;
	 	    do {
	 	      hasToken = ts.incrementToken( );
	 	      if (hasToken) System.out.println( "token:'" + term.toString( ) + "'" );
	 	    } while (hasToken); 	    
		       ts.end();   // Perform end-of-stream operations, e.g. set the final offset.
		     } finally {
		       ts.close(); // Release resources associated with this stream.
		     }
	     
	     analyzer.close();
		
	}
	
	private void printSearchResults(final int limit, final Query query,
			final IndexReader reader) throws IOException {
		IndexSearcher searcher = new IndexSearcher(reader);
		TopDocs docs = searcher.search(query, limit);
 
		System.out.println(docs.totalHits + " found for query: " + query);
 
		for (final ScoreDoc scoreDoc : docs.scoreDocs) {
			System.out.println("score="+scoreDoc.score+"->"+searcher.doc(scoreDoc.doc));
		}
	}
	
		
	//@Test
	public void createFilter() throws IOException{
		SynonymMap.Builder builder = new SynonymMap.Builder(true);
		CharsRefBuilder multiWordCharsRef = new CharsRefBuilder();
	    SynonymMap.Builder.join(new String[] { "and", "indubitably", "single", "only" }, multiWordCharsRef);
	    builder.add(new CharsRef("one"), multiWordCharsRef.get(), true);
	    
	    
	    final String input = "it is only a matter of time until the unexpected indupitable happens";
	    
	    SynonymMap synonymMap = builder.build();
	    WhitespaceTokenizer tokenizer = new WhitespaceTokenizer( );
	    tokenizer.setReader(new StringReader(input));
	    TokenStream stream = new SynonymFilter(tokenizer, synonymMap, true);
	   

	    CharTermAttribute term = stream.addAttribute(CharTermAttribute.class);	 
	    stream.reset();
	   
	    
	    
	    boolean hasToken = false;
	    do {
	      hasToken = stream.incrementToken( );
	      if (hasToken) System.out.println( "token:'" + term.toString( ) + "'" );
	    } while (hasToken);
	    
	    stream.close();
	}
	
	
	
	  public static class MySynonymAnalyzer extends Analyzer{
		  
			public MySynonymAnalyzer(){ super(); }
			
			
			public SynonymMap initSynonymMap() throws IOException{
				SynonymMap synonymMap = null;
				SynonymMap.Builder builder = new SynonymMap.Builder(true);
				
				CharsRefBuilder multiWordCharsRef = new CharsRefBuilder();
				String[] words = new String[] { "income tax", "tax refund", "property tax"};
				SynonymMap.Builder.join(words, multiWordCharsRef);
				builder.add(new CharsRef("tax"), multiWordCharsRef.get(), true);
				
				multiWordCharsRef = new CharsRefBuilder();
				SynonymMap.Builder.join(new String[] { "alone", "indubitably", "single", "only" }, multiWordCharsRef);
			    builder.add(new CharsRef("one"), multiWordCharsRef.get(), true);
			    
			    //multiWordCharsRef = new CharsRefBuilder();
				//SynonymMap.Builder.join(new String[] { "Ca","CA","ca", "Cali", "California" }, multiWordCharsRef);
			    //builder.add(new CharsRef("ca"), multiWordCharsRef.get(), true);
			    
			    
			    multiWordCharsRef = new CharsRefBuilder();
				SynonymMap.Builder.join(new String[] { "az","Arizona", "arizona" }, multiWordCharsRef);
			    builder.add(new CharsRef("az"), multiWordCharsRef.get(), true);
				//builder.add( multiWordCharsRef.get(),new CharsRef("AZ"), true);
			    
			    multiWordCharsRef = new CharsRefBuilder();
				SynonymMap.Builder.join(new String[] { "DC","dc","District of Columbia", "Capitol District" }, multiWordCharsRef);
			    builder.add(new CharsRef("dc"), multiWordCharsRef.get(), true);
			    
			    multiWordCharsRef = new CharsRefBuilder();
				SynonymMap.Builder.join(new String[] { "CA","ca","California","Cali", "Golden State" }, multiWordCharsRef);
			    builder.add(new CharsRef("ca"), multiWordCharsRef.get(), true);

			    
			   /* multiWordCharsRef = new CharsRefBuilder();
				SynonymMap.Builder.join(new String[] { "CA" }, multiWordCharsRef);
			    builder.add(new CharsRef("cali"), multiWordCharsRef.get(), false);
			    
			    multiWordCharsRef = new CharsRefBuilder();
				SynonymMap.Builder.join(new String[] { "CA" }, multiWordCharsRef);
			    builder.add(new CharsRef("california"), multiWordCharsRef.get(), false);
			    
			    multiWordCharsRef = new CharsRefBuilder();
				SynonymMap.Builder.join(new String[] { "CA" }, multiWordCharsRef);
			    builder.add(new CharsRef("golden state"), multiWordCharsRef.get(), false);
			    */
			    
			    
			    //SynonymMap.Builder.join(words, multiWordCharsRef)(new FileInputStream("samples/fulltext/wn_s.pl"));

				synonymMap =  builder.build();
				
				return synonymMap;
			}

			@Override
			protected TokenStreamComponents createComponents(final String fieldName){

				WhitespaceTokenizer source = new WhitespaceTokenizer( );
				//LowerCaseTokenizer source = new LowerCaseTokenizer();
				//KeywordTokenizer source = new KeywordTokenizer();
				
			    SynonymFilter filter =null;
				try {
					filter = new SynonymFilter(  source, initSynonymMap(), true );
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    
			    return new TokenStreamComponents(source, filter);
			  }
			
			  
		  }

}



