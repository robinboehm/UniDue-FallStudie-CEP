package de.uni.due.paluno.casestudy.ui;

import java.util.ArrayList;
import java.util.List;

public class MapsUIDTO extends UIDTO {
	List<RouteDTO> routes;

	public MapsUIDTO() {
		this.routes = new ArrayList<RouteDTO>();
	}

	public List<RouteDTO> getRoutes() {
		return routes;
	}

	public void setRoutes(List<RouteDTO> routes) {
		this.routes = routes;
	}
}
