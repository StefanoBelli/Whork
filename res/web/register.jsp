<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<title>Sign Up - Whork</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="css/register.css">
		<link rel="stylesheet" type="text/css" href="css/whork.css">
		<link rel="preconnect" href="https://fonts.gstatic.com">
	 	<link href="https://fonts.googleapis.com/css2?family=Kameron&display=swap" rel="stylesheet">
	 	<link href="https://fonts.googleapis.com/css2?family=Bungee+Shade&display=swap" rel="stylesheet">
	 	<meta name="viewport" content="width=device-width, initial-scale=1">
		<script src="https://code.jquery.com/jquery-1.10.2.min.js"></script>
	    <link href="https://netdna.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
		<script src="https://netdna.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>		
	</head>
	
	<body>
<%
	String descError = (String) request.getSession().getAttribute("descriptive_error");
	
	if(descError != null) {
%>
	<div class="alert alert-danger" role="alert">
		<%=descError%>
	</div> 
<%
	}
	request.getSession().setAttribute("descriptive_error", null);
%>		
		<div class="container bootstrap snippets bootdeys">
			<div class="row">			 
			    <div class="col-md-4 col-sm-6 content-card">
			    	<form action="/reg_jobseeker.jsp" method="get">
				    	<button type="submit">				    	
				        <div class="card-big-shadow">
				            <div class="card card-just-text" data-background="color" data-color="blue" data-radius="none">
				                <div class="content">
				                    <h6 class="category">Sign Up</h6>
				                    <h4 class="title">Are you a job seeker?</h4>
				                    <p class="description">Job seeker is a person looking for a new job and he would want to get the opportunities of this website</p>			                	
				                </div>
				            </div> <!-- end card -->
				        </div>
				        </button>
			    	</form>				    	
			    </div>
			  
			   
				<div class="col-md-4 col-sm-6 content-card">
				    <form action="/reg_company.jsp" method="get">
			    		<button type="submit">
				        <div class="card-big-shadow">
				            <div class="card card-just-text" data-background="color" data-color="green" data-radius="none">
				                <div class="content">
				                    <h6 class="category">Sign Up</h6>
				                    <h4 class="title">Are you a company?</h4>
				                    <p class="description">If your purpose is looking for new people for your company, this is the right place.</p>
				                </div>
				            </div> <!-- end card -->
				        </div>
				        </button>
			    	</form>
				 </div>
				
			    
			    
				 <div class="col-md-4 col-sm-6 content-card">
				    <form action="/login.jsp" method="get">
			    		<button type="submit">
				        <div class="card-big-shadow">
				            <div class="card card-just-text" data-background="color" data-color="yellow" data-radius="none">
				                <div class="content">
				                    <h6 class="category">Sign In</h6>
				                    <h4 class="title">I have already an account</h4>
				                    <p class="description">Are you lost? If you have already an account on this site, click here.</p>
				                </div>
				            </div> <!-- end card -->
				        </div>
				        </button>
			    	</form>
				 </div>
					    
			</div>
		</div>
		
		<div class="right-container">
			<span class="whork"> W<span class="hred">h</span>ork</span>
			<div class="catchphrase" style="font-size:18pt">
				After all, work is still the best way to get life going.
				Gustave Flaubert
			</div>
		</div>
		
	</body>
	
</html>