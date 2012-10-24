package de.uni.due.paluno.casestudy.model;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedList;
import java.util.List;

public class World {

    public List<Route> routes;
    public List<Truck> trucks;


    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public void addRoute(Route route) {
        this.routes.add(route);
    }


    public void addTruck(Truck truck) {
        this.trucks.add(truck);
    }

    public List<Truck> getTrucks() {
        return trucks;
    }

    public void setTrucks(List<Truck> trucks) {
        this.trucks = trucks;
    }



    public World() {
        this.trucks = new LinkedList<Truck>();
        this.routes = new LinkedList<Route>();
    }

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());
        try {
            return (objectMapper.writeValueAsString(this));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return (e.getClass() + " : " + e.getMessage());
        }
    }
}
