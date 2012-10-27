package de.uni.due.paluno.casestudy.service.command.dump;

import com.espertech.esper.client.EPRuntime;

import de.uni.due.paluno.casestudy.cep.events.RouteEvent;
import de.uni.due.paluno.casestudy.service.command.ComplexEventCommand;

import java.util.HashMap;
import java.util.Map;

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
				RouteEvent.class.getName());

		return eventTypes;
	}

	@Override
	public String[] getColumns() {
		return new String[] { "route", "average" };
	}
}
