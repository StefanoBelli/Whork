<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE HTML>
<html xml:lang="en">
	<head>
		<title>Reset Password - Whork</title>
		<link rel="stylesheet" href="css/changepwd.css">
		<script src="js/changepwd.js"></script>
		<script src="https://code.jquery.com/jquery-1.10.2.min.js"></script>
	    <link href="https://cdn.jsdelivr.net/npm/bootstrap@4.1.1/dist/css/bootstrap.min.css" rel="stylesheet">
		<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.1.1/dist/js/bootstrap.bundle.min.js"></script>
	</head>
	
	<body>
<%
	String token = (String) request.getParameter("token");
	if(token != null) {
%>
	<div class="card login-form">
	<div class="card-body">
		<h3 class="card-title text-center">Change password</h3>
		<div class="card-text">				
			<form action="changePassword" method="post">
				<div class="form-group">
					<label for="pwdFirst" style="font-size:12pt">Your new password</label>
					<input type="password" id="pwd" name="pwdFirst" onchange='pwdm();' class="form-control form-control-sm" required>
				</div>
				<div class="form-group">
					<label for="pwdSecond" style="font-size:12pt">Repeat password</label>
					<input type="password" class="form-control form-control-sm" id="conf_pwd" onchange='pwdm();' required>
				</div>
				<input type="hidden" name="token" value="<%=token%>">
				<button id="submit" type="submit" class="btn btn-primary btn-block submit-btn" disabled>Confirm</button>
			</form>
		</div>
	</div>
	</div>
<%
	} else {
%>
		<div class="container">
		    <div class="row">
		        <div class="col-md-12">
		            <div class="error-template">
		                <h1>
		                    Oops!</h1>
		                <h2>
		                    Token Not Found</h2>
		                <div class="error-details">
		                    Sorry, an error has occured, No token supplied!
		                </div>
		                <div class="error-actions">
		                    <a href="index.jsp" class="btn btn-primary btn-lg"><span class="glyphicon glyphicon-home"></span>
		                        Take Me Home </a>
		                </div>
		            </div>
		        </div>
		    </div>
		</div>
<%
	}
%>
	</body>

</html>