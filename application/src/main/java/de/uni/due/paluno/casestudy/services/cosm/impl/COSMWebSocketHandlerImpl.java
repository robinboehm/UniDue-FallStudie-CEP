package de.uni.due.paluno.casestudy.services.cosm.impl;

import com.ning.http.client.websocket.WebSocket;
import de.uni.due.paluno.casestudy.Globals;
import de.uni.due.paluno.casestudy.services.cosm.COSMWebSocketHandler;
import de.uni.due.paluno.casestudy.services.cosm.COSMWebSocketHelper;
import de.uni.due.paluno.casestudy.services.cosm.event.COSMWebSocketEvent;
import de.uni.due.paluno.casestudy.services.cosm.event.COSMWebSocketListener;
import de.uni.due.paluno.casestudy.services.cosm.model.cosm.COSMServerRequest;
import de.uni.due.paluno.casestudy.services.cosm.model.cosm.COSMServerResponse;

import javax.swing.event.EventListenerList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class COSMWebSocketHandlerImpl implements COSMWebSocketHandler {

    private final COSMWebSocketHelper helper;
    private final Set<String> dataStreams;
    private EventListenerList listeners = new EventListenerList();

    public COSMWebSocketHandlerImpl(Set<String> dataStreams) {
        this.helper = new COSMWebSocketHelper();
        this.dataStreams = dataStreams;
    }

    @Override
    public void onOpen(WebSocket webSocket) {
        List<COSMServerRequest> requests;
        requests = generateSubscripeRequests(dataStreams);

        String strRequest;
        for (COSMServerRequest request : requests) {
            strRequest = helper.getAsAsJSON(request);
            webSocket.sendTextMessage(strRequest);
        }
    }

    @Override
    public void onMessage(String message) {
        Map<String, Object> map_response = (Map<String, Object>) helper
                .getObjectFromJson(message, Map.class);
        if (map_response.get("body") != null) {
            Map<String, Object> bodyMap = (Map<String, Object>) map_response
                    .get("body");

            if (bodyMap.get("status") != null) {
                COSMServerResponse response = (COSMServerResponse) helper
                        .getObjectFromJson(message, COSMServerResponse.class);
                COSMWebSocketEvent event = new COSMWebSocketEvent(this);

                event.setEvent(helper.createEvent(response.getBody()
                        .getDatastreams()[0], (Integer) bodyMap.get("id")));
                notifyCOSMWebSocketEvent(event);
            }
        }

    }

    public List<COSMServerRequest> generateSubscripeRequests(
            Set<String> dataStreams) {
        List<COSMServerRequest> list = new LinkedList<COSMServerRequest>();
        for (String dataStream : dataStreams) {
            list.add(getAsSubscribeRequest(dataStream));
        }
        return list;
    }



    public COSMServerRequest getAsSubscribeRequest(String dataStream) {
        COSMServerRequest cosmServerRequest = new COSMServerRequest();
        cosmServerRequest.setResource(dataStream);
        cosmServerRequest.setMethod(COSMServerRequest.METHOD_SUBSCRIBE);
        cosmServerRequest.setAPIKey(Globals.API_KEY);
        return cosmServerRequest;
    }


    public synchronized void notifyCOSMWebSocketEvent(
            COSMWebSocketEvent event) {
        for (COSMWebSocketListener l : listeners
                .getListeners(COSMWebSocketListener.class))
            l.handleWebSocketEvent(event);
    }

    @Override
    public void addListener(COSMWebSocketListener listener) {
        listeners.add(COSMWebSocketListener.class, listener);
    }


    @Override
    public void onFragment(String s, boolean b) {
        // Not used by COSM API
    }

    @Override
    public void onClose(WebSocket webSocket) {
        // Not used
    }

     @Override
    public void onError(Throwable throwable) {
         // Not used
    }
}
