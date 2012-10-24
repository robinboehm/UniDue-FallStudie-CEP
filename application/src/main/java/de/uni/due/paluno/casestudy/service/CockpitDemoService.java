package de.uni.due.paluno.casestudy.service;

import java.util.List;

import de.uni.due.paluno.casestudy.model.AbstractWorldObject;
import de.uni.due.paluno.casestudy.model.Route;

public class CockpitDemoService implements CockpitService {
	private LookupService lookupService;

	public CockpitDemoService() {
		this.lookupService = new LookupDemoService();
	}

	@Override
	public void updateModel(AbstractWorldObject object) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Route> lookUpRoutes() {
		return this.lookupService.lookUpRoutes();
	}
}
