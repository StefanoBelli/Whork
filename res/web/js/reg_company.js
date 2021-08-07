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
		error_field.innerHTML = "<div class='alert alert-danger' role='alert'>Passwords are not matching</div>";
	}
	
	if(!valid_fc(my_fc)) {
		ok = false;
		error_field.innerHTML += "<div class='alert alert-danger' role='alert'>Your fiscal code is not passing our validity test</div>";
	}

	if(!valid_fc(company_fc)) {
		ok = false;
		error_field.innerHTML += "<div class='alert alert-danger' role='alert'>Company fiscal code is not passing our validity test</div>";
	}

	return ok;
}
