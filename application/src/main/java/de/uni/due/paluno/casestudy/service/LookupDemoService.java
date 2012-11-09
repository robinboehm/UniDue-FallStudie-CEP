package de.uni.due.paluno.casestudy.service;

import java.util.LinkedList;
import java.util.List;

import de.uni.due.paluno.casestudy.model.Route;
import de.uni.due.paluno.casestudy.model.WayPoint;
import de.uni.due.paluno.casestudy.model.World;

public class LookupDemoService implements LookupService {

	@Override
	public World getWorld() {
		List<Route> routes = new LinkedList<Route>();

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

		World world = new World();
		world.setRoutes(routes);

		return world;
	}
}
