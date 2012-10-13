package fl.routeGenerator;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MapMouseListener implements MouseListener {

	private RouteGenerator routeGenerator;

	public MapMouseListener(RouteGenerator routeGenerator) {
		this.routeGenerator = routeGenerator;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		this.routeGenerator.addPoint(this.routeGenerator.getMaps().getMainMap()
				.convertPointToGeoPosition(e.getPoint()));
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}
