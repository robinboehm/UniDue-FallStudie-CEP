package de.uni.due.paluno.casestudy.service.command;

import java.util.HashMap;
import java.util.Map;

import com.espertech.esper.client.EPRuntime;

import de.uni.due.paluno.casestudy.Globals;
import de.uni.due.paluno.casestudy.cep.events.RouteEvent;
import de.uni.due.paluno.casestudy.model.Route;

public class RouteStatusUpdateCommand extends RouteEventCommand {

	public RouteStatusUpdateCommand(Route route) {
		super(route);
	}

	@Override
	protected void executeLogic(EPRuntime epr, Map<String, Object> eventParams) {
		this.getService().updateRoute(this.route.getId(),
				(String) eventParams.get("key"));
	}

	@Override
	public String getEPL() {
		String epl = "select key from " + Globals.E_ROUTE_ENTITY
				+ " where target = '" + route.getId() + "'";

		return epl;
	}

	@Override
	public Map<String, String> getEventTypes() {
		Map<String, String> eventTypes = new HashMap<String, String>();

		eventTypes.put(Globals.E_ROUTE_ENTITY, RouteEvent.class.getName());

		return eventTypes;
	}

	@Override
	public String[] getColumns() {
		return new String[] { "key" };
	}

	@Override
	protected String getRouteInfoMessage(Map<String, Object> eventParams) {
		return (String) eventParams.get("key");
	}
}
