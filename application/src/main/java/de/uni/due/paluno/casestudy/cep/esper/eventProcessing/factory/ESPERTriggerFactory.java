package de.uni.due.paluno.casestudy.cep.esper.eventProcessing.factory;

import com.espertech.esper.client.*;
import de.uni.due.paluno.casestudy.cep.cosm.event.COSMWebSocketEvent;
import de.uni.due.paluno.casestudy.cep.cosm.event.COSMWebSocketListener;
import de.uni.due.paluno.casestudy.cep.events.command.ComplexEventCommand;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ESPERTriggerFactory implements TriggerFactory,COSMWebSocketListener {

	private Configuration cepConfig;
	private EPServiceProvider cep;
	private List<String> eventTypes;
	private List<ComplexEventCommand> commands;

	public ESPERTriggerFactory() {
		this.cepConfig = new Configuration();
		this.eventTypes = new ArrayList<String>();
		this.commands = new ArrayList<ComplexEventCommand>();
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

	private void createAndConfigureCEPStatement(String epl, ESPERTrigger et) {
		EPAdministrator cepAdm = this.cep.getEPAdministrator();
		EPStatement cepStatement = cepAdm.createEPL(epl);

		cepStatement.addListener(et);
	}

	public EPServiceProvider getCep() {
		return cep;
	}

	public void createTriggers() {
		this.cep = EPServiceProviderManager.getProvider("myCEPEngine",
				this.cepConfig);

		Iterator<ComplexEventCommand> i = this.commands.iterator();
		while (i.hasNext()) {
			ComplexEventCommand cec = i.next();
			cec.setEpr(this.cep.getEPRuntime());

			ESPERTrigger et = new ESPERTrigger(cec);

			createAndConfigureCEPStatement(cec.getEPL(), et);
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
				System.out.println(((ESPERTrigger) ul).getCEC());
			}
		}
	}

    @Override
    public void handleWebSocketEvent(COSMWebSocketEvent e) {
        getCep().getEPRuntime().sendEvent(e.getEvent());
    }
}