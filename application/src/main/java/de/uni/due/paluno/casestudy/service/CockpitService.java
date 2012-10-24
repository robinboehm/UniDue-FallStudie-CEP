package de.uni.due.paluno.casestudy.service;

import de.uni.due.paluno.casestudy.model.AbstractWorldObject;

public interface CockpitService extends LookupService {

	public void updateModel(AbstractWorldObject object);
}
