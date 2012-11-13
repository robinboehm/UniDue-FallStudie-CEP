package de.uni.due.paluno.casestudy.control;

import java.util.Iterator;
import java.util.List;

import de.uni.due.paluno.casestudy.Globals;
import de.uni.due.paluno.casestudy.control.command.WaypointTemperatureUpdate;
import de.uni.due.paluno.casestudy.control.command.RouteStatusUpdateCommand;
import de.uni.due.paluno.casestudy.control.command.prohibition.RouteAverageExceededCommand;
import de.uni.due.paluno.casestudy.control.command.prohibition.WaypointMaxTemperatureExceededCommand;
import de.uni.due.paluno.casestudy.control.command.release.NoWaypointMaxTemperatureExceededCommand;
import de.uni.due.paluno.casestudy.control.command.release.RouteAverageNotExceededCommand;
import de.uni.due.paluno.casestudy.model.Route;
import de.uni.due.paluno.casestudy.model.TemperatureStatus;
import de.uni.due.paluno.casestudy.model.Transport;
import de.uni.due.paluno.casestudy.model.WayPoint;
import de.uni.due.paluno.casestudy.model.World;
import de.uni.due.paluno.casestudy.services.cep.EsperCOSMAdapter;
import de.uni.due.paluno.casestudy.services.lookup.LookupDemoService;
import de.uni.due.paluno.casestudy.services.lookup.LookupService;
import de.uni.due.paluno.casestudy.servlet.UIUpdateController;

/**
 * Demo implementation of the CockpitService
 * 
 * @author saids
 * 
 */
public class CockpitDemoService implements CockpitService {
	public CockpitDemoService(UIUpdateController uiUpdateController) {
		this.uiUpdateController = uiUpdateController;
		this.lookupService = new LookupDemoService();

		this.initEsperCOSMAdapter();
	}

	private LookupService lookupService;
	private EsperCOSMAdapter eca;
	private UIUpdateController uiUpdateController;

	/**
	 * Init ECA based on the underlying data model
	 */
	private void initEsperCOSMAdapter() {
		if (this.eca == null) {
			this.eca = new EsperCOSMAdapter(this);

			// Generate ECA Trigger configuration
			this.createRouteTriggers(eca);

			// Init configuration
			eca.createTriggers();
		}
	}

	/**
	 * Creates event triggers for all routes contained in the underlying data
	 * model
	 * 
	 * @param eca
	 *            ECA that is configured
	 */
	private void createRouteTriggers(EsperCOSMAdapter eca) {
		List<Route> routes = this.getRoutes();

		Iterator<Route> i = routes.iterator();
		while (i.hasNext()) {
			Route route = i.next();

			eca.addToConfig(new RouteAverageExceededCommand(route));
			eca.addToConfig(new WaypointMaxTemperatureExceededCommand(route));
			eca.addToConfig(new RouteStatusUpdateCommand(route));
			eca.addToConfig(new RouteAverageNotExceededCommand(route));
			eca.addToConfig(new NoWaypointMaxTemperatureExceededCommand(route));
		}

		eca.addToConfig(new WaypointTemperatureUpdate());
	}

	/**
	 * Returns all existing routes
	 * 
	 * @return List of Routes
	 */
	private List<Route> getRoutes() {
		return this.lookupService.getWorld().getRoutes();
	}

	@Override
	public void updateRoute(String id, String key) {
		Route route = this.getRouteById(id);

		if (route != null) {
			if (key.matches(Globals.E_EVENT_ROUTE_AVERAGE_EXCEEDED + "|"
					+ Globals.E_EVENT_WAYPOINT_MAX_EXCEEDED)) {
				route.setStatus(TemperatureStatus.critical);
				updateTransportsOnRoute(route.getId(),
						TemperatureStatus.critical);
			} else {
				route.setStatus(TemperatureStatus.ok);
				updateTransportsOnRoute(route.getId(), TemperatureStatus.ok);
			}
		}

		this.uiUpdateController.update(this.getWorld());
	}

	/**
	 * Updates all transports that are affected by the status change of a route
	 * 
	 * @param id
	 *            Id of the route whose status has been changed
	 * @param status
	 *            Status to set in the transports
	 */
	private void updateTransportsOnRoute(String id, TemperatureStatus status) {
		Iterator<Transport> i = this.getWorld().getTransports().iterator();
		while (i.hasNext()) {
			Transport transport = i.next();

			if (transport.getRoute().getId().equals(id)) {
				transport.setStatus(status);
			}
		}
	}

	/**
	 * Look up a route by id
	 * 
	 * @param id
	 *            Id to look for
	 * @return Route
	 */
	private Route getRouteById(String id) {
		Iterator<Route> i = this.lookupService.getWorld().getRoutes()
				.iterator();

		while (i.hasNext()) {
			Route current = i.next();
			if (current.getId().equals(id))
				return current;
		}

		return null;
	}

	@Override
	public EsperCOSMAdapter getECA() {
		return this.eca;
	}

	@Override
	public void updateWaypoint(String id, Double temperature) {
		Iterator<Route> i = this.getWorld().getRoutes().iterator();
		while (i.hasNext()) {
			Route route = i.next();

			Iterator<WayPoint> j = route.getPoints().iterator();
			while (j.hasNext()) {
				WayPoint wp = j.next();

				if (wp.getId().equals(id))
					wp.setTemperature(temperature);
			}
		}

	}

	@Override
	public World getWorld() {
		return this.lookupService.getWorld();
	}
}
