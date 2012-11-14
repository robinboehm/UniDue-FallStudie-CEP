package de.uni.due.paluno.casestudy.services.cosm.model.cosm;

public class COSMDataStreamBody {
	private double max_value;
	private double min_value;
	private String at;
	private String[] tags;
	private String id;
	private double current_value;

	public double getMax_value() {
		return max_value;
	}

	public void setMax_value(double max_value) {
		this.max_value = max_value;
	}

	public double getMin_value() {
		return min_value;
	}

	public void setMin_value(double min_value) {
		this.min_value = min_value;
	}

	public String getAt() {
		return at;
	}

	public void setAt(String at) {
		this.at = at;
	}

	public String[] getTags() {
		return tags;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getCurrent_value() {
		return current_value;
	}

	public void setCurrent_value(double current_value) {
		this.current_value = current_value;
	}
}
