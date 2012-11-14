package de.uni.due.paluno.casestudy.model;

/**
 * Super class to all classes that are part of the data model.
 * 
 * @author saids
 * 
 */
public abstract class AbstractWorldObject {
	public String id;

	public AbstractWorldObject(String id) {
		this.id = id;
	}

	public AbstractWorldObject() {
		this(System.nanoTime() + "");
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
