package de.uni.due.paluno.casestudy.model;

import java.util.LinkedList;
import java.util.List;

public class Route extends AbstractWorldObject {

	public List<WayPoint> points;
	public RouteStatus status;

	public Route() {
		super();
		this.points = new LinkedList<WayPoint>();
		this.status = RouteStatus.ok;
	}

	public Route(String id) {
		super(id);
		this.points = new LinkedList<WayPoint>();
	}

	public void addWaypoint(WayPoint wayPoint) {
		this.points.add(wayPoint);
	}

	public List<WayPoint> getPoints() {
		return points;
	}

	public void setPoints(List<WayPoint> points) {
		this.points = points;
	}

	public RouteStatus getStatus() {
		return status;
	}

	public void setStatus(RouteStatus status) {
		this.status = status;
	}

	public WayPoint getStart() {
		return this.points.get(0);
	}

	public WayPoint getDestination() {
		return this.points.get(this.points.size() - 1);
	}
}