function createGoogleMapsComponent() {
	// some geo-locations for the markers
	var latlng = new google.maps.LatLng(51.2277411, 6.7734556);
	var mapOptions = {
		zoom : 8,
		center : latlng,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};

	// creates google maps map
	var map = new google.maps.Map(document.getElementById("mapUIContainer"),
			mapOptions);

	// Add to global ui component storage
	var uiElement = new UIElement("mapUI", "mapsUIElement", map);
	window.uiElements[window.uiElements.length] = uiElement;
}

function getUIElement(id) {
	for ( var j = 0; j < window.uiElements.length; j++) {
		if (window.uiElements[j].id == id) {
			return window.uiElements[j].reference;
		}
	}
}

// puts colored temperature markers on the map
function setMarkers(map, mapsDto) {
	// import different marker colors
	// red marker
	var pinColorRed = "FF0000";
	var pinImageRed = new google.maps.MarkerImage(
			"http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|"
					+ pinColorRed, new google.maps.Size(21, 34),
			new google.maps.Point(0, 0), new google.maps.Point(10, 34));

	// yellow marker
	var pinColorYellow = "FFFF42"
	var pinImageYellow = new google.maps.MarkerImage(
			"http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|"
					+ pinColorYellow, new google.maps.Size(21, 34),
			new google.maps.Point(0, 0), new google.maps.Point(10, 34));

	// green marker
	var pinColorGreen = "C6EF8C";
	var pinImageGreen = new google.maps.MarkerImage(
			"http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|"
					+ pinColorGreen, new google.maps.Size(21, 34),
			new google.maps.Point(0, 0), new google.maps.Point(10, 34));

	// start marker
	var pinImageStart = new google.maps.MarkerImage(
			"http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=S|007FFF|0D0608",
			new google.maps.Size(21, 34), new google.maps.Point(0, 0),
			new google.maps.Point(10, 34));

	// destination marker
	var pinImageDestination = new google.maps.MarkerImage(
			"http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=D|007FFF|0D0608",
			new google.maps.Size(21, 34), new google.maps.Point(0, 0),
			new google.maps.Point(10, 34));

	// import marker shadow
	var pinShadow = new google.maps.MarkerImage(
			"http://chart.apis.google.com/chart?chst=d_map_pin_shadow",
			new google.maps.Size(40, 37), new google.maps.Point(0, 0),
			new google.maps.Point(12, 35));

	// declarated here but used in upcoming for-loop
	var pinImageColor;
	var titleDynamic;

	// iterates over the markerArray and creates GoogleMaps markers
	for ( var j = 0; j < mapsDto.routes.length; j++) {

		for ( var i = 0; i < mapsDto.routes[j].waypoints.length; i++) {

			// check for start or destination waypoint
			if (i == 0) {
				pinImageColor = pinImageStart;
				titleDynamic = mapsDto.routes[j].start + "\n"
						+ mapsDto.routes[j].waypoints[i].temperature
			} else if (i == mapsDto.routes[j].waypoints.length - 1) {
				pinImageColor = pinImageDestination;
				titleDynamic = mapsDto.routes[j].destination + "\n"
						+ mapsDto.routes[j].waypoints[i].temperature
			}

			else {

				// controll construct to differentiate between marker colors
				if (mapsDto.routes[j].waypoints[i].status == "critical") {
					pinImageColor = pinImageRed
					titleDynamic = mapsDto.routes[j].waypoints[i].temperature
				} else if (mapsDto.routes[j].waypoints[i].status == "warning") {
					pinImageColor = pinImageYellow
					titleDynamic = mapsDto.routes[j].waypoints[i].temperature
				} else if (mapsDto.routes[j].waypoints[i].status == "Ok") {
					pinImageColor = pinImageGreen
					titleDynamic = mapsDto.routes[j].waypoints[i].temperature
				}
			}

			// actual marker creation
			var marker = new google.maps.Marker({
				position : new google.maps.LatLng(
						mapsDto.routes[j].waypoints[i].x,
						mapsDto.routes[j].waypoints[i].y),
				map : map,
				icon : pinImageColor,
				shadow : pinShadow,
				title : titleDynamic
			});
			window.markers[window.markers.length] = marker;
		}
	}

}

// draw coloured routes via polylines
function drawColoredRoutes(map, mapsDto) {
	// created for upcoming fill with LatLng data
	drawnRouteArray = new Array();
	dummyRouteArray = new Array();
	var strokeColorVariable;
	var routePath;

	// loop over markerArray and create waypoints for routes to be drawn
	for ( var j = 0; j < mapsDto.routes.length; j++) {
		for ( var i = 0; i < mapsDto.routes[j].waypoints.length; i++) {
			drawnRouteArray[drawnRouteArray.length] = new google.maps.LatLng(
					mapsDto.routes[j].waypoints[i].x,
					mapsDto.routes[j].waypoints[i].y);
		}

		// determine route color
		if (mapsDto.routes[j].status == "critical") {
			strokeColorVariable = "#FF0000";
		} else if (mapsDto.routes[j].status == "warning") {
			strokeColorVariable = "#FFFF00";
		} else if (mapsDto.routes[j].status == "Ok") {
			strokeColorVariable = "#00FF00";
		}

		// draw actual route
		routePath = new google.maps.Polyline({
			path : drawnRouteArray,
			strokeColor : strokeColorVariable,
			strokeOpacity : 1.0,
			strokeWeight : 2
		});
		window.lines[window.lines.length] = routePath;

		// put it all on the map object
		routePath.setMap(map);
		drawnRouteArray = dummyRouteArray;
	}
}

function removeLinesAndMarkers(markers) {
	for ( var i = 0; i < window.markers.length; i++) {
		window.markers[i].setMap(null);
	}

	for ( var i = 0; i < window.lines.length; i++) {
		window.lines[i].setMap(null);
	}
}