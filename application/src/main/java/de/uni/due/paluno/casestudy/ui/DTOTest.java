package de.uni.due.paluno.casestudy.ui;

public class DTOTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MapsUIDTO mui = new MapsUIDTO();

		RouteDTO r = new RouteDTO();
		r.setId(1);
		r.setDestination("Berlin");
		r.setStart("Kleve");
		r.setStatus("Ok");
		mui.getRoutes().add(r);

		WayPointDTO wp = new WayPointDTO();
		wp.setId(1);
		wp.setStatus("Ok");
		wp.setTemperature("20");
		wp.setX("1");
		wp.setY("1");
		r.getWaypoints().add(wp);

		System.out.println(mui.toString());
	}
}
