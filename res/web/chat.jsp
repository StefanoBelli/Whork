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
		<link rel="stylesheet" type="text/css" href="css/chat.css"></link>
		<script>
			var ws;
			var token = "<%=chatInit.getToken()%>";

			function handleError() {

			}

			function tokenRefresh() {
				var req = "TokenRefresh\tToken:" + token + "\nContent-Length:0\n\0";
				console.log(req);
				ws.send(req);
			}

			window.onload = function() { //main parser
				const endpoint = "ws://" + window.location.hostname + ":" + "<%=chatInit.getServicePort()%>";
				ws = new WebSocket(endpoint);

				ws.addEventListener('open', function (event) {
					console.log("open"); //DEBUG
					tokenRefresh();

					document.getElementById("chatbox").appendChild(document.createTextNode("opened"));
					document.getElementById("chatbox").appendChild(document.createElement("br"));
				});

				ws.addEventListener('message', function (event) {
					console.log(event.data); //DEBUG
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
							} else if(kv[0] === "Content-Type" && kv[1] === "text/json") {
								return;
							} else if(kv[0] === "Content-Length" && kv[1] === "1") {
								return;
							} else {
								return;
							}
						}
					} else {
						alert("Something went bad while receiving a response from chat service!");
						handleError(arr[1]);
					}
				});
			}
		</script>
	</head>
	<body>
		<div id="wrapper">
			<div id="menu">
                <p class="chatting_with">Chatting with: </p>
            </div>
 
			<div id="chatbox"></div>
		
			<div id="ctrl">
				<textarea rows="2" cols="60" id="mymsg"></textarea>
				<button id="sendbtn">Send</button>
			</div>
		</div>
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