package de.uni.due.paluno.casestudy.service.command.release;

import java.util.HashMap;
import java.util.Map;

import com.espertech.esper.client.EPRuntime;

import de.uni.due.paluno.casestudy.Globals;
import de.uni.due.paluno.casestudy.cep.events.RouteEvent;
import de.uni.due.paluno.casestudy.cep.events.WaypointTemperatureEvent;
import de.uni.due.paluno.casestudy.model.Route;
import de.uni.due.paluno.casestudy.service.command.RouteEventCommand;

public class RouteAverageNotExceededCommand extends RouteEventCommand {

	public RouteAverageNotExceededCommand(Route route) {
		super(route);
	}

	@Override
	protected void executeLogic(EPRuntime epr, Map<String, Object> eventParams) {
		RouteEvent raee = new RouteEvent();
		raee.setTarget(route.getId());
		raee.setData((Double) eventParams.get(this.getColumns()[0]));
		raee.setKey(Globals.E_EVENT_ROUTE_AVERAGE_NOT_EXCEEDED);

		epr.sendEvent(raee);
	}

	@Override
	public String getEPL() {
		String epl = "select avg(data) as " + this.getColumns()[0] + " from "
				+ Globals.E_TEMPERATURE_ENTITY + " where target in ("
				+ getWaypoints() + ") having count(data) = "
				+ route.getPoints().size() + " and avg(data) <= "
				+ Globals.MAXIMUM_AVERAGE;

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
		return new String[] { "temperature" };
	}

	@Override
	protected String getRouteInfoMessage(Map<String, Object> eventParams) {
		return "Average temperature not exceeded";
	}
}
