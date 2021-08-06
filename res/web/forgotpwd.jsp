<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE HTML>
<html xml:lang="en">

	<head>
		<title>Whork - Forgot my password</title>
		<link rel="stylesheet" href="css/forgotpwd.css">
		<link rel="stylesheet" type="text/css" href="css/whork.css">
		<link rel="preconnect" href="https://fonts.gstatic.com">
	 	<link href="https://fonts.googleapis.com/css2?family=Kameron&display=swap" rel="stylesheet">
	 	<link href="https://fonts.googleapis.com/css2?family=Bungee+Shade&display=swap" rel="stylesheet">
		<script src="https://code.jquery.com/jquery-1.10.2.min.js"></script>
	    <link href="https://cdn.jsdelivr.net/npm/bootstrap@4.1.1/dist/css/bootstrap.min.css" rel="stylesheet">
		<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.1.1/dist/js/bootstrap.bundle.min.js"></script>
	</head>

	<body style="background-color:#FA8072">
<%
		String email = (String) request.getAttribute("reqEmail");
		if(email != null) {
%>
		<div id="errmsg" class="alert alert-success" role="alert">
			<p name="pwd_reset">A password reset request has been made.
			If "<%=email%>" is a valid and registered email address, then you will find an
			email in your inbox (check spam) with instructions.</p>
		</div>
<%
		}
%>

	<div style="font-size:30pt; margin-left:150px; padding-top: 85px;">
 		<a href="index.jsp">
            <span class="whork"> W<span class="hred">h</span>ork</span>
        </a>
    </div>
	<div class="container h-100" style="padding-top:100px; margin-left:340px;">
    		<div class="row h-100">
				<div class="col-sm-10 col-md-8 col-lg-6 mx-auto d-table h-100">
					<div class="d-table-cell align-middle">

						<div class="text-center mt-4" style="font-family: URW Chancery L, cursive">
							<h1 class="h2"><marquee align="middle" behavior="alternate" direction="right"scrolldelay="20" style="font-size:35pt;">Reset password</marquee></h1>
						</div>

						<div class="card">
							<div class="card-body"><p class="lead" style="font-family: URW Chancery L, cursive; text-align:center; font-size:20pt;">
								Enter your email to reset your password.
							</p>
								<div class="m-sm-4">
									<form action="forgotPassword" method="post">
										<div class="form-group">
											<label for="email">Email</label>
											<input class="form-control form-control-lg" type="email" name="email" placeholder="Enter your email" required>
										</div>
										<div class="text-center mt-3">
											<button type="submit" class="btn btn-lg btn-primary">Reset password</button>
										</div>
									</form>
								</div>
							</div>
						</div>

					</div>
				</div>
			</div>
		</div>
	
	</body>
</html>