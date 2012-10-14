package fl.routeGenerator;

import org.jdesktop.swingx.mapviewer.DefaultWaypoint;
import org.jdesktop.swingx.mapviewer.GeoPosition;

public class RoutePoint extends DefaultWaypoint implements
		Comparable<RoutePoint> {

	private int routePointNo;

	public RoutePoint(int orderNumber) {
		super();
	}

	public RoutePoint(int routePointNo, double latitude, double longitude) {
		super(latitude, longitude);
		this.routePointNo = routePointNo;
	}

	public RoutePoint(int routePointNo, GeoPosition geoPosition) {
		super(geoPosition);
		this.routePointNo = routePointNo;
	}

	public int getRoutePointNo() {
		return routePointNo;
	}

	public int compareTo(RoutePoint o) {
		if (routePointNo < o.routePointNo) {
			return -1;
		} else if (routePointNo > o.routePointNo) {
			return 1;
		}

		return 0;
	}
}
