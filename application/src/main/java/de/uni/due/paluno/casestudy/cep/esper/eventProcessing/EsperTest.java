package de.uni.due.paluno.casestudy.cep.esper.eventProcessing;

public class EsperTest {
/*
	public static void loadTemperatureData(EPRuntime cepRT) {
		Pachube cosm = new Pachube(
				"5T64pgQVJiKlfgQU2Q9IvH_UyUKSAKxTNjZma1kyQnFsQT0g");

		try {
			Feed feed = cosm.getFeed(80264);

			Iterator<Data> i = feed.getData().iterator();
			while (i.hasNext()) {
				Data current = i.next();
				WaypointTemperatureEvent t = new WaypointTemperatureEvent();
				t.setTarget("80264;" + current.getTag());
				t.setData(current.getValue());

				cepRT.sendEvent(t);
			}
		} catch (PachubeException e) {
		}
	}

	public static void main(String[] args) {
		ESPERTriggerFactory etf = new ESPERTriggerFactory();
		etf.addToConfig(new RouteAverageExceededCommand(getRoute("80264")));
		etf.addToConfig(new AverageDumpCommand());
		etf.createTriggers();

		etf.dumpTriggers();

		loadTemperatureData(etf.getCep().getEPRuntime());
	}

	private static Route getRoute(String no) {
		Route r = new Route();

		Pachube cosm = new Pachube(
				"5T64pgQVJiKlfgQU2Q9IvH_UyUKSAKxTNjZma1kyQnFsQT0g");

		try {
			Feed feed = cosm.getFeed(80264);

			r.setUid(no);

			Iterator<Data> i = feed.getData().iterator();
			while (i.hasNext()) {
				Data current = i.next();
				Waypoint wp = new Waypoint();
				wp.setUid(no + ";" + current.getTag());
				r.getWaypoints().add(wp);
			}
		} catch (PachubeException e) {
		}

		return r;
	}
	*/
}