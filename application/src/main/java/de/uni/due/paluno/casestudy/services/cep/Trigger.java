package de.uni.due.paluno.casestudy.services.cep;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

import de.uni.due.paluno.casestudy.control.command.Command;

/**
 * Mediator between complex event commands and esper
 * classes/dependencies/EPStatements
 * 
 * Instances of this class are used as the listener objects of the EPStatements
 * that are created by the EsperCOSMAdapter.
 * 
 * @author saids
 * 
 */
public class Trigger implements UpdateListener {

	private Command ec;

	public Trigger(Command ec) {
		this.ec = ec;
	}

	// @Override
	public void update(EventBean[] newData, EventBean[] oldData) {
		ec.delegate(newData);
	}

	public String getCEC() {
		return this.ec.toString();
	}
}
