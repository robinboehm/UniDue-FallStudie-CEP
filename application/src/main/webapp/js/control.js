function initWebSocket() {
	// Create and connect web socket
	var host = "ws://localhost:8080/world";

	try {
		socket = new WebSocket(host);
		socket.onopen = function(msg) {
			socket.send(JSON.stringify(buildRegistration()));
		};
		socket.onmessage = function(msg) {
			updateUi(msg);
		};
		socket.onclose = function(msg) {
		};

	} catch (ex) {
		console.log(ex);
	}
}

function updateUi(msg) {
	var clientDTO = JSON.parse(msg.data);

	for ( var j = 0; j < clientDTO.uiElements.length; j++) {

		if (clientDTO.uiElements[j].type == "mapsUIElement") {
			// Clean Up
			removeLinesAndMarkers();

			// Draw Markers
			setMarkers(getUIElement(clientDTO.uiElements[j].id),
					clientDTO.uiElements[j]);

			// Draw Routes
			drawColoredRoutes(getUIElement(clientDTO.uiElements[j].id),
					clientDTO.uiElements[j]);
		} else if (clientDTO.uiElements[j].type == "tableUIElement") {
			clearTablesUIElement();
			fillTablesUIElement(clientDTO.uiElements[j].transports);
		}
	}
}

function buildRegistration() {
	var regObj = new ClientObject();
	regObj.id = "WebClient";

	for (i = 0; i < window.uiElements.length; i++) {
		var uiElement = new UIElement(window.uiElements[i].id,
				window.uiElements[i].type, "")
		regObj.UIElements[i] = uiElement;
	}

	return regObj;
}