package de.uni.due.paluno.casestudy.cosm.event;

import de.uni.due.paluno.casestudy.cep.events.Event;

import java.util.EventObject;

public class COSMWebSocketEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5176514277565402559L;
	private Event event;

	/**
	 * Constructs a prototypical Event.
	 * 
	 * @param source
	 *            The object on which the Event initially occurred.
	 * @throws IllegalArgumentException
	 *             if source is null.
	 */
	public COSMWebSocketEvent(Object source) {
		super(source);
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

}
