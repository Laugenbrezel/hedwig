package de.ikano.hedwig.data;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import de.ikano.hedwig.model.Target;
import java.io.Serializable;

@SessionScoped
public class TargetListProducer implements Serializable {
 
	private static final long serialVersionUID = 543893963291508100L;

	@Inject
	private EntityManager em;

	private List<Target> targets;

	// @Named provides access the return value via the EL variable name
	// "members" in the UI (e.g.,
	// Facelets or JSP view)
	@Produces
	@Named
	public List<Target> getTargets() {
		return targets;
	}

	public void onMemberListChanged(
			@Observes(notifyObserver = Reception.IF_EXISTS) final Target target) {
		retrieveAllTargetsOrderedByName();
	}

	@PostConstruct
	public void retrieveAllTargetsOrderedByName() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Target> criteria = cb.createQuery(Target.class);
		Root<Target> target = criteria.from(Target.class);
		criteria.select(target).orderBy(cb.asc(target.get("name")));
		targets = em.createQuery(criteria).getResultList();
	}
}
