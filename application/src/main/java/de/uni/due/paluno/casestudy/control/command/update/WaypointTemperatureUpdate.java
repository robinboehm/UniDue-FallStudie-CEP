package de.uni.due.paluno.casestudy.control.command.update;

import java.util.Map;

import com.espertech.esper.client.EPRuntime;

import de.uni.due.paluno.casestudy.control.command.RouteEventCommand;
import de.uni.due.paluno.casestudy.model.Route;
import de.uni.due.paluno.casestudy.services.cep.events.ControlledWaypointTemperatureUpdate;

/**
 * Updates the data model with the recently received temperature values
 * waypoints. No derived logic implemented. Plain delegation of values.
 * 
 * @author saids
 * 
 */
public class WaypointTemperatureUpdate extends RouteEventCommand {
	public WaypointTemperatureUpdate(Route route) {
		super(route);
	}

	@Override
	public String getEPL() {
		String epl = "select target, data from " + getEventName();

		return epl;
	}

	@Override
	public String[] getColumns() {
		return new String[] { "target", "data" };
	}

	@Override
	protected String getRouteInfoMessage(Map<String, Object> eventParams) {
		return "[Waypoint][" + ((String) eventParams.get(this.getColumns()[0]))
				+ "][Update][Model] Temperature: "
				+ eventParams.get(this.getColumns()[1]);
	}

	@Override
	protected String getEntity() {
		return ControlledWaypointTemperatureUpdate.class.getSimpleName();
	}

	@Override
	protected void execute(EPRuntime epr, Map<String, Object> eventParams) {
		this.getService().updateWaypoint(
				(String) eventParams.get(this.getColumns()[0]),
				(Double) eventParams.get(this.getColumns()[1]));
	}
}
