package de.uni.due.paluno.casestudy.service;

import java.util.LinkedList;
import java.util.List;

import de.uni.due.paluno.casestudy.model.Route;

public class LookupDemoService implements LookupService {

	@Override
	public List<Route> lookUpRoutes() {
		List<Route> routes = new LinkedList<Route>();

		routes.add(new Route("80152"));
		routes.add(new Route("80262"));
		routes.add(new Route("80263"));
		routes.add(new Route("80264"));

		return routes;
	}

}
