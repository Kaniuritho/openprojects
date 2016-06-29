package com.ritho.jsf.html.component;

/**Is the component class. Set in faces-config*/
import java.io.IOException;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

public class RESTfulImageComponent extends UIOutput {
	
	/**
	 *  	<div id="searchForm:listData:0:imgDiv1" class="img">
							<script type="text/javascript">
							var loader = new ImageLoader("http://l.yimg.com/k/im_siggVSDqZH82Vx0T8RBZzlsjBQ---x75-xc1-yc1-wc666-hc666-q75-n0/omg/us/img/c7/0d/607_3086439100.jpg", 'resultImg');
							loader.load();	
							</script>
							<div class="controls">
								<div id="searchForm:listData:0:sourceCssBtn" class="controlBtn"><a id="searchForm:listData:0:_idJsp22" name="searchForm:listData:0:_idJsp22" href="http://l.yimg.com/k/im_siggVSDqZH82Vx0T8RBZzlsjBQ---x75-xc1-yc1-wc666-hc666-q75-n0/omg/us/img/c7/0d/607_3086439100.jpg" target="_blank">View Source</a></div>
								<div id="searchForm:listData:0:searchCssBtn" class="controlBtn"><a id="searchForm:listData:0:_idJsp24" name="searchForm:listData:0:_idJsp24" href="" onclick="javascript:doCachedSearch('http://l.yimg.com/k/im_siggVSDqZH82Vx0T8RBZzlsjBQ---x75-xc1-yc1-wc666-hc666-q75-n0/omg/us/img/c7/0d/607_3086439100.jpg')">Search Matches</a></div>
								<div id="searchForm:listData:0:searchCssBtn2" class="controlBtn"><a id="searchForm:listData:0:cssBtnLink" name="searchForm:listData:0:cssBtnLink" href="ajaxSearch.jsf?urlPath=http%3A%2F%2Fl.yimg.com%2Fk%2Fim_siggVSDqZH82Vx0T8RBZzlsjBQ---x75-xc1-yc1-wc666-hc666-q75-n0%2Fomg%2Fus%2Fimg%2Fc7%2F0d%2F607_3086439100.jpg">Ajax Test</a></div>
							</div>
							<div class="data">
								<div id="searchForm:listData:0:webDiv" class="web"></div><div id="searchForm:listData:0:scoreDiv" class="scores">c 97 , e 97 , m  0 :  T 97.0</div>
								<div id="searchForm:listData:0:matchDiv" class="match">97 % Match</div>
							</div>
			</div>

	 *  */
	
	public void encodeBegin(FacesContext context) throws IOException {
		
		ResponseWriter writer = context.getResponseWriter();

		writer.startElement("div", this);
		
		String id = "restImgDiv"+Math.random()+"_"+(String)this.getAttributes().get("id");
		
		writer.writeAttribute("id", id, null);
			
		String width = (String)this.getAttributes().get("width");	    
	    //if (width  != null)
	    //	 writer.writeAttribute("width", width, null);
		
		String height = (String)this.getAttributes().get("height");	    
	    //if (height != null)
	    //	 writer.writeAttribute("height", height, null);	    
	    
		String styleClass = (String)this.getAttributes().get("styleClass");
		//if(styleClass!=null)
		//	writer.writeAttribute("class", styleClass, null);
		
		String title = (String)this.getAttributes().get("title");
		//if(title!=null)
		//	writer.writeAttribute("title",title,null);
		
		String row = (String)this.getAttributes().get("row");
		writer.write("<script type=\"text/javascript\"><!--  \n ");
		writer.write(" var loader = new ImageLoader( '"+id+"' , null , '"+styleClass+"', '"+height+"', '"+width+"', '"+title+"' ); ");
		writer.write(" getRowImageUrl('"+row+"', eval(loader.getRESTcallbackmethod())  );");
		writer.write(" //--></script>");
						
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
		
		ResponseWriter writer = context.getResponseWriter();		
		writer.endElement("div");
	
	}

}

