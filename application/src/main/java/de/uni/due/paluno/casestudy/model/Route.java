package de.uni.due.paluno.casestudy.model;

import java.util.LinkedList;
import java.util.List;

public class Route extends AbstractWorldObject {

	public List<WayPoint> points;

	public Route() {
		super();
		this.points = new LinkedList<WayPoint>();
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
}
