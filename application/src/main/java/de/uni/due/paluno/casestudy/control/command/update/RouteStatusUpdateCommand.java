package de.uni.due.paluno.casestudy.control.command.update;

import java.util.Iterator;
import java.util.Map;

import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EventBean;

import de.uni.due.paluno.casestudy.Globals;
import de.uni.due.paluno.casestudy.control.command.Command;
import de.uni.due.paluno.casestudy.model.Route;
import de.uni.due.paluno.casestudy.services.cep.events.RouteEvent;

/**
 * Updates the data model with status changes that have been created by status
 * listener commands (see prohibition/release subpackage).
 * 
 * @author saids
 * 
 */
public class RouteStatusUpdateCommand extends Command {
	private Route route;

	public RouteStatusUpdateCommand(Route route) {
		this.route = route;
	}

	@Override
	protected void execute(EPRuntime epr, EventBean[] newData) {
		String status = getNewStatus(newData);

		this.getService().updateRoute(this.route.getId(), status);
	}

	@Override
	protected void execute(EPRuntime epr, Map<String, Object> eventParams) {
		// DO NOTHING
	}

	@SuppressWarnings("unchecked")
	private String getNewStatus(Map<String, Object> eventParams) {
		String status = Globals.E_EVENT_ROUTE_OK;

		Iterator<String> i = eventParams.keySet().iterator();
		while (i.hasNext()) {
			String datarowKey = (String) i.next();

			Map<String, Object> props = (Map<String, Object>) eventParams
					.get(datarowKey);

			String key = (String) props.get(this.getColumns()[0]);

			if (key.matches(Globals.E_EVENT_ROUTE_AVERAGE_EXCEEDED + "|"
					+ Globals.E_EVENT_WAYPOINT_MAX_EXCEEDED + "|"
					+ Globals.E_EVENT_ROUTE_AVERAGE_WARNING))
				status = key;
		}

		return status;
	}

	private String getNewStatus(EventBean[] newData) {
		return this.getNewStatus(toMap(newData));
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

	protected String getEntity() {
		return RouteEvent.class.getSimpleName();
	}

	@Override
	public String getEventName() {
		return this.getEntity() + this.route.getId();
	}

	@Override
	protected String getInfoMessage(Map<String, Object> eventParams) {
		return "[Route][" + this.route.getId() + "][Update][Status] "
				+ getNewStatus(eventParams);
	}
}
