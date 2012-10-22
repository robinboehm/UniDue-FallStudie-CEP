package de.uni.due.paluno.casestudy.cep.model.mock;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uni.due.paluno.casestudy.cep.model.Route;
import de.uni.due.paluno.casestudy.cep.model.Truck;
import de.uni.due.paluno.casestudy.cep.model.WayPoint;
import de.uni.due.paluno.casestudy.cep.model.World;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MockWorld extends World {
    public MockWorld() {
        Random r = new Random();
        int max = r.nextInt(10) + 1;
        List<WayPoint> wayPoints = new LinkedList<WayPoint>();
        List<Route> routes = new LinkedList<Route>();
        for (int i = 0; i < max; i++) {
            routes.add(new MockRoute());
        }
        this.setRoutes(routes);


        List<Truck> trucks = new LinkedList<Truck>();
        for (int i = 0; i < max; i++) {
            trucks.add(new MockTruck());
        }
        this.setTrucks(trucks);
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
