function fillTablesUIElement(transports) {
	for ( var j = 0; j < transports.length; j++) {
		$('#truckTable').dataTable().fnAddData([ transports[j].attributes ]);
	}

	// iterate over rows and change their color
	var rows = document.getElementById("truckTable").getElementsByTagName('tr');

	for ( var j = 1; j < rows.length; j++) {
		var columns = rows[j].getElementsByTagName('td');

		for ( var i = 0; i < columns.length; i++) {
			if (jQuery(columns[i]).html() == "critical") {
				jQuery(columns[i]).css("background-color", "#FF0000");
			} else if (jQuery(columns[i]).html() == "warning") {
				jQuery(columns[i]).css("background-color", "#FFFF00")
			} else if (jQuery(columns[i]).html() == "ok") {
				jQuery(columns[i]).css("background-color", "#00CC00");
			}
		}
	}
}

function clearTablesUIElement() {
	$('#truckTable').dataTable().fnClearTable();
}

function createTableComponent() {
	var uiElement = new UIElement("tableUI", "tableUIElement", null);
	window.uiElements[window.uiElements.length] = uiElement;

	$('#truckTable').dataTable({
		"bPaginate" : false,
		"bInfo": false,
		"aoColumns" : [ {
			"sTitle" : "Truck ID"
		}, {
			"sTitle" : "Truck Temperature"
		}, {
			"sTitle" : "Route Start"
		}, {
			"sTitle" : "Route Destination"
		}, {
			"sTitle" : "Order Status",
			"sClass" : "center"
		} ]

	});
}