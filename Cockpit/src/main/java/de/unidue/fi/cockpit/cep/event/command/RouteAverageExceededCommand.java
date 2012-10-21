package de.unidue.fi.cockpit.cep.event.command;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.espertech.esper.client.EPRuntime;

import de.unidue.fi.cockpit.cep.event.complex.RouteAverageExceededEvent;
import de.unidue.fi.cockpit.cep.event.simple.WaypointTemperatureEvent;
import de.unidue.fi.cockpit.model.Route;
import de.unidue.fi.cockpit.model.Waypoint;

public class RouteAverageExceededCommand extends RouteEventCommand {

	public RouteAverageExceededCommand(Route route) {
		super(route);
	}

	@Override
	protected void executeLogic(EPRuntime epr, Map<String, Object> eventParams) {
		RouteAverageExceededEvent raee = new RouteAverageExceededEvent();
		raee.setTarget(route.getUid());
		raee.setData((Double) eventParams.get("temperature"));

		epr.sendEvent(raee);
	}

	@Override
	public String getEPL() {
		String epl = "select avg(data) as temperature from Temperature where target in ("
				+ getWaypoints()
				+ ") having count(data) = 3 and avg(data) > 20";

		System.out.println("Registering listener on: " + epl);

		return epl;
	}

	private String getWaypoints() {
		String waypoints = "";

		Iterator<Waypoint> i = this.route.getWaypoints().iterator();
		while (i.hasNext()) {
			Waypoint wp = i.next();
			waypoints += "'" + wp.getUid() + "',";
		}

		if (!waypoints.equals(""))
			waypoints = waypoints.substring(0, waypoints.length() - 1);

		return waypoints;
	}

	@Override
	public Map<String, String> getEventTypes() {
		Map<String, String> eventTypes = new HashMap<String, String>();

		eventTypes.put("Temperature", WaypointTemperatureEvent.class.getName());

		return eventTypes;
	}

	@Override
	public String[] getColumns() {
		return new String[] { "temperature" };
	}
}
