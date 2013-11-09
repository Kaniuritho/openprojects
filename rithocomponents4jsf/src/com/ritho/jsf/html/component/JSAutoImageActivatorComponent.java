package com.ritho.jsf.html.component;

/**Is the component class. Set in faces-config*/
import java.io.IOException;

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

public class JSAutoImageActivatorComponent extends UIOutput {
	
	/**
		Load contents of 

	 *  */
	
	public void encodeAll(FacesContext context)throws IOException{
		
		
		ResponseWriter writer = context.getResponseWriter();
		writeImageLoader(writer);
		writeLoadingOfResources(writer);
		
	}
	
	private void writeLoadingOfResources(ResponseWriter writer) throws IOException{
		//
		/*writer.startElement("link", this);
		writer.writeAttribute("type", "text/css", null);
		writer.writeAttribute("rel", "stylesheet", null);
		writer.writeAttribute("href", "./rithoresources/pichamain.css", null);
		writer.endElement("link");*/
		writer.append("<link type='text/css' rel='stylesheet' href='./rithoresource/pichamain.css'/>");
		
		//
		writer.startElement("script", this);
		writer.writeAttribute("type", "text/javascript", null);
		writer.writeAttribute("src", "./rithoresource/pichasearchOOJS.js", null);
		writer.endElement("script");
		
		//
		writer.startElement("script", this);
		writer.writeAttribute("type", "text/javascript", null);
		writer.writeAttribute("src", "./rithoresource/pichawsAjax.js", null);
		writer.endElement("script");
		

	}

	private void writeImageLoader(ResponseWriter writer)throws IOException{
		
		writer.startElement("script", this);
		writer.writeAttribute("type", "text/javascript", null);
		writer.writeAttribute("src", "./rithoresource/pichaimageloader.js", null);
		writer.endElement("script");
		
		/**
		writer.startElement("script", this);
		
		writer.writeAttribute("type", "text/javascript", null);
		
		String jsContent = ResourceFileUtilities.readResourceAsString("com/ritho/jsf/resource/text/javascript/pichaimageloader.js");
		
		writer.write("<!-- ");
		writer.write(jsContent);
		writer.write(" //-->");
		
		writer.endElement("script");
		**/
	}
	
/*	public void encodeBegin(FacesContext context) throws IOException {
		
		ResponseWriter writer = context.getResponseWriter();
		
		writer.startElement("script", this);
		
		writer.writeAttribute("type", "text/javascript", null);
		
		String jsContent = ResourceFileUtilities.readResourceAsString("com/ritho/jsf/resource/text/javascript/pichaimageloader.js");
		
		writer.write("<!-- ");
		writer.write(jsContent);
		writer.write(" //-->");
				
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
		
		ResponseWriter writer = context.getResponseWriter();		
		writer.endElement("script");
	
	}
	*/
	

}

