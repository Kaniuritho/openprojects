package com.ritho.util.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtil {
	
	public static Class<?> getClass(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		
		Class<?> _clazz = Class.forName(className);

		return _clazz;
	}
	
	public static Object getNewInstance(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		
		Class<?> _clazz = Class.forName(className);
		Object reObject = _clazz.newInstance();

		return reObject;
	}
	
	
	public static Object invokeMethod(Class<?> _clazz, String methodName,Class<?>[] methodParamTypes, Object ...args) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Object ret = null;
		
		Method method = _clazz.getMethod(methodName, methodParamTypes); 

		ret = method.invoke(null,args); 
		
		return ret;
	}
}
