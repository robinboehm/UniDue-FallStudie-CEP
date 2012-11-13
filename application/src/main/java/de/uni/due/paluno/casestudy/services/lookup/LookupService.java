package de.uni.due.paluno.casestudy.services.lookup;

import de.uni.due.paluno.casestudy.model.World;

/**
 * The LookupServices is used to look up the persisted data model
 * 
 * @author saids
 * 
 */
public interface LookupService {
	/**
	 * Returns the whole data model
	 * 
	 * @return World object: the data model
	 */
	public World getWorld();
}
