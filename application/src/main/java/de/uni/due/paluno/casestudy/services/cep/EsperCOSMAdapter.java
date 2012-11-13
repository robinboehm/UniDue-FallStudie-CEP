package de.uni.due.paluno.casestudy.services.cep;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.UpdateListener;

import de.uni.due.paluno.casestudy.Globals;
import de.uni.due.paluno.casestudy.control.CockpitService;
import de.uni.due.paluno.casestudy.control.command.ComplexEventCommand;
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
	private List<ComplexEventCommand> commands;
	private CockpitService service;

	public EsperCOSMAdapter(CockpitService service) {
		this.cepConfig = new Configuration();
		this.eventTypes = new ArrayList<String>();
		this.commands = new ArrayList<ComplexEventCommand>();
		this.service = service;
	}

	@Override
	public void handleWebSocketEvent(COSMWebSocketEvent e) {
		this.cep.getEPRuntime().sendEvent(e.getEvent());
	}

	public void addToConfig(ComplexEventCommand cec) {
		this.commands.add(cec);

		this.addEventTypes(cec.getEventTypes());
	}

	private void addEventTypes(Map<String, String> eventTypes) {
		Iterator<String> aliases = eventTypes.keySet().iterator();
		while (aliases.hasNext()) {
			String alias = aliases.next();

			if (!this.eventTypes.contains(alias)) {
				this.cepConfig.addEventType(alias, eventTypes.get(alias));
				this.eventTypes.add(alias);
			}
		}
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

		Iterator<ComplexEventCommand> i = this.commands.iterator();
		while (i.hasNext()) {
			ComplexEventCommand cec = i.next();
			cec.setService(this.service);
			cec.setEpr(this.cep.getEPRuntime());

			Trigger et = new Trigger(cec);

			createAndConfigureCEPStatement(cec.getEPL(), et);

			Globals.dump(this.getClass(), "Trigger created: " + cec.getEPL());
		}
	}

	public void dumpTriggers() {
		for (int i = 0; i < this.cep.getEPAdministrator().getStatementNames().length; i++) {

			EPStatement cepStatement = this.cep
					.getEPAdministrator()
					.getStatement(
							this.cep.getEPAdministrator().getStatementNames()[i]);

			Iterator<UpdateListener> j = cepStatement.getUpdateListeners();
			while (j.hasNext()) {
				UpdateListener ul = j.next();
				System.out.println(((Trigger) ul).getCEC());
			}
		}
	}
}
