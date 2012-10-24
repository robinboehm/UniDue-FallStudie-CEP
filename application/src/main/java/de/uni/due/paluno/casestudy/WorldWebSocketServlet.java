package de.uni.due.paluno.casestudy;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;

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
	private static World world;

	public WorldWebSocketServlet() throws IOException, ExecutionException,
			InterruptedException {
		this.cockpitService = new CockpitDemoService();

		// Create world domain
		List<Route> routes = this.cockpitService.lookUpRoutes();
		List<String> routeURLs = convertToURL(routes);
		world = WorldHelper.createWorldFromDataStreams(routeURLs);

		System.out.println(world);

		// Start cosmwebsocket engine
		initCOSMWebSocketEngine(routeURLs);
	}

	private void initCOSMWebSocketEngine(List<String> list) throws IOException,
			ExecutionException, InterruptedException {
		COSMWebSocketEngine engine = createEngine(list);
		engine.addAdListener(new EsperCOSMAdapter(this.cockpitService));
		engine.addAdListener(this);
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

		return new MyMessageInbound();
	}

	private final class MyMessageInbound extends MessageInbound {
		@Override
		protected void onBinaryMessage(ByteBuffer message) throws IOException {

		}

		@Override
		protected void onTextMessage(CharBuffer message) throws IOException {

		}

		@Override
		protected void onOpen(WsOutbound outbound) {
			connections.add(this);
			CharBuffer buffer = CharBuffer.wrap(world.toString());
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
			CharBuffer buffer = CharBuffer.wrap(world.toString());
			try {
				inbound.getWsOutbound().writeTextMessage(buffer);
			} catch (IOException ex) {
				System.out.println(ex);
			}
		}
	}
}
