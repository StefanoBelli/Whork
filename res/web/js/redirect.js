function redirect(page, delay) {
	window.setTimeout(function(){
        window.location.href = "/" + page;
    }, delay);
}