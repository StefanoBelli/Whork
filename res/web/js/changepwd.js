function check_passwd_match() {
	const field = document.getElementById("pwd").value;
	const field1 = document.getElementById("conf_pwd").value;
	document.getElementById("submit").disabled = field.value != field1.value;
}