package de.uni.due.paluno.casestudy.control.command;

import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EventBean;

abstract class ComplexEventCommand extends AmbigiousCommand {

	@Override
	protected void execute(EPRuntime epr, EventBean[] newData) {
		// Do Nothing / Not Relevant for CEC
	}
}
