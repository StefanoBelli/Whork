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
<!DOCTYPE html>
<html lang="en">
	<head>
		<title>Index - Whork</title>
	</head>	
	<body>
<%
UserBean sessionUser = ServletUtil.getUserForSession(request);
String searchVal=(String) request.getParameter("searchVal");
String jobCategory = (String) request.getParameter("jobCategories");
String jobPosition = (String) request.getParameter("jobPositions");
String qualification = (String) request.getParameter("qualifications");
String typeOfContract = (String) request.getParameter("typesOfContract");
List<OfferBean> offers = OfferController.getOffers(searchVal, 
			jobCategory, jobPosition, qualification, typeOfContract);
String candidateToOffer = (String) request.getParameter("candidate_offer_id");
if(candidateToOffer != null && sessionUser != null) {
	CandidatureController.insertCandidature(
			Integer.parseInt(candidateToOffer), sessionUser.getCf());
}
%>
	<div class="searchDiv">
		<form name="searchForm" action="/index.jsp" method="get">
<%
if(searchVal != null && searchVal != ""){ 
%>
			<input type="text" name="searchVal" value="<%=searchVal %>" />
<%
} else {
%>
			<input type="text" name="searchVal" value="" />
<%
}
%>
			<input type="submit" name="submit" value="Search"/>
			<br>
			<br>
			<label for="jobCategories">Choose a job category:</label>
			<select name="jobCategories" id="categories">
				<option value="">--select an option--</option>
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
			<label for="jobPositions">Choose a job position:</label>
			<select name="jobPositions" id="positions" >
				<option value="">--select an option--</option>
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
			<label for="qualifications">Choose a qualification:</label>
			<select name="qualifications" id="qualifies" >
				<option value="">--select an option--</option>
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
			<label for="typesOfContract">Choose a type of contract:</label>
			<select name="typesOfContract" id="contracts" >
				<option value="">--select an option--</option>
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
		</form>
		<form action="/index.jsp" method="post">
			<input type="submit" name="reset" value="Reset filters">
		</form>
<%
if(sessionUser==null){
%>
		<form action="/login.jsp" method="post">
			<input type="submit" name="login" value="Login">
		</form>
<%
} else { 
%>
		<form action="/account.jsp" method="post">
			<input type="submit" name="account" value="My Account">
		</form>
<%
}
%>
	</div>
<%
if(offers.isEmpty()){
%>
	<h3>There aren't offers!</h3>
<%
} else {
	for(final OfferBean offer : offers) {
%>
	<div class="offer">
		<div class="immagine">
			<img src="<%= Util.InstanceConfig.getString(Util.InstanceConfig.KEY_CTX_USR_DATA) + "/" + offer.getPhoto() %>" alt="Image offer" style={width:200px; height=200px;}>
		</div>	
		<div class="name">
			<b>Name: <%=offer.getOfferName()%></b>
		</div>
		<div class="description">
			<%=offer.getDescription()%>
		</div>
		<div class="socialReason">
			<%=OfferController.getCompanyByVAT(offer).getSocialReason() %>
		</div>
		<div class="salary">
			<%=offer.getSalaryEUR() %>
		</div>
		<div class="workShift">
			<%=offer.getWorkShit() %>
		</div>
		<div class="jobPosition">
			<%=offer.getJobPosition() %>
		</div>
		<div class="jobCategory">
			<%=offer.getJobCategory() %>
		</div>
		<div class="qualification">
			<%=offer.getQualification() %>
		</div>
		<div class="typeOfContract">
			<%=offer.getTypeOfContract() %>
		</div>
		<div class="publishDate">
			<%=offer.getPublishDate() %>
		</div>
		<div class="verifiedByWhork">
			<%=offer.isVerifiedByWhork() %>
		</div>
		<div class="jobPhysicalLocation">
			<iframe title="whereisjob" width="300" height="300" style="border:0" loading="lazy" allowfullscreen id="maps"
				src="https://www.google.com/maps/embed/v1/place?key=AIzaSyAp5hG3kGqNGj6Auxh4IhC0Y60hzgUyzKo&q=<%=offer.getJobPhysicalLocationFullAddress()%>">
			</iframe>
		</div>
<%
if(sessionUser == null){
%>
		<form action="#" method="get">
			<input type="submit" value="Chat" title="Login to chat" disabled/>
		</form>
		<form action="#" method="get">
			<input type="submit" value="Candidate" title="Login to candidate" disabled/>
		</form>
<%
} else { 
%>
		<button onclick="window.open('/chat.jsp?toEmail=<%=OfferController.getEmployeeEmailByOffer(offer.getId())%>','Chat - Whork', width=600, height=400);">Chat with recruiter</button>
<%
	if(CandidatureController.getCandidature(offer.getId(), sessionUser.getCf()) == null) { 
%>
		<form action="/index.jsp" method="post">
			<input type="hidden" id="candidate_offer_id" name="candidate_offer_id" value=<%=offer.getId()%>>
			<input type="submit" name="candidate_button" value="Candidate">
		</form>
<%
	} else { 
%>
		<form action="/index.jsp" method="post">
			<input type="submit" name="candidate_button" value="Candidate" title="You already placed a candidature for this job" disabled/>
		</form>
<%
	}
}
%>
	</div>
<%
	}
}
%>
	</body>
</html>