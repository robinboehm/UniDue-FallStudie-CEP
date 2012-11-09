package de.uni.due.paluno.casestudy;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.uni.due.paluno.casestudy.cosm.COSMWebSocketEngine;
import de.uni.due.paluno.casestudy.cosm.event.COSMWebSocketEvent;
import de.uni.due.paluno.casestudy.cosm.event.COSMWebSocketListener;
import de.uni.due.paluno.casestudy.model.Route;
import de.uni.due.paluno.casestudy.model.WayPoint;
import de.uni.due.paluno.casestudy.model.World;
import de.uni.due.paluno.casestudy.service.CockpitDemoService;
import de.uni.due.paluno.casestudy.service.CockpitService;
import de.uni.due.paluno.casestudy.ui.UIElement;
import de.uni.due.paluno.casestudy.ui.dto.ClientDTO;
import de.uni.due.paluno.casestudy.ui.dto.DTOGenerator;
import de.uni.due.paluno.casestudy.ui.dto.UserInterfaceComponentDTO;

@WebServlet(name = "WorldWebSocketServlet", urlPatterns = { "/world" }, loadOnStartup = 1)
public class WorldWebSocketServlet extends WebSocketServlet implements
		COSMWebSocketListener {
	private static final long serialVersionUID = -1439435191685551673L;
	private List<MessageInbound> connections = new CopyOnWriteArrayList<MessageInbound>();
	private CockpitService cockpitService;
	private Map<String, List<UIElement>> uiListeners;

	public WorldWebSocketServlet() throws IOException, ExecutionException,
			InterruptedException {
		// UI Listeners
		this.uiListeners = new HashMap<String, List<UIElement>>();

		// Init Service Layer
		this.initServiceLayer();

		// Init WebSocket Engine
		this.initWebSocketEngine();
	}

	private void initServiceLayer() {
		this.cockpitService = new CockpitDemoService();

		try {
			Context ic = new InitialContext();

			ic.addToEnvironment(Globals.IC_SERVICE_OBJECT, this.cockpitService);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	private void initWebSocketEngine() throws IOException, ExecutionException,
			InterruptedException {

		// Start cosmwebsocket engine
		Set<String> routeURLs = convertToURL(this.cockpitService.getWorld());
		initCOSMWebSocketEngine(routeURLs);
	}

	private void initCOSMWebSocketEngine(Set<String> routeURLs)
			throws IOException, ExecutionException, InterruptedException {
		COSMWebSocketEngine engine = createEngine(routeURLs);
		engine.addListener(this.cockpitService.getECA());
		engine.addListener(this);
		engine.start();
	}

	private Set<String> convertToURL(World world) {
		Set<String> urls = new HashSet<String>();

		Iterator<Route> i = world.getRoutes().iterator();
		while (i.hasNext()) {
			Route route = i.next();

			Iterator<WayPoint> j = route.getPoints().iterator();
			while (j.hasNext()) {
				WayPoint wayPoint = j.next();
				try {
					urls.add("/feeds/" + wayPoint.getId());
				} catch (IllegalArgumentException e) {
					// Do nothing
				}
			}
		}

		return urls;
	}

	private COSMWebSocketEngine createEngine(Set<String> routeURLs)
			throws IOException, ExecutionException, InterruptedException {
		COSMWebSocketEngine engine = new COSMWebSocketEngine(routeURLs);
		engine.start();
		return engine;
	}

	@Override
	protected StreamInbound createWebSocketInbound(String subProtocol,
			HttpServletRequest request) {
		return new MyMessageInbound();
	}

	private final class MyMessageInbound extends MessageInbound {
		@Override
		protected void onBinaryMessage(ByteBuffer message) throws IOException {

		}

		@Override
		protected void onTextMessage(CharBuffer message) throws IOException {
			if (message.toString().equals(Globals.INITIAL_UI_DATA_REQUEST))
				updateClient(this);
			else
				processUIRegistration(message);
		}

		private void processUIRegistration(CharBuffer message)
				throws IOException, JsonProcessingException {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode registration = mapper.readTree(message.toString());

			String worldId = registration.get("id").textValue();
			JsonNode elements = registration.get("uiElements");
			List<UIElement> uiElements = new ArrayList<UIElement>();

			Iterator<JsonNode> i = elements.iterator();
			while (i.hasNext()) {
				JsonNode element = i.next();

				UIElement uiElement = new UIElement();
				uiElement.setId(element.get("id").textValue());
				uiElement.setType(element.get("type").textValue());
				uiElements.add(uiElement);
			}

			uiListeners.put(worldId, uiElements);
		}

		@Override
		protected void onOpen(WsOutbound outbound) {
			connections.add(this);
		}
	}

	private void updateClients() {
		for (MessageInbound inbound : connections) {
			updateClient(inbound);
		}
	}

	private void updateClient(MessageInbound inbound) {
		CharBuffer buffer = this.generateDTOForClient("world");
		try {
			inbound.getWsOutbound().writeTextMessage(buffer);
		} catch (IOException ex) {
			System.out.println(ex);
		}
	}

	// @Override
	public void handleWebSocketEvent(COSMWebSocketEvent e) {
		this.updateClients();
	}

	private CharBuffer generateDTOForClient(String id) {
		ClientDTO clientDTO = new ClientDTO();
		clientDTO.setId(id);

		List<UIElement> uiElements = this.uiListeners.get(id);
		Iterator<UIElement> i = uiElements.iterator();
		while (i.hasNext()) {
			UIElement uiElement = i.next();

			if (uiElement.equals(Globals.COMPONENT_MAP_UI)) {
				UserInterfaceComponentDTO uicDTO = DTOGenerator
						.getMapsUIDTO(this.cockpitService.getWorld());
				uicDTO.setId(uiElement.getId());
				clientDTO.getUiElements().add(uicDTO);
			}
		}

		return CharBuffer.wrap(clientDTO.toString());
	}
}
