package de.uni.due.paluno.casestudy.model.mock;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import de.uni.due.paluno.casestudy.model.Route;
import de.uni.due.paluno.casestudy.model.Truck;
import de.uni.due.paluno.casestudy.model.World;

public class MockWorld extends World {
	public MockWorld() {
		Random r = new Random();
		int max = r.nextInt(10) + 1;
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
}
