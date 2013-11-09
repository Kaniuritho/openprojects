package com.ritho.jsf.resource;

import java.util.Arrays;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.ritho.util.ResourceFileUtilities;

public class RithoResourceLoaderPhaseListener implements PhaseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2479923920587805135L;

	private static final String RESOURCE_LOADER_VIEW_ID = "/rithoresource/";

	private static final String RESOURCE_FOLDER = "com/ritho/jsf/resource";

	private static final Logger logger = Logger.getRootLogger();

	public void beforePhase(PhaseEvent event) {
		//No-op
	}

	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

	public void afterPhase(PhaseEvent event) {
		FacesContext facesContext = event.getFacesContext();        
		String viewRootId = facesContext.getViewRoot().getViewId();

		if (viewRootId.indexOf(RESOURCE_LOADER_VIEW_ID) != -1) {
			serveResource(facesContext, viewRootId);
		}

	}

	private void serveResource(FacesContext facesContext, String viewRootId) {

		String contentType  = getContentType(viewRootId);
		String resourceName = contentType+""+viewRootId.substring(viewRootId.lastIndexOf('/'));


		HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();


		String resourcePath = RESOURCE_FOLDER + "/" + resourceName;

		try{

			//byte[] resourceBytes =  ResourceFileUtilities.readResourceAsBytes(resourcePath);
			//response.setContentType(contentType);
			//response.getWriter().write(new String(resourceBytes));
			ResourceFileUtilities.streamIO(resourcePath , response.getOutputStream());
			response.setStatus(200);
			logger.debug("Loaded --> "+resourcePath);
		}catch(Exception exception){
			logger.warn("'"+viewRootId+"' not loaded from "+RESOURCE_LOADER_VIEW_ID+" because "+ exception.getLocalizedMessage(), exception);
			//exception.printStackTrace();
			response.setStatus(401);
		}finally{
			facesContext.responseComplete();        
		}

	}


	public static String getResourceName(FacesContext facesContext) {

		HttpServletRequest req = (HttpServletRequest)facesContext.getExternalContext().getRequest();

		return req.getPathInfo();
	}

	public  static String getContentType(String resourceName) {

		String resourceType = resourceName.substring(resourceName.lastIndexOf('.') + 1, resourceName.length());

		String contentType = null;
		ResourceContentTypeEnum type = null;
		try{
			type = ResourceContentTypeEnum.valueOf(resourceType);
		}catch(RuntimeException e){

		}

		assert type !=null 
		: "Resource type '"+resourceType+"' could not be matched with "+Arrays.toString(ResourceContentTypeEnum.values()); 

		type = type==null? ResourceContentTypeEnum.none :type;

		switch(type){
		case js: contentType = "text/javascript"; break;
		case css: contentType = "text/css"; break;
		case gif: contentType = "image/gif"; break;
		case jpg: contentType = "image/jpeg"; break;
		case jpeg: contentType = "image/jpeg"; break;
		default:
			break;
		}

		return contentType;
	}

	enum ResourceContentTypeEnum { js, css, gif, jpg, jpeg, none };

}

