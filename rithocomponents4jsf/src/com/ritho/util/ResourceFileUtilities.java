package com.ritho.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.Scanner;

public class ResourceFileUtilities {

	
	public static String readResourceAsString(String name) throws IOException{
		
		assert (name !=null ) : "'name' Argument should not be null. Expected a filename";
		
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		URL url = cl.getResource(name);
		
		assert url!=null : "'"+name+"' is an invalid path!";
		
		//FileInputStream fis = new FileInputStream(url.getPath()); 
		 Reader r = new InputStreamReader(url.openStream());
		 BufferedReader fis = new BufferedReader(r);
		 StringBuilder text = new StringBuilder();
		Scanner scanner = new Scanner(fis);
	    try {
	      while (scanner.hasNextLine()){
	        text.append(scanner.nextLine() );
	        text.append("\n");
	      }
	    }
	    finally{
	    	scanner.close();
	    	fis.close();
	    	r.close();
	    }
	    

		return text==null?null:text.toString();
	}
	
	public static byte[] readResourceAsBytes(String name) throws IOException{
		
		assert (name !=null ) : "'name' Argument should not be null. Expected a filename";
		
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		URL url = cl.getResource(name);
		
		assert url!=null : "'"+name+"' is an invalid path!";
		 
		InputStream istream = url.openStream();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		  byte[] buffer = new byte[1024]; // Experiment with this value
		  int bytesRead;

		  while ((bytesRead = istream.read(buffer)) != -1)
		  {
		    baos.write(buffer, 0, bytesRead);
		  }

		  return baos.toByteArray();
	    
	}
	
	public static void streamIO(String name , OutputStream out) throws IOException{
	    // Set content size
	    //File file = new File(resourceName);
	    //resp.setContentLength((int)file.length());
		assert (name !=null ) : "'name' Argument should not be null. Expected a filename";
		
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		URL url = cl.getResource(name);
		
		assert url!=null : "'"+name+"' is an invalid path!";
		 
		InputStream in = url.openStream();
		
	    // Copy the contents of the file to the output stream
	    byte[] buf = new byte[1024];
	    int count = 0;
	    while ((count = in.read(buf)) >= 0) {
	        out.write(buf, 0, count);
	    }
	    in.close();
	    out.close();
	}
	
	public static boolean copyResource(String name, String newName) throws IOException{
		
		assert name!=null : "'name' Argument should not be null. Expected a filename";
		assert newName != null : "'newName' Argument should not be null. Expected a filename";
		
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		URL url = cl.getResource(name);
				
		
		if(url==null ){
			//logger.error("resource not loaded! -> "+name);
			return false;
		}
		
		 Reader r = new InputStreamReader(url.openStream());
		 BufferedReader fis = new BufferedReader(r);
		 Scanner scanner = new Scanner(fis);
		 
		 //writer
		 OutputStream outputStream = new FileOutputStream(newName);
		 Writer writer = new OutputStreamWriter(outputStream);
		 
		 //hide any exception
	    try {
	      
	    	while (scanner.hasNextLine()){
	    	  writer.write(scanner.nextLine());
	    	  writer.write("\n");
	    	}
	      
	    } finally {
	    	writer.close();
	    	outputStream.close();
	    	
	    	scanner.close();
	    	fis.close();
	    	r.close();

	    }
	    
	    
		return true;
	}
	
}
