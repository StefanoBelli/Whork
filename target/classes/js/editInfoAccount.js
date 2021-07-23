function editBio(bio) {
	var box = "";
	
	var editBioMethod = function (bio) {
		box = document.getElementById('editBioText');		
			
		var edit = "";
		edit = "<div class='col-sm-9 text-secondary'>";
		edit += "<input type='text' class='form-control' value='" + bio + "'>";
		edit += "</div>";
		
		if (box.innerHTML.length != edit.length) box.innerHTML = edit;
		else box.innerHTML = bio;
	};
	editBioMethod(bio);
	
	box = document.getElementById('editBioButton');
	if (box.style.display == 'inline') {
		box.style.display = 'none';
	} else {
		box.style.display = 'inline';
	}
	
	box = document.getElementById('editBioForm');
	if (box.value == "Edit") {
		box.value = "Cancel";
		box.innerHTML = "Cancel";
	} else {
		box.value = "Edit";
		box.innerHTML = "Edit";
	}
}

function edit() {
	var box = "";
	var box2 = "";
		
	box = document.getElementById('edit');
	box2 = document.getElementsByName('websiteForm');
	
	console.log(box2[0].value);
	
}

function editSocialAccount(website, twitter, facebook, instagram) {
	var box = "";	
	
	var editWebsite = function (website) {
		box = document.getElementById('website');		
			
		var edit = "";
		edit = "<div class='col-sm-9 text-secondary'>";
		edit += "<input type='text' name='websiteForm' class='form-control' value='" + website + "'>";
		edit += "</div>";
		
		if (box.innerHTML.length != edit.length) box.innerHTML = edit;
		else box.innerHTML = website;
	};
	editWebsite(website);
	
	var editTwitter = function (twitter) {
		box = document.getElementById('twitter');		
			
		var edit = "";
		edit = "<div class='col-sm-9 text-secondary'>";
		edit += "<input type='text' name='twitterForm' class='form-control' value='" + twitter + "'>";
		edit += "</div>";
		
		if (box.innerHTML.length != edit.length) box.innerHTML = edit;
		else box.innerHTML = twitter;
	};
	editTwitter(twitter);
	
	var editFacebook = function (facebook) {
		box = document.getElementById('facebook');		
			
		var edit = "";
		edit = "<div class='col-sm-9 text-secondary'>";
		edit += "<input type='text' name='facebookForm' class='form-control' value='" + facebook + "'>";
		edit += "</div>";
		
		if (box.innerHTML.length != edit.length) box.innerHTML = edit;
		else box.innerHTML = facebook;
	};
	editFacebook(facebook);
	
	var editInstagram = function (instagram) {
		box = document.getElementById('instagram');		
			
		var edit = "";
		edit = "<div class='col-sm-9 text-secondary'>";
		edit += "<input type='text' name='instagramForm' class='form-control' value='" + instagram + "'>";
		edit += "</div>";
		
		if (box.innerHTML.length != edit.length) box.innerHTML = edit;
		else box.innerHTML = instagram;
	};
	editInstagram(instagram);
	
	box = document.getElementById('editSocialAccountForm');
	if (box.style.display == 'inline') {
		box.style.display = 'none';
	} else {
		box.style.display = 'inline';
	}
	
	box = document.getElementById('editSocialAccountButton');
	box.style.color = "white";
	if (box.innerHTML == "Cancel") {
		box.innerHTML = "Edit";
	} else {
		box.innerHTML = "Cancel";
	}
}


