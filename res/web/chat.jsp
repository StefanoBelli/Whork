<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="logic.bean.UserBean" %>
<%@ page import="logic.bean.ChatInitBean" %>
<%@ page import="logic.util.ServletUtil" %>
<%@ page import="logic.controller.ChatController" %>
<%
UserBean userBean = ServletUtil.getUserForSession(request);
String userEmail = ServletUtil.getUserEmailForSession(request);
ChatController chatController = ChatController.getInstance();
if(chatController.isOnlineService()) {
	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Expires", "0");
	ChatInitBean chatInit = chatController.newChatSession(userEmail);
%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<title>Chat - Whork</title>
		<script>
			var ws;
			var token = "<%=chatInit.getToken()%>";
			function tokenRefresh() {
				var req = "TokenRefresh\tToken:" + token + "\nContent-Length:0\n\0";
				console.log(req);
				ws.send(req);
			}

			window.onload = function() {
				const endpoint = "ws://" + window.location.hostname + ":" + "<%=chatInit.getServicePort()%>";
				ws = new WebSocket(endpoint);
				
				console.log("onload()");

				ws.addEventListener('open', function (event) {
					console.log("open");
					tokenRefresh();
				});

				ws.addEventListener('message', function (event) {
					console.log(event.data);
					const response = event.data;
					const arr = response.split("\t");
					if(arr[0] === "OK") {
						const fields = arr[1].split("\n");
						for(let i in fields) {
							const kv = fields[i].split(":");
							if(kv[0] === "Expires-In") {
								token = fields[2].split("\0")[1];
								setTimeout(tokenRefresh, (parseInt(kv[1]) - 15) * 1000);
								return;
							}
							/*
							const kv = field.split(":");
							if(kv[0] === "Expires-In") {
								console.log("ok");
								//token = arr[1].split("\0")[1];
								//setTimeout(tokenRefresh, parseInt(kv[1] - 15) * 1000);
								return;
							} else if(kv[0] === "Content-Type" && kv[1] === "text/json") {
								//handleRequestedMessages(arr[1].split("\0")[1]);
								return;
							} else if(kv[0] === "Content-Length" && kv[1] === "1") {
								//handleOnlineStatusReport(parseInt(arr[1].split("\0")[1]));
								return;
							} else {
								//handleMessageDelivered();
								return;
							}*/
						}
					} else {
						alert("Something went bad while receiving a response from chat service!");
						//handleError(arr[1]);
					}
				});
			}
		</script>
	</head>
	<body>
		
	</body>
</html>
<%
} else {
%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<title>[OFFLINE] Chat - Whork</title>
	</head>

	<body>
		<h1>We're sorry but chat service is currently offline!</h1>
	</body>
</html>
<%
}
%>