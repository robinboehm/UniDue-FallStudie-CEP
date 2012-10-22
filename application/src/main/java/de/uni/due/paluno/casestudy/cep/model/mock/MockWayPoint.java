package de.uni.due.paluno.casestudy.cep.model.mock;

import de.uni.due.paluno.casestudy.cep.model.WayPoint;

import java.util.Random;

public class MockWayPoint extends WayPoint {

    public MockWayPoint() {
        super();
    }

    public MockWayPoint(String id) {
        super(id);
    }

    @Override
    public double getTemperature() {
        Random r = new Random();
        return Math.round(r.nextDouble()*100%40);
    }
}
