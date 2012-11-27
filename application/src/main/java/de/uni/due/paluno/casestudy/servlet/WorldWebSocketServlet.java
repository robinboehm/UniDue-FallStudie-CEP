package de.uni.due.paluno.casestudy.servlet;

import de.uni.due.paluno.casestudy.Globals;
import de.uni.due.paluno.casestudy.control.CockpitDemoService;
import de.uni.due.paluno.casestudy.control.CockpitService;
import de.uni.due.paluno.casestudy.model.Route;
import de.uni.due.paluno.casestudy.model.WayPoint;
import de.uni.due.paluno.casestudy.model.World;
import de.uni.due.paluno.casestudy.services.cosm.COSMWebSocketEngine;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * The applications runs as a Servlet in a Servlet Container.
 * 
 * @author saids
 * 
 */
@WebServlet(name = "WorldWebSocketServlet", urlPatterns = { "/world" }, loadOnStartup = 1)
public class WorldWebSocketServlet extends WebSocketServlet {
	private static final long serialVersionUID = -1439435191685551673L;

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

		// Feed event engine with events
		engine.addListener(this.cockpitService.getECA());

		// Start Engine
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

	/**
	 * Starts the COSM Engine
	 * 
	 * @param routeURLs
	 *            URL's to receive Data from (feeds to Waypoints)
	 * @return Reference to the engine
	 * @throws IOException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	private COSMWebSocketEngine createEngine(Set<String> routeURLs)
			throws IOException, ExecutionException, InterruptedException {
		COSMWebSocketEngine engine = new COSMWebSocketEngine(routeURLs);
		engine.start();
		return engine;
	}

	@Override
	protected StreamInbound createWebSocketInbound(String subProtocol,
			HttpServletRequest request) {
		return new InboundWebSocketHandler(
                this.cockpitService.getUIUpdateController());
	}
}
