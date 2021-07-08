<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="logic.bean.ComuneBean" %>
<%@ page import="logic.bean.ProvinciaBean" %>
<%@ page import="logic.bean.RegioneBean" %>
<%@ page import="logic.bean.EmploymentStatusBean" %>
<%@ page import="logic.pool.EmploymentsStatusPool" %>
<%@ page import="logic.pool.ComuniPool" %>
<!DOCTYPE html>
<html lang="en">
	<head>
		<title>Register as a job seeker - Whork</title>
		<script src="js/reg_jobseeker.js"></script>
		<script src="js/common.js"></script>
		<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
		<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
		<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
		<link rel="stylesheet" href="/resources/demos/style.css">
		<link rel="stylesheet" href="css/reg_jobseeker.css">
		<script>
		$( function() {
			var ittowns = [
<%
for(final ComuneBean comune : ComuniPool.getComuni()) {
	ProvinciaBean provincia = comune.getProvincia();
	RegioneBean regione = provincia.getRegione();
%>
				"<%=comune.getNome()%> <%=provincia.getSigla()%> - <%=comune.getCap()%>, <%=regione.getNome()%>",
<%
}
%>
			];
			$("#town").autocomplete({
				minLength: 2,
		    	source: function (request, response) {
		    		var matches = $.map(ittowns, function (town) {
		    			if (town.toUpperCase().indexOf(request.term.toUpperCase()) === 0) {
		    				return town;
		    			}
		    		});
		    		response(matches);
		    	}
			});
		});
		</script>

		<body>

<% %> <!-- root error-->
		<h2>Authentication</h2>

<% %> <!-- error -->
		<form enctype="multipart/form-data" action="/completeRegistration" method="post"
			onsubmit="return check_jobseeker_fields(ittowns);">

			<label for="email">Email</label>
			<input type="email" name="email" placeholder="Enter your email here ..." 
					maxlength=255 required>

			<label for="password">Password</label>
			<input type="password" id="pwd" name="password" 
					placeholder="Enter your password here ..." required>

			<label for="confirm_password">Confirm your password</label>
			<input type="password" id="conf_pwd" name="confirm_password" 
					placeholder="Confirm your password here..." required>

			<h2>About you</h2>

			<label for="name">Name</label>
			<input type="text" name="name" 
					placeholder="Enter your name here..." maxlength=45 required>

			<label for="surname">Surname</label>
			<input type="text" name="surname" 
					placeholder="Enter your surname here..." maxlength=45 required>

<% %> <!-- error -->
			<label for="fiscal_code">Fiscal code</label>
			<input type="text" id="my_fc" name="fiscal_code" 
					placeholder="Enter your fiscal code here..." maxlength=16 minlength=16 required>
			
			<label for="phone_number">Phone number</label>
			<input type="text" name="phone_number" 
				placeholder="Enter phone number here..." minlength=9 maxlength=10 required>

<% %> <!-- error -->
			<label for="your_photo">Your profile photo</label>
			<input type="file" name="your_photo" accept="image/png, image/jpeg">

			<label for="address">Address</label>
			<input type="text" name="address" 
				placeholder="Enter your address here..." maxlength=45 required>

			<label for="town">Town</label>
			<input type="text" id="town" name="town" 
				placeholder="Enter your town here..." maxlength=34 required>

			<h2>Employment status</h2>

			<select name="employment_status" size="1">
<%
for(final EmploymentStatusBean status : EmploymentsStatusPool.getEmploymentsStatus()) {
	String s = status.getStatus();
%>
				<option value="<%=s%>"><%=s%></option>
<%
}
%>
			</select>

<% %> <!-- error -->
			<label for="CV">Attach your curriculum</label>
			<input type="file" name="cv" accept=".pdf" required>

			<h2>Privacy policy</h2>

			<input type="checkbox" id="ppolicy" name="ppolicy" onchange="reg_toggle_submit();">
			<label for="ppolicy"> I have read the Customer Privacy Policy and consent to the processing
			of my personal data for the purposes related to the management of the contractual relationship and
			the provision of services </label><br>

			<input type="submit" id="submit" name="submit" value="Confirm" disabled>
		</form>
		<br/>
		<p id="error"></p>
	</body>
</html>