<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="logic.bean.ComuneBean" %>
<%@ page import="logic.bean.ProvinciaBean" %>
<%@ page import="logic.bean.RegioneBean" %>
<%@ page import="logic.bean.UserBean" %>
<%@ page import="logic.bean.JobPositionBean" %>
<%@ page import="logic.pool.JobPositionPool" %>
<%@ page import="logic.bean.JobCategoryBean" %>
<%@ page import="logic.pool.JobCategoryPool" %>
<%@ page import="logic.bean.TypeOfContractBean" %>
<%@ page import="logic.pool.TypeOfContractPool" %>
<%@ page import="logic.bean.QualificationBean" %>
<%@ page import="logic.pool.QualificationPool" %>
<%@ page import="logic.pool.ComuniPool" %>
<%@ page import="logic.util.ServletUtil" %>
<!DOCTYPE html>
<html lang="en">
	<head>
		<title>Poster offer - Whork</title>
		<script src="js/common.js"></script>
<%
UserBean userBean = ServletUtil.getUserForSession(request);
if(!(userBean.isEmployee() && userBean.isRecruiter())){%>
		<script>
		redirect("index.jsp",0);
		</script>
<%
}
%>
	</head>
	<body>
<%
String descError = (String) request.getAttribute("descriptive_error");
if(descError != null) {
%>
		<h3><%=descError%></h3>
<%
}
%>
		<form method="post" action="/PostOffer">
			<h2>Offer</h2>
			
			<label for="name">Offer name</label>
			<input type="text" name="name" placeholder="Enter offer name here ..." 
				maxlength=255 required>
			
			<label for="description">Offer description</label>
			<input type="text" name="description" 
				placeholder="Enter offer description here ..." required>
				
			<label for="salaryEur">Offer salary (Euro)</label>
			<input type="text" name="salaryEur" 
				placeholder="Enter offer salary here ..." required>
				
			<label for="address">Address</label>
			<input type="text" name="address" id="address"
				placeholder="Enter company address here..." maxlength=45 required>
			
			<label for="offer_photo">Offer photo</label>
			<input type="file" name="offer_photo" accept="image/png, image/jpeg">
			
			<label for="work_shift">Work shift</label>
			<input type="text" name="work_shift" maxlength=13
				placeholder="Enter work shift here ... (HH:mm - HH:mm)" required>
				
			<label for="job_position">Job position</label>
			<select name="job_position" size="1">
<%
for(final JobPositionBean position : JobPositionPool.getJobPositions()) {
	String p = position.getPosition();
%>
				<option value="<%=p%>"><%=p%></option>
<%
}
%>
			</select>
				
			
			<label for="job_category">Job category</label>
			<select name="job_category" size="1">
<%
for(final JobCategoryBean category : JobCategoryPool.getJobCategories()) {
	String c = category.getCategory();
%>
				<option value="<%=c%>"><%=c%></option>
<%
}
%>
			</select>
			
			
			<label for="type_of_contract">Type of contract</label>
			<select name="type_of_contract" size="1">
<%
for(final TypeOfContractBean contract : TypeOfContractPool.getTypesOfContract()) {
	String c = contract.getContract();
%>
				<option value="<%=c%>"><%=c%></option>
<%
}
%>
			</select>
				
			<label for="qualification">Requirements qualification</label>
			<select name="qualification" size="1">
<%
for(final QualificationBean qualify : QualificationPool.getQualifications()) {
	String q = qualify.getQualify();
%>
				<option value="<%=q%>"><%=q%></option>
<%
}
%>
			</select>
			
			<label for="note">Offer note</label>
			<input type="text" name="note" 
				placeholder="Enter note here ..." required>
			
			
			<input type="submit" name="post_offer_button" value="Post Offer">
				
			
		</form>

		<br/>
		<p id="error"></p>
	</body>
</html>