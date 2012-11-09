package de.uni.due.paluno.casestudy;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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

import de.uni.due.paluno.casestudy.cosm.COSMWebSocketEngine;
import de.uni.due.paluno.casestudy.cosm.event.COSMWebSocketEvent;
import de.uni.due.paluno.casestudy.cosm.event.COSMWebSocketListener;
import de.uni.due.paluno.casestudy.model.Route;
import de.uni.due.paluno.casestudy.model.WayPoint;
import de.uni.due.paluno.casestudy.model.World;
import de.uni.due.paluno.casestudy.service.CockpitDemoService;
import de.uni.due.paluno.casestudy.service.CockpitService;
import de.uni.due.paluno.casestudy.ui.DTOGenerator;

@WebServlet(name = "WorldWebSocketServlet", urlPatterns = { "/world" }, loadOnStartup = 1)
public class WorldWebSocketServlet extends WebSocketServlet implements
		COSMWebSocketListener {
	private static final long serialVersionUID = -1439435191685551673L;
	private List<MessageInbound> connections = new CopyOnWriteArrayList<MessageInbound>();
	private CockpitService cockpitService;

	public WorldWebSocketServlet() throws IOException, ExecutionException,
			InterruptedException {
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

		return new MyMessageInbound(this.cockpitService.getWorld());
	}

	private final class MyMessageInbound extends MessageInbound {

		private World world;

		public MyMessageInbound(World world) {
			this.world = world;
		}

		@Override
		protected void onBinaryMessage(ByteBuffer message) throws IOException {

		}

		@Override
		protected void onTextMessage(CharBuffer message) throws IOException {
			System.out.println(message);
		}

		@Override
		protected void onOpen(WsOutbound outbound) {
			connections.add(this);

			CharBuffer buffer = CharBuffer.wrap(DTOGenerator
					.getMapsUIDTO(world).toString());
			try {
				outbound.writeTextMessage(buffer);
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}

	// @Override
	public void handleWebSocketEvent(COSMWebSocketEvent e) {
		for (MessageInbound inbound : connections) {
			CharBuffer buffer = CharBuffer.wrap(DTOGenerator.getMapsUIDTO(
					this.cockpitService.getWorld()).toString());
			try {
				inbound.getWsOutbound().writeTextMessage(buffer);
			} catch (IOException ex) {
				System.out.println(ex);
			}
		}
	}
}
