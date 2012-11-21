package de.uni.due.paluno.casestudy.control.command.update;

import java.util.Map;

import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EventBean;

import de.uni.due.paluno.casestudy.Globals;
import de.uni.due.paluno.casestudy.control.command.RouteEventCommand;
import de.uni.due.paluno.casestudy.model.Route;
import de.uni.due.paluno.casestudy.services.cep.events.RouteEvent;

/**
 * Updates the data model with status changes that have been created by status
 * listener commands (see prohibition/release subpackage).
 * 
 * @author saids
 * 
 */
public class RouteStatusUpdateCommand extends RouteEventCommand {
	public RouteStatusUpdateCommand(Route route) {
		super(route);
	}

	@Override
	protected void execute(EPRuntime epr, EventBean[] newData) {
		String status = Globals.E_EVENT_ROUTE_OK;

		for (int i = 0; i < newData.length; i++) {
			Map<String, Object> props = this.toMap(newData[i]);

			String key = (String) props.get(this.getColumns()[0]);

			if (key.matches(Globals.E_EVENT_ROUTE_AVERAGE_EXCEEDED + "|"
					+ Globals.E_EVENT_WAYPOINT_MAX_EXCEEDED))
				status = key;
		}

		this.getService().updateRoute(this.route.getId(), status);
	}

	@Override
	protected void execute(EPRuntime epr, Map<String, Object> eventParams) {
		this.getService().updateRoute(this.route.getId(),
				(String) eventParams.get("key"));
	}

	@Override
	public String getEPL() {
		String epl = "select key from " + this.getEventName()
				+ ".win:length_batch(2)";

		return epl;
	}

	@Override
	public String[] getColumns() {
		return new String[] { "key" };
	}

	@Override
	protected String getRouteInfoMessage(Map<String, Object> eventParams) {
		return "[Route][" + this.route.getId() + "][Update][Status] "
				+ (String) eventParams.get("key");
	}

	@Override
	protected String getEntity() {
		return RouteEvent.class.getSimpleName();
	}
}
