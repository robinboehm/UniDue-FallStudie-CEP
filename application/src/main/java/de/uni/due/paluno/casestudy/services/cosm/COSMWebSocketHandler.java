package de.uni.due.paluno.casestudy.services.cosm;

import com.ning.http.client.websocket.WebSocket;
import com.ning.http.client.websocket.WebSocketTextListener;
import de.uni.due.paluno.casestudy.services.cosm.event.COSMWebSocketListener;

public interface COSMWebSocketHandler extends WebSocketTextListener {

    @Override
    void onOpen(WebSocket webSocket);

    @Override
    void onMessage(String message);

    void addListener(COSMWebSocketListener listener);

    @Override
    void onFragment(String s, boolean b);

    @Override
    void onClose(WebSocket webSocket);

    @Override
    void onError(Throwable throwable);
}
