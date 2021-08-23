<!DOCTYPE html>
<html xml:lang="en">
	<head>
		<title>Password Change Outcome- Whork</title>
		<meta name="author" content="Stefano Belli">
		<meta name="author" content="Magliari Elio">
		<link rel="stylesheet" href="css/cpoutcome.css">
		<script src="js/common.js"></script>		
		<link href="https://fonts.googleapis.com/css?family=Nunito+Sans:400,400i,700,900&display=swap" rel="stylesheet">
		<script>
			redirect("login.jsp", 5000);
		</script>
	</head>

<%
	String outcomeStr = (String) request.getParameter("ok");
	boolean outcome = 
		outcomeStr != null && outcomeStr.equals("true");

	if(outcome) {
%>
	<body style="background-color: #90EE90">
		<div class="card">
	      <div style="border-radius:200px; height:200px; width:200px; background: #F8FAF5; margin:0 auto;">
	        <i class="checkmark">&#10003;</i>
	      </div>
	      <h1>Success</h1> 
	      <p>Your password has been changed.<br/>Now you can sign in.</p>
	      <div style="padding-top:40px">
		      <a href="login.jsp">
		      	<button type="submit" class="button button2">Go to Login page</button>
		      </a>
	      </div>
        </div>
      </body>
<%
	} else {
%>
	<body style="background-color: #FFA07A">
		<div class="card">
	      <div style="border-radius:200px; height:200px; width:200px; background: #ffcccb; margin:0 auto;">
	        <i style="color:#FF0000">&#10007;</i>
	      </div>
	      <h1 style="color:#FF0000">Error</h1> 
	      <p>Something went wrong.</p>
	      <div style="padding-top:40px">
		      <a href="login.jsp">
		      	<button type="submit" class="button button1">Go to Login page</button>
		      </a>
	      </div>
	     </div>
	 </body>
<%
	}
%> 
</html>