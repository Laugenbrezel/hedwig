package de.ikano.hedwig.exception;

import javax.faces.context.FacesContext;

import org.jboss.logging.Logger;
import org.jboss.logging.Messages;

//@HandlesExceptions
public class AppExceptionHandler {
	
	//@Inject
	private FacesContext facesContext;
	
	//@Inject
	private Messages messages;
	
	void printExceptions(/*@Handles CaughtException<ViewExpiredException> evt,*/ Logger log) {

//		log.error("Something bad happened: " +
//		evt.getException().getMessage());
//		evt.markHandled();
//		try {
//			facesContext.getExternalContext().redirect(facesContext.getExternalContext().encodeActionURL(evt.getException().getViewId()));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
