package de.ikano.hedwig.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.jboss.solder.logging.Logger;
import org.jboss.seam.international.status.Messages;
import org.jboss.seam.transaction.Transactional;
import org.jboss.solder.logging.Category;

import de.ikano.hedwig.model.PatrolReport;
import de.ikano.hedwig.model.Target;
import de.ikano.hedwig.model.TargetStatus;

@Transactional @Named
@RequestScoped 
public class TargetHome implements Serializable {

	private static final long serialVersionUID = 237693459514144285L;

	@Inject
	@Category("hedwig")
	private Logger log;

	@Inject
	Messages messages;

	@Inject
	private EntityManager em;

	private @Inject
	Conversation conversation;

	@Inject
	private Event<Target> targetEventSrc;
	@Inject
	private PatrolBean patrolBean;

	private Target newTarget = null;

	@Produces
	@Named
	public Target getNewTarget() {
		return newTarget;
	}

	public String save() throws Exception {
		log.info("Saving " + newTarget.getName());		
		if (newTarget.getId()==null) {
			em.persist(newTarget);
		} else {
			em.merge(newTarget);
		}
		messages.info("Successfully saved '{0}' with id {1}.",
				newTarget.getName(), newTarget.getId());
		targetEventSrc.fire(newTarget);
		//conversation.end();
		//initNewMember();
		return "success";
	}

	@PostConstruct
	public void initNewMember() {
		//conversation.begin();
		newTarget = new Target();
	}
	
	public void check() {
		PatrolReport report = patrolBean.checkSingleTarget(newTarget);
		if (TargetStatus.OK.equals(report.getStatus())) {
			messages.info("Target status is: {0}", report.getStatus());
		} else {
			messages.warn("Target status is: {0}", report.getStatus());
		}
	}

	public String find(Long id) {
		newTarget = em.find(Target.class, id);
		return "targethome";
	}
}
