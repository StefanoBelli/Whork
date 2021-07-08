function valid_fc(value) {
	var pattern = /^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$/;
	return value.search(pattern) != -1;
}

function redirect(page, delay) {
	window.setTimeout(function () {
		window.location.href = "/" + page;
	}, delay);
}

function reg_toggle_submit() {
	document.getElementById("submit").disabled = 
		!document.getElementById("ppolicy").checked
}