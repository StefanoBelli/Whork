<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="logic.bean.OfferBean" %>
<%@ page import="logic.bean.UserBean" %>
<%@ page import="java.util.List" %>
<%@ page import="logic.controller.OfferController" %>
<%@ page import="logic.util.Util" %>
<%@ page import="logic.util.ServletUtil" %>
<%@ page import="logic.pool.JobCategoryPool" %>
<%@ page import="logic.bean.JobCategoryBean" %>
<%@ page import="logic.pool.JobPositionPool" %>
<%@ page import="logic.bean.JobPositionBean" %>
<%@ page import="logic.pool.QualificationPool" %>
<%@ page import="logic.bean.QualificationBean" %>
<%@ page import="logic.pool.TypeOfContractPool" %>
<%@ page import="logic.bean.TypeOfContractBean" %>
<%@ page import="logic.controller.CandidatureController" %>
<%@ page import="logic.factory.BeanFactory" %>
<!DOCTYPE html>
<html lang="en">
   <head>
      <!-- basic -->
      <meta charset="utf-8">
      <meta http-equiv="X-UA-Compatible" content="IE=edge">
      <meta name="viewport" content="width=device-width, initial-scale=1">
      <!-- mobile metas -->
      <meta name="viewport" content="width=device-width, initial-scale=1">
      <meta name="viewport" content="initial-scale=1, maximum-scale=1">
      <!-- site metas -->
      <title>Home - Whork</title>
      <!-- style css -->
      <link rel="stylesheet" type="text/css" href="css/indexCss/style.css">
      <!-- Tweaks for older IEs-->
      <link rel="stylesheet" href="https://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css">
      <!-- fonts -->
      <link href="https://fonts.googleapis.com/css?family=Poppins:400,700&display=swap" rel="stylesheet">
      <!-- font awesome -->
      <link rel="stylesheet" type="text/css" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
      <!--  -->
      <!-- owl stylesheets -->
      <link href="https://fonts.googleapis.com/css?family=Great+Vibes|Poppins:400,700&display=swap&subset=latin-ext" rel="stylesheet">
      <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/fancybox/2.1.5/jquery.fancybox.min.css" media="screen">
	  <link rel="stylesheet" href="css/index.css">
   	  <link rel="stylesheet" href="css/whork.css">
   	  <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">
      <script src="https://code.jquery.com/jquery-1.10.2.min.js"></script>
      <link href="https://netdna.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
	  <script src="https://netdna.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
      <link rel="preconnect" href="https://fonts.gstatic.com">
	  <link href="https://fonts.googleapis.com/css2?family=Kameron&display=swap" rel="stylesheet">
	  <link href="https://fonts.googleapis.com/css2?family=Bungee+Shade&display=swap" rel="stylesheet">
   </head>

<%
	UserBean sessionUser = ServletUtil.getUserForSession(request);
	String searchVal=(String) request.getParameter("searchVal");
	String jobCategory = (String) request.getParameter("jobCategories");
	String jobPosition = (String) request.getParameter("jobPositions");
	String qualification = (String) request.getParameter("qualifications");
	String typeOfContract = (String) request.getParameter("typesOfContract");
	List<OfferBean> offers =OfferController.searchOffers(searchVal, 
				jobCategory, jobPosition, qualification	, typeOfContract);
	String candidateToOffer = (String) request.getParameter("candidate_offer_id");
	if(candidateToOffer != null && sessionUser != null) {
		CandidatureController.insertCandidature(BeanFactory.buildCandidatureBean(
				OfferController.getOfferById(Integer.parseInt(candidateToOffer)), sessionUser));
}
%>

   <body style="background-color:#87CEEB">
      <!-- banner bg main start -->
      <div class="banner_bg_main">
         <!-- header top section start -->
         <div class="container">
            <div class="header_section_top">
               <div class="row">
                  <div class="col-sm-12">
                     <div class="custom_menu">
                        <ul>
