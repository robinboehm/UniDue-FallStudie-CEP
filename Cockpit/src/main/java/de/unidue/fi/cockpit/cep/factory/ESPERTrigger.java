package de.unidue.fi.cockpit.cep.factory;

import java.util.Map;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

import de.unidue.fi.cockpit.cep.event.command.ComplexEventCommand;

public class ESPERTrigger implements UpdateListener {

	private ComplexEventCommand ec;

	public ESPERTrigger(ComplexEventCommand ec) {
		this.ec = ec;
	}

	@Override
	public void update(EventBean[] newData, EventBean[] oldData) {
		Map<String, Object> map = ec.toMap(newData[0]);

		ec.execute(map);
	}

	public String getCEC() {
		return this.ec.toString();
	}
}
