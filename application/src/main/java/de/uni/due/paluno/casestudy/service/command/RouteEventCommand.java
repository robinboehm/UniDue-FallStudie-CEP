package de.uni.due.paluno.casestudy.service.command;

import java.util.Iterator;

import de.uni.due.paluno.casestudy.model.Route;
import de.uni.due.paluno.casestudy.model.WayPoint;

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

	protected String getWaypoints() {
		String waypoints = "";

		Iterator<WayPoint> i = this.route.getPoints().iterator();
		while (i.hasNext()) {
			WayPoint wp = i.next();
			waypoints += "'" + route.getId() + ";" + wp.getId() + "',";
		}

		if (!waypoints.equals(""))
			waypoints = waypoints.substring(0, waypoints.length() - 1);

		return waypoints;
	}
}
