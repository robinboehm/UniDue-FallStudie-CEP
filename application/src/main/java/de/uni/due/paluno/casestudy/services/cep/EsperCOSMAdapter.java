package de.uni.due.paluno.casestudy.services.cep;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;

import de.uni.due.paluno.casestudy.Globals;
import de.uni.due.paluno.casestudy.control.CockpitService;
import de.uni.due.paluno.casestudy.control.command.Command;
import de.uni.due.paluno.casestudy.services.cep.events.ControlledWaypointTemperatureUpdate;
import de.uni.due.paluno.casestudy.services.cep.events.RouteEvent;
import de.uni.due.paluno.casestudy.services.cep.events.UncontrolledWaypointTemperatureUpdate;
import de.uni.due.paluno.casestudy.services.cosm.event.COSMWebSocketEvent;
import de.uni.due.paluno.casestudy.services.cosm.event.COSMWebSocketListener;

/**
 * Translates data updates from COSM into ESPER Events. Configures the
 * underlying ESPER engine.
 * 
 * Therefore the COSMWebSocketListener interface is implemented. Instances of
 * this class are registered as listeners in the COSMWebSocketEngine.
 * 
 * @author saids
 * 
 */
public class EsperCOSMAdapter implements COSMWebSocketListener {

	private Configuration cepConfig;
	private EPServiceProvider cep;
	private List<String> eventTypes;

	/**
	 * List of commands that are used as callback objects in ESPER listeners
	 */
	private List<Command> commands;
	private CockpitService service;

	public EsperCOSMAdapter(CockpitService service) {
		this.cepConfig = new Configuration();
		this.eventTypes = new ArrayList<String>();
		this.commands = new ArrayList<Command>();
		this.service = service;
	}

	@Override
	public void handleWebSocketEvent(COSMWebSocketEvent e) {
		Object[] values = new Object[] { e.getEvent().getTarget(),
				e.getEvent().getData() };

		this.cep.getEPRuntime().sendEvent(values,
				e.getEvent().getClass().getSimpleName());
	}

	public void addToConfig(Command cec) {
		this.commands.add(cec);

		if (!this.eventTypes.contains(cec.getEventName())) {
			this.resolveEventType(this.cepConfig, cec.getEventName());
			this.eventTypes.add(cec.getEventName());
		}
	}

	private void resolveEventType(Configuration cepConfig, String eventName) {
		String[] attributes = null;
		Object[] types = null;

		if (eventName.startsWith(ControlledWaypointTemperatureUpdate.class
				.getSimpleName())
				|| eventName
						.startsWith(UncontrolledWaypointTemperatureUpdate.class
								.getSimpleName())) {
			attributes = new String[] { "target", "data" };
			types = new Object[] { String.class, Double.class };
		} else if (eventName.startsWith(RouteEvent.class.getSimpleName())) {
			attributes = new String[] { "target", "key" };
			types = new Object[] { String.class, String.class };
		}

		cepConfig.addEventType(eventName, attributes, types);
	}

	/**
	 * Creates the EPStatement instances that are used by esper. The trigger
	 * instance, which contains an complex event command as a callback object,
	 * is configured as the listener object.
	 * 
	 * @param epl
	 *            The EPL statement to configure
	 * @param et
	 *            the Trigger (Listener) object
	 */
	private void createAndConfigureCEPStatement(String epl, Trigger et) {
		EPAdministrator cepAdm = this.cep.getEPAdministrator();
		EPStatement cepStatement = cepAdm.createEPL(epl);

		cepStatement.addListener(et);
	}

	public EPServiceProvider getCEP() {
		return cep;
	}

	/**
	 * Creates ESPER Listeners based on the list of complex event commands that
	 * has been provided to the adapter.
	 */
	public void createTriggers() {
		this.cep = EPServiceProviderManager.getProvider("myCEPEngine",
				this.cepConfig);

		Iterator<Command> i = this.commands.iterator();
		while (i.hasNext()) {
			Command cec = i.next();
			cec.setService(this.service);
			cec.setEpr(this.cep.getEPRuntime());

			Trigger et = new Trigger(cec);

			createAndConfigureCEPStatement(cec.getEPL(), et);

			Globals.dump(this.getClass(), "Trigger created: " + cec.getEPL());
		}
	}
}
