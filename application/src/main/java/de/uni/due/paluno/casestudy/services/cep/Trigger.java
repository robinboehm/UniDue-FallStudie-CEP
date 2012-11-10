package de.uni.due.paluno.casestudy.services.cep;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

import de.uni.due.paluno.casestudy.control.command.ComplexEventCommand;

import java.util.Map;

public class Trigger implements UpdateListener {

	private ComplexEventCommand ec;

	public Trigger(ComplexEventCommand ec) {
		this.ec = ec;
	}

	//@Override
	public void update(EventBean[] newData, EventBean[] oldData) {
		Map<String, Object> map = ec.toMap(newData[0]);

		ec.execute(map);
	}

	public String getCEC() {
		return this.ec.toString();
	}
}
