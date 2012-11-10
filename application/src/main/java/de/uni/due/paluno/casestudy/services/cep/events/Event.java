package de.uni.due.paluno.casestudy.services.cep.events;

import java.util.Date;

public abstract class Event {
	private String target;
	private Double data;
	private Date timestamp;

	public Event() {
		this.timestamp = new Date();
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public Double getData() {
		return data;
	}

	public void setData(Double data) {
		this.data = data;
	}

	public Date getTimestamp() {
		return timestamp;
	}
}
