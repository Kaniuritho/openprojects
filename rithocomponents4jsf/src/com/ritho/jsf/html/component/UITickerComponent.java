package com.ritho.jsf.html.component;

/**Is the component class. Set in faces-config*/
import java.io.IOException;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

public class UITickerComponent extends UIOutput {
	
	public void encodeBegin(FacesContext context) throws IOException {
		
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("div", this);
		
		
		writer.writeAttribute("id",this.getClientId(context),null);
		
		
		
		String width = (String)this.getAttributes().get("width");
		
		String height = (String)this.getAttributes().get("height");
		
		String style = (String)this.getAttributes().get("style");

		style= (style!=null) ? style + ";" : "";
	    
	    if (width  != null) style += "width:" + width + ";";
	    
	    if (height != null) style += "height:" + height+ ";";

	    writer.writeAttribute("style", style, null);

	    
	    
		String styleClass = (String)this.getAttributes().get("styleClass");
		if(styleClass!=null)
			writer.writeAttribute("class",styleClass,null);
		
		
		
		String title = (String)this.getAttributes().get("title");
		if(title!=null)
			writer.writeAttribute("title",title,null);
		
		
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
		
		ResponseWriter writer = context.getResponseWriter();		
		writer.endElement("div");
	
	}
}

