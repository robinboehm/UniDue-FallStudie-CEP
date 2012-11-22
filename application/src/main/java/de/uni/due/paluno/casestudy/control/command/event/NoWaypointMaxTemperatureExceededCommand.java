package de.uni.due.paluno.casestudy.control.command.event;

import java.util.Map;

import com.espertech.esper.client.EPRuntime;

import de.uni.due.paluno.casestudy.Globals;
import de.uni.due.paluno.casestudy.control.command.RouteEventCommand;
import de.uni.due.paluno.casestudy.model.Route;
import de.uni.due.paluno.casestudy.services.cep.events.ControlledWaypointTemperatureUpdate;
import de.uni.due.paluno.casestudy.services.cep.events.RouteEvent;

/**
 * Sets the OK-Status of a route in case no waypoint exceeds the general maximum
 * waypoint temperature.
 * 
 * @author saids
 * 
 */
public class NoWaypointMaxTemperatureExceededCommand extends RouteEventCommand {

	public NoWaypointMaxTemperatureExceededCommand(Route route) {
		super(route);
	}

	@Override
	protected void execute(EPRuntime epr, Map<String, Object> eventParams) {
		RouteEvent routeEvent = new RouteEvent();
		routeEvent.setTarget(route.getId());
		routeEvent.setKey(Globals.E_EVENT_WAYPOINT_MAX_NOT_EXCEEDED);

		this.sendRouteEvent(routeEvent, epr);
	}

	@Override
	public String getEPL() {
		String epl = "select count(data) as no from " + getEventName()
				+ ".win:length_batch(" + this.getWaypointCount()
				+ ") where target in (" + getWaypoints() + ") and data <= "
				+ Globals.MAXIMUM_WAYPOINT_TEMPERATURE
				+ " having count(data) = " + this.getWaypointCount();

		return epl;
	}

	@Override
	public String[] getColumns() {
		return new String[] { "no" };
	}

	@Override
	protected String getRouteInfoMessage(Map<String, Object> eventParams) {
		return "No Waypoint maximum temperature exceeded";
	}

	@Override
	protected String getEntity() {
		return ControlledWaypointTemperatureUpdate.class.getSimpleName();
	}
}
