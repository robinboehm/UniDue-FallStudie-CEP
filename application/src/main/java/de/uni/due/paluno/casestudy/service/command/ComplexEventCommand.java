package de.uni.due.paluno.casestudy.service.command;

import java.util.LinkedHashMap;
import java.util.Map;

import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EventBean;

import de.uni.due.paluno.casestudy.Globals;
import de.uni.due.paluno.casestudy.service.CockpitService;

public abstract class ComplexEventCommand {
	private EPRuntime epr;
	private CockpitService service;

	public CockpitService getService() {
		return service;
	}

	public void setService(CockpitService service) {
		this.service = service;
	}

	public void setEpr(EPRuntime epr) {
		this.epr = epr;
	}

	public void execute(Map<String, Object> eventParams) {
		Globals.dump(this.getClass(), this.getInfoMessage(eventParams));
		this.executeLogic(epr, eventParams);
	}

	protected String getInfoMessage(Map<String, Object> eventParams) {
		return "";
	}

	protected abstract void executeLogic(EPRuntime epr,
			Map<String, Object> eventParams);

	public abstract String getEPL();

	public abstract Map<String, String> getEventTypes();

	public Map<String, Object> toMap(EventBean eventBean) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();

		if (this.getColumns() != null)
			for (int i = 0; i < this.getColumns().length; i++) {
				map.put(this.getColumns()[i],
						eventBean.get(this.getColumns()[i]));
			}

		return map;
	}

	public abstract String[] getColumns();
}
