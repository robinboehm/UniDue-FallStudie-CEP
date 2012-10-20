package fl.eventProcessing.event.command;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EventBean;

import fl.eventProcessing.event.complex.RouteAverageExceededEvent;

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
	public Map<String, Object> toMap(EventBean eventBean) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();

		map.put("route", eventBean.get("route"));
		map.put("average", eventBean.get("average"));

		return map;
	}
}
