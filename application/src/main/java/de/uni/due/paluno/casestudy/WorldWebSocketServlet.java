package de.uni.due.paluno.casestudy;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

import de.uni.due.paluno.casestudy.cep.EsperCOSMAdapter;
import de.uni.due.paluno.casestudy.cosm.COSMWebSocketEngine;
import de.uni.due.paluno.casestudy.cosm.event.COSMWebSocketEvent;
import de.uni.due.paluno.casestudy.cosm.event.COSMWebSocketListener;
import de.uni.due.paluno.casestudy.model.Route;
import de.uni.due.paluno.casestudy.model.World;
import de.uni.due.paluno.casestudy.model.WorldHelper;
import de.uni.due.paluno.casestudy.service.CockpitDemoService;
import de.uni.due.paluno.casestudy.service.CockpitService;

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
		// Create world domain
		List<Route> routes = this.cockpitService.lookUpRoutes();
		List<String> routeURLs = convertToURL(routes);
		this.createWorldObject(routeURLs);

		// Start cosmwebsocket engine
		initCOSMWebSocketEngine(routeURLs);
	}

	private void createWorldObject(List<String> routeURLs) {
		World world = WorldHelper.createWorldFromDataStreams(routeURLs);

		this.cockpitService.setWorld(world);

		System.out.println(world.toString());
	}

	private void initCOSMWebSocketEngine(List<String> list) throws IOException,
			ExecutionException, InterruptedException {
		COSMWebSocketEngine engine = createEngine(list);
		engine.addListener(new EsperCOSMAdapter(this.cockpitService));
		engine.addListener(this);
		engine.start();
	}

	private List<String> convertToURL(List<Route> routes) {
		List<String> urls = new ArrayList<String>();

		Iterator<Route> i = routes.iterator();
		while (i.hasNext()) {
			Route current = i.next();
			urls.add("/feeds/" + current.getId());
		}

		return urls;
	}

	private COSMWebSocketEngine createEngine(List<String> list)
			throws IOException, ExecutionException, InterruptedException {
		COSMWebSocketEngine engine = new COSMWebSocketEngine(list);
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

		}

		@Override
		protected void onOpen(WsOutbound outbound) {
			connections.add(this);
			CharBuffer buffer = CharBuffer.wrap(this.world.toString());
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
			CharBuffer buffer = CharBuffer.wrap(this.cockpitService.getWorld()
					.toString());
			try {
				inbound.getWsOutbound().writeTextMessage(buffer);
			} catch (IOException ex) {
				System.out.println(ex);
			}
		}
	}
}
