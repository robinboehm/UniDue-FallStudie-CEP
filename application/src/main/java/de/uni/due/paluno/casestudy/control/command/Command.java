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
public abstract class Command {
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
	 * Proxy Method for dumping business logic
	 * 
	 * Delegates the call to the executeLogic method, that contains the actual
	 * business logic to execute.
	 * 
	 * @param eventParams
	 *            Projection of the EPL statement parsed into a map assuming
	 *            result contains only one row
	 */
	public void execute(Map<String, Object> eventParams) {
		Globals.dump(this.getClass(), this.getInfoMessage(eventParams));
		this.execute(epr, eventParams);
	}

	/**
	 * Implements the actual business logic to execute if a CE occurs.
	 * 
	 * @param epr
	 *            Reference to ESPER Runtime
	 * @param eventParams
	 *            Projection of the EPL statement parsed into a map
	 */
	protected abstract void execute(EPRuntime epr,
			Map<String, Object> eventParams);

	/**
	 * Implements the actual business logic to execute if data is received from
	 * COSM and NO CE is involved yet
	 * 
	 * @param Results
	 *            of the EPL Statement
	 */
	public void execute(EventBean[] newData) {
		Globals.dump(this.getClass(), this.getInfoMessage(this.toMap(newData)));
		this.execute(epr, newData);
	}

	/**
	 * Implements the actual business logic to execute if a CE occurs.
	 * 
	 * @param epr
	 *            Reference to ESPER Runtime
	 * @param newData
	 */
	protected abstract void execute(EPRuntime epr, EventBean[] newData);

	/**
	 * Method that is called by ESPER.
	 * 
	 * Delegates to concrete business logic to specific method based on class
	 * hierarchy
	 * 
	 * @param eventParams
	 *            Results of the EPL statement
	 */
	public void delegate(EventBean[] newData) {
		if (this instanceof ComplexEventCommand) {
			this.execute(this.toMap(newData[0]));
		} else {
			this.execute(newData);
		}
	}

	protected String getInfoMessage(Map<String, Object> eventParams) {
		return "";
	}

	/**
	 * Returns the EPL statement that describes the logically linked CE.
	 * 
	 * @return
	 */
	public abstract String getEPL();

	/**
	 * Returns the event that is used in the EPL
	 * 
	 * @return
	 */
	public abstract String getEventName();

	public Map<String, Object> toMap(EventBean eventBean) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();

		if (this.getColumns() != null)
			for (int i = 0; i < this.getColumns().length; i++) {
				map.put(this.getColumns()[i],
						eventBean.get(this.getColumns()[i]));
			}

		return map;
	}

	protected Map<String, Object> toMap(EventBean[] newData) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();

		for (int i = 0; i < newData.length; i++) {
			map.put(Integer.toString(i), toMap(newData[i]));
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
