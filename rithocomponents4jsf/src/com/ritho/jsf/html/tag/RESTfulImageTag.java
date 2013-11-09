package com.ritho.jsf.html.tag;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;

public class RESTfulImageTag extends AutoImageTag {
	
	private String row = null;
	
	@Override
	public String getComponentType() {
		return "restImage";
	}
	
	@Override
	public String getRendererType() {
		// null means the component renders itself
		return null;
	}

	public void setRow(String row) {
		this.row = row;
	}

	public String getRow() {
		return row;
	}
	
	public void release() {
		// the super class method should be called
		super.release();
		
		//style attr
		row=null;

	}
	
	protected void setProperties(UIComponent component) {
		// the super class method should be called
		super.setProperties(component);

		if(row!=null){
			if(isValueReference(row)){
				ValueBinding vb = getFacesContext().getApplication().createValueBinding(row);
				component.setValueBinding("row",vb);
			}else{
				component.getAttributes().put("row",row);
			}
		}
					
	}
	


}
