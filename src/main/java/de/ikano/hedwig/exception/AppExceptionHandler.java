package de.ikano.hedwig.exception;

import java.io.IOException;

import javax.faces.application.ViewExpiredException;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.jboss.seam.exception.control.CaughtException;
import org.jboss.seam.exception.control.Handles;
import org.jboss.seam.exception.control.HandlesExceptions;
import org.jboss.seam.solder.messages.Messages;

@HandlesExceptions
public class AppExceptionHandler {
	
	@Inject
	private FacesContext facesContext;
	
	@Inject
	private Messages messages;
	
	void printExceptions(@Handles CaughtException<ViewExpiredException> evt, Logger log) {

		log.error("Something bad happened: " +
		evt.getException().getMessage());
		evt.markHandled();
		try {
			facesContext.getExternalContext().redirect(facesContext.getExternalContext().encodeActionURL(evt.getException().getViewId()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
