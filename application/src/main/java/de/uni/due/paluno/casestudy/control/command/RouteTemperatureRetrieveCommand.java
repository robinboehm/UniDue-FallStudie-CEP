package de.uni.due.paluno.casestudy.control.command;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EventBean;

import de.uni.due.paluno.casestudy.model.Route;
import de.uni.due.paluno.casestudy.services.cep.events.ControlledWaypointTemperatureUpdate;
import de.uni.due.paluno.casestudy.services.cep.events.UncontrolledWaypointTemperatureUpdate;

public class RouteTemperatureRetrieveCommand extends Command {

	@Override
	protected void execute(EPRuntime epr, EventBean[] events) {
		// Get concerned routes
		Set<Route> affectedRoutes = new HashSet<Route>();

		for (int i = 0; i < events.length; i++) {
			Map<String, Object> items = this.toMap(events[i]);

			List<Route> routes = this.getService().getRoutesForWaypoint(
					(String) items.get(this.getColumns()[0]));

			Iterator<Route> j = routes.iterator();
			while (j.hasNext()) {
				Route route = (Route) j.next();

				affectedRoutes.add(route);
			}
		}

		// Retrieve temperature update for routes
		Iterator<Route> i = affectedRoutes.iterator();
		while (i.hasNext()) {
			Route route = (Route) i.next();

			Map<String, Double> temperatures = this.getService()
					.getTemperaturesForRoute(route);
			Iterator<String> j = temperatures.keySet().iterator();
			while (j.hasNext()) {
				String waypointId = (String) j.next();

				Object[] objectData = new Object[2];
				objectData[0] = waypointId;
				objectData[1] = temperatures.get(waypointId);

				this.getService()
						.getECA()
						.getCEP()
						.getEPRuntime()
						.sendEvent(
								objectData,
								ControlledWaypointTemperatureUpdate.class
										.getSimpleName() + route.getId());
			}
		}
	}

	@Override
	public String getEPL() {
		return "select target from " + this.getEventName();
	}

	@Override
	public String[] getColumns() {
		return new String[] { "target" };
	}

	@Override
	protected void execute(EPRuntime epr, Map<String, Object> eventParams) {
		// Do nothing here, not relevant
	}

	@Override
	public String getEventName() {
		return UncontrolledWaypointTemperatureUpdate.class.getSimpleName();
	}
}
