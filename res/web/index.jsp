<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="logic.bean.OfferBean" %>
<%@ page import="logic.controller.OfferController" %>
<%@ page import="logic.util.Util" %>
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
			<img src="<%= Util.InstanceConfig.getString(Util.InstanceConfig.KEY_CTX_USR_DATA) + "/" + offer.getPhoto() %>" alt="Image offer">
		</div>	
		<div class="name">
			<b>Name: <%=offer.getOfferName()%></b>
		</div>
		<div class="description">
			<%=offer.getDescription()%>
		</div>
	</div>
				
<%
	}
}
%>
	</body>
</html>