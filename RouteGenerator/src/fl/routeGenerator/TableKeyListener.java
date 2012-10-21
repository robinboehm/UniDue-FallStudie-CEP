package fl.routeGenerator;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TableKeyListener implements KeyListener {

	private RouteGenerator routeGenerator;

	public TableKeyListener(RouteGenerator routeGenerator) {
		this.routeGenerator = routeGenerator;
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_DELETE)
			this.routeGenerator.deleteRoutePoint();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
}
