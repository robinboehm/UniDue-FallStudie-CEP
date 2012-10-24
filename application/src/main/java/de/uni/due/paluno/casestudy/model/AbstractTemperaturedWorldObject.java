package de.uni.due.paluno.casestudy.model;

public class AbstractTemperaturedWorldObject extends AbstractWorldObject {

    private double temperature;

    public AbstractTemperaturedWorldObject(){
        super();
    }

    public AbstractTemperaturedWorldObject(String id) {
        super(id);
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
