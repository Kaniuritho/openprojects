package com.ritho.jsf.html.tag;

//import javax.faces.webapp.FacetTag;
import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class JSRESTfulImageActivatorTag extends UIComponentTag{
	
	/**
	 * 
	 */
	private String restUrl = null;
	
	public String getComponentType() {
		return "restImageActivator";
	}
	
	public String getRendererType() {
		// null means the component renders itself
		return null;
	}

	public void setRestUrl(String restUrl) {
		this.restUrl = restUrl;
	}

	public String getRestUrl() {
		return restUrl;
	}
	
	public void release() {
		// the super class method should be called
		super.release();
		
		//style attr
		restUrl=null;
	}
	
	protected void setProperties(UIComponent component) {
		// the super class method should be called
		super.setProperties(component);

		
		if(restUrl!=null){
			if(isValueReference(restUrl)){
				ValueBinding vb = getFacesContext().getApplication().createValueBinding(restUrl);
				component.setValueBinding("restUrl",vb);
			}else{
				component.getAttributes().put("restUrl",restUrl);
			}
		}
		
				
	}
	

	
}
