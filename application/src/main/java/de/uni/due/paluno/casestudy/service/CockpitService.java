package de.uni.due.paluno.casestudy.service;

import de.uni.due.paluno.casestudy.model.World;

public interface CockpitService extends LookupService {
	public void updateRoute(String id, String key);

	public World getWorld();

	public void setWorld(World world);
}
