package de.uni.due.paluno.casestudy.ui.dto;

import java.util.Iterator;

import de.uni.due.paluno.casestudy.model.Route;
import de.uni.due.paluno.casestudy.model.Transport;
import de.uni.due.paluno.casestudy.model.WayPoint;
import de.uni.due.paluno.casestudy.model.World;

public class DTOMapper {
	public static MapsUIElementDTO getMapsUIElementDTO(World world) {
		MapsUIElementDTO mui = new MapsUIElementDTO();

		// Routes
		Iterator<Route> iRoutes = world.getRoutes().iterator();
		while (iRoutes.hasNext()) {
			Route currentRoute = iRoutes.next();

			RouteDTO rDto = new RouteDTO();
			rDto.setId(currentRoute.getId());
			rDto.setStatus(currentRoute.getStatus().toString());

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

	public static TableUIElementDTO getTableUIElementDTO(World world) {
		TableUIElementDTO dto = new TableUIElementDTO();

		Iterator<Transport> i = world.getTransports().iterator();
		while (i.hasNext()) {
			Transport transport = i.next();

			TransportDTO transportDTO = new TransportDTO();
			transportDTO.setId(transport.getId());
			transportDTO.setTemperature(Double.toString(transport.getTruck()
					.getTemperature()));
			transportDTO.setStart(transport.getRoute().getStart().getId());
			transportDTO.setDestination(transport.getRoute().getDestination()
					.getId());

			dto.getTransports().add(transportDTO);
		}

		return dto;
	}
}
