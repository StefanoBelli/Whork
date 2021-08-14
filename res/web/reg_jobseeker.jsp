<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="logic.bean.ComuneBean" %>
<%@ page import="logic.bean.ProvinciaBean" %>
<%@ page import="logic.bean.RegioneBean" %>
<%@ page import="logic.bean.EmploymentStatusBean" %>
<%@ page import="logic.pool.EmploymentsStatusPool" %>
<%@ page import="logic.pool.ComuniPool" %>
<%@ page import="logic.util.Util" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<title>Register as a job seeker - Whork</title>

	<meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0' name='viewport' />
    <meta name="viewport" content="width=device-width" />

	<link rel="apple-touch-icon" sizes="76x76" href="assetsJobSeekerRegist/img/apple-icon.png" />
	<link rel="icon" type="image/png" href="assetsJobSeekerRegist/img/favicon.png" />

	<!--     Fonts and icons     -->
	<link rel="stylesheet" type="text/css" href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700|Roboto+Slab:400,700|Material+Icons" />
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/latest/css/font-awesome.min.css" />

	<!-- CSS Files -->
	<link href="assetsJobSeekerRegist/css/bootstrap.min.css" rel="stylesheet" />
	<link href="assetsJobSeekerRegist/css/material-bootstrap-wizard.css" rel="stylesheet" />
	<link href="assetsJobSeekerRegist/css/demo.css" rel="stylesheet" />
	<!-- CSS Just for demo purpose, don't include it in your project -->

	<!--   Core JS Files   -->
	<script src="assetsJobSeekerRegist/js/jquery-2.2.4.min.js" type="text/javascript"></script>
	<script src="assetsJobSeekerRegist/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="assetsJobSeekerRegist/js/jquery.bootstrap.js" type="text/javascript"></script>

	<!--  Plugin for the Wizard -->
	<script src="assetsJobSeekerRegist/js/material-bootstrap-wizard.js"></script>

	<!--  More information about jquery.validate here: http://jqueryvalidation.org/	 -->
	<script src="assetsJobSeekerRegist/js/jquery.validate.min.js"></script>
	
	<script src="js/common.js"></script>
	<script src="js/reg_jobseeker.js"></script>
	<link rel="stylesheet" type="text/css" href="css/whork.css">
	<link rel="preconnect" href="https://fonts.gstatic.com">
 	<link href="https://fonts.googleapis.com/css2?family=Kameron&display=swap" rel="stylesheet">
 	<link href="https://fonts.googleapis.com/css2?family=Bungee+Shade&display=swap" rel="stylesheet">
 	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
	<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
	<link rel="stylesheet" href="/resources/demos/style.css">
	<link rel="stylesheet" href="css/reg_jobseeker.css">
	
	<script>
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
		$( function() {
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
			refresh_maps();
		});
	</script>

</head>

<body>
<%
	String descError = (String) request.getAttribute("descriptive_error");
	if(descError != null) {
%>
		<div class="alert alert-danger" role="alert">
			<%=descError%>
		</div>
<%
		request.setAttribute("descriptive_error", null);
	}
