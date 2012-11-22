package de.uni.due.paluno.casestudy.control;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.uni.due.paluno.casestudy.Globals;
import de.uni.due.paluno.casestudy.control.command.RouteTemperatureRetrieveCommand;
import de.uni.due.paluno.casestudy.control.command.event.NoWaypointMaxTemperatureExceededCommand;
import de.uni.due.paluno.casestudy.control.command.event.RouteAverageExceededCommand;
import de.uni.due.paluno.casestudy.control.command.event.RouteAverageNotExceededCommand;
import de.uni.due.paluno.casestudy.control.command.event.WaypointMaxTemperatureExceededCommand;
import de.uni.due.paluno.casestudy.control.command.update.RouteStatusUpdateCommand;
import de.uni.due.paluno.casestudy.control.command.update.WaypointTemperatureUpdate;
import de.uni.due.paluno.casestudy.model.Route;
import de.uni.due.paluno.casestudy.model.TemperatureStatus;
import de.uni.due.paluno.casestudy.model.Transport;
import de.uni.due.paluno.casestudy.model.WayPoint;
import de.uni.due.paluno.casestudy.model.World;
import de.uni.due.paluno.casestudy.services.cep.EsperCOSMAdapter;
import de.uni.due.paluno.casestudy.services.cep.events.ControlledWaypointTemperatureUpdate;
import de.uni.due.paluno.casestudy.services.cosm.COSM;
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
	private LookupService lookupService;
	private EsperCOSMAdapter eca;
	private UIUpdateController uiUpdateController;
	private COSM cosm;

	public CockpitDemoService() {
		// Init Services
		this.uiUpdateController = new UIUpdateController(this);
		this.lookupService = new LookupDemoService();

		// Init ECA
		this.initEsperCOSMAdapter();

		// Init COSM API
		this.cosm = new COSM();

		// Enrich data model wit current values form cosm
		this.initialLoad();
	}

	private void initialLoad() {
		// Get a distinct list of waypoints
		Iterator<Route> i = this.lookupService.getWorld().getRoutes()
				.iterator();

		while (i.hasNext()) {
			Route r = i.next();

			Iterator<WayPoint> j = r.getPoints().iterator();
			while (j.hasNext()) {
				WayPoint wp = j.next();

				Object[] objectData = new Object[2];
				objectData[0] = wp.getId();
				objectData[1] = this.cosm.getTemperatureForWayPoint(wp.getId());

				this.getECA()
						.getCEP()
						.getEPRuntime()
						.sendEvent(
								objectData,
								ControlledWaypointTemperatureUpdate.class
										.getSimpleName() + r.getId());
			}
		}
	}

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
			eca.addToConfig(new WaypointTemperatureUpdate(route));
		}

		eca.addToConfig(new RouteTemperatureRetrieveCommand());
	}

	/**
	 * Returns all existing routes
	 * 
	 * @return List of Routes
	 */
	private List<Route> getRoutes() {
		return this.lookupService.getWorld().getRoutes();
	}

	/**
	 * Look up a route by id
	 * 
	 * @param id
	 *            Id to look for
	 * @return Route
	 */
	private Route getRouteById(String id) {
		return this.lookupService.getRouteById(id);
	}

	@Override
	public List<Route> getRoutesForWaypoint(String waypoint) {
		return this.lookupService.getRoutesForWaypoint(waypoint);
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

		this.uiUpdateController.update();
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

				if (wp.getId().equals(id)) {
					wp.setTemperature(temperature);

					if (temperature > Globals.MAXIMUM_WAYPOINT_TEMPERATURE)
						wp.setStatus(TemperatureStatus.critical);
					else
						wp.setStatus(TemperatureStatus.ok);
				}
			}
		}
	}

	@Override
	public World getWorld() {
		return this.lookupService.getWorld();
	}

	public UIUpdateController getUIUpdateController() {
		return uiUpdateController;
	}

	@Override
	public Map<String, Double> getTemperaturesForRoute(Route route) {
		return this.cosm.getTemperaturesForRoute(route);
	}
}
