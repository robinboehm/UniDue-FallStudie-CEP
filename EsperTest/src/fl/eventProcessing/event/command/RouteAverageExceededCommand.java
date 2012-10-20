package fl.eventProcessing.event.command;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EventBean;

import fl.eventProcessing.event.complex.RouteAverageExceededEvent;
import fl.eventProcessing.event.simple.WaypointTemperatureEvent;
import fl.model.Route;
import fl.model.Waypoint;

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
				+ getWaypoints() + ") having count(data) = 3 and avg(data) > 20";

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
	public Map<String, Object> toMap(EventBean eventBean) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();

		map.put("temperature", eventBean.get("temperature"));

		return map;
	}
}
