/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.ikano.hedwig.controller;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;
import org.jboss.seam.international.status.Messages;

import de.ikano.hedwig.model.Target;

/**
 *
 * @author lutz
 */
@Named("patrolHome")
@SessionScoped
public class PatrolHome implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Inject
    Messages messages;
	
    private Target selectedTarget = null;
	
    @Inject Logger log;
    
    public Target getSelectedTarget() {
		return selectedTarget;
	}
    
    public void selectTarget(Target selectedTarget) {
    	this.selectedTarget = selectedTarget;
    }
}
