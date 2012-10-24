package de.uni.due.paluno.casestudy.model.mock;

import de.uni.due.paluno.casestudy.model.Route;
import de.uni.due.paluno.casestudy.model.WayPoint;

import java.util.List;
import java.util.Random;

public class MockRoute extends Route {

    public MockRoute() {
        super();
        Random r = new Random();
        int max = r.nextInt(10)+1;
        for(int i=0;i<max;i++){
            points.add(new MockWayPoint(r.nextInt() + ";" + r.nextDouble() + "-" + r.nextDouble()));
        }
    }

    public MockRoute(String id, List<WayPoint> points) {
        super(id);
    }
}