function fillRouteDropdown() {

	// TODO initially fill routeDropDown;

	var ddl = document.getElementById('routes');

	var theOption = new Option;

	// hard-coded routes
	theOption.text = "Koeln_Bonn";
	theOption.value = "Koeln_Bonn";
	ddl.options[0] = theOption;
	
	var theOption2 = new Option;
	
	theOption2.text = "Koeln_Dortmund";
	theOption2.value = "Koeln_Dortmund";
	ddl.options[1] = theOption2;

}

function routeDropDownOnChange(selectedRoute) {

	// TODO fill waypointDropDown according to routes

	alert(selectedRoute);

}

function updateCOSM() {
	// TODO when Go! button is pressed
	alert("Updating COSM")
}

function fillDropDown() {
	var ddl = document.getElementById('myDropdown');
	var theOption = new Option;
	var x;
	var i;

	for (i = 0; i < 999; i++) {
		x = i + 1;
		theOption.text = x;
		theOption.value = x;
		ddl.options[i] = theOption;
	}
}