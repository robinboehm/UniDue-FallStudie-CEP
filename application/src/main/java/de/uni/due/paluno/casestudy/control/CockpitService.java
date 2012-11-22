package de.uni.due.paluno.casestudy.control;

import java.util.List;
import java.util.Map;

import de.uni.due.paluno.casestudy.model.Route;
import de.uni.due.paluno.casestudy.model.World;
import de.uni.due.paluno.casestudy.services.cep.EsperCOSMAdapter;
import de.uni.due.paluno.casestudy.servlet.UIUpdateController;

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
	public void updateRoute(String id, String key);

	/**
	 * Returns the whole data model
	 * 
	 * @return Data Model
	 */
	public World getWorld();

	/**
	 * Returns reference to UI Update controller
	 * 
	 * @return
	 */
	public UIUpdateController getUIUpdateController();

	/**
	 * Retries all Routes that contain the specified waypoint
	 * 
	 * @param waypoint
	 *            Waypoint that is used in the desired routes
	 * @return
	 */
	public List<Route> getRoutesForWaypoint(String waypoint);

	/**
	 * Gets the temperature for each waypoint that is part of the specified
	 * route
	 * 
	 * @param route
	 *            id of the route
	 * @return Map containg Waypoint->Temperature Elements
	 */
	public Map<String, Double> getTemperaturesForRoute(Route route);
}