function editInfoAccount(fullName, email, phone, cf, address) {
	var box = "";
	
	var editFullNameAccount = function (fullName) {
		box = document.getElementById('full name');
			
		var edit = "";
		edit = "<div class='col-sm-9 text-secondary'>";
		edit += "<input type='text' class='form-control' value='" + fullName + "'>";
		edit += "</div>";
		
		if (box.innerHTML.length != edit.length) box.innerHTML = edit;
		else box.innerHTML = fullName;
	};	
	editFullNameAccount(fullName);
	
	var editEmailAccount = function (email) {
		box = document.getElementById('email');	
		
		var edit = "";
		edit = "<div class='col-sm-9 text-secondary'>";
		edit += "<input type='text' class='form-control' value='" + email + "'>";
		edit += "</div>";
		
		if (box.innerHTML.length != edit.length) box.innerHTML = edit;
		else box.innerHTML = email;
	};	
	editEmailAccount(email);
	
	var editPhoneAccount = function (phone) {
		box = document.getElementById('phone');	
		
		var edit = "";
		edit = "<div class='col-sm-9 text-secondary'>";
		edit += "<input type='text' class='form-control' value='" + phone + "'>";
		edit += "</div>";
		
		if (box.innerHTML.length != edit.length) box.innerHTML = edit;
		else box.innerHTML = phone;
	};	
	editPhoneAccount(phone);
	
	var editFiscalCodeAccount = function (cf) {
		box = document.getElementById('fiscal code');	
		
		var edit = "";
		edit = "<div class='col-sm-9 text-secondary'>";
		edit += "<input type='text' class='form-control' value='" + cf + "'>";
		edit += "</div>";
		
		if (box.innerHTML.length != edit.length) box.innerHTML = edit;
		else box.innerHTML = cf;
	};	
	editFiscalCodeAccount(cf);
	
	var editAddressAccount = function (address) {
		box = document.getElementById('address');	
		
		var edit = "";
		edit = "<div class='col-sm-9 text-secondary'>";
		edit += "<input type='text' class='form-control' value='" + address + "'>";
		edit += "</div>";
		
		if (box.innerHTML.length != edit.length) box.innerHTML = edit;
		else box.innerHTML = address;
	};	
	editAddressAccount(address);	
	
	box = document.getElementById('editInfoButton');
	if (box.style.display == 'inline') {
		box.style.display = 'none';
	} else {
		box.style.display = 'inline';
	}
	
	box = document.getElementById('changePasswordAccountButton');
	if (box.style.display == 'inline') {
		box.style.display = 'none';
	} else {
		box.style.display = 'inline';
	}
	
	box = document.getElementById('editInfoAccountButton');
	if (box.value == "Edit") {
		box.value = "Cancel";
		box.innerHTML = "Cancel";
	} else {
		box.value = "Edit";
		box.innerHTML = "Edit";
	}
}

function changePasswordAccount(){    
    var box = document.getElementById('changePasswordForm');
    
    var oldPassword = "";
	var newPassword = "";
	var confirmNewPassoword = "";

	if (box.innerHTML.length == 0) { 	
		oldPassword = "<hr>";
		oldPassword += "<div class='row'>";
		oldPassword += "<div class='col-sm-3'>";
		oldPassword += "<h6 class='mb-0'>Old Password</h6>";
		oldPassword += "</div>";
		oldPassword += "<div class='col-sm-9 text-secondary'>";
		oldPassword += "<input type='text' class='form-control' value=''>";
		oldPassword += "</div>";
		oldPassword += "</div>";

		newPassword = "<hr>";
		newPassword += "<div class='row'>";
		newPassword += "<div class='col-sm-3'>";
		newPassword += "<h6 class='mb-0'>New Password</h6>";
		newPassword += "</div>";
		newPassword += "<div class='col-sm-9 text-secondary'>";
		newPassword += "<input type='text' class='form-control' value=''>";
		newPassword += "</div>";
		newPassword += "</div>";

		confirmNewPassoword = "<hr>";
		confirmNewPassoword += "<div class='row'>";
		confirmNewPassoword += "<div class='col-sm-3'>";
		confirmNewPassoword += "<h6 class='mb-0'>Confirm New Password</h6>";
		confirmNewPassoword += "</div>";
		confirmNewPassoword += "<div class='col-sm-9 text-secondary'>";
		confirmNewPassoword += "<input type='text' class='form-control' value=''>";
		confirmNewPassoword += "</div>";
		confirmNewPassoword += "</div>";
			

		box.innerHTML = oldPassword;
		box.innerHTML += newPassword;
		box.innerHTML += confirmNewPassoword;
	} else {
		box.innerHTML = "";	
	}
	
	box = document.getElementById('editInfoButton');
	if (box.style.display == "none") {
		box.style.display = "inline";
	} else {
		box.style.display = "none";
	}
	
	box = document.getElementById('editInfoAccountButton');
	if (box.style.display == "inline") {
		box.style.display = "none";
	} else {
		box.style.display = "inline";
	}
	
	box = document.getElementById('changePasswordAccountButton');
	if (box.value == "Change Password") {
		box.value = "Cancel";
		box.innerHTML = "Cancel";
	} else {
		box.value = "Change Password";
		box.innerHTML = "Change Password";
	}
		
}


