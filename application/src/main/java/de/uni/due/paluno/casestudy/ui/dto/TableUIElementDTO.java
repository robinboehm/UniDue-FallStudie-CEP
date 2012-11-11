package de.uni.due.paluno.casestudy.ui.dto;

import java.util.LinkedList;
import java.util.List;

public class TableUIElementDTO extends UserInterfaceComponentDTO {
	private List<TransportDTO> transports;

	public TableUIElementDTO() {
		this.transports = new LinkedList<TransportDTO>();
	}

	public List<TransportDTO> getTransports() {
		return transports;
	}

	public void setTransports(List<TransportDTO> transports) {
		this.transports = transports;
	}
}
