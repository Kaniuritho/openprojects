package test;

import org.junit.Test;

import com.ritho.jsf.resource.RithoResourceLoaderPhaseListener;


public class TestEnum {

	@Test
	public  void testContentType() {
		
		String resourceName = "blahblah.css";

		String resourceType = RithoResourceLoaderPhaseListener.getContentType(resourceName);
		
		System.out.println(resourceType);
		
	}
	
	
	@Test
	public  void negativeTestContentType() {
		
		String resourceName = "blahblah.uwyerwi";
		String resourceType = null;
		String errorMessage = "";
		try{
			resourceType = RithoResourceLoaderPhaseListener.getContentType(resourceName);
		}catch(AssertionError e){
			//e.printStackTrace();
			errorMessage = e.getMessage();
		}
		
		assert resourceType==null : "should be null";
		assert errorMessage!=null : "error message expected";
		System.out.println(errorMessage);
		
	}

}
