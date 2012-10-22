package de.uni.due.paluno.casestudy.cep.model;

import java.util.LinkedList;
import java.util.List;

public class Route extends AbstractWorldObject {

    public List<WayPoint> points;

    public Route() {
        super();
        this.points = new LinkedList<WayPoint>();
    }

    public Route(String id) {
        super(id);
        this.points = new LinkedList<WayPoint>();
    }

    public void addWaypoint(WayPoint wayPoint){
        this.points.add(wayPoint);
    }
}
