// fill JQuery datatable
function fillTablesUIElement(transports) {
	for ( var j = 0; j < transports.length; j++) {
		$('#truckTable').dataTable().fnAddData([ transports[j].attributes ]);
	}

	// iterate over rows and change their color
	$('#truckTable tbody tr td').each(function() {

		if ($(this).html() == "critical") {

			$(this).css("background-color", "#FF0000");

		} else if ($(this).html() == "warning") {

			$(this).css("background-color", "#FFFF00")

		} else if ($(this).html() == "ok") {

			$(this).css("background-color", "#00CC00");

		}
	});
}

// empty JQuery datatable
function clearTablesUIElement() {
	$('#truckTable').dataTable().fnClearTable();
}

// initialize JQuery datatable component
function createTableComponent() {
	var uiElement = new UIElement("tableUI", "tableUIElement", null);

	window.uiElements.push(uiElement);

	$('#truckTable').dataTable({
		"bPaginate" : false,
		"bInfo" : false,
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