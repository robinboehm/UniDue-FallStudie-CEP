package de.uni.due.paluno.casestudy.services.cosm;

import de.uni.due.paluno.casestudy.services.cosm.event.COSMWebSocketListener;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface COSMWebSocketEngine {
    void start() throws IOException, ExecutionException,
            InterruptedException;

    void stop();

    void addListener(COSMWebSocketListener listener);
}
