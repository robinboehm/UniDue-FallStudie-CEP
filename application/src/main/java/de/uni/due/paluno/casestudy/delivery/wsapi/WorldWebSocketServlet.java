package de.uni.due.paluno.casestudy.delivery.wsapi;

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
import org.apache.catalina.websocket.WsOutbound;

import de.uni.due.paluno.casestudy.AbstractCockpitServlet;
import de.uni.due.paluno.casestudy.cep.events.command.WaypointTemperatureDumper;
import de.uni.due.paluno.casestudy.cep.factory.ESPERTriggerFactory;
import de.uni.due.paluno.casestudy.delivery.cosm.COSMWebSocketEngine;
import de.uni.due.paluno.casestudy.delivery.cosm.event.COSMWebSocketEvent;
import de.uni.due.paluno.casestudy.delivery.cosm.event.COSMWebSocketListener;
import de.uni.due.paluno.casestudy.model.Route;
import de.uni.due.paluno.casestudy.model.World;
import de.uni.due.paluno.casestudy.model.WorldHelper;

@WebServlet(name = "WorldWebSocketServlet", urlPatterns = { "/world" }, loadOnStartup = 1)
public class WorldWebSocketServlet extends AbstractCockpitServlet implements
		COSMWebSocketListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -571317291238824893L;
	private List<MessageInbound> connections = new CopyOnWriteArrayList<MessageInbound>();
	private static World world;

	public WorldWebSocketServlet() throws IOException, ExecutionException,
			InterruptedException {
		List<Route> routes = this.getService().lookUpRoutes();
		List<String> list = convertToURL(routes);

		world = WorldHelper.createWorldFromDataStreams(list);

		System.out.println(world);

		ESPERTriggerFactory etf = new ESPERTriggerFactory();
		etf.addToConfig(new WaypointTemperatureDumper());
		etf.createTriggers();

		COSMWebSocketEngine engine = createEngine(list);
		engine.addAdListener(etf);
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
}
