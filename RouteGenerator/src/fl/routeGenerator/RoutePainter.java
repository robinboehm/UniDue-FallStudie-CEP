package fl.routeGenerator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.Waypoint;
import org.jdesktop.swingx.mapviewer.WaypointPainter;

public class RoutePainter<T extends JXMapViewer> extends WaypointPainter<T> {

	public RoutePainter() {
		super();
	}

	@Override
	protected void doPaint(Graphics2D g, T map, int w, int h) {
		super.doPaint(g, map, w, h);

		g = (Graphics2D) g.create();
		Rectangle rect = map.getViewportBounds();
		g.translate(-rect.x, -rect.y);

		Point2D nextPoint = null;
		Point2D firstPoint = null;

		g.setColor(Color.BLUE);

		for (Waypoint wp : getWaypoints()) {
			Point2D currentPoint = map.getTileFactory().geoToPixel(
					wp.getPosition(), map.getZoom());

			if (firstPoint == null) {
				firstPoint = currentPoint;
			}

			if (nextPoint != null) {
				g.drawLine((int) nextPoint.getX(), (int) nextPoint.getY(),
						(int) currentPoint.getX(), (int) currentPoint.getY());
			}

			nextPoint = currentPoint;
		}

		g.dispose();
	}

}
