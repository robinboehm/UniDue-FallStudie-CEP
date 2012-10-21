package de.uni.due.paluno.casestudy.cep.cosm.event;

import de.uni.due.paluno.casestudy.cep.events.Event;

import java.util.EventObject;

public class COSMWebSocketEvent extends EventObject {

    private Event event;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
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

