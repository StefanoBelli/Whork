<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="logic.util.Util" %>
<!DOCTYPE HTML>
<html xml:lang="en">
	<head>
		<title>Sign In - Whork</title>
		<meta name="author" content="Stefano Belli">
		<meta name="author" content="Magliari Elio">
		<link rel="stylesheet" href="css/login.css">
		<link rel="stylesheet" type="text/css" href="css/whork.css">
		<link rel="preconnect" href="https://fonts.gstatic.com">
	 	<link href="https://fonts.googleapis.com/css2?family=Kameron&display=swap" rel="stylesheet">
	 	<link href="https://fonts.googleapis.com/css2?family=Bungee+Shade&display=swap" rel="stylesheet">
		<link rel="preconnect" href="https://fonts.gstatic.com">
		<link href="https://fonts.googleapis.com/css2?family=Ubuntu:wght@300&display=swap" rel="stylesheet">
		<script src="https://code.jquery.com/jquery-1.10.2.min.js"></script>
	    <link href="https://cdn.jsdelivr.net/npm/bootstrap@4.1.1/dist/css/bootstrap.min.css" rel="stylesheet">
		<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.1.1/dist/js/bootstrap.bundle.min.js"></script>
	</head>

	<body lang="en" style="background-color: lightblue">

<%
		if(request.getAttribute("showMustLoginInfo") != null) {
%>
		<div id="errmsg" class="alert alert-danger" role="alert">
			<p id="mustlogin">You must be logged in to do that</p>
		</div>
<%
			request.setAttribute("showMustLoginInfo", null);
		}
%>		

<div id="errors">
<%
		String errorMessage = (String) request.getAttribute("errorMessage");

		if(errorMessage != null) {
%>
		<div id="errmsg" class="alert alert-danger" role="alert">
			<%=errorMessage%>
		</div>
<%
			request.setAttribute("errorMessage", null);
		}
%>
</div>
		<div style="font-size:30pt; margin-left:150px; padding-top: 40px;">
	 		<a href="index.jsp">
	                <span class="whork"> W<span class="hred">h</span>ork</span>
	        </a>
	    </div>
		<div class="container h-100" id="loginform" style="padding-top:70px; margin-left:340px;">
    		<div class="row h-100">
				<div class="col-sm-10 col-md-8 col-lg-6 mx-auto d-table h-100">
					<div class="d-table-cell align-middle">						

						<div class="card">						
							<div class="text-center mt-4">							
								<p class="lead" style="font-family: URW Chancery L, cursive; font-size:30pt; font-weight:550;">Sign in</p>
							</div>						
							<div class="card-body">
							
								<div class="m-sm-4">
									<div class="text-center" style="margin-top:-40px">
										<img src="<%=Util.InstanceConfig.getString(Util.InstanceConfig.KEY_CTX_DFL_ROOT) + "/" + "avatar5.png"%>" class="img-fluid rounded-circle" width="132" height="132">
									</div>
									<form id="loginf" action="validateLogin" method="post">									
									<div id="credInput">
										<div class="form-group">
											<label for="email">Email</label>
											<input class="form-control form-control-lg" type="email" name="email" placeholder="Enter your email" required>
										</div>
										<div class="form-group">
											<label for="passwd">Password</label>
											<input class="form-control form-control-lg" type="password" name="passwd" placeholder="Enter your password" required>
										<%
											if(request.getAttribute("showPasswordRecoveryButton") != null) {
										%>
											<small>
												<a href="forgotpwd.jsp">Forgot password?</a>
											</small>
										<%
													}
										%>								        
										</div>										
										<div>
											<div class="custom-control custom-checkbox align-items-center">
												<input type="checkbox" value="remember-me" name="stayLoggedIn" checked="true">
												<label for="stayLoggedIn">Remember me next time</label>
											</div>
										</div>
										<div class="text-center mt-3">											
											<button type="submit" class="btn btn-lg btn-primary">Sign in</button>
											<a href="register.jsp" type="submit" class="btn btn-lg btn-primary">I don't have an account</a>
										</div>
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