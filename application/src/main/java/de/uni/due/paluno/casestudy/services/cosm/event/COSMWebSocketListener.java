package de.uni.due.paluno.casestudy.services.cosm.event;

import java.util.EventListener;

public interface COSMWebSocketListener extends EventListener {
    void handleWebSocketEvent(COSMWebSocketEvent e);
}
