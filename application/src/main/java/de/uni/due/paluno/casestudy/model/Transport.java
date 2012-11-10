package de.uni.due.paluno.casestudy.model;

public class Transport extends AbstractWorldObject {
	private Truck truck;

	private Route route;

	public Truck getTruck() {
		return truck;
	}

	public void setTruck(Truck truck) {
		this.truck = truck;
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}
}
