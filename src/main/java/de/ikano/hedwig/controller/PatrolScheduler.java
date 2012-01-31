package de.ikano.hedwig.controller;

import javax.ejb.Schedule;
import javax.inject.Inject;

@javax.ejb.Singleton
public class PatrolScheduler {

	@Inject
	private PatrolBean patrolBean;
	
	@Schedule(second="*/30", minute="*",hour="*", persistent=false)
	public void startPatrol() {
		patrolBean.startPatrolling();
	}
}
