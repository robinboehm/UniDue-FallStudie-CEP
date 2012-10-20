package fl.eventProcessing.event.command;

import fl.model.Route;

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
