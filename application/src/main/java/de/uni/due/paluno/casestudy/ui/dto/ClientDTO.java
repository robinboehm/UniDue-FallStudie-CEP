package de.uni.due.paluno.casestudy.ui.dto;

import java.util.ArrayList;
import java.util.List;

public class ClientDTO extends AbstractDTO {
	private List<UserInterfaceComponentDTO> uiElements;

	public ClientDTO() {
		this.uiElements = new ArrayList<UserInterfaceComponentDTO>();
	}

	public List<UserInterfaceComponentDTO> getUiElements() {
		return uiElements;
	}

	public void setUiElements(List<UserInterfaceComponentDTO> uiElements) {
		this.uiElements = uiElements;
	}
}
