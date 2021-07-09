function check_company_fields() {
	const pwd = document.getElementById("pwd").value;
	const conf_pwd = document.getElementById("conf_pwd").value;
	const my_fc = document.getElementById("my_fc").value;
	const company_fc = document.getElementById("company_fc").value;
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

	if(!valid_fc(company_fc)) {
		ok = false;
		error_field.innerHTML += "Company fiscal code is not passing our validity test<br/>";
	}

	return ok;
}