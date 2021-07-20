<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="logic.bean.OfferBean" %>
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
<!DOCTYPE html>
<html lang="en">
	<head>
		<title>Index - Whork</title>
	</head>
		

	<body>
	<div class="searchDiv">
	
		<form name="searchForm" action="search" method="get">
		
			<input type="text" name="searchVal" value="" />
		
			<input type="submit" name="submit" value="Search"/>
			
			<br>
			<br>
			
			<label for="jobCategories">Choose a job category:</label>
			<select name="jobCategories" id="categories" >
				<option value="selectAnOption">--select an option--</option>
				<%
					for(final JobCategoryBean category : JobCategoryPool.getJobCategories()){
				%>	
					<option value="<%=category.getCategory() %>"><%=category.getCategory() %></option>	
				<%}%>
				
			</select>
			
			<label for="jobPosition">Choose a job position:</label>
			<select name="jobPosition" id="positions" >
				<option value="selectAnOption">--select an option--</option>
				<%
					for(final JobPositionBean position : JobPositionPool.getJobPositions()){
				%>	
					<option value="<%=position.getPosition() %>"><%=position.getPosition() %></option>	
				<%}%>
				
			</select>
			
			<label for="qualification">Choose a qualification:</label>
			<select name="qualification" id="qualifies" >
				<option value="selectAnOption">--select an option--</option>
				<%
					for(final QualificationBean qualify : QualificationPool.getQualifications()){
				%>	
					<option value="<%=qualify.getQualify() %>"><%=qualify.getQualify() %></option>	
				<%}%>
				
			</select>
			
			<label for="typeOfContract">Choose a type of contract:</label>
			<select name="typeOfContract" id="contracts" >
				<option value="selectAnOption">--select an option--</option>
				<%
					for(final TypeOfContractBean contract : TypeOfContractPool.getTypesOfContract()){
				%>	
					<option value="<%=contract.getContract() %>"><%=contract.getContract() %></option>	
				<%}%>
				
			</select>
		
		</form>
		
	</div>
	
<%
if(OfferController.getOffers().size()==0){
%>

<h3>There aren't offers!</h3>

<%
}else{
	for(final OfferBean offer : OfferController.getOffers()) {
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
			<%=OfferController.getCompanyByVAT(offer) %>
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
		if(ServletUtil.getUserForSession(request)==null){
		%>
		<form action="#" method="get">
			<input type="submit" value="Chat" title="Login to chat" disabled/>
		</form>
		<form action="#" method="get">
			<input type="submit" value="Candidate" title="Login to candidate" disabled/>
		</form>
		<%}else{ %>
		
		<form action="#" method="get">
			<input type="submit" value="Chat"/>
		</form>
		<form action="#" method="get">
			<input type="submit" value="Candidate"/>
		</form>
		<%}%>
		
		
	</div>
				
<%
	}
}
%>
	</body>
</html>