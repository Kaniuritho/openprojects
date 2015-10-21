

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.junit.Before;
import org.junit.Test;


/** Similarity unit test.
 *
 */
public class TestSimilarity {
	Directory index = null;
	@Before
	public void init() throws IOException{
		 index = FSDirectory.open(Paths.get("./similarityindex"));
		//index = new RAMDirectory();
		IndexWriter indexwriter = openIndex();
		createDocuments(indexwriter);
		
	}
	
	private void createDocuments(IndexWriter writer) throws IOException{
	
		Document doc = new Document();
		doc.add(new TextField("fullname", "ken cat", Store.YES));
		doc.add(new TextField("address1", "12 Someplace street", Store.YES));
		doc.add(new TextField("address2", "Apartment 6", Store.YES));
		doc.add(new TextField("city", "Washington", Store.YES));
		doc.add(new TextField("state", "DC", Store.YES));
		//doc.add(new TextField("zip5", "20002", Store.YES));
		doc.add(new IntField("zip5", 20002, Store.YES));
		writer.addDocument(doc);
		
		doc = new Document();
		doc.add(new TextField("fullname", "big cat", Store.YES));
		doc.add(new TextField("address1", "10001 Somewhere ave", Store.YES));
		doc.add(new TextField("address2", "Apartment 3", Store.YES));
		doc.add(new TextField("city", "Washington", Store.YES));
		doc.add(new TextField("state", "DC", Store.YES));
		//doc.add(new TextField("zip5", "20008", Store.YES));
		doc.add(new IntField("zip5", 20008, Store.YES));
		writer.addDocument(doc);
		
		doc = new Document();
		doc.add(new TextField("fullname", "big cat", Store.YES));
		doc.add(new TextField("address1", "10001 Somewhere Avenue", Store.YES));
		doc.add(new TextField("address2", "Apartment 3", Store.YES));
		doc.add(new TextField("city", "Washington", Store.YES));
		doc.add(new TextField("state", "DC", Store.YES));
		//doc.add(new TextField("zip5", "20008", Store.YES));
		doc.add(new IntField("zip5", 20008, Store.YES));
		writer.addDocument(doc);

		doc = new Document();
		doc.add(new TextField("fullname", "kitty cat", Store.YES));
		doc.add(new TextField("address1", "555 Someplace street", Store.YES));
		doc.add(new TextField("address2", "Apartment 3", Store.YES));
		doc.add(new TextField("city", "San Mateo", Store.YES));
		doc.add(new TextField("state", "CA", Store.YES));
		doc.add(new IntField("zip5", 94404, Store.YES));
		//doc.add(new TextField("zip5", "94404",Store.YES));
		writer.addDocument(doc);
				
		doc = new Document();
		doc.add(new TextField("fullname", "ken cat", Store.YES));
		doc.add(new TextField("address1", "555 Someplace street", Store.YES));
		doc.add(new TextField("address2", "Apartment 6", Store.YES));
		doc.add(new TextField("city", "San Mateo", Store.YES));
		doc.add(new TextField("state", "CA", Store.YES));
		//doc.add(new TextField("zip5", "94404", Store.YES));
		doc.add(new IntField("zip5", 94404, Store.YES));
		writer.addDocument(doc);
		
		
		doc = new Document();
		doc.add(new TextField("fullname", "some cat", Store.YES));
		doc.add(new TextField("address1", "1212 noplace ave", Store.YES));
		doc.add(new TextField("address2", "", Store.YES));
		doc.add(new TextField("city", "San Jose", Store.YES));
		doc.add(new TextField("state", "CA", Store.YES));
		//doc.add(new TextField("zip5", "92201", Store.YES));
		doc.add(new IntField("zip5", 92201, Store.YES));
		writer.addDocument(doc);
		
		doc = new Document();
		doc.add(new TextField("fullname", "desert cat", Store.YES));
		doc.add(new TextField("address1", "1 nowhere street", Store.YES));
		doc.add(new TextField("address2", "", Store.YES));
		doc.add(new TextField("city", "Tuson", Store.YES));
		doc.add(new TextField("state", "AZ", Store.YES));
		//doc.add(new TextField("zip5", "92201", Store.YES));
		doc.add(new IntField("zip5", 82270, Store.YES));
		writer.addDocument(doc);
		
		doc = new Document();
		doc.add(new TextField("fullname", "sand cat", Store.YES));
		doc.add(new TextField("address1", "5 nowhere street", Store.YES));
		doc.add(new TextField("address2", "", Store.YES));
		doc.add(new TextField("city", "Tuson", Store.YES));
		doc.add(new TextField("state", "AZ", Store.YES));
		//doc.add(new TextField("zip5", "92201", Store.YES));
		doc.add(new IntField("zip5", 82270, Store.YES));
		writer.addDocument(doc);
		
		doc = new Document();
		doc.add(new TextField("fullname", "fur cat", Store.YES));
		doc.add(new TextField("address1", "6 hollywood blvd", Store.YES));
		doc.add(new TextField("address2", "", Store.YES));
		doc.add(new TextField("city", "Los angeles", Store.YES));
		doc.add(new TextField("state", "CA", Store.YES));
		//doc.add(new TextField("zip5", "92201", Store.YES));
		doc.add(new IntField("zip5", 90210, Store.YES));
		writer.addDocument(doc);
		
		
		writer.commit();
		writer.close();
	}
	

	
	private IndexWriter openIndex() throws IOException{
		Map<String, Analyzer> analyzerPerField = new HashMap<String, Analyzer>();
		analyzerPerField.put("state", new KeywordAnalyzer());
		//analyzerPerField.put("zip5", new KeywordAnalyzer());
		analyzerPerField.put("fullname", new SimpleAnalyzer());
		analyzerPerField.put("state",  new TestSynonymFilter.MySynonymAnalyzer());
		PerFieldAnalyzerWrapper analyzer = new PerFieldAnalyzerWrapper(
				new StandardAnalyzer(), analyzerPerField);
		IndexWriterConfig config = new IndexWriterConfig(analyzer)
				.setOpenMode(OpenMode.CREATE);
		config.setSimilarity(new DTSimpleSimilarity());///SET SIMILARITY
		
		IndexWriter writer = new IndexWriter(index, config);
		
		return writer;
	}
	
