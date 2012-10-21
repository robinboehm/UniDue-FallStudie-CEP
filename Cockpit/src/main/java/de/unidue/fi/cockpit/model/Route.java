package de.unidue.fi.cockpit.model;

import java.util.ArrayList;
import java.util.List;

public class Route extends Entity {
	private int status;

	private String name;

	private List<Waypoint> waypoints;

	public Route() {
		super();
		this.waypoints = new ArrayList<Waypoint>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Waypoint> getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(List<Waypoint> waypoints) {
		this.waypoints = waypoints;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
