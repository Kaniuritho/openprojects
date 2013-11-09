package com.ritho.spring.web;

public class ContextLoaderException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 145362476389733730L;

	public ContextLoaderException(){
		super();
	}
	
	public ContextLoaderException(Exception e){
		super(e);
	}
	
	public ContextLoaderException(String e){
		super(e);
	}
}
