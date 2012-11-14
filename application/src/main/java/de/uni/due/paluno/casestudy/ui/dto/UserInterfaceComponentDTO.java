package de.uni.due.paluno.casestudy.ui.dto;

/**
 * Represents an UI Element.
 * 
 * @author saids
 * 
 */
public abstract class UserInterfaceComponentDTO extends AbstractDTO {
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
