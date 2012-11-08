package de.uni.due.paluno.casestudy.model;

public class WayPoint extends AbstractTemperaturedWorldObject {

	private String x;
	private String y;

	public WayPoint() {
		super();
	}

	public WayPoint(String id) {
		super(id);
	}

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}

	public String getStatus() {
		if (this.getTemperature() > 40)
			return "critical";
		else
			return "Ok";
	}
}
