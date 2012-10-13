package fl.routeGenerator;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class DefaultMouseListener implements MouseListener {

	private RouteGenerator routeGenerator;

	public DefaultMouseListener(RouteGenerator routeGenerator) {
		this.routeGenerator = routeGenerator;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (arg0.getComponent().getName().equals("save")) {
			this.routeGenerator.saveRoute();
		} else {
			this.routeGenerator.clear();
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent arg0) {

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

	}

}
