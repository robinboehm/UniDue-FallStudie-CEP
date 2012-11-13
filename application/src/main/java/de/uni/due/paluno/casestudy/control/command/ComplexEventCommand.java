package de.uni.due.paluno.casestudy.control.command;

import java.util.LinkedHashMap;
import java.util.Map;

import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EventBean;

import de.uni.due.paluno.casestudy.Globals;
import de.uni.due.paluno.casestudy.control.CockpitService;

/**
 * Command pattern that is used to execute business logic if a specific EPL
 * statement is met. The EPL condition describes a complex event (CE).
 * 
 * The Class is used as a callback object in an ESPER Listener
 * 
 * The business logic is called against the reference of CockpitService
 * 
 * The specific logic and the EPL are implemented in subclasses
 * 
 * @author saids
 * 
 */
public abstract class ComplexEventCommand {
	/**
	 * Reference to the Esper Runtime
	 */
	private EPRuntime epr;

	/**
	 * Reference to the active Service Instance
	 */
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

	/**
	 * Method that is called by ESPER.
	 * 
	 * Delegates the call to the executeLogic method, that contains the actual
	 * business logic to execute.
	 * 
	 * @param eventParams
	 *            Projection of the EPL statement parsed into a map
	 */
	public void execute(Map<String, Object> eventParams) {
		Globals.dump(this.getClass(), this.getInfoMessage(eventParams));
		this.executeLogic(epr, eventParams);
	}

	protected String getInfoMessage(Map<String, Object> eventParams) {
		return "";
	}

	/**
	 * Implements the actual business logic to execute if a CE occurs.
	 * 
	 * @param epr
	 *            Reference to ESPER Runtime
	 * @param eventParams
	 *            Projection of the EPL statement parsed into a map
	 */
	protected abstract void executeLogic(EPRuntime epr,
			Map<String, Object> eventParams);

	/**
	 * Returns the EPL statement that describes the logically linked CE.
	 * 
	 * @return
	 */
	public abstract String getEPL();

	/**
	 * Returns list of references to Complex / Simple Events that are used in
	 * the EPL statement.
	 * 
	 * @return
	 */
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

	/**
	 * Returns the columns that are used in the projection of the EPL statement.
	 * 
	 * @return
	 */
	public abstract String[] getColumns();
}
