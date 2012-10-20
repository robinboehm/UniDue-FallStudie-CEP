package fl.eventProcessing.event;

import fl.eventProcessing.event.complex.ComplexEventCategory;

public abstract class Event {
	private String target;
	private ComplexEventCategory type;
	private Double data;

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public ComplexEventCategory getType() {
		return type;
	}

	public void setType(ComplexEventCategory type) {
		this.type = type;
	}

	public Double getData() {
		return data;
	}

	public void setData(Double data) {
		this.data = data;
	}
}
