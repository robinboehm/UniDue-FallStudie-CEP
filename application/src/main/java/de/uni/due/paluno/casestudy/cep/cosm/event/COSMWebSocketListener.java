package de.uni.due.paluno.casestudy.cep.cosm.event;

import java.util.EventListener;

public interface COSMWebSocketListener extends EventListener {
    void handleWebSocketEvent(COSMWebSocketEvent e);
}
