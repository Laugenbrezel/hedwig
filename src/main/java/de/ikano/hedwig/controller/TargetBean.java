package de.ikano.hedwig.controller;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.jboss.seam.international.status.Level;
import org.jboss.seam.international.status.Message;
import org.jboss.seam.international.status.Messages;
import org.jboss.seam.international.status.builder.TemplateMessage;
import org.jboss.solder.logging.Logger;
import org.jboss.solder.logging.TypedCategory;

import de.ikano.hedwig.model.Target;

@Named
@Stateful
@ConversationScoped
public class TargetBean implements Serializable {

	private static final long serialVersionUID = 237693459514144285L;

	@Inject
	@TypedCategory(TargetBean.class)
	private Logger log;

	@Inject
	private Instance<TemplateMessage> messageBuilder;

	@Inject
	private Messages messages;

	@Inject
	@Saved
	private Event<Target> targetEvent;

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Target target;

	public Target getTarget() {
		return this.target;
	}

	@Inject
	private Conversation conversation;

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	public String create() {

		this.conversation.begin();
		return "/target/create?faces-redirect=true";
	}

	public void retrieve() {

		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}

		if (this.conversation.isTransient()) {
			this.conversation.begin();
		}

		if (this.id == null) {
			this.target = this.search;
		} else {
			this.target = this.entityManager.find(Target.class, getId());
			log.info("Loaded Target: " + this.target);
		}
	}

	public String update() {
		log.info("Updating: " + this.target);
		this.conversation.end();

		if (this.id == null) {
			this.entityManager.persist(this.target);
			targetEvent.fire(target);
			return "/target/search?faces-redirect=true";
		} else {
			this.entityManager.merge(this.target);
			targetEvent.fire(target);
			return "/target/search?faces-redirect=true&id="
					+ this.target.getId();
		}

	}

	public String delete() {
		this.conversation.end();
		this.entityManager.remove(this.entityManager
				.find(Target.class, getId()));
		return "/target/search?faces-redirect=true";
	}

	public String cancel() {
		this.conversation.end();
		return "/target/search?faces-redirect=true";
	}

	/*
	 * Support searching Customer entities with pagination
	 */

	private int page;
	private long count;
	private List<Target> pageItems;

	private Target search = new Target();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public Target getSearch() {
		return this.search;
	}

	public void setSearch(Target search) {
		this.search = search;
	}

	public void search() {
		this.page = 0;
	}

	public void paginate() {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

		// Populate this.count

		CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
		Root<Target> root = countCriteria.from(Target.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<Target> criteria = builder.createQuery(Target.class);
		root = criteria.from(Target.class);
		TypedQuery<Target> query = this.entityManager.createQuery(criteria
				.select(root).where(getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<Target> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String name = this.search.getName();
		if (name != null && !"".equals(name)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("name")),
					'%' + name.toLowerCase() + '%'));
		}
		// String lastName = this.search.getLastName();
		// if (lastName != null && !"".equals(lastName)) {
		// predicatesList.add(builder.like(root.<String>get("lastName"), '%' +
		// lastName + '%'));
		// }

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<Target> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back Customer entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<Target> getAll() {

		CriteriaQuery<Target> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(Target.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(Target.class))).getResultList();
	}

	public Converter getConverter() {

		return new Converter() {

			@Override
			public Object getAsObject(FacesContext context,
					UIComponent component, String value) {

				return TargetBean.this.entityManager.find(Target.class,
						Long.valueOf(value));
			}

			@Override
			public String getAsString(FacesContext context,
					UIComponent component, Object value) {

				if (value == null) {
					return "";
				}

				return String.valueOf(((Target) value).getId());
			}
		};
	}

	public void checkHost(ValueChangeEvent e) {
	    String host = (String) e.getNewValue();
	    Message msg = null;
		if (host != null) {
			try {
				if (isHostname(host)) {
					// get IP
					String hostIP = InetAddress
							.getByName(host)
							.getHostAddress();
					log.info("IP: "+hostIP);
					msg = messageBuilder
							.get()
							.level(Level.INFO)
							.text("IP-Address: {0}")
							.textParams(hostIP)
							.targets(e.getComponent().getClientId())
							.build();
				} else {
					// get Hostname
					String hostName = InetAddress.getByAddress(
							convertIp(host))
							.getHostName();
					log.info("Hostname: "+hostName);
					msg = messageBuilder
							.get()
							.level(Level.INFO)
							.text("Hostname: {0}")
							.textParams(hostName)
							.targets(e.getComponent().getClientId())
							.build();
				}
			} catch (UnknownHostException ex) {
				msg = messageBuilder.get().level(Level.WARN)						
						.text("Error resolving Host")
						.targets(e.getComponent().getClientId())
						.build();
			}
			messages.add(msg);
		} else {
			log.info("Target / host is null");
		}
	}

	private byte[] convertIp(String host) {
		byte[] ip = new byte[4];
		String[] str = StringUtils.split(host, ".");
		for (int i = 0; i < str.length; i++) {
			ip[i] = Byte.valueOf(str[i]);
		}
		return ip;
	}

	private static boolean isHostname(String s) {
		char[] ca = s.toCharArray();
		for (int i = 0; i < ca.length; i++) {
			if (!Character.isDigit(ca[i])) {
				if (ca[i] != '.') {
					return true;
				}
			}
		}
		return false;

	}
}
