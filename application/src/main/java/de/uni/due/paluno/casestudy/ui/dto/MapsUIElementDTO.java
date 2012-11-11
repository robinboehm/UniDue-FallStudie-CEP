package de.uni.due.paluno.casestudy.ui.dto;

import java.util.ArrayList;
import java.util.List;

public class MapsUIElementDTO extends UserInterfaceComponentDTO {
	List<RouteDTO> routes;

	public MapsUIElementDTO() {
		this.routes = new ArrayList<RouteDTO>();
	}

	public List<RouteDTO> getRoutes() {
		return routes;
	}

	public void setRoutes(List<RouteDTO> routes) {
		this.routes = routes;
	}
}
