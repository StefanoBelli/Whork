function pwd_check_and_disable(field_name, field1_name, submit_name) {
	const field = document.getElementById(field_name).value;
	const field1 = document.getElementById(field1_name).value;
	document.getElementById(submit_name).disabled = field.value != field1.value;
}

function pwdm() {
	pwd_check_and_disable("pwd", "conf_pwd", "submit");
}
