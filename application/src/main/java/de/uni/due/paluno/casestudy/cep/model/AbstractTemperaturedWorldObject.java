package de.uni.due.paluno.casestudy.cep.model;

public class AbstractTemperaturedWorldObject extends AbstractWorldObject {

    private double temperature;

    public AbstractTemperaturedWorldObject(String id) {
        super(id);
        this.temperature = 0.0d;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
