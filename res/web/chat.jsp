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
			const myEmail = "<%=userEmail%>";
			const toEmail = "sa.belli@hotmail.it"; //DEBUG
			var tokenExpiresInMs = <%=(chatInit.getTokenExpiresIn() - 15) * 1000%>;
			var latestMessageTime = 0;
			var earliestMessageTime = Number.MAX_VALUE;
			var delayTimePullMs = 0;
			var noMsgsCounter = 0;
			var currentLatestDate = 0;

			function padTime(s) {
				return String(s).padStart(2, '0');
			}

			function createMessageElement(msg) {
				if(msg.sender_email == myEmail) {
					msg.sender_email = "Me";
				} else {
					msg.sender_email = "Remote"
				}
				
				const date = new Date(msg.delivery_request_date);
				const hhmm = padTime(date.getHours()) + ":" + padTime(date.getMinutes());

				return document.createTextNode(msg.sender_email + "(" + hhmm + "): " + msg.text);
			}

			function createDateElement(date) {
				const elem = document.createElement("p");
				elem.appendChild(document.createTextNode("[" + date.toLocaleDateString() + "]"));
				return elem;
			}
			
			function notSameDay(d, d1) {
				return d.getDay() != d1.getDay() || d.getMonth() != d1.getMonth() || d.getYear() != d1.getYear();
			}

			function parseRequestedMsgs(msgsEncoded) {
				const msgs = JSON.parse(msgsEncoded);
				if(msgs.length > 0) {
					const endIndex = msgs.length - 1;
					delayTimePullMs = 0;
					noMsgsCounter = 0;

					if(latestMessageTime < msgs[0].delivery_request_date) {
						latestMessageTime = msgs[0].delivery_request_date;
					}

					if(msgs[endIndex].delivery_request_date < earliestMessageTime) {
						earliestMessageTime = msgs[endIndex].delivery_request_date;
					}
					
					var currentDate = msgs[endIndex].delivery_request_date;
					for(var i = endIndex; i >= 0; --i) {
						var thisDate = msgs[i].delivery_request_date;
						
						if(thisDate >= currentLatestDate) {
							if(notSameDay(new Date(thisDate), new Date(currentLatestDate))) {
								appendNode(createDateElement(new Date(thisDate)));
							}

							appendNode(createMessageElement(msgs[i])).appendChild(document.createElement("br"));
							currentLatestDate = thisDate;
						} else {
							insertBeforeNode(createMessageElement(msgs[endIndex - i]));

							if(i == 0) {
								currentDate = 0;
							}

							if(notSameDay(new Date(thisDate), new Date(currentDate))) {
								document.getElementById("chatbox").prepend(createDateElement(new Date(thisDate)));
							}
						}
					}
				} else {
					++noMsgsCounter;
					if(noMsgsCounter == 3) {
						delayTimePullMs = 500;
					} else if(noMsgsCounter >= 9) {
						delayTimePullMs = 1000;
					}
				}
			}

			function handleError() {

			}

			function pullMessages(fromLatest, toEarliest) {
				var req = "PullMessages\tToken:" + token + "\nTo:" + toEmail + 
					"\nContent-Length:0\nTs-From-Latest:" + fromLatest + "\nTs-To-Earliest:" + toEarliest + "\n\0";
				console.log(req); //DEBUG
				ws.send(req);
			}

			function appendNode(node) {
				var elem = document.getElementById("chatbox");
				elem.appendChild(node);
				return elem;
			}

			function insertBeforeNode(node) {
				var elem = document.getElementById("chatbox");
				elem.insertBefore(document.createElement("br"), elem.firstChild);
				elem.insertBefore(node, elem.firstChild);
			}

			function tokenRefresh() {
				var req = "TokenRefresh\tToken:" + token + "\nContent-Length:0\n\0";
				console.log(req); //DEBUG
				ws.send(req);
			}

			function pushMessage(msg) {
				var req = "PushMessage\tToken:" + token + "\nTo:" + toEmail + "\nContent-Length:"
							+ msg.length + "\n\0" + msg;
				console.log(req); //DEBUG
				ws.send(req);
			}

			function checkOnlineStatus() {
				var req = "CheckOnlineStatus\tToken:" + token + "\nContent-Length:0\nTo:" 
							+ toEmail + "\n\0";
				console.log(req); //DEBUG
				ws.send(req);
			}

			window.onload = function() { //main parser
				const endpoint = "ws://" + window.location.hostname + ":" + "<%=chatInit.getServicePort()%>";
				ws = new WebSocket(endpoint);

				ws.addEventListener('open', function (event) {
					console.log("open"); //DEBUG
					setTimeout(function() { location.reload(); }, 600000);
					setTimeout(checkOnlineStatus, <%=chatInit.getShouldPullMessagesEvery()%>);
					setTimeout(tokenRefresh, tokenExpiresInMs);
					const curtime = Date.now();
					pullMessages(curtime, curtime - 43200000);
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
								tokenExpiresInMs = (parseInt(kv[1]) - 15) * 1000;
								setTimeout(tokenRefresh, tokenExpiresInMs);
								return;
							} else if(kv[0] === "Content-Type" && kv[1] === "text/json") {
								shouldPullEveryMs = parseInt(fields[0].split(":")[1]) + delayTimePullMs;
								parseRequestedMsgs(fields[3].split("\0")[1]);
								setTimeout(function() {
									pullMessages(Date.now(), latestMessageTime + 1000);
								}, shouldPullEveryMs);
								return;
							} else if(kv[0] === "Content-Length" && kv[1] === "1") {
								shouldPullEveryMs = parseInt(fields[0].split(":")[1]);
								console.log(fields[2].split("\0")[1]); //online status
								setTimeout(checkOnlineStatus, (shouldPullEveryMs + tokenExpiresInMs) / 2);
								return;
							}
						}
						//message accepted for delivery, otherwise
					} else {
						alert("Something went bad while receiving a response from chat service!");
						handleError(arr[1]);
					}
				});
				
				document.getElementById("sendbtn").addEventListener("click", function() {
					const area = document.getElementById("mymsg");
					if(area.value.length > 0) {
						pushMessage(area.value);
						area.value = "";
					}
				});
			}

			function chatboxScroll() {
				var x = document.getElementById("chatbox").scrollTop; //DEBUG
				console.log(x); //DEBUG
				
				if(x === 0) {
					pullMessages(earliestMessageTime - 1000, earliestMessageTime - 1000 - 43200000);
				}
			}
		</script>
	</head>
	<body>
		<div id="wrapper">
			<div id="menu">
                <p class="chatting_with">Chatting with: </p>
            </div>
 
			<div id="chatbox" onscroll="chatboxScroll();"></div>
		
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