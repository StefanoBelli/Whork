<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="logic.bean.UserBean" %>
<%@ page import="logic.bean.ChatInitBean" %>
<%@ page import="logic.util.ServletUtil" %>
<%@ page import="logic.controller.service.ServiceControllerHolder" %>
<%@ page import="logic.controller.service.Service" %>

<%
    UserBean userBean = ServletUtil.getUserForSession(request);
	String userEmail = ServletUtil.getUserEmailForSession(request);
	ChatInitBean chatInitBean = 
		ServiceControllerHolder.getService(Service.CHAT).newChatSession(userEmail);
%>

TESTING