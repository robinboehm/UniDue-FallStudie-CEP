package de.uni.due.paluno.casestudy;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;

import org.apache.catalina.websocket.WebSocketServlet;

import de.uni.due.paluno.casestudy.service.CockpitDemoServiceImpl;
import de.uni.due.paluno.casestudy.service.CockpitService;

public abstract class AbstractCockpitServlet extends WebSocketServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2395624564230405938L;
	private Context ic;

	@Override
	public void init() throws ServletException {
		super.init();

		try {
			this.ic = new InitialContext();

			// Build up app context
			this.ic.addToEnvironment(Globals.IC_SERVICE_OBJECT,
					new CockpitDemoServiceImpl());
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	public CockpitService getService() {
		try {
			return (CockpitService) this.ic.lookup(Globals.IC_SERVICE_OBJECT);
		} catch (NamingException e) {
			e.printStackTrace();
		}

		return null;
	}
}
