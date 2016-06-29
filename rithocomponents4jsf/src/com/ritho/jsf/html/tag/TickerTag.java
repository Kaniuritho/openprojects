package com.ritho.jsf.html.tag;

import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;
import javax.faces.el.ValueBinding;

/**Is the component type class type = ticker: tag class set in tld*/
public class TickerTag extends UIComponentTag{
	
	private String style;
	
	private String styleClass;
	
	private String title;
	
	private String width;
	
	private String height;

	public String getStyle(){return style;}
	
	public void setStyle(String style){this.style=style;}
	
	public String getStyleClass(){return styleClass;}
	
	public void setStyleClass(String styleClass){this.styleClass=styleClass;}
	
	public String getTitle(){return title;} 
	
	public void setTitle(String title){this.title=title;}
	
	public String getHeight() {return height;}

	public void setHeight(String height) {this.height = height;}

	public String getWidth() {return width;}

	public void setWidth(String width) {this.width = width;}
	
	public void release() {
		// the super class method should be called
		super.release();
		
		//style attr
		style=null;
		styleClass=null;
		title=null;
		width=null;
		height=null;
	}
	
	protected void setProperties(UIComponent component) {
		// the super class method should be called
		super.setProperties(component);
		//style attr
		if(style!=null)
			component.getAttributes().put("style",style);
		
		if(styleClass!=null)
			component.getAttributes().put("styleClass",styleClass);
		
		if(title!=null){//if value is referenced, create value binding else pass value from jsp
			if(isValueReference(title)){
				ValueBinding vb = getFacesContext().
				getApplication().
				createValueBinding(title);
				//System.out.println("title props: vb="+vb.toString()+" ; title="+title);
				component.setValueBinding("title",vb);
			}else{
				//System.out.println("title props: vb=null ; title="+title);
				component.getAttributes().put("title",title);
			}
		}
		
		if(width!=null)
			component.getAttributes().put("width",width);
		
		if(height!=null)
			component.getAttributes().put("height",height);
			
	}
	
	public String getComponentType() {
		return "ticker";
	}
	
	public String getRendererType() {
		// null means the component renders itself
		return null;
	}
	
}
