package de.unidue.fi.cockpit.cep.event.command;

import de.unidue.fi.cockpit.model.Route;

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
