package de.uni.due.paluno.casestudy.ui.dto;

import java.util.LinkedList;
import java.util.List;

public class TransportDTO extends AbstractDTO {
	private List<String> attributes;

	public TransportDTO() {
		this.attributes = new LinkedList<String>();
	}

	public List<String> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}
}
