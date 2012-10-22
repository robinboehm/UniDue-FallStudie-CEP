package de.uni.due.paluno.casestudy.cep.model.mock;

import de.uni.due.paluno.casestudy.cep.model.Truck;

import java.util.Random;

public class MockTruck extends Truck {
    public MockTruck() {
        super();
    }

    @Override
    public double getTemperature() {
        Random r = new Random();
        return Math.round(r.nextDouble()*100%25);
    }
}
