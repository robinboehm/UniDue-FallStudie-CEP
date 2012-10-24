package de.uni.due.paluno.casestudy.cep.events.command;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.espertech.esper.client.EPRuntime;

import de.uni.due.paluno.casestudy.cep.events.complex.RouteAverageExceededEvent;
import de.uni.due.paluno.casestudy.cep.events.simple.WaypointTemperatureEvent;
import de.uni.due.paluno.casestudy.model.Route;
import de.uni.due.paluno.casestudy.model.WayPoint;

public class RouteAverageExceededCommand extends RouteEventCommand {

	public RouteAverageExceededCommand(Route route) {
		super(route);
	}

	@Override
	protected void executeLogic(EPRuntime epr, Map<String, Object> eventParams) {
		RouteAverageExceededEvent raee = new RouteAverageExceededEvent();
		raee.setTarget(route.getId());
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