	private IndexReader openIndexForeReading() throws IOException{
		
		IndexReader reader = DirectoryReader.open(index);
		
		return reader;
	}
	
	@Test
	public void queryOneField() throws IOException{
		int limit = 1;
		IndexReader reader = openIndexForeReading();
		Query query = new TermQuery(new Term("fullname", "cat"));
		printSearchResults(limit, query, reader);
	}
	
	@Test
	public void queryMutipleBooleanShould() throws IOException{
		 BooleanQuery query = new BooleanQuery();
		 query.add(new TermQuery(new Term("fullname", "cat")), BooleanClause.Occur.SHOULD);
		 query.add(new TermQuery(new Term("state", "dc")), BooleanClause.Occur.SHOULD);
		    
		    int limit = 8;
			IndexReader reader = openIndexForeReading();
			printSearchResults(limit, query, reader);
	}
	
	@Test
	public void queryMutipleBooleanMust() throws IOException{
		 BooleanQuery query = new BooleanQuery();
		 query.add(new TermQuery(new Term("fullname", "cat")), BooleanClause.Occur.MUST);
		 query.add(new TermQuery(new Term("state", "District of Columbia")), BooleanClause.Occur.MUST);
		    
		 int limit = 8;
		 IndexReader reader = openIndexForeReading();
		 printSearchResults(limit, query, reader);
	}
	
	@Test
	public void queryMutipleBooleanFilter() throws IOException{
		 BooleanQuery query = new BooleanQuery();
		 query.add(new TermQuery(new Term("fullname", "cat")), BooleanClause.Occur.FILTER);
		 //query.add(new TermQuery(new Term("address2", "6")), BooleanClause.Occur.FILTER);
		 query.add(new TermQuery(new Term("address1", "somewhere")), BooleanClause.Occur.FILTER);
		 query.add(new TermQuery(new Term("state", "DC")), BooleanClause.Occur.SHOULD);
		 
		 //BytesRefBuilder bytes = new BytesRefBuilder();
         //NumericUtils.intToPrefixCoded(20008, 0, bytes);
		 //query.add(new TermQuery(new Term("zip5", bytes.get())), BooleanClause.Occur.SHOULD);
		 
		 //query.add( NumericRangeQuery.newIntRange("zip5",20000,20010, true, true), BooleanClause.Occur.FILTER);
		 
		 int limit = 6;
		 IndexReader reader = openIndexForeReading();
		 printSearchResults(limit, query, reader);
	}
	
	@Test
	public void queryBooleanFilter_Synonym() throws IOException{
		 BooleanQuery query = new BooleanQuery();
		 query.add(new TermQuery(new Term("fullname", "cat")), BooleanClause.Occur.FILTER);
		 query.add(new TermQuery(new Term("state", "Cali")), BooleanClause.Occur.FILTER);
		 
		 //BytesRefBuilder bytes = new BytesRefBuilder();
         //NumericUtils.intToPrefixCoded(20008, 0, bytes);
		 //query.add(new TermQuery(new Term("zip5", bytes.get())), BooleanClause.Occur.SHOULD);
		 
		 //query.add( NumericRangeQuery.newIntRange("zip5",20000,20010, true, true), BooleanClause.Occur.FILTER);
		 
		 int limit = 8;
		 IndexReader reader = openIndexForeReading();
		 printSearchResults(limit, query, reader);
	}
	
	@Test
	public void queryMutiplePhrases() throws IOException{
		PhraseQuery query = new PhraseQuery();
		query.add(new Term("fullname", "ken"));
		query.add(new Term("fullname", "cat"));//NOTE all terms must be in the same field but is position sensitive
	    int limit = 3;
		IndexReader reader = openIndexForeReading();
		printSearchResults(limit, query, reader);
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
  
  public static class DTSimpleSimilarity extends DefaultSimilarity {
	    @Override public float tf(float f){return 1.0f;}
	    @Override public float lengthNorm(FieldInvertState state) {
	        String field = state.getName();
	        int numTerms = state.getMaxTermFrequency();
	    	if(field.equals("state")|
	    			field.equals("city")|
	    			field.equals("address1")|
	    			field.equals("zip5")|
	    			field.equals("fullname")) 
	    		return super.lengthNorm(state);
	        else return (float) (0.1 * Math.log(numTerms));
	      }
	}
  
 
}