package de.uni.due.paluno.casestudy.service;

import java.util.Iterator;
import java.util.List;

import de.uni.due.paluno.casestudy.Globals;
import de.uni.due.paluno.casestudy.cep.EsperCOSMAdapter;
import de.uni.due.paluno.casestudy.model.Route;
import de.uni.due.paluno.casestudy.model.RouteStatus;
import de.uni.due.paluno.casestudy.model.World;
import de.uni.due.paluno.casestudy.service.command.RouteAverageExceededCommand;
import de.uni.due.paluno.casestudy.service.command.RouteProhibitedCommand;
import de.uni.due.paluno.casestudy.service.command.WaypointMaxTemperatureExceededCommand;

public class CockpitDemoService implements CockpitService {
	public CockpitDemoService() {
		this.lookupService = new LookupDemoService();
	}

	private LookupService lookupService;
	private World world;
	private EsperCOSMAdapter eca;

	private void initEsperCOSMAdapter() {
		if (this.eca == null) {
			this.eca = new EsperCOSMAdapter(this);

			this.createRouteTriggers(eca);

			eca.createTriggers();
		}
	}

	private void createRouteTriggers(EsperCOSMAdapter eca) {
		List<Route> routes = this.getRoutes();

		Iterator<Route> i = routes.iterator();
		while (i.hasNext()) {
			Route route = i.next();

			eca.addToConfig(new RouteAverageExceededCommand(route));
			eca.addToConfig(new WaypointMaxTemperatureExceededCommand(route));
			eca.addToConfig(new RouteProhibitedCommand(route));
		}
	}

	private List<Route> getRoutes() {
		return this.world.getRoutes();
	}

	@Override
	public void updateRoute(String id, String key) {
		Route route = this.getRouteById(id);

		if ((route != null)
				&& key.matches(Globals.E_EVENT_ROUTE_AVERAGE_EXCEEDED + "|"
						+ Globals.E_EVENT_WAYPOINT_MAX_EXCEEDED)) {
			route.setStatus(RouteStatus.PROHIBITED);
		}
	}

	private Route getRouteById(String id) {
		Iterator<Route> i = this.world.getRoutes().iterator();

		while (i.hasNext()) {
			Route current = i.next();
			if (current.getId().equals(id))
				return current;
		}

		return null;
	}

	@Override
	public List<Route> lookUpRoutes() {
		return this.lookupService.lookUpRoutes();
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;

		this.initEsperCOSMAdapter();
	}
}
