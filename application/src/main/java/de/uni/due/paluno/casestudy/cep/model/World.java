package de.uni.due.paluno.casestudy.cep.model;

import java.util.List;

public class World {

    public List<Route> routes;

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public List<Truck> getTrucks() {
        return trucks;
    }

    public void setTrucks(List<Truck> trucks) {
        this.trucks = trucks;
    }

    public List<Truck> trucks;

    public World() {
    }

}
