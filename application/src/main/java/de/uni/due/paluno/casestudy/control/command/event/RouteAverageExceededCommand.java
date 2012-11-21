package de.uni.due.paluno.casestudy.control.command.event;

import java.util.Map;

import com.espertech.esper.client.EPRuntime;

import de.uni.due.paluno.casestudy.Globals;
import de.uni.due.paluno.casestudy.control.command.RouteEventCommand;
import de.uni.due.paluno.casestudy.model.Route;
import de.uni.due.paluno.casestudy.services.cep.events.ControlledWaypointTemperatureUpdate;
import de.uni.due.paluno.casestudy.services.cep.events.RouteEvent;

/**
 * Sets the Critical-Status of a route in case the average route temperature is
 * exceeded.
 * 
 * @author saids
 * 
 */
public class RouteAverageExceededCommand extends RouteEventCommand {

	public RouteAverageExceededCommand(Route route) {
		super(route);
	}

	@Override
	protected void execute(EPRuntime epr, Map<String, Object> eventParams) {
		RouteEvent raee = new RouteEvent();
		raee.setTarget(route.getId());
		raee.setData((Double) eventParams.get(this.getColumns()[0]));
		raee.setKey(Globals.E_EVENT_ROUTE_AVERAGE_EXCEEDED);

		this.sendRouteEvent(raee, epr);
	}

	@Override
	public String getEPL() {
		String epl = "select avg(data) as " + this.getColumns()[0] + " from "
				+ getEventName() + ".win:length_batch("
				+ this.getWaypointCount() + ") where target in ("
				+ this.getWaypoints() + ") having avg(data) > "
				+ Globals.MAXIMUM_AVERAGE;

		return epl;
	}

	@Override
	protected String getEntity() {
		return ControlledWaypointTemperatureUpdate.class.getSimpleName();
	}

	@Override
	public String[] getColumns() {
		return new String[] { "temperature" };
	}

	@Override
	protected String getRouteInfoMessage(Map<String, Object> eventParams) {
		return "Average temperature exceeded";
	}
}
