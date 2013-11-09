package com.ritho.jsf.html.component;

/**Is the component class. Set in faces-config*/
import java.io.IOException;
import java.net.URL;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class JSRESTfulImageActivatorComponent extends JSAutoImageActivatorComponent {
	private static final Logger logger = Logger.getLogger(JSRESTfulImageActivatorComponent.class);
	/**
		Load contents of 

	 *  */
	@Override
	public void encodeAll(FacesContext context)throws IOException{
		super.encodeAll(context);
		
		ResponseWriter writer = context.getResponseWriter();
		writeHBaseRestCaller(writer);		

	}
	
	
	private void writeHBaseRestCaller(ResponseWriter writer)throws IOException{
		//String jsContent = ResourceFileUtilities.readResourceAsString("com/ritho/jsf/resource/text/javascript/hbasecaller.js");
		
		writer.startElement("script", this);
		writer.writeAttribute("type", "text/javascript", null);
		writer.writeAttribute("src", "./rithoresource/hbasecaller.js", null);
		writer.endElement("script");
		
		writer.startElement("script", this);
		writer.writeAttribute("type", "text/javascript", null);
		writer.writeAttribute("src", "./rithoresource/jCrypt.js", null);
		writer.endElement("script");	
		
		String restUrl = (String)this.getAttributes().get("restUrl");
		if(restUrl==null)throw new IOException("JSRESTfulImageActivatorComponent requires restUrl: scripts not initialized");
		else{
			if(!restUrl.startsWith("http:")){
				HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
				String hostinfo = req.getScheme()+"://"+req.getServerName()+":"+req.getServerPort();

				restUrl = hostinfo+"/"+restUrl;
			}
			logger.info(restUrl+"  connected with permissions: "+new URL(restUrl).openConnection().getPermission().getName());
		}
		//initialize hbase jscript element
		writer.startElement("script", this);	
		writer.writeAttribute("type", "text/javascript", null);		
		writer.write("<!-- ");
		writer.write(" \n ");
		writer.write(" var jsHBase =  new HBaseCaller('"+restUrl+"');");
		writer.write(" \n ");
		writer.write(" function getRowImage(row){ ");
		writer.write("  	jsHBase.searchByRowJSON(row); ");
		writer.write("  };");
		writer.write(" function getRowImageUrl(row, callback){ ");
		writer.write("  	 return jsHBase.searchByRowJSON(row, callback); ");
		writer.write("  };");
		writer.write(" //-->");	
		writer.endElement("script");
	}
	
	

}

