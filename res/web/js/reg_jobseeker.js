import { 
	valid_fc 
} from './common.js'

function check_jobseeker_fields(towns) {
	const pwd = document.getElementById("pwd").value;
	const conf_pwd = document.getElementById("conf_pwd").value;
	const my_fc = docuemnt.getElementById("my_fc").value;
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

	var found = false;

	for(let t of towns) {
		if(t == town) {
			found = true;
			break;
		}
	}

	if(!found) {
		ok = false;
		error_field.innerHTML += "Could not find your town! Use autocompletion to fullfill<br/>";
	}

	return ok;
}