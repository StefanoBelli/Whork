function check_fiscal_code_validity(field_cf_name) {
	var pattern = /^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$/;
	var cf = document.getElementById("cf");

	if (cf.value.search(pattern) == -1) {
		CodiceFiscale.focus();
		return false;
	}

	return true;
}

function check_company_fields() {
	const my_fc_ok = check_fiscal_code_validity("my_fc");
	const company_fc_ok = check_fiscal_code_validity("company_fc");

	return my_fc_ok && company_fc_ok;
}

function toggle_submit() {
	document.getElementById("submit").disabled = !document.getElementById("ppolicy").checked
}