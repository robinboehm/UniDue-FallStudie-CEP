package de.uni.due.paluno.casestudy.model;

public class Transport extends AbstractWorldObject {
	private Truck truck;
	private Route route;
	private TemperatureStatus status;

	public Transport() {
		this.status = TemperatureStatus.ok;
	}

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

	public TemperatureStatus getStatus() {
		return status;
	}

	public void setStatus(TemperatureStatus status) {
		this.status = status;
	}
}
