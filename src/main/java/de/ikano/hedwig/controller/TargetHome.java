package de.ikano.hedwig.controller;

import static javax.persistence.PersistenceContextType.EXTENDED;

import java.io.Serializable;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.faces.context.conversation.Begin;
import org.jboss.seam.faces.context.conversation.End;
import org.jboss.seam.international.status.Messages;
import org.jboss.seam.international.status.builder.TemplateMessage;
import org.jboss.solder.logging.Logger;
import org.jboss.solder.logging.TypedCategory;

import de.ikano.hedwig.model.PatrolReport;
import de.ikano.hedwig.model.Target;
import de.ikano.hedwig.model.TargetStatus;

@Stateful
@ConversationScoped
@Named
public class TargetHome implements Serializable {

	private static final long serialVersionUID = 237693459514144285L;

	@Inject
	@TypedCategory(TargetHome.class)
	private Logger log;

	@Inject
	private Instance<TemplateMessage> messageBuilder;

	@Inject
	private Messages messages;

	@Inject
	private Locale locale;

	@Inject
	@PersistenceContext(type = EXTENDED)
	private EntityManager em;

	@Inject
	private Conversation conversation;

	@Inject @Saved
	private Event<Target> targetEventSrc;
	
	@Inject
	private PatrolBean patrolBean;

	private Target target = null;

	private boolean instanceValid;
	

	@Begin
	public void select(final Long id) {
		conversation.setTimeout(600000); // 10 * 60 * 1000 (10 minutes)

		// NOTE get a fresh reference that's managed by the extended persistence
		// context
		target = em.find(Target.class, id);
		if (target != null) {
			log.info("Loaded: " + target.getName());
		}
	}

	public void validate() {
		log.warn("Target exisits: " + em.contains(target));
		// if we got here, all validations passed
		instanceValid = true;
	}

	@End
	public String save() throws Exception {
		log.info("Saving " + target.getName());
		if (!em.contains(target)) {
			em.persist(target);
		} else {
			em.merge(target);
		}
		messages.info("Successfully saved '{0}' with id {1}.",
				target.getName(), target.getId());
		targetEventSrc.fire(target);
		return "success";
	}
	
	@End
    public void cancel() {
        target = null;
    }
	
	public void onSaveComplete(@Observes(during = TransactionPhase.AFTER_SUCCESS) @Saved final Target target) {
        log.info("Successfully saved Target");
        messages.info("Successfully saved Target");
    }

	@PostConstruct
	public void initNewTarget() {
		target = new Target();
	}
	
	@Produces
    @ConversationScoped
    @Named
    public Target getTarget() {
        return target;
    }

	public void check() {
		PatrolReport report = patrolBean.checkSingleTarget(target);
		if (TargetStatus.OK.equals(report.getStatus())) {
			messages.info("Target status is: {0}", report.getStatus());
		} else {
			messages.warn("Target status is: {0}", report.getStatus());
		}
	}

	public boolean isInstanceValid() {
		return instanceValid;
	}
}
