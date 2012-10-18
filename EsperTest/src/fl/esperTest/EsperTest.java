package fl.esperTest;

import java.util.Iterator;

import Pachube.Data;
import Pachube.Feed;
import Pachube.Pachube;
import Pachube.PachubeException;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class EsperTest {

	public static void loadTemperatureData(EPRuntime cepRT) {
		Pachube cosm = new Pachube(
				"5T64pgQVJiKlfgQU2Q9IvH_UyUKSAKxTNjZma1kyQnFsQT0g");

		try {
			Feed feed = cosm.getFeed(80264);

			Iterator<Data> i = feed.getData().iterator();
			while (i.hasNext()) {
				Data current = i.next();
				Temperature t = new Temperature();
				t.route = feed.getId();
				t.temperature = current.getValue();

				cepRT.sendEvent(t);
			}
		} catch (PachubeException e) {
		}
	}

	public static class CEPListener implements UpdateListener {

		public void update(EventBean[] newData, EventBean[] oldData) {
			// System.out.println("Event received: "
			// + ((Temperature) newData[0].getUnderlying())
			// .getTemperature());
			System.out.println("Event received: " + newData[0].getUnderlying());
		}
	}

	public static void main(String[] args) {

		// The Configuration is meant only as an initialization-time object.
		Configuration cepConfig = new Configuration();

		cepConfig.addEventType("Temperature", Temperature.class.getName());

		EPServiceProvider cep = EPServiceProviderManager.getProvider(
				"myCEPEngine", cepConfig);
		EPRuntime cepRT = cep.getEPRuntime();

		EPAdministrator cepAdm = cep.getEPAdministrator();
		EPStatement cepStatement = cepAdm
				.createEPL("select route, avg(temperature) from Temperature.win:length(5) group by route having count(temperature) = 3");

		cepStatement.addListener(new CEPListener());

		loadTemperatureData(cepRT);
	}
}