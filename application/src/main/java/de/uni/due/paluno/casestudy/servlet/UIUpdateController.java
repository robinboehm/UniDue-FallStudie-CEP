package de.uni.due.paluno.casestudy.servlet;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.catalina.websocket.MessageInbound;

import de.uni.due.paluno.casestudy.Globals;
import de.uni.due.paluno.casestudy.model.World;
import de.uni.due.paluno.casestudy.ui.UIElement;
import de.uni.due.paluno.casestudy.ui.dto.ClientDTO;
import de.uni.due.paluno.casestudy.ui.dto.DTOGenerator;
import de.uni.due.paluno.casestudy.ui.dto.UserInterfaceComponentDTO;

/**
 * Updates UI Components in case the application model has been updated due to
 * COSM Events
 * 
 * @author saids
 * 
 */
public class UIUpdateController {
	private List<MessageInbound> connections;
	private Map<String, List<UIElement>> uiListeners;

	public UIUpdateController() {
		this.connections = new CopyOnWriteArrayList<MessageInbound>();
		this.uiListeners = new HashMap<String, List<UIElement>>();
	}

	private void updateClients(World world) {
		for (MessageInbound inbound : connections) {
			updateClient(inbound, world);
		}
	}

	private void updateClient(MessageInbound inbound, World world) {
		CharBuffer buffer = this.generateDTOForClient("WebClient", world);
		try {
			inbound.getWsOutbound().writeTextMessage(buffer);
		} catch (IOException ex) {
			System.out.println(ex);
		}
	}

	private CharBuffer generateDTOForClient(String id, World world) {
		ClientDTO clientDTO = new ClientDTO();
		clientDTO.setId(id);

		List<UIElement> uiElements = this.uiListeners.get(id);
		Iterator<UIElement> i = uiElements.iterator();
		while (i.hasNext()) {
			UIElement uiElement = i.next();

			if (uiElement.getType().equals(Globals.COMPONENT_MAP_UI)) {
				UserInterfaceComponentDTO uicDTO = DTOGenerator
						.getMapsUIDTO(world);
				uicDTO.setId(uiElement.getId());
				uicDTO.setType(Globals.COMPONENT_MAP_UI);
				clientDTO.getUiElements().add(uicDTO);
			}
		}

		return CharBuffer.wrap(clientDTO.toString());
	}

	public void addUIListeners(String client, List<UIElement> uiElements) {
		this.uiListeners.put(client, uiElements);
	}

	public void addConnection(InboundWebSocketHandler myMessageInbound) {
		this.connections.add(myMessageInbound);
	}

	public void update(World world) {
		this.updateClients(world);
	}
}
