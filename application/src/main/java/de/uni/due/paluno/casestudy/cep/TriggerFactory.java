package de.uni.due.paluno.casestudy.cep;

import com.espertech.esper.client.EPServiceProvider;

import de.uni.due.paluno.casestudy.cep.events.command.ComplexEventCommand;

public interface TriggerFactory {

	public abstract void addToConfig(ComplexEventCommand cec);

	public abstract EPServiceProvider getCep();

	public abstract void createTriggers();

	public abstract void dumpTriggers();
}