<%
							if(sessionUser==null){
%>
								<li><a href="/login.jsp">Sign In</a></li>
								<li><a href="/register.jsp">Sign Up</a></li>
<%
							} else { 
%>
								<li><a href="/account.jsp" name="account">My Account</a></li>
<%	
								if(sessionUser.isEmployee()) {
								
									if(sessionUser.isRecruiter()){ 
%>
										<li><a href="/post_offer.jsp" name="post_offer">Post Offer</a></li>
<%		
									}
								}
%>
								<li><a href="/logout">Log Out</a></li>
<%
							}
%>
                        </ul>
                     </div>
                  </div>
               </div>
            </div>
         </div>
         <!-- header top section start -->
         <!-- logo section start -->
         <div class="logo_section">
            <div class="container">
               <div class="row">
                  <div class="col-sm-12">
                     <div class="logo"><a href="index.jsp" style="font-size:55px"><span class="whork"> W<span class="hred">h</span>ork</span></a></div>
                  </div>
               </div>
            </div>
         </div>
         <!-- logo section end -->
         <!-- header section start -->
         <div class="header_section">
            <div class="container">
            <form name="searchForm" action="/index.jsp" method="get">
               <div class="containt_main">
                  <div class="main">
                     <!-- Another variation with a button -->
                     <div class="form-inline my-2 my-lg-0">
	                     <div class="collapse navbar-collapse">
<%
								if(searchVal != null && searchVal != ""){ 
%>
									<input class="form-control mr-sm-2" type="search" name="searchVal" id="searchVal" aria-label="Search" value="<%=searchVal %>" />
<%
								} else {
%>
									<input class="form-control mr-sm-2" type="search" name="searchVal" id="searchVal" aria-label="Search" value="" />
<%
								}
%>
							<button class="btn btn-outline-success my-2 my-sm-0" type="submit" name="submit" id="search" value="Search">Search</button>
							<button onclick="window.location='/index.jsp';" type="button" id="reset" class="btn btn-outline-success my-2 my-sm-0" value="Reset filters">Reset filters</button>
	                  	</div>
                     </div>
                  </div>
               </div>
               <br>
               <div class="dropdown">
					<select class="form-control" name="jobCategories" id="categories">
						<option value="">-- Choose a job category --</option>
<%
						for(final JobCategoryBean category : JobCategoryPool.getJobCategories()){
							if(jobCategory!=null && jobCategory.equals(category.getCategory())){
%>	
								<option value="<%=category.getCategory() %>" selected><%=category.getCategory() %></option>	
<%
							} else {
%>
								<option value="<%=category.getCategory() %>"><%=category.getCategory() %></option>	
<%
							}
						}
%>
					</select>
	              </div>
	              <div class="dropdown">
					<select class="form-control" name="jobPositions" id="positions" >
							<option value="">-- Choose a job position --</option>
<%
								for(final JobPositionBean position : JobPositionPool.getJobPositions()){
									if(jobPosition!=null && jobPosition.equals(position.getPosition())){
%>	
										<option value="<%=position.getPosition() %>" selected><%=position.getPosition() %></option>	
<%
									} else {
%>
										<option value="<%=position.getPosition()%>" > <%=position.getPosition() %></option>	
<%
									}
								}
%>
					</select>
	              </div>
	              <div class="dropdown">
					<select class="form-control" name="qualifications" id="qualifies" >
						<option value="">-- Choose a qualification --</option>
<%
						for(final QualificationBean qualify : QualificationPool.getQualifications()){
							if(qualification!=null && qualification.equals(qualify.getQualify())){
%>	
							
								<option value="<%=qualify.getQualify() %>" selected><%=qualify.getQualify() %></option>	
<%
							} else {
%>
								<option value="<%=qualify.getQualify()%>"><%=qualify.getQualify() %></option>	
<%
							}
						}
%>
					</select>
	              </div>
	              <div class="dropdown">
					<select class="form-control" name="typesOfContract" id="contracts" >
						<option value="">-- Choose a type of contract --</option>
<%
							for(final TypeOfContractBean contract : TypeOfContractPool.getTypesOfContract()){
								if(typeOfContract!=null && typeOfContract.equals(contract.getContract())){
%>	
							
									<option value="<%=contract.getContract() %>" selected><%=contract.getContract() %></option>	
<%
							} else {
%>
									<option value="<%=contract.getContract()%>"><%=contract.getContract() %></option>	
<%
							}
						}
