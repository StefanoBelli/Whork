function check_jobseeker_fields() {
	const pwd = document.getElementById("pwd").value;
	const conf_pwd = document.getElementById("conf_pwd").value;
	const my_fc = document.getElementById("my_fc").value;
	const town = document.getElementById("town").value;
	const error_field = document.getElementById("error");

	var ok = true;
	
	error_field.innerHTML = "";
	
	if(pwd != conf_pwd) {
		ok = false;
		error_field.innerHTML = "Passwords are not matching<br/>";
	}
	
	if(!valid_fc(my_fc)) {
		ok = false;
		error_field.innerHTML += "Your fiscal code is not passing our validity test<br/>";
	}

	if(!found_town(town)) {
		ok = false;
		error_field.innerHTML += "Could not find your town! Use autocompletion to fullfill<br/>";
	}

	return ok;
}

function found_town(town) {
	for(let t of ittowns) {
		if(t == town) {
			return true;
		}
	}

	return false;
}

function refresh_maps() {
	var base_url = "https://www.google.com/maps/embed/v1/place?key=AIzaSyAp5hG3kGqNGj6Auxh4IhC0Y60hzgUyzKo&q=";
	var search_term = "Italy";
	var town = document.getElementById("town").value;
	var addr = document.getElementById("address").value;
	var maps_frame = document.getElementById("maps");
	var old_src = maps_frame.src;

	if(town !== null && town.trim()) {
		search_term = town;
		if(addr !== null && addr.trim()) {
			search_term += ", " + addr;
		}
	}

	var new_src = base_url + search_term;
	if(new_src !== old_src) {
		maps_frame.src = new_src;
	}
}