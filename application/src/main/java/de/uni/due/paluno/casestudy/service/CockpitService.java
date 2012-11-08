package de.uni.due.paluno.casestudy.service;

import de.uni.due.paluno.casestudy.cep.EsperCOSMAdapter;
import de.uni.due.paluno.casestudy.model.World;

public interface CockpitService {
	public void updateWaypoint(String id, Double temperature);

	public World getWorld();

	public EsperCOSMAdapter getECA();

	void updateRoute(String id, String key);
}
