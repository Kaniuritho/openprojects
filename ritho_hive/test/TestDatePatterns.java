import java.util.regex.Pattern;

import org.junit.Test;

import com.ritho.hadoop.hive.udf.CompareYearInDateString;


public class TestDatePatterns {
	
	@Test
	public void testGetPattern(){
		
		Pattern p = CompareYearInDateString.getDatePattern("2004-01-01");
		System.out.println(p.toString());
	}
	
	@Test
	public void testExtractYearStr(){
		String dateStr = "20040101";
		Pattern p = CompareYearInDateString.getDatePattern(dateStr);
		String year = CompareYearInDateString.getYearFromDate(p, dateStr);
		System.out.println(year);
		
		
		
		 dateStr = "2004-01-01";
		 p = CompareYearInDateString.getDatePattern(dateStr);
		 year = CompareYearInDateString.getYearFromDate(p, dateStr);
		System.out.println(year);
		
		
		
		 dateStr = "2004/01/01";
		 p = CompareYearInDateString.getDatePattern(dateStr);
		 year = CompareYearInDateString.getYearFromDate(p, dateStr);
		System.out.println(year);
		
		 dateStr = "2004";
		 p = CompareYearInDateString.getDatePattern(dateStr);
		 year = CompareYearInDateString.getYearFromDate(p, dateStr);
		System.out.println(year);
		
	}


}
