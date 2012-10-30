package de.uni.due.paluno.casestudy.ui;

import java.util.ArrayList;
import java.util.List;

public class RouteDTO extends AbstractDTO {
	private String status;

	private String start;

	private String destination;

	private List<WayPointDTO> waypoints;

	public RouteDTO() {
		this.waypoints = new ArrayList<WayPointDTO>();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public List<WayPointDTO> getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(List<WayPointDTO> waypoints) {
		this.waypoints = waypoints;
	}
}
