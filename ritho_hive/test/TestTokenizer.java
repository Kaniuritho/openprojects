import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.standard.UAX29URLEmailTokenizer;
import org.apache.lucene.analysis.standard.ClassicTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.AttributeFactory;
import org.apache.lucene.util.Version;
import org.junit.Test;


public class TestTokenizer {

	
	@Test public void testLuceneStandardTokenizer() throws Exception {
		  String[] gold={"I","can't","beleive","that","the","Carolina","Hurricanes","won","the","2005","2006","Stanley","Cup"};
		  
		  Tokenizer tokenizer=new StandardTokenizer( );
		  tokenizer.setReader(new StringReader("I can't beleive that the Carolina Hurricanes won the 2005-2006 Stanley Cup.") );
		  tokenizer.reset();
		  List<String> result=new ArrayList<String>();
		  while (tokenizer.incrementToken()) {
		    result.add(((CharTermAttribute)tokenizer.getAttribute(CharTermAttribute.class)).toString());
		    System.out.println(((CharTermAttribute)tokenizer.getAttribute(CharTermAttribute.class)).toString());
		  }
		  assertTrue("result Size: " + result.size() + " is not: "+ gold.length,result.size() == gold.length);
		  int i=0;
		  for (  String chunk : result) {
		    assertTrue(chunk + " is not equal to " + gold[i],chunk.equals(gold[i]) == true);
		    i++;
		  }
		}
	
	
	@Test public void testLuceneClassicTokenizer() throws Exception {
		  String[] gold={"I","can't","beleive","that","the","Carolina","Hurricanes","won","the","2005","2006","Stanley","Cup"};
		  
		  Tokenizer tokenizer=new ClassicTokenizer( );
		  tokenizer.setReader(new StringReader("I can't beleive that the Carolina Hurricanes won the 2005-2006 Stanley Cup.") );
		  tokenizer.reset();
		  List<String> result=new ArrayList<String>();
		  while (tokenizer.incrementToken()) {
		    result.add(((CharTermAttribute)tokenizer.getAttribute(CharTermAttribute.class)).toString());
		    System.out.println(((CharTermAttribute)tokenizer.getAttribute(CharTermAttribute.class)).toString());
		  }
		  assertTrue("result Size: " + result.size() + " is not: "+ gold.length,result.size() == gold.length);
		  int i=0;
		  for (  String chunk : result) {
		    assertTrue(chunk + " is not equal to " + gold[i],chunk.equals(gold[i]) == true);
		    i++;
		  }
		}
	
	@Test public void testLuceneUAX29URLEmailTokenizer() throws Exception {
		  String[] gold={"I","can't","beleive","that","the","Carolina","Hurricanes","won","the","2005","2006","Stanley","Cup"};
		  
		  Tokenizer tokenizer=new UAX29URLEmailTokenizer( );
		  tokenizer.setReader(new StringReader("I can't beleive that the Carolina Hurricanes won the 2005-2006 Stanley Cup.") );
		  tokenizer.reset();
		  List<String> result=new ArrayList<String>();
		  while (tokenizer.incrementToken()) {
		    result.add(((CharTermAttribute)tokenizer.getAttribute(CharTermAttribute.class)).toString());
		    System.out.println(((CharTermAttribute)tokenizer.getAttribute(CharTermAttribute.class)).toString());
		  }
		  assertTrue("result Size: " + result.size() + " is not: "+ gold.length,result.size() == gold.length);
		  int i=0;
		  for (  String chunk : result) {
		    assertTrue(chunk + " is not equal to " + gold[i],chunk.equals(gold[i]) == true);
		    i++;
		  }
		}
	
}
