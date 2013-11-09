package com.ritho.spring.web;

import java.lang.reflect.InvocationTargetException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import com.ritho.util.PropertiesUtil;
import com.ritho.util.reflection.ReflectionUtil;

/**
 * 
 * This class should be used to construct application context for springframework supported framewroks i.e JSF, Struts etc. Application
 * context is dependent on web framework, be it Struts2 , JSF, etc.
 * 
 * @author Kaniu Ndungu
 * 
 * */
public class ApplicationContextLoaderFactory {

	private static Logger logger = Logger.getLogger(ApplicationContextLoaderFactory.class.getName());

	public enum APPLICATION_FRAMEWORK {
		JSF, STRUTS2
	};

	private static ResourceBundle contextLoaderProps = PropertiesUtil.getResourceBundle("contextLoader");
	private static ApplicationContextLoaderFactory singleton = null;

	private ApplicationContextLoaderFactory() {

	}

	public static ApplicationContextLoaderFactory getSingleton() {
		if (singleton == null) {
			singleton = new ApplicationContextLoaderFactory();
		}
		return singleton;
	}

	public static ApplicationContext getContext(
			APPLICATION_FRAMEWORK application) throws ContextLoaderException {

		ApplicationContext applicationContext = null;

		switch (application) {
		case JSF:	
			applicationContext = loadJSFContext();
			break;
		case STRUTS2:	
			applicationContext = loadStruts2AppContext();
			break;
		}

		return applicationContext;
	}



	public static Object getBean(APPLICATION_FRAMEWORK application,
			String beanName) throws BeansException, ContextLoaderException {
		Object bean = null;

		switch (application) {
		case JSF:
			bean = getContext(application).getBean(beanName);
			break;
		case STRUTS2:
			bean = getContext(application).getBean(beanName);

			break;
		}
		return bean;
	}

	/**
	 * Expects to make a call like this:
	 * <code>applicationContext = FacesContextUtils.getWebApplicationContext(FacesContext.getCurrentInstance());</code>
	 * */
	private static ApplicationContext loadJSFContext() throws ContextLoaderException {

		String springjsfUtil = contextLoaderProps.getString("contextLoader.springjsf");
		String corejsfContext = contextLoaderProps.getString("contextLoader.corejsf");
		Object jsfApplicationContext = null;

		try {


			Class<?> coreJSFContextClass = ReflectionUtil.getClass(corejsfContext);
			Object coreJSFContextCurrentInstance =  ReflectionUtil.invokeMethod(coreJSFContextClass, "getCurrentInstance", null);

			assert coreJSFContextCurrentInstance!=null;
			
			Class<?> facesContextUtilClass = ReflectionUtil.getClass(springjsfUtil);
			jsfApplicationContext =  ReflectionUtil.invokeMethod(facesContextUtilClass, "getWebApplicationContext",
					new Class[]{coreJSFContextClass}, new Object[]{coreJSFContextCurrentInstance});

			assert jsfApplicationContext != null;
			
			if( !(jsfApplicationContext instanceof ApplicationContext))
				throw new ContextLoaderException(jsfApplicationContext+" is not an instance of  "+ApplicationContext.class.getCanonicalName());

		} catch (ClassNotFoundException e) {
			logger.error(e);
			throw new ContextLoaderException(e);
		} catch (InstantiationException e) {
			logger.error(e);
			throw new ContextLoaderException(e);
		} catch (IllegalAccessException e) {
			logger.error(e);
			throw new ContextLoaderException(e);
		}
		catch (SecurityException e) {
			logger.error(e);
			throw new ContextLoaderException(e);
		} catch (IllegalArgumentException e) {
			logger.error(e);
			throw new ContextLoaderException(e);
		} catch (NoSuchMethodException e) {
			logger.error(e);
			throw new ContextLoaderException(e);
		} catch (InvocationTargetException e) {
			logger.error(e);
			throw new ContextLoaderException(e);
		}


		return (ApplicationContext) jsfApplicationContext;
	}

	private static ApplicationContext loadStruts2AppContext() throws ContextLoaderException {

		String struts2Context = contextLoaderProps.getString("contextLoader.struts2");

		Object retObject = null;

		try {
			retObject = ReflectionUtil.getNewInstance(struts2Context);

			if(!(retObject instanceof ApplicationContext))
				throw new ContextLoaderException(struts2Context+" is not an instance of  "+ApplicationContext.class.getCanonicalName());

		} catch (ClassNotFoundException e) {
			logger.error(e);
			throw new ContextLoaderException(e);

		} catch (InstantiationException e) {

			logger.error(e);
			throw new ContextLoaderException(e);
		} catch (IllegalAccessException e) {

			logger.error(e);
			throw new ContextLoaderException(e);
		}


		return (ApplicationContext) retObject;
	}

}
