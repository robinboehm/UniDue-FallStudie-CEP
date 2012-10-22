package de.uni.due.paluno.casestudy.cep.model;

public abstract class AbstractWorldObject{

    public String id;

    public AbstractWorldObject(String id) {
        this.id = id;
    }

    public AbstractWorldObject() {
        this(System.nanoTime()+"");
    }

}