%>
		
		<p id="error"></p>

	<form enctype="multipart/form-data" action="/completeRegistration" method="post"
			onsubmit='return check_jobseeker_fields();'>
	<div class="image-container set-full-height" style="background-image: url('<%= Util.InstanceConfig.getString(Util.InstanceConfig.KEY_CTX_DFL_ROOT) + "/" + "backgroundRegJobSeeker.jpeg" %>')">

	    <!--   Big container   -->
	    <div class="containers">
	        <div class="row">
		        <div class="col-sm-8 col-sm-offset-2">
		            <!--      Wizard container        -->
		            <div class="wizard-container">
		                <div class="card wizard-card" data-color="orange">
							<div style="font-size:20pt; margin-left:50px; padding-top: 20px;">
						 		<a href="index.jsp">
						                <span class="whork"> W<span class="hred">h</span>ork</span>
						        </a>
						    </div>
		                    	<div class="wizard-header">
		                        	<h3 class="wizard-title" style="font-size:30pt">
		                        		Sign Up as Job Seeker
		                        	</h3>
		                    	</div>
								<div class="wizard-navigation">
									<ul>
			                            <li><a href="#details" data-toggle="tab">Account</a></li>
			                        </ul>
								</div>

		                        <div class="tab-content">
		                            <div class="tab-pane" id="details">
		                            	<div class="row">
		                                	<div class="col-sm-6">
												<div class="input-group">
													<span class="input-group-addon">
														<i class="material-icons">email</i>
													</span>
													<div class="form-group label-floating">
				                                        <label class="control-label" for="email">Email</label>
														<input class="form-control" type="email" name="email" 
																maxlength=255 required>
			                                        </div>
												</div>

												<div class="input-group">
													<span class="input-group-addon">
														<i class="material-icons">lock_outline</i>
													</span>
													<div class="form-group label-floating">
				                                        <label class="control-label" for="password">Password</label>
														<input class="form-control" type="password" id="pwd" name="pwd" required>
			                                        </div>
												</div>
												
												<div class="input-group">
													<span class="input-group-addon">
														<i class="material-icons">lock_outline</i>
													</span>
													<div class="form-group label-floating">
				                                        <label class="control-label" for="confirm_password">Confirm your password</label>
														<input class="form-control" type="password" id="conf_pwd" name="conf_pwd" required>
			                                        </div>
												</div>
		                                	</div>

											<div class="col-sm-6">
	                                        	<iframe title="whereareyou" width="580" height="400" style="border:1" loading="lazy" allowfullscreen id="maps"
													src="https://www.google.com/maps/embed/v1/place?key=AIzaSyAp5hG3kGqNGj6Auxh4IhC0Y60hzgUyzKo&q=Italy">
												</iframe>
		                                	</div>

		                                	<div class="col-sm-6">
		                                    	<div class="input-group">
													<span class="input-group-addon">
														<i class="material-icons">assignment_ind</i>
													</span>
													<div class="form-group label-floating">
				                                        <label class="control-label" for="name">Name</label>
														<input class="form-control" type="text" name="name" 
																maxlength=45 required>
			                                        </div>
												</div>

												<div class="input-group">
													<span class="input-group-addon">
														<i class="material-icons">assignment_ind</i>
													</span>
													<div class="form-group label-floating">
				                                        <label class="control-label" for="surname">Surname</label>
														<input class="form-control" type="text" name="surname"
																maxlength=45 required>
			                                        </div>
												</div>
		                                		<div class="input-group">
													<span class="input-group-addon">
														<i class="material-icons">fingerprint</i>
													</span>
													<div class="form-group label-floating">
				                                        <label class="control-label" for="fiscal_code">Fiscal code</label>
														<input class="form-control" type="text" id="my_fc" name="fiscal_code" 
																maxlength=16 minlength=16 required>
			                                        </div>
												</div>
		                                		<div class="input-group">
													<span class="input-group-addon">
														<i class="material-icons">phone</i>
													</span>
													<div class="form-group label-floating">
				                                        <label class="control-label" for="phone_number">Phone number</label>
														<input class="form-control" type="text" name="phone_number" onkeypress="return is_digit(event);"
																minlength=9 maxlength=10 required>
			                                        </div>
												</div>
		                                		<div class="input-group">
													<span class="input-group-addon">
														<i class="material-icons">photo</i>
													</span>
													<div class="form-group label-floating">
			                                        	<label class="btn btn-default btn-file">                      	
									                     	<font color="white">Your profile photo</font>
									                     	<input type="file" name="your_photo" accept="image/png, image/jpeg">
														</label>
			                                        </div>
												</div>
		                                	</div>

		                                	<div class="col-sm-6">
		                                		<div class="input-group">
													<span class="input-group-addon">
														<i class="material-icons">home</i>
													</span>
													<div class="form-group label-floating">
				                                        <label class="control-label" for="town">Town</label>
														<input class="form-control" type="text" id="town" name="town" 
															 maxlength=34 required onchange='refresh_maps();'>
			                                        </div>
												</div>
		                                		<div class="input-group">
													<span class="input-group-addon">
														<i class="material-icons">location_on</i>
													</span>
													<div class="form-group label-floating">
				                                        <label class="control-label" for="address">Address</label>
														<input class="form-control" type="text" name="address" id="address"
															 maxlength=45 required oninput='refresh_maps();'>
			                                        </div>
												</div>
		                                		<div class="input-group">
													<span class="input-group-addon">
														<i class="material-icons">vpn_key</i>
													</span>
													<div class="form-group label-floating">
			                                          	<select class="form-control" name="employment_status" size="1">
<%
															for(final EmploymentStatusBean status : EmploymentsStatusPool.getEmploymentsStatus()) {
																String s = status.getStatus();
%>
																	<option value="<%=s%>"><%=s%></option>
<%
															}
%>
														</select>
			                                        </div>
												</div>
		                                		<div class="input-group">
													<span class="input-group-addon">
														<i class="material-icons">file_upload</i>
													</span>
													<div class="form-group label-floating">
														<label class="btn btn-default btn-file">                      	
									                     	<font color="white">Attach your curriculum</font>
									                     	<input type="file" name="cv" accept=".pdf" required>
														</label>
			                                        </div>
												</div>
		                                	</div>
		                                	
		                                	<div class="wizard-footer">
				                                <div class="pull-left">
													<div class="footer-checkbox">
														<div class="col-sm-5">
														  <div class="checkbox">
															  <label for="ppolicy">
															  <input type="checkbox" id="ppolicy" name="ppolicy" onchange="reg_toggle_submit();">
																I have read the Customer Privacy Policy and consent to the processing
																of my personal data for the purposes related to the management of the contractual relationship and
																the provision of services </label><br>
														  </div>
													  </div>
													</div>
				                                </div>
				                                <div class="pull-right">
				                                	<input class='btn btn-finish btn-fill btn-danger btn-wd' type="submit" id="submit" name="submit" value="Confirm" disabled>
				                                </div>
				                                <div class="clearfix"></div>
				                        	</div>
		                            	</div>
		                            </div>
		                        </div>
	                        	
		                </div>
		            </div> <!-- wizard container -->
		        </div>
	    	</div> <!-- row -->
		</div> <!--  big container -->

	</div>
	</form>

</body>
</html>
