package com.ritho.jsf.html.component;

/**Is the component class. Set in faces-config*/
import java.io.IOException;

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

public class JSFishEyeActivatorComponent extends UIOutput {

	/**
		Load contents of 

	 *  */

	public void encodeBegin(FacesContext context) throws IOException {

		ResponseWriter writer = context.getResponseWriter();

		writer.startElement("script", this);

		writer.writeAttribute("type", "text/javascript", null);

		String jsContent = "" +
		"function loadjscssfile(filename, filetype){" +
		"	 if (filetype==\"js\"){ //if filename is a external JavaScript file" +
		"	  	var fileref=document.createElement('script')" +
		"	  	fileref.setAttribute(\"type\",\"text/javascript\")" +
		"	  	fileref.setAttribute(\"src\", filename)" +
		"	 }" +
		"	 else if (filetype==\"css\"){ //if filename is an external CSS file" +
		"	  	var fileref=document.createElement(\"link\")" +
		"	  	fileref.setAttribute(\"rel\", \"stylesheet\")" +
		"	  	fileref.setAttribute(\"type\", \"text/css\")" +
		"	  	fileref.setAttribute(\"href\", filename)" +
		"	 }" +
		"	 if (typeof fileref!=\"undefined\")" +
		"	  	document.getElementsByTagName(\"head\")[0].appendChild(fileref)" +
		"}" +
		"" +
		"	loadjscssfile(\"js/pichasearchOOJS.js\", \"js\");" +
		"	loadjscssfile(\"js/pichawsAjax.js\", \"js\");" +
		"	loadjscssfile(\"js/pichaimageloader.js\",\"js\");" +
		"	loadjscssfile(\"css/pichamain.css\", \"css\");" +
		"	loadjscssfile(\"css/basic.css\", \"css\");" +
		"" +
		"function callIniter(){" +
		"		//setFishEyediv('imgDiv');//specify the container's div id" +
		"   	//setFishEyePaneldiv('searchForm:imgDivParent');//specify the object's div id" +
		"   	Div_FisheyePaneldiv = 'searchForm:imgDivParent';" +
		"   	Div_Fisheyediv_normal_height = 60;" +
		"   	Div_FisheyePaneldiv_max_width = 700;" +
		"   	Div_fisheyediv_normal_height = 60;	" +
		"   	Div_fisheyediv_max_height = 200;" +
		"   	Div_fisheyediv_min_height = 60;" +
		"		Div_fisheyePanel_column_count = 6;" +
		"		Div_fisheyePanel_padding = 60; " +
		"		var fisheyePanel = new FishEyePanel();  			" +
		"  		fisheyePanel.initSOM();" +
		"" +
		"  		//TODO - remove page block" +
		" }" +
		"" +
		"window.onload = callIniter;" +
		"		";

		writer.write("<!-- ");
		writer.write(jsContent);
		writer.write(" //-->");

	}

	public void encodeEnd(FacesContext context) throws IOException {

		ResponseWriter writer = context.getResponseWriter();		
		writer.endElement("script");

	}



}

