function addRecruiter(){    
    var box = document.getElementById('addRecruiterForm');
    
    var name = "";
	var surname = "";
	var email = "";
	var password = "";
	var fiscalCode = "";
	var phoneNumber = "";
	var photo = "";

	if (box.innerHTML.length == 0) { 	
		name = "<hr>";
		name += "<div class='row'>";
		name += "<div class='col-sm-3'>";
		name += "<h6 class='mb-0'>Name</h6>";
		name += "</div>";
		name += "<div class='col-sm-8 text-secondary'>";
		name += "<input type='text' name='nameForm' class='form-control' value='' maxlength='45'>";
		name += "</div>";
		name += "</div>";

		surname = "<hr>";
		surname += "<div class='row'>";
		surname += "<div class='col-sm-3'>";
		surname += "<h6 class='mb-0'>Surname</h6>";
		surname += "</div>";
		surname += "<div class='col-sm-8 text-secondary'>";
		surname += "<input type='text' name='surnameForm' class='form-control' value='' maxlength='45'>";
		surname += "</div>";
		surname += "</div>";
		
		email = "<hr>";
		email += "<div class='row'>";
		email += "<div class='col-sm-3'>";
		email += "<h6 class='mb-0'>Email</h6>";
		email += "</div>";
		email += "<div class='col-sm-8 text-secondary'>";
		email += "<input type='text' name='emailForm' class='form-control' value='' maxlength='255'>";
		email += "</div>";
		email += "</div>";

		password = "<hr>";
		password += "<div class='row'>";
		password += "<div class='col-sm-3'>";
		password += "<h6 class='mb-0'>Password</h6>";
		password += "</div>";
		password += "<div class='col-sm-8 text-secondary'>";
		password += "<input type='password' name='passwordForm' class='form-control' value='' maxlength='70'>";
		password += "</div>";
		password += "</div>";
				
		fiscalCode = "<hr>";
		fiscalCode += "<div class='row'>";
		fiscalCode += "<div class='col-sm-3'>";
		fiscalCode += "<h6 class='mb-0'>Fiscal Code</h6>";
		fiscalCode += "</div>";
		fiscalCode += "<div class='col-sm-8 text-secondary'>";
		fiscalCode += "<input type='text' name='fiscalCodeForm' class='form-control' value='' maxlength='16'>";
		fiscalCode += "</div>";
		fiscalCode += "</div>";
		
		phoneNumber = "<hr>";
		phoneNumber += "<div class='row'>";
		phoneNumber += "<div class='col-sm-3'>";
		phoneNumber += "<h6 class='mb-0'>Phone Number</h6>";
		phoneNumber += "</div>";
		phoneNumber += "<div class='col-sm-8 text-secondary'>";
		phoneNumber += "<input type='text' name='phoneNumberForm' class='form-control' value='' maxlength='10'>";
		phoneNumber += "</div>";
		phoneNumber += "</div>";
		
		photo = "<hr>";
		photo += "<div class='row'>";
		photo += "<div class='col-sm-3'>";
		photo += "<h6 class='mb-0'>Picture</h6>";
		photo += "</div>";
		photo += "<div class='col-sm-8 text-secondary'>";
		photo += "<input type='file' name='photoForm' accept='image/png, image/jpeg'>";
		photo += "</div>";
		photo += "</div>";			

		box.innerHTML = name;
		box.innerHTML += surname;
		box.innerHTML += email;
		box.innerHTML += password;
		box.innerHTML += fiscalCode;
		box.innerHTML += phoneNumber;
		box.innerHTML += photo;
		box.innerHTML += "<hr>";
		box.innerHTML += "<div class='row'>";
		
		document.getElementById('addRecruiterButtonSubmit').style.display = 'inline';
		document.getElementById('addRecruiterButtonCancel').style.display = 'inline';
		
		box.innerHTML += "</div>";
		
	} else {
		box.innerHTML = "";
		document.getElementById('addRecruiterButtonSubmit').style.display = 'none';
		document.getElementById('addRecruiterButtonCancel').style.display = 'none';	
	}	
		
}
