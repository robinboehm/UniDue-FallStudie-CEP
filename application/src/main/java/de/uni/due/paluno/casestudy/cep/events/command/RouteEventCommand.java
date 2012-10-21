package de.uni.due.paluno.casestudy.cep.events.command;

import de.uni.due.paluno.casestudy.cep.esper.model.Route;

public abstract class RouteEventCommand extends ComplexEventCommand {

	protected Route route;

	public RouteEventCommand(Route route) {
		this.route = route;
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}
}
