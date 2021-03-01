<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="logic.bean.UserBean" %>
<%@ page import="logic.util.Util" %>

<%
    UserBean userBean = Util.getUserForSession(request);
%>
<!DOCTYPE HTML>
<html xml:lang="en">
    <head>
        <title>Whork</title>
    </head>
    <body>
        <h1>Welcome back, <%= userBean.getName() %></h1>
    </body>

    <form action="/logout" method="get">
        <input type="submit" value="Logout">
    </form>
</html>