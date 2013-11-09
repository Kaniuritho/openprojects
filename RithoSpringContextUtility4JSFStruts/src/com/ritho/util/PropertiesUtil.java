package com.ritho.util;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class PropertiesUtil {
	private static Map <String,ResourceBundle> propertiesInMap = new HashMap<String,ResourceBundle> ();
	
	public static ResourceBundle getResourceBundle(String propFileName){
	
		ResourceBundle props = propertiesInMap.get(propFileName);
		
		if(props==null){
			props = ResourceBundle.getBundle(propFileName);;

			propertiesInMap.put(propFileName, props);

		}

		return props;
	}
}
