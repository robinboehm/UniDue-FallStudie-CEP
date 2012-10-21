package de.unidue.fi.cockpit.cep.event.command;

import java.util.HashMap;
import java.util.Map;

import com.espertech.esper.client.EPRuntime;

import de.unidue.fi.cockpit.cep.event.complex.RouteAverageExceededEvent;


public class AverageDumpCommand extends ComplexEventCommand {

	@Override
	protected void executeLogic(EPRuntime epr, Map<String, Object> eventParams) {
		System.out.println("Exception - Route: " + eventParams.get("route")
				+ " - Temperature average exceeded: "
				+ eventParams.get("average"));
	}

	@Override
	public String getEPL() {
		return "select target as route, data as average from RouteAverageExceededEvent";
	}

	@Override
	public Map<String, String> getEventTypes() {
		Map<String, String> eventTypes = new HashMap<String, String>();

		eventTypes.put("RouteAverageExceededEvent",
				RouteAverageExceededEvent.class.getName());

		return eventTypes;
	}

	@Override
	public String[] getColumns() {
		return new String[] { "route", "average" };
	}
}
