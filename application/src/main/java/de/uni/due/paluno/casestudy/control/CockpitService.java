package de.uni.due.paluno.casestudy.control;

import de.uni.due.paluno.casestudy.model.World;
import de.uni.due.paluno.casestudy.services.cep.EsperCOSMAdapter;

public interface CockpitService {
	public void updateWaypoint(String id, Double temperature);

	public EsperCOSMAdapter getECA();

	void updateRoute(String id, String key);

	public World getWorld();
}
