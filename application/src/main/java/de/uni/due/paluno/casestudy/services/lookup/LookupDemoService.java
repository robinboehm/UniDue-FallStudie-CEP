package de.uni.due.paluno.casestudy.services.lookup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.uni.due.paluno.casestudy.model.Route;
import de.uni.due.paluno.casestudy.model.Transport;
import de.uni.due.paluno.casestudy.model.Truck;
import de.uni.due.paluno.casestudy.model.WayPoint;
import de.uni.due.paluno.casestudy.model.World;

/**
 * Demo class to generate a static data model
 * 
 * @author saids
 * 
 */
public class LookupDemoService implements LookupService {

	private World world;

	public LookupDemoService() {
		List<Route> routes = new LinkedList<Route>();

		// Düsseldorf to Essen
		Route route = new Route();
		route.setId("1");
		WayPoint wayPoint = new WayPoint("84536", 1);
		wayPoint.setX("51.4540069100598");
		wayPoint.setY("7.0037841796875");
		wayPoint.setName("WP_Essen");
		route.getPoints().add(wayPoint);
		wayPoint = new WayPoint("84537", 2);
		wayPoint.setX("51.2249478477161");
		wayPoint.setY("6.78131103515625");
		wayPoint.setName("WP_Düsseldorf");
		route.getPoints().add(wayPoint);
		routes.add(route);

		// Köln to Bonn
		route = new Route();
		route.setId("2");
		wayPoint = new WayPoint("84737", 1);
		wayPoint.setX("50.9342000130637");
		wayPoint.setY("6.96258544921875");
		wayPoint.setName("WP_Köln");
		route.getPoints().add(wayPoint);
		wayPoint = new WayPoint("84738", 2);
		wayPoint.setX("50.7225468336324");
		wayPoint.setY("7.1026611328125");
		wayPoint.setName("WP_Bonn");
		route.getPoints().add(wayPoint);
		routes.add(route);

		// Köln to Dortmund via Wuppertal
		route = new Route();
		route.setId("3");
		wayPoint = new WayPoint("84737", 1);
		wayPoint.setX("50.9342000130637");
		wayPoint.setY("6.96258544921875");
		wayPoint.setName("WP_Köln");
		route.getPoints().add(wayPoint);
		wayPoint = new WayPoint("86099", 2);
		wayPoint.setX("51.2378466891444");
		wayPoint.setY("7.16583251953125");
		wayPoint.setName("WP_Wuppertal");
		route.getPoints().add(wayPoint);
		wayPoint = new WayPoint("86100", 3);
		wayPoint.setX("51.513870548724");
		wayPoint.setY("7.46658325195312");
		wayPoint.setName("WP_Dortmund");
		route.getPoints().add(wayPoint);
		routes.add(route);

		this.world = new World();
		this.world.setRoutes(routes);

		Truck truck = new Truck();
		truck.setId("TR1");
		truck.setTemperature(20);
		this.world.trucks.add(truck);

		truck = new Truck();
		truck.setId("TR2");
		truck.setTemperature(15);
		this.world.trucks.add(truck);

		truck = new Truck();
		truck.setId("TR3");
		truck.setTemperature(35);
		this.world.trucks.add(truck);

		Transport transport = new Transport();
		transport.setId("1");
		transport.setRoute(this.world.getRoutes().get(0));
		transport.setTruck(this.world.getTrucks().get(0));
		this.world.getTransports().add(transport);

		transport = new Transport();
		transport.setId("2");
		transport.setRoute(this.world.getRoutes().get(1));
		transport.setTruck(this.world.getTrucks().get(1));
		this.world.getTransports().add(transport);

		transport = new Transport();
		transport.setId("3");
		transport.setRoute(this.world.getRoutes().get(2));
		transport.setTruck(this.world.getTrucks().get(2));
		this.world.getTransports().add(transport);
	}

	@Override
	public World getWorld() {
		return this.world;
	}

	@Override
	public Route getRouteById(String id) {
		Iterator<Route> i = this.getWorld().getRoutes().iterator();

		while (i.hasNext()) {
			Route current = i.next();
			if (current.getId().equals(id))
				return current;
		}

		return null;
	}

	@Override
	public List<Route> getRoutesForWaypoint(String waypoint) {
		List<Route> routes = new ArrayList<Route>();

		Iterator<Route> i = this.getWorld().getRoutes().iterator();
		while (i.hasNext()) {
			Route route = i.next();

			Iterator<WayPoint> j = route.getPoints().iterator();
			while (j.hasNext()) {
				WayPoint wp = j.next();

				if (wp.getId().equals(waypoint))
					routes.add(route);
			}
		}

		return routes;
	}
}
