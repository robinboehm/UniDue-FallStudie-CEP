package de.uni.due.paluno.casestudy.control.command;

import java.util.Iterator;
import java.util.Map;

import de.uni.due.paluno.casestudy.model.Route;
import de.uni.due.paluno.casestudy.model.WayPoint;

/**
 * Super class to all commands that deal with status changes of a Route due to
 * status changes of waypoints.
 * 
 * @author saids
 * 
 */
public abstract class RouteEventCommand extends ComplexEventCommand {

	/**
	 * Route that is observed
	 */
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

	/**
	 * Returns the route's waypoints as a condition that can be used in an EPL
	 * statement.
	 * 
	 * @return
	 */
	protected String getWaypoints() {
		String waypoints = "";

		Iterator<WayPoint> i = this.route.getPoints().iterator();
		while (i.hasNext()) {
			WayPoint wp = i.next();
			waypoints += "'" + wp.getId() + "',";
		}

		if (!waypoints.equals(""))
			waypoints = waypoints.substring(0, waypoints.length() - 1);

		return waypoints;
	}

	@Override
	protected String getInfoMessage(Map<String, Object> eventParams) {
		return "[Route][" + this.getRoute().getId() + "][Update][Status] "
				+ this.getRouteInfoMessage(eventParams);
	}

	protected abstract String getRouteInfoMessage(
			Map<String, Object> eventParams);

	/**
	 * 
	 * @return
	 */
	protected String getWaypointCount() {
		return Integer.toString(this.route.getPoints().size());
	}
}
