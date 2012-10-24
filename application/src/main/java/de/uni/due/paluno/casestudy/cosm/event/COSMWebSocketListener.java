package de.uni.due.paluno.casestudy.cosm.event;

import java.util.EventListener;

public interface COSMWebSocketListener extends EventListener {
    void handleWebSocketEvent(COSMWebSocketEvent e);
}
