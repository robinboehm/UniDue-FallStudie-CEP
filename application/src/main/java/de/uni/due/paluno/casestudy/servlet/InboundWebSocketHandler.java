package de.uni.due.paluno.casestudy.servlet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.uni.due.paluno.casestudy.Globals;
import de.uni.due.paluno.casestudy.ui.UIElement;

/**
 * Manages the WebSocket Connection to UI Clients
 * 
 * @author saids
 * 
 */
public final class InboundWebSocketHandler extends MessageInbound {

	private UIUpdateController uiUpdateController;

	public InboundWebSocketHandler(UIUpdateController uiUpdateController) {
		this.uiUpdateController = uiUpdateController;
	}

	@Override
	protected void onBinaryMessage(ByteBuffer message) throws IOException {

	}

	@Override
	protected void onTextMessage(CharBuffer message) throws IOException {
		if (message.toString().equals(Globals.INITIAL_UI_DATA_REQUEST))
			System.out.println("Initial Data Request");
		else
			this.processUIRegistration(message);
	}

	private void processUIRegistration(CharBuffer message) throws IOException,
			JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode registration = mapper.readTree(message.toString());

		String client = registration.get("id").textValue();
		JsonNode elements = registration.get("UIElements");
		List<UIElement> uiElements = new ArrayList<UIElement>();

		Iterator<JsonNode> i = elements.iterator();
		while (i.hasNext()) {
			JsonNode element = i.next();

			UIElement uiElement = new UIElement();
			uiElement.setId(element.get("id").textValue());
			uiElement.setType(element.get("type").textValue());
			uiElements.add(uiElement);
		}

		this.uiUpdateController.addUIListeners(client, uiElements);
	}

	@Override
	protected void onOpen(WsOutbound outbound) {
		this.uiUpdateController.addConnection(this);
	}
}