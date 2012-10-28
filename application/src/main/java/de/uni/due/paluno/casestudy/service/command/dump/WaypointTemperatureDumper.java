package de.uni.due.paluno.casestudy.service.command.dump;

import com.espertech.esper.client.EPRuntime;

import de.uni.due.paluno.casestudy.cep.events.WaypointTemperatureEvent;
import de.uni.due.paluno.casestudy.service.command.ComplexEventCommand;

import java.util.HashMap;
import java.util.Map;

public class WaypointTemperatureDumper extends ComplexEventCommand {

	@Override
	protected void executeLogic(EPRuntime epr, Map<String, Object> eventParams) {
		System.out.println(eventParams.get("target"));
		System.out.println(eventParams.get("data"));
	}

	@Override
	public String getEPL() {
		return "select target, data from Temperature";
	}

	@Override
	public Map<String, String> getEventTypes() {
		Map<String, String> eventTypes = new HashMap<String, String>();

		eventTypes.put("Temperature", WaypointTemperatureEvent.class.getName());

		return eventTypes;
	}

	@Override
	public String[] getColumns() {
		return new String[] { "target", "data" };
	}
}