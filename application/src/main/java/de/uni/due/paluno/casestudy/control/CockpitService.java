package de.uni.due.paluno.casestudy.control;

import de.uni.due.paluno.casestudy.model.World;
import de.uni.due.paluno.casestudy.services.cep.EsperCOSMAdapter;

/**
 * Business Layer containing the functionality to do model/ui updates and
 * initialize the CEP engine
 * 
 * @author saids
 * 
 */
public interface CockpitService {

	/**
	 * Updates the temperature of a waypoint in the data model
	 * 
	 * @param id
	 *            Id of the waypoint
	 * @param temperature
	 *            Temperature
	 */
	public void updateWaypoint(String id, Double temperature);

	/**
	 * Returns the reference to the service's instance of the EsperCOSMAdapter
	 * (ECA)
	 * 
	 * @return Reference to ECA
	 */
	public EsperCOSMAdapter getECA();

	/**
	 * Updates the status of a route based on the specified complex event key
	 * 
	 * @param id
	 *            Id of the route
	 * @param key
	 *            Event key
	 */
	void updateRoute(String id, String key);

	/**
	 * Returns the whole data model
	 * 
	 * @return Data Model
	 */
	public World getWorld();
}
