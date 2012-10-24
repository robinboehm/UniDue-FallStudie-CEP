package de.uni.due.paluno.casestudy.model;

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
