package fl.eventProcessing.event.command;

import java.util.HashMap;
import java.util.Map;

import com.espertech.esper.client.EPRuntime;

import fl.eventProcessing.event.simple.WaypointTemperatureEvent;

public class WaypointTemperatureDumper extends ComplexEventCommand {

	@Override
	protected void executeLogic(EPRuntime epr, Map<String, Object> eventParams) {
		System.out.println(eventParams.get("target"));
	}

	@Override
	public String getEPL() {
		return "select target from Temperature";
	}

	@Override
	public Map<String, String> getEventTypes() {
		Map<String, String> eventTypes = new HashMap<String, String>();

		eventTypes.put("Temperature", WaypointTemperatureEvent.class.getName());

		return eventTypes;
	}

	@Override
	public String[] getColumns() {
		return new String[] { "target", "target" };
	}
}
