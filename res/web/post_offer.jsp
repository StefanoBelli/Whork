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
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Post offer - Whork</title>
		<meta name="author" content="Michele Tosi">
		<meta name="author" content="Magliari Elio">
		<script src="js/common.js"></script>
		<script src="js/post_offer.js"></script>
		<link href="css/postOffer.css" rel="stylesheet" />
		<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.bundle.min.js"></script>
		<link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet">
		<link href="https://use.fontawesome.com/releases/v5.8.1/css/all.css" rel="stylesheet">
		<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
		<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
		<link rel="stylesheet" type="text/css" href="css/whork.css">
		<link rel="preconnect" href="https://fonts.gstatic.com">
	 	<link href="https://fonts.googleapis.com/css2?family=Kameron&display=swap" rel="stylesheet">
	 	<link href="https://fonts.googleapis.com/css2?family=Bungee+Shade&display=swap" rel="stylesheet">
	</head>
	<body style="background-color:#FFA07A">
<%
	UserBean userBean = ServletUtil.getUserForSession(request);
	if(!(userBean.isEmployee() && userBean.isRecruiter())){
%>
		<script>
			redirect("index.jsp",0);
		</script>
<%
	}

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

			<div class="signup-form">
				<div style="font-size:30pt; margin-left:100px; margin-top: 40px;">
			 		<a href="index.jsp">
			                <span class="whork"> W<span class="hred">h</span>ork</span>
			        </a>
			    </div>
				<form enctype="multipart/form-data" method="post" action="/postOffer"
						onsubmit='return check_work_shift();'>
				<div class="container py-5">
			    	<!-- For demo purpose -->
				    <div class="row mb-4">
				        <div class="col-lg-8 mx-auto text-center">
				            <h1 class="display-6"><b>Post Offer</b></h1>
				        </div>
				    </div> <!-- End -->
			    <div class="row">
			        <div class="col-lg-6 mx-auto">
			            <div class="card ">
			                <div class="card-header">
			                    <!-- Credit card form content -->
			                    <div class="tab-content">
	                        <!-- credit card info-->
		                        	<div class="tab-pane fade show active pt-3">
		                                <div class="form-group"> 
			                                <label for="name">
			                                	<h6>Offer name</h6>
			                                </label>
											<input class="form-control" type="text" name="name"	maxlength=255 placeholder="Offer name" required>
		                                </div>
		                             	<div class="form-group"> 
		                                	<label for="description">
		                                		<h6>Offer description</h6>
		                                	</label>
											<textarea class="form-control " name="description" placeholder="Offer description" required></textarea>
		                                </div>
		                            	<div class="form-group"> 
		                                	<label for="salaryEur">
		                                		<h6>Offer salary (Euro)</h6>
		                                	</label>
											<input class="form-control " type="text" name="salaryEur" placeholder="Offer salary" required>
		                                </div>
		                                <div class="form-group"> 
		                                	<label for="address">
		                                		<h6>Address</h6>
		                                	</label>
											<input class="form-control "  type="text" name="address" id="address"
												placeholder="Company address" maxlength=45 required>
		                                </div>
		                            </div>
	                                <div class="row">
		                                <div class="col-sm-4">     
		                                    <div class="form-group">
		                                    	<label for="work_shift">
				                                	<h6>&nbsp;</h6>
				                                </label> 
			                                	<div class="input-group">
				                                	<label class="subscribe btn btn-primary btn-block shadow-sm">                      	
								                     	<h6>Offer photo</h6>
								                     	<input type="file" name="offer_photo" accept="image/png, image/jpeg" style="display:none">
													</label>
												</div>
				                            </div>
	                                    </div>
		                                <div class="col-sm-8">     
		                                    <div class="form-group mb-4"> 
		                                        <label for="work_shift">
				                                	<h6>Work shift</h6>
				                                </label>
		                                        <div class="input-group"> 
													 <input class="form-control " type="text" id="work_shift" name="work_shift" maxlength=13
													placeholder="Work shift (HH:mm - HH:mm)" required>
		                                        </div>
	                                        </div>
	                                    </div>
	                                </div>
	                                <div class="row">
	                                   <div class="col-sm-4">
	                                        <div class="form-group mb-4"> 
	                                        <label for="job_position">
                                                 <h6>Job position</h6>
                                            </label>
	                                        <div class="input-group"> 
												<select class="form-control" name="job_position" size="1">
<%
													for(final JobPositionBean position : JobPositionPool.getJobPositions()) {
														String p = position.getPosition();
%>
														<option value="<%=p%>"><%=p%></option>
<%
													}
%>
												</select> 
	                                        </div>
	                                        </div>
	                                    </div>
	                                    <div class="col-sm-4">
	                                        <div class="form-group mb-4"> 
		                                        <label for="job_category">
		                                                <h6>Job category</h6>
		                                        </label> 
												<div class="input-group"> 
													<select class="form-control" name="job_category" size="1">
<%
														for(final JobCategoryBean category : JobCategoryPool.getJobCategories()) {
															String c = category.getCategory();
%>
															<option value="<%=c%>"><%=c%></option>
<%
														}
%>
													</select> 
	                                        	</div>
	                                        </div>
	                                    </div>
	                                	<div class="col-sm-4">
	                                        <div class="form-group mb-4"> 
		                                        <label for="type_of_contract">
		                                                <h6>Type of contract</h6>
		                                        </label> 
												<div class="input-group">
													<select class="form-control" name="type_of_contract" size="1">
<%
														for(final TypeOfContractBean contract : TypeOfContractPool.getTypesOfContract()) {
															String c = contract.getContract();
%>
															<option value="<%=c%>"><%=c%></option>
<%
														}
%>
													</select> 
	                                        	</div>
	                                        </div>
	                                    </div>
	                                </div>
	                                <div class="row">
	                                    <div class="col-sm">
	                                        <div class="form-group mb-4"> 
		                                        <label for="note">
		                                                <h6>Offer note</h6>
		                                        </label> 
												<div class="input-group">
													<textarea class="form-control" type="text" name="note" placeholder="Offer Note"></textarea>
	                                        	</div>
	                                        </div>
	                                    </div>
	                                	<div class="form-group mb-4"> 
	                                        <label for="qualification">
                                                 <h6>Requirements qualification</h6>
                                            </label>
											<div class="input-group">
												<select class="form-control" name="qualification" size="1">
<%
													for(final QualificationBean qualify : QualificationPool.getQualifications()) {
														String q = qualify.getQualify();
%>
														<option value="<%=q%>"><%=q%></option>
<%
													}
%>
												</select>
	                                        </div>
                                        </div>
	                                </div>
	                                <div class="card-footer"> 
	                                	<button type="submit" name="post_offer_button" value="Post Offer" class="subscribe btn btn-primary btn-block shadow-sm"> Post Offer </button>
	                        		</div>
			                    </div> <!-- End -->
			                </div>
			            </div>
        			</div>
	    		</div>
			</div>
		</form>
		</div>
	</body>
</html>