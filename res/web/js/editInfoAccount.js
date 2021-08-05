function equalPassword() {
	var box = document.getElementById('editInfoButton');
	var password = document.getElementsByName('newPasswordForm')[0].value;
	var confirm = document.getElementsByName('confirmPasswordForm')[0].value;
	
	if(password != confirm) box.disabled = true;
	else box.disabled = false;
}

function noEmptyInfo() {
	var box = document.getElementById('editInfoButton');
	var name = document.getElementsByName('nameForm')[0].value;
	var surname = document.getElementsByName('surnameForm')[0].value;
	var email = document.getElementsByName('emailForm')[0].value;
	var phone = document.getElementsByName('phoneForm')[0].value;
	var address = document.getElementsByName('addressForm')[0].value;	
	
	if(name.length == 0) box.disabled = true;
	else if(surname.length == 0) box.disabled = true;
	else if(email.length == 0) box.disabled = true;
	else if(phone.length == 0) box.disabled = true;
	else if(address.length == 0) box.disabled = true;
	else box.disabled = false;
}

function noEmptySocial() {
	var box = document.getElementById('editSocialAccountForm');
	var website = document.getElementsByName('websiteForm')[0].value;
	var twitter = document.getElementsByName('twitterForm')[0].value;
	var facebook = document.getElementsByName('facebookForm')[0].value;
	var instagram = document.getElementsByName('instagramForm')[0].value;
	
	if(website.length == 0) box.disabled = true;
	else if(twitter.length == 0) box.disabled = true;
	else if(facebook.length == 0) box.disabled = true;
	else if(instagram.length == 0) box.disabled = true;
	else box.disabled = false;
}

function changePicture() {
	var box = document.getElementById('changePictureForm');	
	
	if (box.style.display == 'none') 
		box.style.display = 'inline';
	else 
		box.style.display = 'none';
			
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
		edit += "<input type='text' name='websiteForm' class='form-control' onchange='noEmptySocial()' value='" + website + "' maxlength='45'>";
		edit += "</div>";
		
		if (box.innerHTML.length != edit.length) box.innerHTML = edit;
		else box.innerHTML = website;
	};
	editWebsite(website);
	
	var editTwitter = function (twitter) {
		box = document.getElementById('twitter');		
			
		var edit = "";
		edit = "<div class='col-sm-9 text-secondary'>";
		edit += "<input type='text' name='twitterForm' class='form-control' onchange='noEmptySocial()' value='" + twitter + "' maxlength='25'>";
		edit += "</div>";
		
		if (box.innerHTML.length != edit.length) box.innerHTML = edit;
		else box.innerHTML = twitter;
	};
	editTwitter(twitter);
	
	var editFacebook = function (facebook) {
		box = document.getElementById('facebook');		
			
		var edit = "";
		edit = "<div class='col-sm-9 text-secondary'>";
		edit += "<input type='text' name='facebookForm' class='form-control' onchange='noEmptySocial()' value='" + facebook + "' maxlength='25'>";
		edit += "</div>";
		
		if (box.innerHTML.length != edit.length) box.innerHTML = edit;
		else box.innerHTML = facebook;
	};
	editFacebook(facebook);
	
	var editInstagram = function (instagram) {
		box = document.getElementById('instagram');		
			
		var edit = "";
		edit = "<div class='col-sm-9 text-secondary'>";
		edit += "<input type='text' name='instagramForm' class='form-control' onchange='noEmptySocial()' value='" + instagram + "' maxlength='25'>";
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
		edit += "<input type='text' name='nameForm' class='form-control' onchange='noEmptyInfo()' value='" + name + "' maxlength='45'>";
		edit += "</div>";
		
		if (box.innerHTML.length != edit.length) box.innerHTML = edit;
		else box.innerHTML = name;		
						
	};	
	editNameAccount(name);
	
	var editSurnameAccount = function (surname) {
		box = document.getElementById('surname');
			
		edit = "";
		edit = "<div class='col-sm-9 text-secondary'>";
		edit += "<input type='text' name='surnameForm' class='form-control' onchange='noEmptyInfo()' value='" + surname + "' maxlength='45'>";
		edit += "</div>";
		
		if (box.innerHTML.length != edit.length) box.innerHTML = edit;
		else box.innerHTML = surname;		
						
	};	
	editSurnameAccount(surname);
	
	var editEmailAccount = function (email) {
		box = document.getElementById('email');	
		
		edit = "";
		edit = "<div class='col-sm-9 text-secondary'>";
		edit += "<input type='text' name='emailForm' class='form-control' onchange='noEmptyInfo()' value='" + email + "' maxlength='255'>";
		edit += "</div>";
		
		if (box.innerHTML.length != edit.length) box.innerHTML = edit;
		else box.innerHTML = email;
	};	
	editEmailAccount(email);
	
	var editPhoneAccount = function (phone) {
		box = document.getElementById('phone');	
		
		edit = "";
		edit = "<div class='col-sm-9 text-secondary'>";
		edit += "<input type='text' name='phoneForm' class='form-control' onchange='noEmptyInfo()' value='" + phone + "' maxlength='10'>";
		edit += "</div>";
		
		if (box.innerHTML.length != edit.length) box.innerHTML = edit;
		else box.innerHTML = phone;
	};	
	editPhoneAccount(phone);	
	
	var editAddressAccount = function (address) {
		box = document.getElementById('address');	
		
		edit = "";
		edit = "<div class='col-sm-9 text-secondary'>";
		edit += "<input type='text' name='addressForm' class='form-control' onchange='noEmptyInfo()' value='" + address + "' maxlength='45'>";
		edit += "</div>";
		
		if (box.innerHTML.length != edit.length) box.innerHTML = edit;
		else box.innerHTML = address;
	};	
	editAddressAccount(address);	
	
	box = document.getElementById('editInfoButton');
	if (box.style.display == 'inline') {
		box.value = null;
		box.style.display = 'none';
	} else {
		box.value = 'editInfo';
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
		oldPassword += "<input type='password' name='oldPasswordForm' class='form-control' value='' maxlength='70'>";
		oldPassword += "</div>";
		oldPassword += "</div>";

		newPassword = "<hr>";
		newPassword += "<div class='row'>";
		newPassword += "<div class='col-sm-3'>";
		newPassword += "<h6 class='mb-0'>New Password</h6>";
		newPassword += "</div>";
		newPassword += "<div class='col-sm-9 text-secondary'>";
		newPassword += "<input type='password' name='newPasswordForm' class='form-control' onchange='equalPassword()' value='' maxlength='70'>";
		newPassword += "</div>";
		newPassword += "</div>";

		confirmNewPassoword = "<hr>";
		confirmNewPassoword += "<div class='row'>";
		confirmNewPassoword += "<div class='col-sm-3'>";
		confirmNewPassoword += "<h6 class='mb-0'>Confirm New Password</h6>";
		confirmNewPassoword += "</div>";
		confirmNewPassoword += "<div class='col-sm-9 text-secondary'>";
		confirmNewPassoword += "<input type='password' name='confirmPasswordForm' onchange='equalPassword()' class='form-control' value='' maxlength='70'>";
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
		box.value = 'editPassword';
		box.style.display = "inline";
	} else {
		box.value = null;
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