%>				
					</select>
	              </div>
	           </form>
            </div>
         </div>
         <!-- header section end -->
		<div class="container">
		<div class="row bootstrap snippets bootdeys"> 
		    <div class="col-md-9 col-sm-7" style="text-align:center"> 
		        &nbsp;
		    </div> 
		</div>
<%
		if(offers.isEmpty()) {
%>
			<div class="member-entry"> 
				 <a href="#" class="member-img"> 
	   				<img class="img-rounded" src="<%= Util.InstanceConfig.getString(Util.InstanceConfig.KEY_CTX_DFL_ROOT) + "/" + "offerPhoto.jpg" %>" alt="Image offer">
		        	<i class="fa fa-forward"></i> 
		    	</a> 
		    <div class="member-details"> 
		        <h4> <b> No offers </b> </h4>
			        <div class="row info-list"> 
			            <div class="col-sm-4"> 
			                <i class="fa fa-briefcase"></i>
			                	There are not offers.
			        	</div> 
					</div>
				</div> 
		    </div> 
			<div class="member-entry"> 
				 <a href="#" class="member-img"> 
	   				<img class="img-rounded" src="<%= Util.InstanceConfig.getString(Util.InstanceConfig.KEY_CTX_DFL_ROOT) + "/" + "offerPhoto.jpg" %>" alt="Image offer">
		        	<i class="fa fa-forward"></i> 
		    	</a> 
		    <div class="member-details"> 
		        <h4> <b> No offers </b> </h4>
			        <div class="row info-list"> 
			            <div class="col-sm-4"> 
			                <i class="fa fa-briefcase"></i>
			                	There are not offers.
			        	</div> 
					</div>
				</div> 
		    </div>
		    <div class="member-entry"> 
				 <a href="#" class="member-img"> 
	   				<img class="img-rounded" src="<%= Util.InstanceConfig.getString(Util.InstanceConfig.KEY_CTX_DFL_ROOT) + "/" + "offerPhoto.jpg" %>" alt="Image offer">
		        	<i class="fa fa-forward"></i> 
		    	</a> 
		    <div class="member-details"> 
		        <h4> <b> No offers </b> </h4>
			        <div class="row info-list"> 
			            <div class="col-sm-4"> 
			                <i class="fa fa-briefcase"></i>
			                	There are not offers.
			        	</div> 
					</div>
				</div> 
		    </div>
		    <div class="member-entry"> 
				 <a href="#" class="member-img"> 
	   				<img class="img-rounded" src="<%= Util.InstanceConfig.getString(Util.InstanceConfig.KEY_CTX_DFL_ROOT) + "/" + "offerPhoto.jpg" %>" alt="Image offer">
		        	<i class="fa fa-forward"></i> 
		    	</a> 
		    <div class="member-details"> 
		        <h4> <b> No offers </b> </h4>
			        <div class="row info-list"> 
			            <div class="col-sm-4"> 
			                <i class="fa fa-briefcase"></i>
			                	There are not offers.
			        	</div> 
					</div>
				</div> 
		    </div>
<%
		} else {
			for(final OfferBean offer : offers) {
%>
			<div class="member-entry"> 
				 <a href="#" class="member-img"> 
<%
				if(offer.getPhoto() == null) {
%>
	   				<img class="img-rounded" src="<%= Util.InstanceConfig.getString(Util.InstanceConfig.KEY_CTX_DFL_ROOT) + "/" + "offerPhoto.jpg" %>" alt="Image offer">
<%
				} else {
%>
					<img class="img-rounded" src="<%= Util.InstanceConfig.getString(Util.InstanceConfig.KEY_CTX_USR_DATA) + "/" + offer.getPhoto() %>" alt="Image offer">
<%
				} 
%>
		        	<i class="fa fa-forward"></i> 
		    	</a> 
		    <div class="member-details"> 
		        <h4> <b id="offer-name"> <%= offer.getOfferName() %> </b>

<%
					if(offer.isVerifiedByWhork()){ 
%>
						<span style="padding-left:25px; font-size:15px;">
			                (Verified by Whork ©)
		            	</span>
<%
					} 
%> 
				</h4>
		        <div class="col-sm-4"> 
	                <iframe title="whereisjob" width="300" height="250" style="border:1" loading="lazy" allowfullscreen id="maps"
						src="https://www.google.com/maps/embed/v1/place?key=AIzaSyAp5hG3kGqNGj6Auxh4IhC0Y60hzgUyzKo&q=<%=offer.getJobPhysicalLocationFullAddress()%>">
					</iframe>
	            </div>
		        <div class="row info-list"> 
		            <div class="col-sm-4"> 
		                <i class="fa fa-briefcase"></i>
		                <%=offer.getDescription()%> 
		            </div> 
		            <div class="col-sm-4"> 
		                <i class="fa fa-building"></i>
		                <%=offer.getCompany().getSocialReason() %> 
		            </div> 
		            <br>
		            <div class="col-sm-4"> 
		                <i class="fa fa-euro"></i>
		                <%=offer.getSalaryEUR() %> 
		            </div> 
		            <div class="col-sm-4"> 
		                <i class="fa fa-calendar"></i>
		                <%=offer.getWorkShift() %> 
		            </div> 
		            <br>
		            <div class="col-sm-4"> 
		                <i class="fa fa-book"></i>
		                <%=offer.getJobPosition().getPosition() %> 
		            </div> 
		            <div class="col-sm-4"> 
		                <i class="fa fa-tags"></i>
		                <%=offer.getJobCategory().getCategory() %> 
		            </div>
		            <br>
		            <div class="col-sm-4"> 
		                <i class="fa fa-certificate"></i> 
		                <%=offer.getQualification().getQualify() %> 
		            </div>
		            <div class="col-sm-4"> 
		                <i class="fa fa-database"></i> 
		                <%=offer.getTypeOfContract().getContract() %> 
		            </div>
		            <br>
		            <div class="col-sm-4"> 
		                <i class="fa fa-unlock"></i> 
		                <%=offer.getPublishDate() %> 
		            </div>
		            <div class="col-sm-4">&nbsp;
		            </div>
<%
					if(sessionUser == null || sessionUser.isEmployee()){
%>
					<br>
					<div class="col-sm-4">
						<form action="#" method="get">
							<button class="btn btn-outline-success my-2 my-sm-0" type="submit" value="Chat" title="Login to chat or you are not a jobSeeker" style="border:1px solid black" disabled>Chat</button>
						</form>
					</div>	
					<div class="col-sm-4">	
						<form action="#" method="get">
							<button class="btn btn-outline-success my-2 my-sm-0" type="submit" value="Candidate" title="Login to candidate or you are not a jobSeeker" style="border:1px solid black" disabled>Candidate</button>
						</form>
					</div>
<%
					} else { 
%>
					<br>
					<div class="col-sm-4">
						<button class="btn btn-outline-success my-2 my-sm-0" style="border:1px solid black" onclick="window.open('/chat.jsp?toEmail=<%=CandidatureController.getEmployeeEmailByCf(offer.getEmployee()) %>','Chat - Whork', width=674, height=634);">Chat with recruiter</button>
					</div>
					<div class="col-sm-4">
<%
						if(CandidatureController.getCandidature(offer.getId(), sessionUser.getCf()) == null) { 
%>
							<form action="/index.jsp" method="post">
								<input type="hidden" id="candidate_offer_id" name="candidate_offer_id" value=<%=offer.getId()%>>
								<button class="btn btn-outline-success my-2 my-sm-0" style="border:1px solid black" type="submit" name="candidate_button" value="Candidate">Candidate</button>
							</form>
<%
						} else { 
%>
							<form action="/index.jsp" method="post">
								<button class="btn btn-outline-success my-2 my-sm-0" style="border:1px solid black" type="submit" name="candidate_button" value="Candidate" title="You already placed a candidature for this job" disabled>Candidate</button>
							</form>
<%
						}
					} 
%>
				</div> 
		    </div> 
		</div>
<%
			}
		}
%>
		</div>
       </div>
      </div>
   </body>
</html>