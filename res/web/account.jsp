<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="logic.bean.UserBean" %>
<%@ page import="logic.util.ServletUtil" %>

<%
    UserBean userBean = ServletUtil.getUserForSession(request);
	String email = ServletUtil.getUserEmailForSession(request);
%>
<!DOCTYPE HTML>
<html xml:lang="en">
    <head>
    	<link rel="stylesheet" href="css/accountJobSeeker.css">
    	<title>profile with data and skills - Bootdey.com</title>
	    <meta name="viewport" content="width=device-width, initial-scale=1">
		<script src="https://code.jquery.com/jquery-1.10.2.min.js"></script>
	    <link href="https://cdn.jsdelivr.net/npm/bootstrap@4.4.1/dist/css/bootstrap.min.css" rel="stylesheet">
		<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.4.1/dist/js/bootstrap.bundle.min.js"></script>
    </head>
    
    <body>
	    <div class="container">
	    <div class="main-body">
	    
	          <!-- Breadcrumb -->
	          <nav aria-label="breadcrumb" class="main-breadcrumb">
	            <ol class="breadcrumb">
	              <li class="breadcrumb-item"><a href="index.jsp">Home</a></li>
	              <li class="breadcrumb-item active" aria-current="page">User Profile</li>
	            </ol>
	          </nav>
	          <!-- /Breadcrumb -->
	    
	          <div class="row gutters-sm">
	            <div class="col-md-4 mb-3">
	              <div class="card">
	                <div class="card-body">
	                  <div class="d-flex flex-column align-items-center text-center">
	                    <img src="https://bootdey.com/img/Content/avatar/avatar7.png" alt="Admin" class="rounded-circle" width="150">
	                    <div class="mt-3">
	                      <h4><%= userBean.getName() + " " + userBean.getSurname() %></h4>
	                      <p class="text-secondary mb-1"><%= userBean.getEmploymentStatus().getStatus() %></p>
	                      <p class="text-muted font-size-sm"><%= userBean.getComune().getNome() + " (" + userBean.getComune().getProvincia().getSigla() + "), " + userBean.getComune().getCap() %></p>
	                    </div>
	                  </div>
	                </div>
	              </div>
	              <div class="card mt-3">
	                <ul class="list-group list-group-flush">
	                  <li class="list-group-item d-flex justify-content-between align-items-center flex-wrap">
	                    <h6 class="mb-0"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-globe mr-2 icon-inline"><circle cx="12" cy="12" r="10"></circle><line x1="2" y1="12" x2="22" y2="12"></line><path d="M12 2a15.3 15.3 0 0 1 4 10 15.3 15.3 0 0 1-4 10 15.3 15.3 0 0 1-4-10 15.3 15.3 0 0 1 4-10z"></path></svg>Website</h6>
	                    <span class="text-secondary">https://bootdey.com</span>
	                  </li>	                 
	                  <li class="list-group-item d-flex justify-content-between align-items-center flex-wrap">
	                    <h6 class="mb-0"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-twitter mr-2 icon-inline text-info"><path d="M23 3a10.9 10.9 0 0 1-3.14 1.53 4.48 4.48 0 0 0-7.86 3v1A10.66 10.66 0 0 1 3 4s-4 9 5 13a11.64 11.64 0 0 1-7 2c9 5 20 0 20-11.5a4.5 4.5 0 0 0-.08-.83A7.72 7.72 0 0 0 23 3z"></path></svg>Twitter</h6>
	                    <span class="text-secondary">@bootdey</span>
	                  </li>
	                  <li class="list-group-item d-flex justify-content-between align-items-center flex-wrap">
	                    <h6 class="mb-0"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-instagram mr-2 icon-inline text-danger"><rect x="2" y="2" width="20" height="20" rx="5" ry="5"></rect><path d="M16 11.37A4 4 0 1 1 12.63 8 4 4 0 0 1 16 11.37z"></path><line x1="17.5" y1="6.5" x2="17.51" y2="6.5"></line></svg>Instagram</h6>
	                    <span class="text-secondary">bootdey</span>
	                  </li>
	                  <li class="list-group-item d-flex justify-content-between align-items-center flex-wrap">
	                    <h6 class="mb-0"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-facebook mr-2 icon-inline text-primary"><path d="M18 2h-3a5 5 0 0 0-5 5v3H7v4h3v8h4v-8h3l1-4h-4V7a1 1 0 0 1 1-1h3z"></path></svg>Facebook</h6>
	                    <span class="text-secondary">bootdey</span>
	                  </li>
	                </ul>
	              </div>
	            </div>
	            
	            <div class="col-md-8">
	              <div class="card mb-3">
	                <div class="card-body">
	                  <div class="row">
	                    <div class="col-sm-3">
	                      <h6 class="mb-0">Full Name</h6>
	                    </div>
	                    <div class="col-sm-9 text-secondary">
	                      <%= userBean.getName() + " " + userBean.getSurname() %>
	                    </div>
	                  </div>
	                  <hr>
	                  <div class="row">
	                    <div class="col-sm-3">
	                      <h6 class="mb-0">Email</h6>
	                    </div>
	                    <div class="col-sm-9 text-secondary">
	                      <%= email %>
	                    </div>
	                  </div>
	                  <hr>
	                  <div class="row">
	                    <div class="col-sm-3">
	                      <h6 class="mb-0">Phone</h6>
	                    </div>
	                    <div class="col-sm-9 text-secondary">
	                      <%= userBean.getPhoneNumber() %>
	                    </div>
	                  </div>
	                  <hr>
	                  <div class="row">
	                    <div class="col-sm-3">
	                      <h6 class="mb-0">Fiscal Code</h6>
	                    </div>
	                    <div class="col-sm-9 text-secondary">
	                      <%= userBean.getCf() %>
	                    </div>
	                  </div>
	                  <hr>
	                  <div class="row">
	                    <div class="col-sm-3">
	                      <h6 class="mb-0">Address</h6>
	                    </div>
	                    <div class="col-sm-9 text-secondary">
	                       <%= userBean.getHomeAddress() %>
	                    </div>
	                  </div>	                  
	                  <hr>
	                  <div class="row">
	                    <div class="col-sm-12">
	                      <a class="btn btn-info " target="__blank" href="https://www.bootdey.com/snippets/view/profile-edit-data-and-skills">Edit</a>
	                      <a class="btn btn-info " target="__blank" href="https://www.bootdey.com/snippets/view/profile-edit-data-and-skills">Change Password</a>
	                    </div>
	                  </div>
	                </div>
	              </div>
	
	            
	             <div class="col-md-20">
	              <div class="card mb-3">		          
	               <div class="card-body">
	              	<h6 class="mb-0">My Biography</h6>
	              	<small> 
<%
if(userBean.getBiography() == null) {
%>
	Insert here your bio.
<%
} else {
	userBean.getBiography();
}
%>
		            </small>                
		           </div>		              
		           <hr>
	               <div class="row">
	                <div class="col-sm-12">
	                  <a class="btn btn-info " target="__blank" href="https://www.bootdey.com/snippets/view/profile-edit-data-and-skills">Edit</a>
	                </div>
	               </div>
	              </div>		             
			     </div>

	            </div>
	          </div>
	
	        </div>
	    </div>	    
	    
    </body>    
</html>