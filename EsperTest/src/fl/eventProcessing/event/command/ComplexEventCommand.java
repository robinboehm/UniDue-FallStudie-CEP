package fl.eventProcessing.event.command;

import java.util.LinkedHashMap;
import java.util.Map;

import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EventBean;

public abstract class ComplexEventCommand {
	private EPRuntime epr;

	public void setEpr(EPRuntime epr) {
		this.epr = epr;
	}

	public void execute(Map<String, Object> eventParams) {
		this.executeLogic(epr, eventParams);
	}

	protected abstract void executeLogic(EPRuntime epr,
			Map<String, Object> eventParams);

	public abstract String getEPL();

	public abstract Map<String, String> getEventTypes();

	public Map<String, Object> toMap(EventBean eventBean) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();

		for (int i = 0; i < this.getColumns().length; i++) {
			map.put(this.getColumns()[i], eventBean.get(this.getColumns()[i]));
		}

		return map;
	}

	public abstract String[] getColumns();
}
