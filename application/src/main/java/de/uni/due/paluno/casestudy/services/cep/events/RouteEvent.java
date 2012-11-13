package de.uni.due.paluno.casestudy.services.cep.events;

/**
 * An event that occurs to an route.
 * 
 * @author saids
 * 
 */
public class RouteEvent extends Event {
	/**
	 * Key that represents the logical (non technical) event.
	 */
	public String key;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
