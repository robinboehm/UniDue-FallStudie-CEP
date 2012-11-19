
// object for world-registration in service-layer
function ClientObject(id) {
	this.id = id;
	this.UIElements = new Array();
}

// sub-element of world that also registers in service-layer
function UIElement(id, type, reference) {
	this.id = id;
	this.type = type;
	this.reference = reference;
}

// main actions to initialize UI
function initialize() {
	window.uiElements = new Array();
	createGoogleMapsComponent();
	createTableComponent();

	window.markers = new Array();
	window.lines = new Array();
	initWebSocket();
}

// initialize web socket connection
// register world and UIElements
function initWebSocket() {
	// create and connect web socket
	var host = "ws://localhost:8080/world";

	try {
		socket = new WebSocket(host);
		// TODO: Aware of closures and context issues
		socket.onopen = function(msg) {
			socket.send(JSON.stringify(buildRegistration()));
			socket.send("INITIAL_UI_DATA_REQUEST");
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

// execute when messages reach web socket
function updateUi(msg) {
	var clientDTO = JSON.parse(msg.data);

	for ( var j = 0; j < clientDTO.uiElements.length; j++) {

		if (clientDTO.uiElements[j].type == "mapsUIElement") {
			// clean Up
			removeLinesAndMarkers();

			// draw Markers
			setMarkers(getUIElement(clientDTO.uiElements[j].id),
					clientDTO.uiElements[j]);

			// draw Routes
			drawColoredRoutes(getUIElement(clientDTO.uiElements[j].id),
					clientDTO.uiElements[j]);
		} else if (clientDTO.uiElements[j].type == "tableUIElement") {
			// refill table element each time update occurs
			clearTablesUIElement();
			fillTablesUIElement(clientDTO.uiElements[j].transports);
		}
	}
}

// creates registration object which is send to backend
// for registration of world-ui-elements
function buildRegistration() {

	var regObj = new ClientObject("WebClient");

	for (i = 0; i < window.uiElements.length; i++) {
		var uiElement = new UIElement(window.uiElements[i].id,
				window.uiElements[i].type, "")
		regObj.UIElements[i] = uiElement;
	}

	return regObj;
}