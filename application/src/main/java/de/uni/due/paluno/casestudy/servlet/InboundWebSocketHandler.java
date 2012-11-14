package de.uni.due.paluno.casestudy.servlet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;

import de.uni.due.paluno.casestudy.Globals;

/**
 * Manages the WebSocket Connection to UI Clients
 * 
 * @author saids
 * 
 */
public final class InboundWebSocketHandler extends MessageInbound {

	/**
	 * Manages UI Updates in terms of DTO generation and UI registration
	 */
	private UIUpdateController uiUpdateController;

	public InboundWebSocketHandler(UIUpdateController uiUpdateController) {
		this.uiUpdateController = uiUpdateController;
	}

	@Override
	protected void onBinaryMessage(ByteBuffer message) throws IOException {

	}

	@Override
	protected void onTextMessage(CharBuffer message) throws IOException {
		// Provide initial data to Clients
		if (message.toString().equals(Globals.INITIAL_UI_DATA_REQUEST))
			this.uiUpdateController.update();
		// Register UI Elements
		else
			this.uiUpdateController.processUIRegistration(message);
	}

	@Override
	protected void onOpen(WsOutbound outbound) {
		this.uiUpdateController.addConnection(this);
	}
}