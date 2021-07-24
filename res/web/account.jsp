<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="logic.bean.UserBean" %>
<%@ page import="logic.util.ServletUtil" %>
<%@ page import="logic.util.Util" %>
<%@ page import="java.util.List" %>
<%@ page import="logic.bean.CandidatureBean" %>
<%@ page import="logic.controller.AccountController" %>
<%@ page import="logic.controller.CandidatureController" %>

<%
    UserBean userBean = ServletUtil.getUserForSession(request);
	String email = ServletUtil.getUserEmailForSession(request);
	String name = userBean.getName();
	String surname = userBean.getSurname();
	String fullName = name.concat(" ").concat(surname);
	String phone = userBean.getPhoneNumber();
	String cf = userBean.getCf();
	String address = userBean.getHomeAddress();	
	
	String website = userBean.getWebsite() == null ? "https://whork.it" : userBean.getWebsite();
	String twitter = userBean.getTwitter() == null ? "whork" : userBean.getTwitter();
	String facebook = userBean.getFacebook() == null ? "whork" : userBean.getFacebook();
	String instagram = userBean.getInstagram() == null ? "whork" : userBean.getInstagram();	
	
	String bio = userBean.getBiography(); 
%>
<!DOCTYPE HTML>
<html xml:lang="en">
    <head>
    	<link rel="stylesheet" href="css/account.css">
    	<title>User Profile - Whork</title>
	    <meta name="viewport" content="width=device-width, initial-scale=1">
		<script src="https://code.jquery.com/jquery-1.10.2.min.js"></script>
	    <link href="https://cdn.jsdelivr.net/npm/bootstrap@4.4.1/dist/css/bootstrap.min.css" rel="stylesheet">
		<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.4.1/dist/js/bootstrap.bundle.min.js"></script>
		<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">
		<script src="js/editInfoAccount.js"></script>
    </head>
    
    
    <body>   
     <form action="accountServlet" method="post">  
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
	          
<%
	String descError = (String) request.getAttribute("descriptive_error");
	if(descError != null) {
%>
		<h3><%=descError%></h3>
<%
	}
%>   
	          <div class="row gutters-sm">
	            <div class="col-md-4 mb-3">
	              <div class="card">
	                <div class="card-body">
	                  <div class="d-flex flex-column align-items-center text-center">
<%
if(userBean.getPhoto() == null) {
%>
    <img src="<%= Util.InstanceConfig.getString(Util.InstanceConfig.KEY_CTX_DFL_ROOT) + "/" + "avatar.png" %>" alt="Admin" class="rounded-circle" width="150">
<%
} else {
%>
    <img src="<%= Util.InstanceConfig.getString(Util.InstanceConfig.KEY_CTX_USR_DATA) + "/" + userBean.getPhoto() %>" alt="Admin" class="rounded-circle" width="150">
<%
}
%>
                    <div class="mt-3">
                      <h4><%= fullName %></h4>
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
                    <span id="website" class="text-secondary"><%= website %></span>
                  </li>	                 
                  <li class="list-group-item d-flex justify-content-between align-items-center flex-wrap">
                    <h6 class="mb-0"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-twitter mr-2 icon-inline text-info"><path d="M23 3a10.9 10.9 0 0 1-3.14 1.53 4.48 4.48 0 0 0-7.86 3v1A10.66 10.66 0 0 1 3 4s-4 9 5 13a11.64 11.64 0 0 1-7 2c9 5 20 0 20-11.5a4.5 4.5 0 0 0-.08-.83A7.72 7.72 0 0 0 23 3z"></path></svg>Twitter</h6>
                    <span id="twitter" class="text-secondary"><%= '@' + twitter %></span>
                  </li>                  
                  <li class="list-group-item d-flex justify-content-between align-items-center flex-wrap">
                    <h6 class="mb-0"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-facebook mr-2 icon-inline text-primary"><path d="M18 2h-3a5 5 0 0 0-5 5v3H7v4h3v8h4v-8h3l1-4h-4V7a1 1 0 0 1 1-1h3z"></path></svg>Facebook</h6>
                    <span id="facebook" class="text-secondary"><%= '@' + facebook %></span>                  	
                  </li>
                  <li class="list-group-item d-flex justify-content-between align-items-center flex-wrap">
                    <h6 class="mb-0"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-instagram mr-2 icon-inline text-danger"><rect x="2" y="2" width="20" height="20" rx="5" ry="5"></rect><path d="M16 11.37A4 4 0 1 1 12.63 8 4 4 0 0 1 16 11.37z"></path><line x1="17.5" y1="6.5" x2="17.51" y2="6.5"></line></svg>Instagram</h6>
                    <span id="instagram" class="text-secondary"><%= '@' + instagram %></span>                    
                  </li>                  
                  <li class="list-group-item d-flex justify-content-between align-items-center flex-wrap">
                   <hr>
                   <div class="row">
                     <div class="col-sm-12">
                     	<button id="editSocialAccountForm" type="submit" class="btn btn-info " style="display:none" >Submit</button>
                        <a id="editSocialAccountButton" class="btn btn-info " onclick='editSocialAccount("<%=website%>", "<%=twitter%>", "<%=facebook%>", "<%=instagram%>")' ><font color="white">Edit</font></a>
                     </div>
                   </div>
                  </li>
                </ul>
              </div>             
            </div>
           
            <div class="col-md-8">
              <div class="card mb-3">               
                <div class="card-body">
                  <div class="row">
                    <div class="col-sm-3">
                      <h6 class="mb-0">Name</h6>
                    </div>
                    <div id="name" class="col-sm-9 text-secondary">
                      <%= name %>
                    </div>
                  </div>
                  <hr>
                  <div class="row">
                    <div class="col-sm-3">
                      <h6 class="mb-0">Surname</h6>
                    </div>
                    <div id="surname" class="col-sm-9 text-secondary">
                      <%= surname %>
                    </div>
                  </div>                  
                  <hr>
                  <div class="row">
                    <div class="col-sm-3">
                      <h6 class="mb-0">Email</h6>
                    </div>
                    <div id="email" class="col-sm-9 text-secondary">
                      <%= email %>
                    </div>
                  </div>
                  <hr>
                  <div class="row">
                    <div class="col-sm-3">
                      <h6 class="mb-0">Phone</h6>
                    </div>
                    <div id="phone" class="col-sm-9 text-secondary">
                      <%= phone %>
                    </div>
                  </div>
                  <hr>
                  <div class="row">
                    <div class="col-sm-3">
                      <h6 class="mb-0">Fiscal Code</h6>
                    </div>
                    <div id="fiscal code" class="col-sm-9 text-secondary">
                      <%= cf %>
                    </div>
                  </div>
                  <hr>
                  <div class="row">
                    <div class="col-sm-3">
                      <h6 class="mb-0">Address</h6>
                    </div>
                    <div id="address" class="col-sm-9 text-secondary">
                       <%= address %>
                    </div>
                  </div>	
                  <span id="editInfoAccountForm"></span>
                  <span id="changePasswordForm"></span>
                  <hr>
                  <div class="row">
                    <div class="col-sm-12">
                      <button id="editInfoButton" type="submit" class="btn btn-info " style="display:none" >Submit</button>
                      <a id="editInfoAccountButton" class="btn btn-info " style="display:inline" onclick='editInfoAccount("<%=name%>", "<%=surname%>", "<%=email%>", "<%=phone%>", "<%=cf%>", "<%=address%>")' ><font color="white">Edit</font></a>
                      <a id="changePasswordAccountButton" class="btn btn-info " style="display:inline" onclick="changePasswordAccount()" ><font color="white">Change Password</font></a>                      
                    </div>
                  </div>
                </div>               
              </div>	            
	         
	          
	         <div class="col-md-20">
	          <div class="card mb-3">		          
	           <div class="card-body">
	            <div class="row">
                 <div class="col-sm-3">
                  <h6 class="mb-0">My Biography</h6>
                 </div>
                 <div id="editBioText" class="col-sm-9 text-secondary">
