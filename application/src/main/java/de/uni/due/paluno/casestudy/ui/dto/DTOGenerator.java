package de.uni.due.paluno.casestudy.ui.dto;

import java.util.Iterator;

import de.uni.due.paluno.casestudy.model.Route;
import de.uni.due.paluno.casestudy.model.WayPoint;
import de.uni.due.paluno.casestudy.model.World;

public class DTOGenerator {
	public static MapsComponentDTO getMapsUIDTO(World world) {
		MapsComponentDTO mui = new MapsComponentDTO();

		// Routes
		Iterator<Route> iRoutes = world.getRoutes().iterator();
		while (iRoutes.hasNext()) {
			Route currentRoute = iRoutes.next();

			RouteDTO rDto = new RouteDTO();
			rDto.setId(currentRoute.getId());
			rDto.setStatus("critical");

			// Waypoints
			Iterator<WayPoint> iWP = currentRoute.getPoints().iterator();
			while (iWP.hasNext()) {
				WayPoint wp = iWP.next();

				WayPointDTO wpDTO = new WayPointDTO();
				wpDTO.setId(wp.getId());
				wpDTO.setX(wp.getX());
				wpDTO.setY(wp.getY());
				wpDTO.setStatus(wp.getStatus());
				wpDTO.setTemperature(Double.toString(wp.getTemperature()));

				rDto.getWaypoints().add(wpDTO);
			}

			mui.getRoutes().add(rDto);
		}

		return mui;
	}
}
