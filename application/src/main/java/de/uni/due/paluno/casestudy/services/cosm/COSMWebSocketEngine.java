package de.uni.due.paluno.casestudy.services.cosm;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.swing.event.EventListenerList;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.ListenableFuture;
import com.ning.http.client.websocket.WebSocket;
import com.ning.http.client.websocket.WebSocketTextListener;
import com.ning.http.client.websocket.WebSocketUpgradeHandler;

import de.uni.due.paluno.casestudy.Globals;
import de.uni.due.paluno.casestudy.services.cosm.event.COSMWebSocketEvent;
import de.uni.due.paluno.casestudy.services.cosm.event.COSMWebSocketListener;
import de.uni.due.paluno.casestudy.services.cosm.model.cosm.COSMServerRequest;
import de.uni.due.paluno.casestudy.services.cosm.model.cosm.COSMServerResponse;

public class COSMWebSocketEngine implements WebSocketTextListener {

	private final AsyncHttpClientConfig config;
	private final AsyncHttpClient client;
	private final COSMHelper helper;
	private final ListenableFuture<WebSocket> futureWebSocket;
	private volatile WebSocket websocket;
	private final WebSocketUpgradeHandler webSocketUpgradeHandler;
	private final Set<String> dataStreams;

	private EventListenerList listeners = new EventListenerList();

	// Constants

	public COSMWebSocketEngine(Set<String> dataStreams) throws IOException,
			ExecutionException {
		this.config = new AsyncHttpClientConfig.Builder().build();
		this.client = new AsyncHttpClient(config);
		this.helper = new COSMHelper();

		this.dataStreams = dataStreams;

		webSocketUpgradeHandler = new WebSocketUpgradeHandler.Builder()
				.addWebSocketListener(this).build();
		futureWebSocket = client.prepareGet(Globals.API_URL).execute(
				webSocketUpgradeHandler);
	}

	public void start() throws IOException, ExecutionException,
			InterruptedException {
		websocket = futureWebSocket.get();
	}

	public void stop() {
		websocket.close();
	}

	// @Override
	@SuppressWarnings("unchecked")
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

	// @Override
	public void onFragment(String s, boolean b) {
		// Not used by COSM API
	}

	// @Override
	public void onOpen(WebSocket webSocket) {
		List<COSMServerRequest> requests;
		requests = generateSubscripeRequests(dataStreams);

		String strRequest;
		for (COSMServerRequest request : requests) {
			strRequest = helper.getAsAsJSON(request);
			webSocket.sendTextMessage(strRequest);
		}
	}

	// @Override
	public void onClose(WebSocket webSocket) {

	}

	// @Override
	public void onError(Throwable throwable) {

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

	/*
	 * Listener Pattern
	 */

	public void addListener(COSMWebSocketListener listener) {
		listeners.add(COSMWebSocketListener.class, listener);
	}

	public void removeAdListener(COSMWebSocketListener listener) {
		listeners.remove(COSMWebSocketListener.class, listener);
	}

	protected synchronized void notifyCOSMWebSocketEvent(
			COSMWebSocketEvent event) {
		for (COSMWebSocketListener l : listeners
				.getListeners(COSMWebSocketListener.class))
			l.handleWebSocketEvent(event);
	}
}