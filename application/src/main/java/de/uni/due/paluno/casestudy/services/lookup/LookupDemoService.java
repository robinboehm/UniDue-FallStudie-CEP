package de.uni.due.paluno.casestudy.services.lookup;

import java.util.LinkedList;
import java.util.List;

import de.uni.due.paluno.casestudy.model.Route;
import de.uni.due.paluno.casestudy.model.Transport;
import de.uni.due.paluno.casestudy.model.Truck;
import de.uni.due.paluno.casestudy.model.WayPoint;
import de.uni.due.paluno.casestudy.model.World;

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
		route.getPoints().add(wayPoint);
		wayPoint = new WayPoint("84537", 2);
		wayPoint.setX("51.2249478477161");
		wayPoint.setY("6.78131103515625");
		route.getPoints().add(wayPoint);
		routes.add(route);

		// Köln to Bonn
		route = new Route();
		route.setId("2");
		wayPoint = new WayPoint("84737", 1);
		wayPoint.setX("50.9342000130637");
		wayPoint.setY("6.96258544921875");
		route.getPoints().add(wayPoint);
		wayPoint = new WayPoint("84738", 2);
		wayPoint.setX("50.7225468336324");
		wayPoint.setY("7.1026611328125");
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

		Transport transport = new Transport();
		transport.setId("1");
		transport.setRoute(this.world.getRoutes().get(0));
		transport.setTruck(this.world.getTrucks().get(0));
		this.world.getTransports().add(transport);
	}

	@Override
	public World getWorld() {
		return this.world;
	}
}
