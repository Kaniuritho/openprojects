package com.ritho.jsf.html.tag;

import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;
import javax.faces.el.ValueBinding;

/**Is the component type class type = ticker: tag class set in tld*/
public class AutoImageTag extends UIComponentTag{
	
	private String src;
	
	private String styleClass;
	
	private String title;
	
	private String width;
	
	private String height;

	
	public final String getSrc() {
		return src;
	}

	public final void setSrc(String src) {
		this.src = src;
	}

	
	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public void release() {
		// the super class method should be called
		super.release();
		
		//style attr
		src=null;
		styleClass=null;
		title=null;
		width=null;
		height=null;
	}
	
	protected void setProperties(UIComponent component) {
		// the super class method should be called
		super.setProperties(component);
		//style attr
		if(title!=null)
			component.getAttributes().put("title",title);
		
		if(styleClass!=null)
			component.getAttributes().put("styleClass",styleClass);
		
		if(src!=null){
			if(isValueReference(src)){
				ValueBinding vb = getFacesContext().getApplication().createValueBinding(src);
				component.setValueBinding("src",vb);
			}else{
				component.getAttributes().put("src",src);
			}
		}
		
		if(width!=null)
			component.getAttributes().put("width",width);
		
		if(height!=null)
			component.getAttributes().put("height",height);
			
	}
	
	public String getComponentType() {
		return "autoImage";
	}
	
	public String getRendererType() {
		// null means the component renders itself
		return null;
	}
	
}
