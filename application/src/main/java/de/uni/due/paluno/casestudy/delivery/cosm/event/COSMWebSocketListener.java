package de.uni.due.paluno.casestudy.delivery.cosm.event;

import java.util.EventListener;

public interface COSMWebSocketListener extends EventListener {
    void handleWebSocketEvent(COSMWebSocketEvent e);
}
