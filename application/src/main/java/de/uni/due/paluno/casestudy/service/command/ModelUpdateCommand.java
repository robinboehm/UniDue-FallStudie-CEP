package de.uni.due.paluno.casestudy.service.command;

import java.util.HashMap;
import java.util.Map;

import com.espertech.esper.client.EPRuntime;

import de.uni.due.paluno.casestudy.Globals;
import de.uni.due.paluno.casestudy.cep.events.WaypointTemperatureEvent;

public class ModelUpdateCommand extends ComplexEventCommand {

	@Override
	protected void executeLogic(EPRuntime epr, Map<String, Object> eventParams) {
		this.getService().updateWaypoint(
				(String) eventParams.get(this.getColumns()[0]),
				(Double) eventParams.get(this.getColumns()[1]));
	}

	@Override
	public String getEPL() {
		String epl = "select target, data from " + Globals.E_TEMPERATURE_ENTITY;

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
		return new String[] { "target", "data" };
	}

	@Override
	protected String getInfoMessage(Map<String, Object> eventParams) {
		return "[Waypoint][" + ((String) eventParams.get(this.getColumns()[0]))
				+ "][Update][Model] Temperature: "
				+ eventParams.get(this.getColumns()[1]);
	}
}
