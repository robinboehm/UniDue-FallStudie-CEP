package de.uni.due.paluno.casestudy.model;

public class WayPoint extends AbstractTemperaturedWorldObject {

	private String x;
	private String y;
	private int no;
	private String name;

	public WayPoint(String id, int no) {
		super(id);
		this.no = no;
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

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getStatus() {
		if (this.getTemperature() > 40)
			return "critical";
		else
			return "Ok";
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
