package de.uni.due.paluno.casestudy.cep.cosm;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.ListenableFuture;
import com.ning.http.client.websocket.WebSocket;
import com.ning.http.client.websocket.WebSocketTextListener;
import com.ning.http.client.websocket.WebSocketUpgradeHandler;
import de.uni.due.paluno.casestudy.cep.cosm.event.COSMWebSocketEvent;
import de.uni.due.paluno.casestudy.cep.cosm.event.COSMWebSocketListener;
import de.uni.due.paluno.casestudy.cep.cosm.model.cosm.COSMServerRequest;
import de.uni.due.paluno.casestudy.cep.cosm.model.cosm.COSMServerResponse;

import javax.swing.event.EventListenerList;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class COSMWebSocketEngine implements WebSocketTextListener {

    private final AsyncHttpClientConfig config;
    private final AsyncHttpClient client;
    private final COSMHelper helper;
    private final ListenableFuture<WebSocket> futureWebSocket;
    private volatile WebSocket websocket;
    private final WebSocketUpgradeHandler webSocketUpgradeHandler;
    private final List<String> dataStreams;

    private EventListenerList listeners = new EventListenerList();

    // Constants
    private final String API_KEY = "5T64pgQVJiKlfgQU2Q9IvH_UyUKSAKxTNjZma1kyQnFsQT0g";
    private final String API_URL = "ws://api.cosm.com:8080";


    public COSMWebSocketEngine(List<String> dataStreams) throws IOException, ExecutionException {
        this.config = new AsyncHttpClientConfig.Builder().build();
        this.client = new AsyncHttpClient(config);
        this.helper = new COSMHelper();

        this.dataStreams = dataStreams;

        webSocketUpgradeHandler = new WebSocketUpgradeHandler.Builder().addWebSocketListener(this).build();
        futureWebSocket = client.prepareGet(API_URL).execute(webSocketUpgradeHandler);
    }

    public void start() throws IOException, ExecutionException, InterruptedException {
        websocket = futureWebSocket.get();
    }

    public void stop() {
        websocket.close();
    }

    @Override
    public void onMessage(String message) {
        COSMServerResponse response = (COSMServerResponse) helper.getObjectFromJson(message, COSMServerResponse.class);
        //Measurement measure = helper.createMeasurement(response);
        // Generate Event
        //System.out.println(measure);
        COSMWebSocketEvent event = new COSMWebSocketEvent(this);
        event.setEvent(helper.createEvent(response));
        notifyCOSMWebSocketEvent(event);
    }

    @Override
    public void onFragment(String s, boolean b) {
        // Not used by COSM API
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
    public void onClose(WebSocket webSocket) {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    public List<COSMServerRequest> generateSubscripeRequests(List<String> dataStreams) {
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
        cosmServerRequest.setAPIKey(API_KEY);
        return cosmServerRequest;
    }


    /*
       Listener Pattern
    */

    public void addAdListener(COSMWebSocketListener listener) {
        listeners.add(COSMWebSocketListener.class, listener);
    }

    public void removeAdListener(COSMWebSocketListener listener) {
        listeners.remove(COSMWebSocketListener.class, listener);
    }

    protected synchronized void notifyCOSMWebSocketEvent(COSMWebSocketEvent event) {
        for (COSMWebSocketListener l : listeners.getListeners(COSMWebSocketListener.class))
            l.handleWebSocketEvent(event);
    }
}