<%
	if(userBean.getBiography() == null) {
		bio = "Insert here your bio";
	} else {
		bio = userBean.getBiography();
	}
%>
				<%= bio %>
                 </div>
                 </div>
                 <hr>
                  <div class="row" style="text-align:center">
	               <div class="col-sm-12">
	                  <button id="editBioButton" class="btn btn-info " style="display:none" >Submit</button>
                      <a id="editBioForm" class="btn btn-info " style="display:inline" onclick='editBio("<%= bio %>")' ><font color="white">Edit</font></a>
	                </div>
	               </div>
	              </div>
               </div>	
	          </div>
	         </div>	        
	       </div>
	    
<!--  -->
<%
	List<CandidatureBean> listCandidatureBean = AccountController.getSeekerCandidature(userBean);
	
	if(listCandidatureBean == null || listCandidatureBean.size() == 0) {
%>
	   <div class="center">
		<div class="col-md-7">
         <div class="card mb-3">
          <div class="card-body">
           <div class="row" style="text-align:right">                    
             <div class="col-sm-9 text-secondary">
                There is no application here.
             </div>
           </div>
          </div>
         </div>
        </div>
       </div> 	
<%
	} else {
%>  	  
			<div class="container">
			 <div class="row">
			  <div class="col-lg-12">
			   <div class="main-box clearfix">
			    <div class="table-responsive">
				 <table class="table user-list">
					<thead>
						<tr>
							<th><span>Company</span></th>
							<th><span>Application Date</span></th>
							<th><span>Type of Contract</span></th>
							<th><span>Job Position</span></th>
							<th><span>Recruiter Email</span></th>
							<th>&nbsp;</th>
						</tr>
					</thead>
					<tbody>
<%
	for(int i=0; i<listCandidatureBean.size(); i++) {
%>
		<tr>
			<td>
				<img src="<%= Util.InstanceConfig.getString(Util.InstanceConfig.KEY_CTX_DFL_ROOT) + "/" + "avatar1.png" %>" alt="Company Admin" width="150">
				<a href="#" class="user-link"><%= listCandidatureBean.get(i).getOffer().getCompany().getSocialReason() %></a>				
			</td>
			<td>
				<%= listCandidatureBean.get(i).getCandidatureDate().toString() %>
			</td>
			<td>
				<%= listCandidatureBean.get(i).getOffer().getTypeOfContract().getContract() %>
			</td>
			<td>
				<%= listCandidatureBean.get(i).getOffer().getJobPosition().getPosition() %>
			</td>					
			<td>
				<a href="#"><%= CandidatureController.GetEmployeeEmailByCf(listCandidatureBean.get(i).getOffer().getEmployee())%></a>
			</td>
			<td style="width: 20%;">
				<a href="#" class="table-link">
					<span class="fa-stack">
						<i class="fa fa-square fa-stack-2x"></i>
						<i class="fa fa-search-plus fa-stack-1x fa-inverse"></i>
					</span>
				</a>				
				<a href="#" class="table-link danger">
					<span class="fa-stack">
						<i class="fa fa-square fa-stack-2x"></i>
						<i class="fa fa-trash-o fa-stack-1x fa-inverse"></i>
					</span>
				</a>
			</td>
		</tr>	
<%		
	}
}			
%>											
					</tbody>
				</table>
			</div>			
		   </div>
	      </div>
	     </div>
        </div>       
       
      </div>
	 </div>
	

  	 </form>
    </body>    
</html>