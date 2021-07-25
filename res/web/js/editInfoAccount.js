function equalPassword() {
	var box = document.getElementById('editInfoButton');
	var password = document.getElementsByName('newPasswordForm')[0].value;
	var confirm = document.getElementsByName('confirmPasswordForm')[0].value;
	
	if(password != confirm) box.disabled = true;
	else box.disabled = false;
}

function editBio(bio) {
	var box = "";
	
	var editBioMethod = function (bio) {
		box = document.getElementById('editBioText');		
			
		var edit = "";
		edit = "<div class='col-sm-9 text-secondary'>";
		edit += "<input type='text' name='editBioTextForm' class='form-control' value='" + bio + "'>";
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
	box.style.color = "white";
	if (box.innerHTML == "Cancel") {
		box.innerHTML = "Edit";
	} else {
		box.innerHTML = "Cancel";
	}
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


function editInfoAccount(name, surname, email, phone, cf, address) {
	var box = "";
	var edit = "";
	var box2 = "";
	
	var editNameAccount = function (name) {
		box = document.getElementById('name');
			
		edit = "";
		edit = "<div class='col-sm-9 text-secondary'>";
		edit += "<input type='text' name='nameForm' class='form-control' value='" + name + "'>";
		edit += "</div>";
		
		if (box.innerHTML.length != edit.length) box.innerHTML = edit;
		else box.innerHTML = name;		
						
	};	
	editNameAccount(name);
	
	var editSurnameAccount = function (surname) {
		box = document.getElementById('surname');
			
		edit = "";
		edit = "<div class='col-sm-9 text-secondary'>";
		edit += "<input type='text' name='surnameForm' class='form-control' value='" + surname + "'>";
		edit += "</div>";
		
		if (box.innerHTML.length != edit.length) box.innerHTML = edit;
		else box.innerHTML = surname;		
						
	};	
	editSurnameAccount(surname);
	
	var editEmailAccount = function (email) {
		box = document.getElementById('email');	
		
		edit = "";
		edit = "<div class='col-sm-9 text-secondary'>";
		edit += "<input type='text' name='emailForm' class='form-control' value='" + email + "'>";
		edit += "</div>";
		
		if (box.innerHTML.length != edit.length) box.innerHTML = edit;
		else box.innerHTML = email;
	};	
	editEmailAccount(email);
	
	var editPhoneAccount = function (phone) {
		box = document.getElementById('phone');	
		
		edit = "";
		edit = "<div class='col-sm-9 text-secondary'>";
		edit += "<input type='text' name='phoneForm' class='form-control' value='" + phone + "'>";
		edit += "</div>";
		
		if (box.innerHTML.length != edit.length) box.innerHTML = edit;
		else box.innerHTML = phone;
	};	
	editPhoneAccount(phone);	
	
	var editAddressAccount = function (address) {
		box = document.getElementById('address');	
		
		edit = "";
		edit = "<div class='col-sm-9 text-secondary'>";
		edit += "<input type='text' name='addressForm' class='form-control' value='" + address + "'>";
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
	box.style.color = "white";
	if (box.style.display == 'inline') {
		box.style.display = 'none';
	} else {
		box.style.display = 'inline';
	}
	
	box = document.getElementById('editInfoAccountButton');
	box.style.color = "white";
	if (box.innerHTML == "Cancel") {
		box.innerHTML = "Edit";
	} else {
		box.innerHTML = "Cancel";
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
		oldPassword += "<input type='text' name='oldPasswordForm' class='form-control' value=''>";
		oldPassword += "</div>";
		oldPassword += "</div>";

		newPassword = "<hr>";
		newPassword += "<div class='row'>";
		newPassword += "<div class='col-sm-3'>";
		newPassword += "<h6 class='mb-0'>New Password</h6>";
		newPassword += "</div>";
		newPassword += "<div class='col-sm-9 text-secondary'>";
		newPassword += "<input type='text' name='newPasswordForm' class='form-control' onchange='equalPassword()' value=''>";
		newPassword += "</div>";
		newPassword += "</div>";

		confirmNewPassoword = "<hr>";
		confirmNewPassoword += "<div class='row'>";
		confirmNewPassoword += "<div class='col-sm-3'>";
		confirmNewPassoword += "<h6 class='mb-0'>Confirm New Password</h6>";
		confirmNewPassoword += "</div>";
		confirmNewPassoword += "<div class='col-sm-9 text-secondary'>";
		confirmNewPassoword += "<input type='text' name='confirmPasswordForm' onchange='equalPassword()' class='form-control' value=''>";
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
	box.style.color = "white";
	if (box.style.display == "inline") {
		box.style.display = "none";
	} else {
		box.style.display = "inline";
	}
	
	box = document.getElementById('changePasswordAccountButton');
	box.style.color = "white";
	if (box.innerHTML == "Cancel") {		
		box.innerHTML = "Change Password";
	} else {
		box.innerHTML = "Cancel";
	}
		
}


