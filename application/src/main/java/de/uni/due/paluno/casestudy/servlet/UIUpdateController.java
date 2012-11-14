package de.uni.due.paluno.casestudy.servlet;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.catalina.websocket.MessageInbound;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.uni.due.paluno.casestudy.Globals;
import de.uni.due.paluno.casestudy.control.CockpitService;
import de.uni.due.paluno.casestudy.model.World;
import de.uni.due.paluno.casestudy.ui.UIElement;
import de.uni.due.paluno.casestudy.ui.dto.ClientDTO;
import de.uni.due.paluno.casestudy.ui.dto.DTOMapper;
import de.uni.due.paluno.casestudy.ui.dto.UserInterfaceComponentDTO;

/**
 * Updates UI Components in case the application model has been updated due to
 * COSM Events
 * 
 * @author saids
 * 
 */
public class UIUpdateController {
	/**
	 * Websocket connections
	 */
	private List<MessageInbound> connections;

	/**
	 * Registered clients
	 */
	private Map<String, List<UIElement>> uiListeners;

	/**
	 * Reference to service layer
	 */
	private CockpitService service;

	public UIUpdateController(CockpitService service) {
		this.service = service;
		this.connections = new CopyOnWriteArrayList<MessageInbound>();
		this.uiListeners = new HashMap<String, List<UIElement>>();
	}

	/**
	 * Updates all clients with the provided data model
	 * 
	 * @param world
	 *            Data model
	 */
	private void updateClients(World world) {
		for (MessageInbound inbound : connections) {
			updateClient(inbound, world);
		}
	}

	/**
	 * Updates the client listening on the inbound channel
	 * 
	 * @param inbound
	 *            Websocket channel
	 * @param world
	 *            Data model
	 */
	private void updateClient(MessageInbound inbound, World world) {
		CharBuffer buffer = this.generateClientDTO(Globals.CLIENT_WEB_CLIENT,
				world);
		try {
			inbound.getWsOutbound().writeTextMessage(buffer);
		} catch (IOException ex) {
			System.out.println(ex);
		}
	}

	/**
	 * Generates DTO objects for the specified client
	 * 
	 * @param id
	 *            Client ID
	 * @param world
	 *            data model
	 * @return JSON String to put in to the websocket
	 */
	private CharBuffer generateClientDTO(String id, World world) {
		ClientDTO clientDTO = new ClientDTO();
		clientDTO.setId(id);

		List<UIElement> uiElements = this.uiListeners.get(id);
		Iterator<UIElement> i = uiElements.iterator();
		while (i.hasNext()) {
			UIElement uiElement = i.next();

			if (uiElement.getType().equals(Globals.COMPONENT_MAP_UI)) {
				UserInterfaceComponentDTO uicDTO = DTOMapper
						.getMapsUIElementDTO(world);
				uicDTO.setId(uiElement.getId());
				uicDTO.setType(Globals.COMPONENT_MAP_UI);
				clientDTO.getUiElements().add(uicDTO);
			} else if (uiElement.getType().equals(Globals.COMPONENT_LIST_UI)) {
				UserInterfaceComponentDTO uicDTO = DTOMapper
						.getTableUIElementDTO(world);
				uicDTO.setId(uiElement.getId());
				uicDTO.setType(Globals.COMPONENT_LIST_UI);
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

	public void update() {
		this.updateClients(this.service.getWorld());
	}

	/**
	 * Registers clients / ui elements
	 * 
	 * @param message
	 * @throws IOException
	 * @throws JsonProcessingException
	 */
	public void processUIRegistration(CharBuffer message) throws IOException,
			JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();

		// Parse inpout
		JsonNode registration = mapper.readTree(message.toString());
		String client = registration.get("id").textValue();
		JsonNode elements = registration.get("UIElements");
		List<UIElement> uiElements = new ArrayList<UIElement>();

		Iterator<JsonNode> i = elements.iterator();
		while (i.hasNext()) {
			JsonNode element = i.next();

			// Create UI Element representations
			UIElement uiElement = new UIElement();
			uiElement.setId(element.get("id").textValue());
			uiElement.setType(element.get("type").textValue());
			uiElements.add(uiElement);
		}

		this.addUIListeners(client, uiElements);
	}
}
