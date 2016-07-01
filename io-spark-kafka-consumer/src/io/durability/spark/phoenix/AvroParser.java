package io.durability.spark.phoenix;

public class AvroParser {
	
	 public MessageRow parse(String inStr)
	    {
		 MessageRow row = new MessageRow();
		 row.setMessage(inStr);
		 row.setId(System.currentTimeMillis()+"_"+Math.random());

	      return row;
	    }
}
