package com.ritho.jsf.html.tag;

//import javax.faces.webapp.FacetTag;
import javax.faces.webapp.UIComponentTag;

public class JSAutoImageActivatorTag extends UIComponentTag{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8617998905327526135L;

	public String getComponentType() {
		return "autoImageActivator";
	}
	
	public String getRendererType() {
		// null means the component renders itself
		return null;
	}
	
}
