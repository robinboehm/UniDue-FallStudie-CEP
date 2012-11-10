package de.uni.due.paluno.casestudy.control.command.prohibition;

import java.util.HashMap;
import java.util.Map;

import com.espertech.esper.client.EPRuntime;

import de.uni.due.paluno.casestudy.Globals;
import de.uni.due.paluno.casestudy.control.command.RouteEventCommand;
import de.uni.due.paluno.casestudy.model.Route;
import de.uni.due.paluno.casestudy.services.cep.events.RouteEvent;
import de.uni.due.paluno.casestudy.services.cep.events.WaypointTemperatureEvent;

public class WaypointMaxTemperatureExceededCommand extends RouteEventCommand {

	public WaypointMaxTemperatureExceededCommand(Route route) {
		super(route);
	}

	@Override
	protected void executeLogic(EPRuntime epr, Map<String, Object> eventParams) {
		RouteEvent routeEvent = new RouteEvent();
		routeEvent.setTarget(route.getId());
		routeEvent.setKey(Globals.E_EVENT_WAYPOINT_MAX_EXCEEDED);

		epr.sendEvent(routeEvent);
	}

	@Override
	public String getEPL() {
		String epl = "select count(data) as no from "
				+ Globals.E_TEMPERATURE_ENTITY + " where target in ("
				+ getWaypoints() + ") having data > " + Globals.MAXIMUM_WAYPOINT_TEMPERATURE;

		return epl;
	}

	@Override
	public Map<String, String> getEventTypes() {
		Map<String, String> eventTypes = new HashMap<String, String>();

		eventTypes.put(Globals.E_TEMPERATURE_ENTITY,
				WaypointTemperatureEvent.class.getName());

		return eventTypes;
	}

	@Override
	public String[] getColumns() {
		return null;
	}

	@Override
	protected String getRouteInfoMessage(Map<String, Object> eventParams) {
		return "Waypoint maximum temperature exceeded";
	}
}
