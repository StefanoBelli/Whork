<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="logic.bean.UserBean" %>
<%@ page import="logic.util.ServletUtil" %>
<%@ page import="logic.util.Util" %>
<%@ page import="logic.bean.CandidatureBean" %>
<%@ page import="logic.controller.AccountController" %>
<%@ page import="logic.controller.CandidatureController" %>
<%@ page import="logic.pool.ComuniPool" %>
<%@ page import="logic.bean.ComuneBean" %>
<%@ page import="logic.bean.ProvinciaBean" %>
<%@ page import="logic.bean.RegioneBean" %>
<%@ page import="logic.bean.ChatLogEntryBean" %>
<%@ page import="java.util.*" %>
<%@ page import="com.google.gson.Gson"%>
<%@ page import="com.google.gson.JsonObject"%>

<%
    UserBean userBean = ServletUtil.getUserForSession(request);
	String email = ServletUtil.getUserEmailForSession(request);
	String name = userBean.getName();
	String surname = userBean.getSurname();
%>

<!DOCTYPE HTML>
<html xml:lang="en">

<%
	if(userBean.isAdmin()) {
		
		Gson gsonObj = new Gson();
		Map<Object,Object> map = null;
		List<Map<Object,Object>> list = new ArrayList<Map<Object,Object>>();
		List<Integer> listCandidatureByVat = CandidatureController.getCandidatureByCompanyVat(userBean.getCompany());
		List<String> listMonth = new ArrayList<>();
		listMonth.add("Jan");
		listMonth.add("Feb");
		listMonth.add("Mar");
		listMonth.add("Apr");
		listMonth.add("May");
		listMonth.add("Jun");
		listMonth.add("Jul");
		listMonth.add("Aug");
		listMonth.add("Sep");
		listMonth.add("Oct");
		listMonth.add("Nov");
		listMonth.add("Dec");
		
		for(int i=0; i<listCandidatureByVat.size(); i++) {			
			map = new HashMap<Object,Object>(); map.put("label", listMonth.get(i)); map.put("y", listCandidatureByVat.get(i)); list.add(map);		
		}
		 
		String dataPoints = gsonObj.toJson(list);
		
		
		Gson gsonObjPieChart = new Gson();
		Map<Object,Object> mapPieChart = null;
		List<Map<Object,Object>> listPieChart = new ArrayList<Map<Object,Object>>();
		
		Map<String, Double> mapEmployment = AccountController.getEmploymentStatusBtCompanyVAT(userBean.getCompany());
		Iterator<String> keys = mapEmployment.keySet().iterator();
		int i=0;
		String key = null;
		
		if(keys.hasNext()) {
			key = keys.next();	
			mapPieChart = new HashMap<Object,Object>(); mapPieChart.put("label", key); mapPieChart.put("y", mapEmployment.get(key)*100); mapPieChart.put("exploded", true); listPieChart.add(mapPieChart);		
		}
		
		while(keys.hasNext()) {
			key = keys.next();
			mapPieChart = new HashMap<Object,Object>(); mapPieChart.put("label", key); mapPieChart.put("y", mapEmployment.get(key)*100); listPieChart.add(mapPieChart);			
			i++;
		}
		 
		String dataPointsPieChart = gsonObjPieChart.toJson(listPieChart);	
%>    
   
<head>
    <meta charset="utf-8">    
    <!-- Favicon icon <link rel="icon" type="image/png" sizes="16x16" href="../assets/images/favicon.png">-->    
    <title>Account - Whork</title>
    <!-- Custom CSS -->
    <link href="../assets/extra-libs/c3/c3.min.css" rel="stylesheet">
    <link href="../assets/libs/chartist/dist/chartist.min.css" rel="stylesheet">
    <link href="../assets/extra-libs/jvector/jquery-jvectormap-2.0.2.css" rel="stylesheet" />
    <!-- Custom CSS -->
    <link href="../dist/css/style.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="css/whork.css">
	<link rel="preconnect" href="https://fonts.gstatic.com">
 	<link href="https://fonts.googleapis.com/css2?family=Kameron&display=swap" rel="stylesheet">
 	<link href="https://fonts.googleapis.com/css2?family=Bungee+Shade&display=swap" rel="stylesheet">
 	<link rel="stylesheet" href="css/accountAdminCompany.css">
 	
 	 <!-- All Jquery -->
    <!-- ============================================================== -->
    <script src="../assets/libs/jquery/dist/jquery.min.js"></script>
    <script src="../assets/libs/popper.js/dist/umd/popper.min.js"></script>
    <script src="../assets/libs/bootstrap/dist/js/bootstrap.min.js"></script>
    <!-- apps -->
    <!-- apps     <script src="../dist/js/app-style-switcher.js"></script>
        <script src="../dist/js/sidebarmenu.js"></script>
    
    -->
    <script src="../dist/js/feather.min.js"></script>
    <script src="../assets/libs/perfect-scrollbar/dist/perfect-scrollbar.jquery.min.js"></script>
    <!--Custom JavaScript -->
    <script src="../dist/js/custom.min.js"></script>
    <!--This page JavaScript -->
    <script src="../assets/extra-libs/c3/d3.min.js"></script>
    <script src="../assets/extra-libs/c3/c3.min.js"></script>
    <script src="../assets/libs/chartist/dist/chartist.min.js"></script>
    <script src="../assets/libs/chartist-plugin-tooltips/dist/chartist-plugin-tooltip.min.js"></script>
    <script src="../assets/extra-libs/jvector/jquery-jvectormap-2.0.2.min.js"></script>
    <script src="../assets/extra-libs/jvector/jquery-jvectormap-world-mill-en.js"></script>
    <script src="../dist/js/pages/dashboards/dashboard1.min.js"></script>
    
    <script src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>
    <script src="js/accountAdmin.js"></script>
    <link rel="stylesheet" href="css/account.css">
    
	<script type="text/javascript">
		window.onload = function() { 
			 
			var chart = new CanvasJS.Chart("chartContainer", {
				theme: "light2",
				title: {
					text: ""
				},
				subtitles: [{
					text: ""
				}],
				axisY: {
					title: "",
					labelFormatter: addSymbols
				},
				data: [{
					type: "bar",
					indexLabel: "{y}",
					indexLabelFontColor: "#444",
					indexLabelPlacement: "inside",
					dataPoints: <%out.print(dataPoints);%>			
				}]
			});
			chart.render();
			 
			function addSymbols(e) {
				var suffixes = ["", "K", "M", "B"];
			 
				var order = Math.max(Math.floor(Math.log(e.value) / Math.log(1000)), 0);
				if(order > suffixes.length - 1)
					order = suffixes.length - 1;
			 
				var suffix = suffixes[order];
				return CanvasJS.formatNumber(e.value / Math.pow(1000, order)) + suffix;
			}
		
		
			var chartPieChart = new CanvasJS.Chart("chartContainerPieChart", {
				theme: "light2",
				animationEnabled: true,
				exportFileName: "",
				exportEnabled: false,
				title:{
					text: ""
				},
				data: [{
					type: "pie",
					showInLegend: true,
					legendText: "{label}",
					toolTipContent: "{label}: <strong>{y}%</strong>",
					indexLabel: "{label} {y}%",
					dataPoints: <%out.print(dataPointsPieChart);%>			
				}]
			});	 
			chartPieChart.render();
		 
		}
	</script>
</head>

<body>
    <!-- ============================================================== -->
    <!-- Preloader - style you can find in spinners.css -->
    <!-- ============================================================== -->
    <div class="preloader">
        <div class="lds-ripple">
            <div class="lds-pos"></div>
            <div class="lds-pos"></div>
        </div>
    </div>
    <!-- ============================================================== -->
    <!-- Main wrapper - style you can find in pages.scss -->
    <!-- ============================================================== -->
    <div id="main-wrapper" data-theme="light" data-layout="vertical" data-navbarbg="skin6" data-sidebartype="full"
        data-sidebar-position="fixed" data-header-position="fixed" data-boxed-layout="full">
        <!-- ============================================================== -->
        <!-- Topbar header - style you can find in pages.scss -->
        <!-- ============================================================== -->
        <header class="topbar" data-navbarbg="skin6">
            <nav class="navbar top-navbar navbar-expand-md">
                <div class="navbar-header" data-logobg="skin6">
                    <!-- This is for the sidebar toggle which is visible on mobile only -->
                    <a class="nav-toggler waves-effect waves-light d-block d-md-none" href="javascript:void(0)"><i
                            class="ti-menu ti-close"></i></a>
                    <!-- ============================================================== -->
                    <!-- Logo -->
                    <!-- ============================================================== -->
                    <div class="navbar-brand">
                        <!-- Logo icon -->
                        <a href="index.jsp">
                            <span class="whork"> W<span class="hred">h</span>ork</span>
                        </a>
                    </div>
                    <!-- ============================================================== -->
                    <!-- End Logo -->
                    <!-- ============================================================== -->
                    <!-- ============================================================== -->
                    <!-- Toggle which is visible on mobile only -->
                    <!-- ============================================================== -->
                    <a class="topbartoggler d-block d-md-none waves-effect waves-light" href="javascript:void(0)"
                        data-toggle="collapse" data-target="#navbarSupportedContent"
                        aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation"><i
                            class="ti-more"></i></a>
                </div>
                <!-- ============================================================== -->
                <!-- End Logo -->
                <!-- ============================================================== -->
                <div class="navbar-collapse collapse" id="navbarSupportedContent">
                	<div class="col-7 align-self-center">                                          
                        <div class="d-flex align-items-center">                   
		                    <a class="nav-link dropdown-toggle" href="#" data-toggle="dropdown"
		                        aria-haspopup="true" aria-expanded="false">                        
		                        <span class="ml-2 d-none d-lg-inline-block"><span style="font-size:18pt"><%=name%> <%=surname%></span>
		                        <br><span class="text-dark"><%=userBean.getCompany().getSocialReason()%></span></span>
		                    </a>
                    	</div>
                    </div>                      
                </div>
            </nav>
        </header>
        <!-- ============================================================== -->
        <!-- End Topbar header -->
        <!-- ============================================================== -->
        <!-- ============================================================== -->
        <!-- Left Sidebar - style you can find in sidebar.scss  -->
        <!-- ============================================================== -->
        <aside class="left-sidebar" data-sidebarbg="skin6">
            <!-- Sidebar scroll-->
            <div class="scroll-sidebar" data-sidebarbg="skin6">
                <!-- Sidebar navigation-->
                <nav class="sidebar-nav">
                    <ul id="sidebarnav">
                        <li class="sidebar-item"> <a class="sidebar-link sidebar-link" href="index.jsp"
                                aria-expanded="false"><i data-feather="home" class="feather-icon"></i><span
                                    class="hide-menu">Home</span></a></li>
                        <li class="list-divider"></li>
						
						<%
							if(userBean.isRecruiter()) {
						%>
							<li class="nav-small-cap"><span class="hide-menu">Applications</span></li>
							<li class="sidebar-item"> <a class="sidebar-link sidebar-link" href="post_offer.jsp"
	                                aria-expanded="false"><i data-feather="edit" class="feather-icon"></i><span
	                                    class="hide-menu">Post Offer</span></a></li>
						<%
							}
						%>                        
                        <li class="list-divider"></li>                        
                        <li class="sidebar-item"> <a class="sidebar-link sidebar-link" href="logout"
                                aria-expanded="false"><i data-feather="log-out" class="feather-icon"></i><span
                                    class="hide-menu">Logout</span></a></li>
                    </ul>
                </nav>
                <!-- End Sidebar navigation -->
            </div>
            <!-- End Sidebar scroll-->
        </aside>
        <!-- ============================================================== -->
        <!-- End Left Sidebar - style you can find in sidebar.scss  -->
        <!-- ============================================================== -->
        <!-- ============================================================== -->
        <!-- Page wrapper  -->
        <!-- ============================================================== -->
        <div class="page-wrapper">
            <!-- ============================================================== -->
            <!-- Bread crumb and right sidebar toggle -->
            <!-- ============================================================== -->
            <div class="page-breadcrumb">
                <div class="row">
                    <div class="col-7 align-self-center">                                          
                        <div class="d-flex align-items-center">
                            <nav aria-label="breadcrumb">
                                <ol class="breadcrumb m-0 p-0">
                                    <li class="breadcrumb-item"><h3>Dashboard</h3></li>
                                </ol>
                            </nav>
                        </div>
                    </div>
                    <div class="col-5 align-self-center">                    
                        <div class="customize-input float-right"> 
                        <%
                        	String picture = null; 
                	 		picture = userBean.getPhoto();
                	 		picture = (picture == null) ? Util.InstanceConfig.getString(Util.InstanceConfig.KEY_CTX_DFL_ROOT) + "/" + "avatar4.jpg" : Util.InstanceConfig.getString(Util.InstanceConfig.KEY_CTX_USR_DATA) + "/" + picture;
                        %>
                        	<img src="<%=picture%>" alt="user" class="rounded-circle" width="60">            
                            <a><%=new Date().toString().substring(0, 10)%></a>
                        </div>
                    </div>
                </div>                
                <div class="row">
                    <div class="col-7 align-self-center">
                    
                    </div>                    
                </div>
            </div>
            <!-- ============================================================== -->
            <!-- End Bread crumb and right sidebar toggle -->
            <!-- ============================================================== -->
            <!-- ============================================================== -->
            <!-- Container fluid  -->
            <!-- ============================================================== -->
            <div class="container-fluid">
                <!-- *************************************************************** -->
                <!-- Start First Cards -->
                <!-- *************************************************************** -->
                <div class="card-group">
                    <div class="card border-right">
                        <div class="card-body">
                            <div class="d-flex d-lg-flex d-md-block align-items-center">
                                <div>
                                    <div class="d-inline-flex align-items-center">
                                        <h2 class="text-dark mb-1 font-weight-medium"><%=AccountController.getNumberOfEmployees(userBean)%></h2>                                        
                                    </div>
                                    <h6 class="text-muted font-weight-normal mb-0 w-100 text-truncate">Number of employees of the company on this site</h6>
                                </div>
                                <div class="ml-auto mt-md-3 mt-lg-0">
                                    <span class="opacity-7 text-muted"><i data-feather="user-plus"></i></span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="card border-right">
                        <div class="card-body">
                            <div class="d-flex d-lg-flex d-md-block align-items-center">
                                <div>
                                    <h2 class="text-dark mb-1 w-100 text-truncate font-weight-medium"><%=AccountController.getNumberOfOffers(userBean)%></h2>
                                    <h6 class="text-muted font-weight-normal mb-0 w-100 text-truncate">Number of Offers posted</h6>
                                </div>
                                <div class="ml-auto mt-md-3 mt-lg-0">
                                    <span class="opacity-7 text-muted"><i data-feather="dollar-sign"></i></span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="card border-right">
                        <div class="card-body">
                            <div class="d-flex d-lg-flex d-md-block align-items-center">
                                <div>
                                    <div class="d-inline-flex align-items-center">
                                        <h2 class="text-dark mb-1 font-weight-medium"><%=AccountController.getNumberOfClick(userBean)%></h2>                                        
                                    </div>
                                    <h6 class="text-muted font-weight-normal mb-0 w-100 text-truncate">Total number of clicks</h6>
                                </div>
                                <div class="ml-auto mt-md-3 mt-lg-0">
                                    <span class="opacity-7 text-muted"><i data-feather="file-plus"></i></span>
                                </div>
                            </div>
                        </div>
                    </div>                    
                </div>
                <!-- *************************************************************** -->
                <!-- End First Cards -->
                <!-- *************************************************************** -->
                <!-- *************************************************************** -->
                <!-- Start Sales Charts Section -->
                <!-- *************************************************************** -->
                <div class="row">
                	<%
						if(mapEmployment.size() != 0) {
                    %>
                    <div class="col-lg-5 col-md-12">                       
                        <div class="card">
                            <div class="card-body">
                                <h4 class="card-title">Average Employment Status of Candidate</h4>                                
                                   	<div id="chartContainerPieChart" style="height: 400px; width: 100%;">                               
                                </div>
                            </div>
                        </div>
                    </div>
                    <%
                     	}
                    %>
                    <div class="col-lg-5 col-md-12">
                        <div class="card">
                            <div class="card-body">
                                <h4 class="card-title">Number of Candidate</h4>
								<div id="chartContainer" style="height: 400px; width: 100%;"></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-4 col-md-12">
                        <div class="card">
                            <div class="card-body">
                                <h4 class="card-title mb-4">Candidate by Location</h4>
                                <div class="" style="height:300px">
                                    <div id="visitbylocate" style="height:100%"></div>
                                </div>
                                
                                <%
                                Map<String, Double> mapCountry = AccountController.getCountryCandidateByFiscalCode(userBean.getCompany());
                        		keys = mapCountry.keySet().iterator();
                        		i=0;
                        		int j = 0;
                        		key = null;
                        		
                        		List<String> listColors = new ArrayList<>();
                        		listColors.add("primary"); listColors.add("danger"); listColors.add("cyan"); listColors.add("success");
                        		
                        		if(mapCountry.size() == 0) {
                        		%>
                        			<div class="row mb-3 align-items-center mt-1 mt-5">
                                    	<div class="col-4 text-right">
                                        	<span class="text-muted font-14">There are not candidate for your offers</span>
                                    	</div>
                                    	<div class="col-5">
                                        	<div class="progress" style="height: 5px;">
                                           		<div class="progress-bar bg-danger" role="progressbar" style="width: 100%"
                                                	aria-valuenow="25" aria-valuemin="0" aria-valuemax="100"></div>
                                        	</div>
                                    	</div>                                    	
                                	</div>
                        		<%
                        		}                        		
                        		
                        		while(keys.hasNext()) {
                        			key = keys.next();
                        			
                        		%>
                        			
                        			<div class="row mb-3 align-items-center mt-1 mt-5">
                                    	<div class="col-4 text-right">
                                        	<span class="text-muted font-14"><%=key%></span>
                                    	</div>
                                    	<div class="col-5">
                                        	<div class="progress" style="height: 5px;">
                                           		<div class="progress-bar bg-<%=listColors.get(j)%>" role="progressbar" style="width: 100%"
                                                	aria-valuenow="25" aria-valuemin="0" aria-valuemax="100"></div>
                                        	</div>
                                    	</div>
                                    	<div class="col-3 text-right">
                                        	<span class="mb-0 font-14 text-dark font-weight-medium"><%=(int)Math.round(mapCountry.get(key)*100)%>%</span>
                                    	</div>
                                	</div>
                        			
                        			
                        		<%
                        			i++;
                        			j++;
                        			j = (j>i) ? 0 : j;
                        		}                        		
                                %>                                
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6 col-lg-5">
                      <div class="card">
                        <div class="card-body">
                       	 <div class="card-box ribbon-box">
						  <div class="ribbon ribbon-primary">Messages</div>
                    	   <div class="clearfix"></div>
                    		<div class="inbox-widget">
                   			 <div data-spy="scroll" data-target="#navbar-example2" data-offset="0"
                           	 class="position-relative mt-2" style="height: 300px; overflow: auto;">   
                           	 
                           	 <%
                           	 	List<ChatLogEntryBean> listChat = AccountController.getLastMessage(email);                           	    
                           	 	if(listChat.size() == 0) {
                           	 %>
                           	 		<h3> There are not messages here! </h3> 
                           	 <%
                           	 	} else {
	                        	 	for(i=0; i<listChat.size(); i++) {
	                           	 		String nameChat = (!listChat.get(i).getSenderEmail().equals(email)) ? listChat.get(i).getSenderEmail() : listChat.get(i).getReceiverEmail();
	                           	 		String date = new Date(listChat.get(i).getDeliveryRequestTime()).toString();	                           	 			                           	 		 
	                           	 		
	                           	 		UserBean userPicture = AccountController.getPictureForMessage(nameChat);	                           	 		
	                           	 		picture = (userPicture != null) ? userPicture.getPhoto() : null;
	                           	 		picture = (picture == null) ? Util.InstanceConfig.getString(Util.InstanceConfig.KEY_CTX_DFL_ROOT) + "/" + "avatar2.png" : Util.InstanceConfig.getString(Util.InstanceConfig.KEY_CTX_USR_DATA) + "/" + picture;
	                         %>
	                           	 		<a href="#">
				                          <div class="inbox-item">		
				                               <div class="inbox-item-img"><img src='<%=picture%>' class="rounded-circle" alt=""></div>
				                               <p class="inbox-item-author"><%=nameChat%></p>
				                               <p class="inbox-item-text"><%=listChat.get(i).getText()%></p>
				                               <p class="inbox-item-date">
				                               	   <%=date.substring(0, date.length()-13)%>
				                                   <button class="btn btn-icon btn-sm waves-effect waves-light btn-success"
				                                       onclick="window.open('/chat.jsp?toEmail=<%=nameChat%>','Chat - Whork', width=674, height=634);">Reply</button>
				                               </p>
				                          </div>
                                        </a>
				             <%
	                         		}
                           	 	}
	                         %>

                         	</div>	                                             
	                      </div>
	                     </div>
                        </div>
                       </div>
                    </div>
                </div>
                <!-- *************************************************************** -->
                <!-- End Sales Charts Section -->
                <!-- *************************************************************** -->
<%
	String descError = (String) request.getSession().getAttribute("descriptive_error");
 	String success = (String) request.getSession().getAttribute("change_alert");
	
 	if(descError != null) {
%>
		<div class="alert alert-danger" role="alert">
			<%=descError%>
		</div> 
<%
	} if(success != null) {
%>		
		<div class="alert alert-success" role="alert">
			<%=success%>
		</div>
<%	
	}
	request.getSession().setAttribute("descriptive_error", null);
	request.getSession().setAttribute("change_alert", null);
%>
				<!-- *************************************************************** -->
                <!-- Start Top Leader Table -->
                <!-- *************************************************************** -->
                <div class="row">
                    <div class="col-12">
                        <div class="card">
                            <div class="card-body">
                                <div class="d-flex align-items-center mb-4">
                                    <h4 class="card-title">Recruiters</h4>
                                    <div class="ml-auto">
                                        <div class="dropdown sub-dropdown">
                                            <button class="btn btn-link text-muted dropdown-toggle" type="button"
                                                id="dd1" data-toggle="dropdown" aria-haspopup="true"
                                                aria-expanded="false">
                                                <i data-feather="more-vertical"></i>
                                            </button>                                            
                                            <div class="dropdown-menu dropdown-menu-right" aria-labelledby="dd1">                                                
                                                <a class="dropdown-item" onclick="addRecruiter()">Add Recruiter</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="table-responsive">
                                <form action="accountServlet" enctype="multipart/form-data" method="post">
                                    <span id="addRecruiterForm"></span>
                                    
                                    <button id="addRecruiterButtonSubmit" name="addRecruiterButtonSubmit" type="submit" class="btn btn-info " style="display:none" >Submit</button>
                                    <button id="addRecruiterButtonCancel" name="addRecruiterButtonCancel" onclick="addRecruiter()" class="btn btn-info " style="display:none" >Cancel</button>
                                </form>
                                    <table class="table no-wrap v-middle mb-0">
                                        <thead>
                                            <tr class="border-0">
                                                <th class="border-0 font-14 font-weight-medium text-muted">Recruiter</th>
                                                <th class="border-0 font-14 font-weight-medium text-muted">Fiscal Code</th>
                                                <th class="border-0 font-14 font-weight-medium text-muted">Number of Posts</th>
                                                <th class="border-0 font-14 font-weight-medium text-muted">Phone Number</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        
                                        <%
                                        	Map<String, UserBean> mapRecruiter = AccountController.getEmployeeByCompanyVAT(userBean.getCompany());                                    		
                                    		
                                    		keys = mapRecruiter.keySet().iterator();                                    		
                                    		
                                    		while(keys.hasNext()) {
                                    			key = keys.next();                                    			
                                    			UserBean userPicture = AccountController.getPictureForMessageByCf(mapRecruiter.get(key).getCf());                                    			
        	                           	 		picture = (userPicture != null) ? userPicture.getPhoto() : null;        	                           	 		
        	                           	 		picture = (picture == null) ? Util.InstanceConfig.getString(Util.InstanceConfig.KEY_CTX_DFL_ROOT) + "/" + "avatar3.jpg" : Util.InstanceConfig.getString(Util.InstanceConfig.KEY_CTX_USR_DATA) + "/" + picture;
                                    	%>
	                                            <tr>
	                                                <td class="border-top-0 px-2 py-4">
	                                                    <div class="d-flex no-block align-items-center">
	                                                        <div class="mr-3"><img src='<%=picture%>'
	                                                                alt="user" class="rounded-circle" width="45" height="45"/></div>
	                                                        <div class="">
	                                                            <h5 class="text-dark mb-0 font-16 font-weight-medium"><%=mapRecruiter.get(key).getName() + " " + mapRecruiter.get(key).getSurname()%></h5>
	                                                            <span class="text-muted font-14"><%=key%></span>
	                                                        </div>
	                                                    </div>
	                                                </td>
	                                                <td class="border-top-0 text-muted px-2 py-4 font-14"><%=mapRecruiter.get(key).getCf()%></td>                                                
	                                                <td class="border-top-0 text-muted px-4 py-4 font-14"><%=AccountController.getNumberOfferOfAnEmployee(mapRecruiter.get(key))%></td>                                                
	                                                <td class="border-top-0 text-muted px-4 py-4 font-14"><%=mapRecruiter.get(key).getPhoneNumber()%></td>                                                
	                                            </tr>
                                            
										<%
                                    		}
										%>                                                                                       
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- *************************************************************** -->
                <!-- End Top Leader Table -->
                <!-- *************************************************************** -->
            </div>
            <!-- ============================================================== -->
            <!-- End Container fluid  -->
            <!-- ============================================================== -->
            <!-- ============================================================== -->            
        </div>
        <!-- ============================================================== -->
        <!-- End Page wrapper  -->
        <!-- ============================================================== -->
    </div>
    <!-- ============================================================== -->
    <!-- End Wrapper -->
    <!-- ============================================================== -->
    <!-- End Wrapper -->
    <!-- ============================================================== -->   
</body> 

<%		
	} else if(!userBean.isAdmin() && userBean.isRecruiter()){

		Gson gsonObj = new Gson();
		Map<Object,Object> map = null;
		List<Map<Object,Object>> list = new ArrayList<Map<Object,Object>>();
		List<Integer> listCandidatureByVat = CandidatureController.getCandidatureByCompanyVat(userBean.getCompany());
		List<String> listMonth = new ArrayList<>();
		listMonth.add("Jan");
		listMonth.add("Feb");
		listMonth.add("Mar");
		listMonth.add("Apr");
		listMonth.add("May");
		listMonth.add("Jun");
		listMonth.add("Jul");
		listMonth.add("Aug");
		listMonth.add("Sep");
		listMonth.add("Oct");
		listMonth.add("Nov");
		listMonth.add("Dec");
		
		for(int i=0; i<listCandidatureByVat.size(); i++) {			
			map = new HashMap<Object,Object>(); map.put("label", listMonth.get(i)); map.put("y", listCandidatureByVat.get(i)); list.add(map);		
		}
		 
		String dataPoints = gsonObj.toJson(list);
		
		
		Gson gsonObjPieChart = new Gson();
		Map<Object,Object> mapPieChart = null;
		List<Map<Object,Object>> listPieChart = new ArrayList<Map<Object,Object>>();
		
		Map<String, Double> mapEmployment = AccountController.getEmploymentStatusBtCompanyVAT(userBean.getCompany());
		Iterator<String> keys = mapEmployment.keySet().iterator();
		int i=0;
		String key = null;
		
		if(keys.hasNext()) {
			key = keys.next();	
			mapPieChart = new HashMap<Object,Object>(); mapPieChart.put("label", key); mapPieChart.put("y", mapEmployment.get(key)*100); mapPieChart.put("exploded", true); listPieChart.add(mapPieChart);		
		}
		
		while(keys.hasNext()) {
			key = keys.next();
			mapPieChart = new HashMap<Object,Object>(); mapPieChart.put("label", key); mapPieChart.put("y", mapEmployment.get(key)*100); listPieChart.add(mapPieChart);			
			i++;
		}
		 
		String dataPointsPieChart = gsonObjPieChart.toJson(listPieChart);	
%>


<head>
    <meta charset="utf-8">    
    <!-- Favicon icon <link rel="icon" type="image/png" sizes="16x16" href="../assets/images/favicon.png">-->    
    <title>Account - Whork</title>
    <!-- Custom CSS -->
    <link href="../assets/extra-libs/c3/c3.min.css" rel="stylesheet">
    <link href="../assets/libs/chartist/dist/chartist.min.css" rel="stylesheet">
    <link href="../assets/extra-libs/jvector/jquery-jvectormap-2.0.2.css" rel="stylesheet" />
    <!-- Custom CSS -->
    <link href="../dist/css/style.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="css/whork.css">
	<link rel="preconnect" href="https://fonts.gstatic.com">
 	<link href="https://fonts.googleapis.com/css2?family=Kameron&display=swap" rel="stylesheet">
 	<link href="https://fonts.googleapis.com/css2?family=Bungee+Shade&display=swap" rel="stylesheet">
 	<link rel="stylesheet" href="css/accountAdminCompany.css">
 	
 	 <!-- All Jquery     <script src="../assets/libs/bootstrap/dist/js/bootstrap.min.js"></script>
 	 -->
    <!-- ============================================================== -->
    <script src="../assets/libs/jquery/dist/jquery.min.js"></script>
    <script src="../assets/libs/popper.js/dist/umd/popper.min.js"></script>
    <!-- apps -->
    <!-- apps     <script src="../dist/js/app-style-switcher.js"></script>
        <script src="../dist/js/sidebarmenu.js"></script>
    
    -->
    <script src="../dist/js/feather.min.js"></script>
    <script src="../assets/libs/perfect-scrollbar/dist/perfect-scrollbar.jquery.min.js"></script>
    <!--Custom JavaScript -->
    <script src="../dist/js/custom.min.js"></script>
    <!--This page JavaScript -->
    <script src="../assets/extra-libs/c3/d3.min.js"></script>
    <script src="../assets/extra-libs/c3/c3.min.js"></script>
    <script src="../assets/libs/chartist/dist/chartist.min.js"></script>
    <script src="../assets/libs/chartist-plugin-tooltips/dist/chartist-plugin-tooltip.min.js"></script>
    <script src="../assets/extra-libs/jvector/jquery-jvectormap-2.0.2.min.js"></script>
    <script src="../assets/extra-libs/jvector/jquery-jvectormap-world-mill-en.js"></script>
    <script src="../dist/js/pages/dashboards/dashboard1.min.js"></script>
    
    <script src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>
    <script src="js/accountAdmin.js"></script>
    <link rel="stylesheet" href="css/account.css">
    
	<script type="text/javascript">
		window.onload = function() { 
			 
			var chart = new CanvasJS.Chart("chartContainer", {
				theme: "light2",
				title: {
					text: ""
				},
				subtitles: [{
					text: ""
				}],
				axisY: {
					title: "",
					labelFormatter: addSymbols
				},
				data: [{
					type: "bar",
					indexLabel: "{y}",
					indexLabelFontColor: "#444",
					indexLabelPlacement: "inside",
					dataPoints: <%out.print(dataPoints);%>			
				}]
			});
			chart.render();
			 
			function addSymbols(e) {
				var suffixes = ["", "K", "M", "B"];
			 
				var order = Math.max(Math.floor(Math.log(e.value) / Math.log(1000)), 0);
				if(order > suffixes.length - 1)
					order = suffixes.length - 1;
			 
				var suffix = suffixes[order];
				return CanvasJS.formatNumber(e.value / Math.pow(1000, order)) + suffix;
			}
		
		
			var chartPieChart = new CanvasJS.Chart("chartContainerPieChart", {
				theme: "light2",
				animationEnabled: true,
				exportFileName: "",
				exportEnabled: false,
				title:{
					text: ""
				},
				data: [{
					type: "pie",
					showInLegend: true,
					legendText: "{label}",
					toolTipContent: "{label}: <strong>{y}%</strong>",
					indexLabel: "{label} {y}%",
					dataPoints: <%out.print(dataPointsPieChart);%>			
				}]
			});	 
			chartPieChart.render();
		 
		}
	</script>
</head>

<body>
    <!-- ============================================================== -->
    <!-- Preloader - style you can find in spinners.css -->
    <!-- ============================================================== -->
    <div class="preloader">
        <div class="lds-ripple">
            <div class="lds-pos"></div>
            <div class="lds-pos"></div>
        </div>
    </div>
    <!-- ============================================================== -->
    <!-- Main wrapper - style you can find in pages.scss -->
    <!-- ============================================================== -->
    <div id="main-wrapper" data-theme="light" data-layout="vertical" data-navbarbg="skin6" data-sidebartype="full"
        data-sidebar-position="fixed" data-header-position="fixed" data-boxed-layout="full">
        <!-- ============================================================== -->
        <!-- Topbar header - style you can find in pages.scss -->
        <!-- ============================================================== -->
        <header class="topbar" data-navbarbg="skin6">
            <nav class="navbar top-navbar navbar-expand-md">
                <div class="navbar-header" data-logobg="skin6">
                    <!-- This is for the sidebar toggle which is visible on mobile only -->
                    <a class="nav-toggler waves-effect waves-light d-block d-md-none" href="javascript:void(0)"><i
                            class="ti-menu ti-close"></i></a>
                    <!-- ============================================================== -->
                    <!-- Logo -->
                    <!-- ============================================================== -->
                    <div class="navbar-brand">
                        <!-- Logo icon -->
                        <a href="index.jsp">
                            <span class="whork"> W<span class="hred">h</span>ork</span>
                        </a>
                    </div>
                    <!-- ============================================================== -->
                    <!-- End Logo -->
                    <!-- ============================================================== -->
                    <!-- ============================================================== -->
                    <!-- Toggle which is visible on mobile only -->
                    <!-- ============================================================== -->
                    <a class="topbartoggler d-block d-md-none waves-effect waves-light" href="javascript:void(0)"
                        data-toggle="collapse" data-target="#navbarSupportedContent"
                        aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation"><i
                            class="ti-more"></i></a>
                </div>
                <!-- ============================================================== -->
                <!-- End Logo -->
                <!-- ============================================================== -->
                <div class="navbar-collapse collapse" id="navbarSupportedContent">
                	<div class="col-7 align-self-center">                                          
                        <div class="d-flex align-items-center">                   
		                    <a class="nav-link dropdown-toggle" href="#" data-toggle="dropdown"
		                        aria-haspopup="true" aria-expanded="false">                        
		                        <span class="ml-2 d-none d-lg-inline-block"><span style="font-size:18pt"><%=name%> <%=surname%></span>
		                        <br><span class="text-dark"><%=userBean.getCompany().getSocialReason()%></span></span>
		                    </a>
                    	</div>
                    </div>                      
                </div>
            </nav>
        </header>
        <!-- ============================================================== -->
        <!-- End Topbar header -->
        <!-- ============================================================== -->
        <!-- ============================================================== -->
        <!-- Left Sidebar - style you can find in sidebar.scss  -->
        <!-- ============================================================== -->
        <aside class="left-sidebar" data-sidebarbg="skin6">
            <!-- Sidebar scroll-->
            <div class="scroll-sidebar" data-sidebarbg="skin6">
                <!-- Sidebar navigation-->
                <nav class="sidebar-nav">
                    <ul id="sidebarnav">
                        <li class="sidebar-item"> <a class="sidebar-link sidebar-link" href="index.jsp"
                                aria-expanded="false"><i data-feather="home" class="feather-icon"></i><span
                                    class="hide-menu">Home</span></a></li>
                        <li class="list-divider"></li>
                        <li class="nav-small-cap"><span class="hide-menu">Applications</span></li>

						<li class="sidebar-item"> <a class="sidebar-link sidebar-link" href="post_offer.jsp"
                                aria-expanded="false"><i data-feather="edit" class="feather-icon"></i><span
                                    class="hide-menu">Post Offer</span></a></li>
                        
                        <li class="list-divider"></li>                        
                        <li class="sidebar-item"> <a class="sidebar-link sidebar-link" href="logout"
                                aria-expanded="false"><i data-feather="log-out" class="feather-icon"></i><span
                                    class="hide-menu">Logout</span></a></li>
                    </ul>
                </nav>
                <!-- End Sidebar navigation -->
            </div>
            <!-- End Sidebar scroll-->
        </aside>
        <!-- ============================================================== -->
        <!-- End Left Sidebar - style you can find in sidebar.scss  -->
        <!-- ============================================================== -->
        <!-- ============================================================== -->
        <!-- Page wrapper  -->
        <!-- ============================================================== -->
        <div class="page-wrapper">
            <!-- ============================================================== -->
            <!-- Bread crumb and right sidebar toggle -->
            <!-- ============================================================== -->
            <div class="page-breadcrumb">
                <div class="row">
                    <div class="col-7 align-self-center">                                          
                        <div class="d-flex align-items-center">
                            <nav aria-label="breadcrumb">
                                <ol class="breadcrumb m-0 p-0">
                                    <li class="breadcrumb-item"><h3>Dashboard</h3></li>
                                </ol>
                            </nav>
                        </div>
                    </div>
                    <div class="col-5 align-self-center">                    
                        <div class="customize-input float-right"> 
                        <%
                        	String picture = null; 
                	 		picture = userBean.getPhoto();
                	 		picture = (picture == null) ? Util.InstanceConfig.getString(Util.InstanceConfig.KEY_CTX_DFL_ROOT) + "/" + "avatar4.jpg" : Util.InstanceConfig.getString(Util.InstanceConfig.KEY_CTX_USR_DATA) + "/" + picture;
                        %>
                        	<img src="<%=picture%>" alt="user" class="rounded-circle" width="60">            
                            <a><%=new Date().toString().substring(0, 10)%></a>
                        </div>
                    </div>
                </div>                
                <div class="row">
                    <div class="col-7 align-self-center">
                    
                    </div>                    
                </div>
            </div>
            <!-- ============================================================== -->
            <!-- End Bread crumb and right sidebar toggle -->
            <!-- ============================================================== -->
            <!-- ============================================================== -->
            <!-- Container fluid  -->
            <!-- ============================================================== -->
            <div class="container-fluid">
                <!-- *************************************************************** -->
                <!-- Start First Cards -->
                <!-- *************************************************************** -->
                <div class="card-group">
                    <div class="card border-right">
                        <div class="card-body">
                            <div class="d-flex d-lg-flex d-md-block align-items-center">
                                <div>
                                    <div class="d-inline-flex align-items-center">
                                        <h2 class="text-dark mb-1 font-weight-medium"><%=AccountController.getNumberOfEmployees(userBean)%></h2>                                        
                                    </div>
                                    <h6 class="text-muted font-weight-normal mb-0 w-100 text-truncate">Number of employees of the company on this site</h6>
                                </div>
                                <div class="ml-auto mt-md-3 mt-lg-0">
                                    <span class="opacity-7 text-muted"><i data-feather="user-plus"></i></span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="card border-right">
                        <div class="card-body">
                            <div class="d-flex d-lg-flex d-md-block align-items-center">
                                <div>
                                    <h2 class="text-dark mb-1 w-100 text-truncate font-weight-medium"><%=AccountController.getNumberOfferOfAnEmployee(userBean)%></h2>
                                    <h6 class="text-muted font-weight-normal mb-0 w-100 text-truncate">Number of Offers posted</h6>
                                </div>
                                <div class="ml-auto mt-md-3 mt-lg-0">
                                    <span class="opacity-7 text-muted"><i data-feather="dollar-sign"></i></span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="card border-right">
                        <div class="card-body">
                            <div class="d-flex d-lg-flex d-md-block align-items-center">
                                <div>
                                    <div class="d-inline-flex align-items-center">
                                        <h2 class="text-dark mb-1 font-weight-medium"><%=AccountController.getNUmberClickOfAnEmployee(userBean)%></h2>                                        
                                    </div>
                                    <h6 class="text-muted font-weight-normal mb-0 w-100 text-truncate">Total number of clicks</h6>
                                </div>
                                <div class="ml-auto mt-md-3 mt-lg-0">
                                    <span class="opacity-7 text-muted"><i data-feather="file-plus"></i></span>
                                </div>
                            </div>
                        </div>
                    </div>                    
                </div>
                <!-- *************************************************************** -->
                <!-- End First Cards -->
                <!-- *************************************************************** -->
                <!-- *************************************************************** -->
                <!-- Start Sales Charts Section -->
                <!-- *************************************************************** -->
                <div class="row">
                	<%
						if(mapEmployment.size() != 0) {
                    %>
                    <div class="col-lg-5 col-md-12">                       
                        <div class="card">
                            <div class="card-body">
                                <h4 class="card-title">Average Employment Status of Candidate</h4>                                
                                   	<div id="chartContainerPieChart" style="height: 400px; width: 100%;">                               
                                </div>
                            </div>
                        </div>
                    </div>
                    <%
                     	}
                    %>
                    <div class="col-lg-5 col-md-12">
                        <div class="card">
                            <div class="card-body">
                                <h4 class="card-title">Number of Candidate</h4>
								<div id="chartContainer" style="height: 400px; width: 100%;"></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-4 col-md-12">
                        <div class="card">
                            <div class="card-body">
                                <h4 class="card-title mb-4">Candidate by Location</h4>
                                <div class="" style="height:300px">
                                    <div id="visitbylocate" style="height:100%"></div>
                                </div>
                                
                                <%
                                Map<String, Double> mapCountry = AccountController.getCountryCandidateByFiscalCode(userBean.getCompany());
                        		keys = mapCountry.keySet().iterator();
                        		i=0;
                        		int j = 0;
                        		key = null;
                        		
                        		List<String> listColors = new ArrayList<>();
                        		listColors.add("primary"); listColors.add("danger"); listColors.add("cyan"); listColors.add("success");
                        		
                        		if(mapCountry.size() == 0) {
                        		%>
                        			<div class="row mb-3 align-items-center mt-1 mt-5">
                                    	<div class="col-4 text-right">
                                        	<span class="text-muted font-14">There are not candidate for your offers</span>
                                    	</div>
                                    	<div class="col-5">
                                        	<div class="progress" style="height: 5px;">
                                           		<div class="progress-bar bg-danger" role="progressbar" style="width: 100%"
                                                	aria-valuenow="25" aria-valuemin="0" aria-valuemax="100"></div>
                                        	</div>
                                    	</div>                                    	
                                	</div>
                        		<%
                        		}                        		
                        		
                        		while(keys.hasNext()) {
                        			key = keys.next();
                        			
                        		%>
                        			
                        			<div class="row mb-3 align-items-center mt-1 mt-5">
                                    	<div class="col-4 text-right">
                                        	<span class="text-muted font-14"><%=key%></span>
                                    	</div>
                                    	<div class="col-5">
                                        	<div class="progress" style="height: 5px;">
                                           		<div class="progress-bar bg-<%=listColors.get(j)%>" role="progressbar" style="width: 100%"
                                                	aria-valuenow="25" aria-valuemin="0" aria-valuemax="100"></div>
                                        	</div>
                                    	</div>
                                    	<div class="col-3 text-right">
                                        	<span class="mb-0 font-14 text-dark font-weight-medium"><%=(int)Math.round(mapCountry.get(key)*100)%>%</span>
                                    	</div>
                                	</div>
                        			
                        			
                        		<%
                        			i++;
                        			j++;
                        			j = (j>i) ? 0 : j;
                        		}                        		
                                %>                                
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6 col-lg-5">
                      <div class="card">
                        <div class="card-body">
                       	 <div class="card-box ribbon-box">
						  <div class="ribbon ribbon-primary">Messages</div>
                    	   <div class="clearfix"></div>
                    		<div class="inbox-widget">
                   			 <div data-spy="scroll" data-target="#navbar-example2" data-offset="0"
                           	 class="position-relative mt-2" style="height: 300px; overflow: auto;">   
                           	 
                           	 <%
                           	 	List<ChatLogEntryBean> listChat = AccountController.getLastMessage(email);                           	    
                           	 	if(listChat.size() == 0) {
                           	 %>
                           	 		<h3> There are not messages here! </h3> 
                           	 <%
                           	 	} else {
	                        	 	for(i=0; i<listChat.size(); i++) {
	                           	 		String nameChat = (!listChat.get(i).getSenderEmail().equals(email)) ? listChat.get(i).getSenderEmail() : listChat.get(i).getReceiverEmail();
	                           	 		String date = new Date(listChat.get(i).getDeliveryRequestTime()).toString();	                           	 			                           	 		 
	                           	 		
	                           	 		UserBean userPicture = AccountController.getPictureForMessage(nameChat);                           	 		
	                           	 		picture = (userPicture != null) ? userPicture.getPhoto() : null;
	                           	 		picture = (picture == null) ? Util.InstanceConfig.getString(Util.InstanceConfig.KEY_CTX_DFL_ROOT) + "/" + "avatar2.png" : Util.InstanceConfig.getString(Util.InstanceConfig.KEY_CTX_USR_DATA) + "/" + picture;
	                         %>
	                           	 		<a href="#">
				                          <div class="inbox-item">		
				                               <div class="inbox-item-img"><img src='<%=picture%>' class="rounded-circle" alt=""></div>
				                               <p class="inbox-item-author"><%=nameChat%></p>
				                               <p class="inbox-item-text"><%=listChat.get(i).getText()%></p>
				                               <p class="inbox-item-date">
				                               	   <%=date.substring(0, date.length()-13)%>
				                                   <button class="btn btn-icon btn-sm waves-effect waves-light btn-success"
				                                       onclick="window.open('/chat.jsp?toEmail=<%=nameChat%>','Chat - Whork', width=674, height=634);">Reply</button>
				                               </p>
				                          </div>
                                        </a>
				             <%
	                         		}
                           	 	}
	                         %>

                         	</div>	                                             
	                      </div>
	                     </div>
                        </div>
                       </div>
                    </div>
                </div>
                <!-- *************************************************************** -->
                <!-- End Sales Charts Section -->
                <!-- *************************************************************** -->                
            </div>
            <!-- ============================================================== -->
            <!-- End Container fluid  -->
            <!-- ============================================================== -->
            <!-- ============================================================== -->            
        </div>
        <!-- ============================================================== -->
        <!-- End Page wrapper  -->
        <!-- ============================================================== -->
    </div>
    <!-- ============================================================== -->
    <!-- End Wrapper -->
    <!-- ============================================================== -->
    <!-- End Wrapper -->
    <!-- ============================================================== -->   
</body>	
		
<%		
	} else {	

	String fullName = name.concat(" ").concat(surname);
	String phone = userBean.getPhoneNumber();
	String cf = userBean.getCf();
	String address = userBean.getHomeAddress();	
	
	if(userBean.getWebsite() == null) userBean.setWebsite("https://whork.it");
	if(userBean.getTwitter() == null) userBean.setTwitter("whork");
	if(userBean.getFacebook() == null) userBean.setFacebook("whork");
	if(userBean.getInstagram() == null) userBean.setInstagram("whork");

	String website = userBean.getWebsite();
	String twitter = userBean.getTwitter();
	String facebook = userBean.getFacebook();
	String instagram = userBean.getInstagram();
	
	String bio = userBean.getBiography(); 
%>

    <head>
    	<link rel="stylesheet" href="css/account.css">
    	<title>User Profile - Whork</title>
	    <meta name="viewport" content="width=device-width, initial-scale=1">
		<script src="https://code.jquery.com/jquery-1.10.2.min.js"></script>
	    <link href="https://cdn.jsdelivr.net/npm/bootstrap@4.4.1/dist/css/bootstrap.min.css" rel="stylesheet">
		<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.4.1/dist/js/bootstrap.bundle.min.js"></script>
		<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">
		<script src="js/editInfoAccount.js"></script>
		
		<link rel="shortcut icon" href="../favicon.ico"> 
		<link rel="stylesheet" href="css/default.css" />
		<link rel="stylesheet" href="css/component.css" />
		<script src="js/modernizr.custom.js"></script>
    </head>
    
    
    <body>   
     <form enctype="multipart/form-data" action="accountServlet" method="post"> 
	   <div class="container">
	    <div class="main-body">	     
	    
	          <!-- Breadcrumb -->
	          <nav aria-label="breadcrumb" class="main-breadcrumb">
	            <ol class="breadcrumb">
	              <li class="breadcrumb-item"><a href="/index.jsp">Home</a></li>
	              <li class="breadcrumb-item active" aria-current="page">User Profile</li>
	            </ol>
	          </nav>
	          <!-- /Breadcrumb -->

 <%
	String descError = (String) request.getSession().getAttribute("descriptive_error");
 	String success = (String) request.getSession().getAttribute("change_alert");
	
 	if(descError != null) {
%>
		<div class="alert alert-danger" role="alert">
			<%=descError%>
		</div> 
<%
	} if(success != null) {
%>		
		<div class="alert alert-success" role="alert">
			<%=success%>
		</div>
<%	
	}
	request.getSession().setAttribute("descriptive_error", null);
	request.getSession().setAttribute("change_alert", null);
%> 
	          <div class="row gutters-sm">
	            <div class="col-md-4 mb-3">
	              <div class="card">
	                <div class="card-body">	                
	                  <div class="d-flex flex-column align-items-center text-center">
	                  <a href="/index.jsp">
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
 </a> 
                    <div class="mt-3">
                      <label class="btn btn-default btn-file">                      	
                     	Change Picture 
                     	<a onclick='changePicture()'>
                     	  <input type="file" accept=".png, .jpg, .jpeg" name="changePhotoInput" style="display:none">
                     	  <button id="changePictureForm" name="changePicture" type="submit" class="btn btn-info " style="display:none" >Submit</button>                     	                       	
                     	</a>
					  </label>
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
                    <a id="website" href="<%= website %>" class="text-secondary"><%= website %></a>
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
                     	<button id="editSocialAccountForm" name="editSocialAccountForm" type="submit" class="btn btn-info " style="display:none" >Submit</button>
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
                      <button id="editInfoButton" name="editInfoButton" type="submit" class="btn btn-info " style="display:none" >Submit</button>
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
                  <a class="btn btn-info " href="/index.jsp">home</a>
                  <a class="btn btn-info "
                      onclick="window.open('/chat.jsp','Chat - Whork', width=674, height=634);">chat</a>
                  <a class="btn btn-info " href="/logout">logout</a>                 
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
	                  <button id="editBioButton" name="editBioButton" class="btn btn-info " style="display:none" >Submit</button>
                      <a id="editBioForm" class="btn btn-info " style="display:inline" onclick='editBio("<%= bio %>")' ><font color="white">Edit</font></a>
	                </div>
	               </div>
	              </div>
               </div>	
	          </div>
	         </div>	        
	       </div>
	    
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
				<a href="#"><%= CandidatureController.getEmployeeEmailByCf(listCandidatureBean.get(i).getOffer().getEmployee())%></a>
			</td>
			<td>				
				<button name="deleteCandidatureButton" class="btn btn-primary" style="background-color:#FF6347" value=<%=i%>><i class="fa fa-trash" style="font-size:20px" aria-hidden="true"></i></button>	 			
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
<%
}
%>
</html>