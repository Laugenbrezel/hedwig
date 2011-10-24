package de.ikano.hedwig.controller;

import javax.ejb.Schedule;
import javax.inject.Inject;

@javax.ejb.Singleton
public class PatrolScheduler {

	@Inject
	private PatrolBean patrolBean;
	
	@Schedule(second = "*/20", minute = "*", hour = "*")
	public void startPatrol() {
		patrolBean.startPatrolling();
	}
}
