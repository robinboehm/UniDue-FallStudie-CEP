package fl.routeGenerator;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import fl.routeGenerator.cosm.Environment;

public class DefaultItemListener implements ItemListener {

	private RouteGenerator routeGenerator;

	public DefaultItemListener(RouteGenerator routeGenerator) {
		this.routeGenerator = routeGenerator;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		this.routeGenerator.loadExistingRoute((Environment) e.getItem());
	}
}
