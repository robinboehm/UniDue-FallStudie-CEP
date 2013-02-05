package de.uni.due.paluno.casestudy.services.cosm.impl;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.ListenableFuture;
import com.ning.http.client.websocket.WebSocket;
import com.ning.http.client.websocket.WebSocketUpgradeHandler;
import de.uni.due.paluno.casestudy.Globals;
import de.uni.due.paluno.casestudy.services.cosm.COSMWebSocketEngine;
import de.uni.due.paluno.casestudy.services.cosm.COSMWebSocketHandler;
import de.uni.due.paluno.casestudy.services.cosm.event.COSMWebSocketListener;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class COSMWebSocketEngineImpl implements COSMWebSocketEngine {

	private final AsyncHttpClientConfig config;
	private final AsyncHttpClient client;
	private final ListenableFuture<WebSocket> futureWebSocket;
	private volatile WebSocket websocket;
	private final WebSocketUpgradeHandler webSocketUpgradeHandler;
    private final COSMWebSocketHandler handler;



	public COSMWebSocketEngineImpl(Set<String> dataStreams) throws IOException,
			ExecutionException {
		this.config = new AsyncHttpClientConfig.Builder().build();
		this.client = new AsyncHttpClient(config);


        handler = new COSMWebSocketHandlerImpl(dataStreams);

        webSocketUpgradeHandler =
                new WebSocketUpgradeHandler
                        .Builder()
                        .addWebSocketListener(handler)
                        .build();

		futureWebSocket = client.prepareGet(Globals.INBOUND_API_URL).execute(webSocketUpgradeHandler);
	}

    public void start() throws IOException, ExecutionException,
			InterruptedException {
		websocket = futureWebSocket.get();
	}

    public void stop() {
		websocket.close();
	}

    public void addListener(COSMWebSocketListener listener) {
        handler.addListener(listener);
	}

}
