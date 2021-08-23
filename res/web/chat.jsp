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
		<meta name="author" content="Stefano Belli">
		<meta name="author" content="Magliari Elio">
		<link rel="stylesheet" type="text/css" href="css/chat.css"></link>
		<script>
			var ws;
			var token = "<%=chatInit.getToken()%>";
			const myEmail = "<%=userEmail%>";
<%
	String toEmail = (String) request.getParameter("toEmail");
	if(toEmail == null) {
%>
			var toEmail = null;
<%	
	} else {
%>
			var toEmail = "<%=toEmail%>";
<%
	}
%>
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
				const hhmmss = padTime(date.getHours()) + ":" + padTime(date.getMinutes()) + ":" + padTime(date.getSeconds());

				return document.createTextNode(msg.sender_email + "(" + hhmmss + "): " + msg.text);
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
							explicitDownScroll();
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
				ws.send("PullMessages\tToken:" + token + "\nTo:" + toEmail + 
					"\nContent-Length:0\nTs-From-Latest:" + fromLatest + "\nTs-To-Earliest:" + toEarliest + "\n\0");
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
				ws.send("TokenRefresh\tToken:" + token + "\nContent-Length:0\n\0");
			}

			function pushMessage(msg) {
				ws.send("PushMessage\tToken:" + token + "\nTo:" + toEmail + "\nContent-Length:"
							+ msg.length + "\n\0" + msg);
			}

			function checkOnlineStatus() {
				ws.send("CheckOnlineStatus\tToken:" + token + "\nContent-Length:0\nTo:" 
							+ toEmail + "\n\0");
			}

			window.onload = function() {
				const endpoint = "ws://" + window.location.hostname + ":" + "<%=chatInit.getServicePort()%>";
				ws = new WebSocket(endpoint);
				document.getElementById("sendbtn").disabled = true;
				document.getElementById("mymsg").disabled = true;

				ws.addEventListener('open', function (event) {
					if(!goodprompt()) {
						ws.close();
						return;
					}
					document.getElementById("chatting_with").innerHTML += " " + toEmail;
					checkOnlineStatus();
					document.getElementById("sendbtn").disabled = false;
					document.getElementById("mymsg").disabled = false;
					setTimeout(checkOnlineStatus, <%=chatInit.getShouldPullMessagesEvery()%>);
					setTimeout(tokenRefresh, tokenExpiresInMs);
					const curtime = Date.now();
					pullMessages(curtime, curtime - 43200000);
				});

				ws.addEventListener('message', function (event) {
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
								setOnlineStatus(fields[2].split("\0")[1]);
								setTimeout(checkOnlineStatus, shouldPullEveryMs + 1000);
								return;
							}
						}
						//message accepted for delivery, otherwise
					} else {
						alert("Something went bad while receiving a response from chat service!");
						handleError(arr[1]);
					}
				});

				ws.addEventListener('close', function () {
					document.getElementById("sendbtn").disabled = true;
					document.getElementById("mymsg").disabled = true;
					document.getElementById("chatting_with").innerHTML = "Chatting with:";
					alert("Connection closed");
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
				if(document.getElementById("chatbox").scrollTop === 0) {
					pullMessages(earliestMessageTime - 1000, earliestMessageTime - 1000 - 43200000);
				}
			}

			function explicitDownScroll() {
				const elem = document.getElementById("chatbox");
				elem.scrollBy(0,elem.scrollHeight);
			}

			function goodprompt() {
				if(toEmail == null) {
					toEmail = prompt("Type in remote's email address","");
					return toEmail !== null && toEmail !== "";
				}
				
				return true;
			}

			function setOnlineStatus(v) {
				const color = v === "1" ? "#008000" : "#ff0000";
				document.getElementById("chatting_with").style.color = color;
			}
		</script>
		<link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet">
	</head>
	<body style="background-color:#B0E0E6">
		<div id="wrapper">
			<div id="menu">
                <span style="font-size:20px; font-weight:700;">
					<p id="online_status" style="color:green"></p>
					<p id="chatting_with" class="chatting_with">Chatting with: </p>
				</span>
            </div>
 
			<div id="chatbox" style="border:2px solid black" onscroll="chatboxScroll();"></div>
		
			<div id="ctrl">
				<div class="column" style="padding-right:20px; padding-left:42px;">
					<textarea class="md-textarea form-control" rows="2" cols="60" id="mymsg" style="border:1px solid black"></textarea>
				</div>
				<div class="column">
					<button id="sendbtn" class="btn btn-primary" style="text-align:center; padding-right:50px;">Send</button>
				</div>				
			</div>
		</div>
	</body>
</html>
<%
} else {
%>
<html lang="en">
	<head>
		<title>[OFFLINE] Chat - Whork</title>
		<meta name="author" content="Stefano Belli">
		<meta name="author" content="Magliari Elio">
		<link rel="stylesheet" href="css/cpoutcome.css">
	</head>

	<body style="background-color: #FFA07A">
		<div class="card">
	      <div style="border-radius:200px; height:200px; width:200px; background: #ffcccb; margin:0 auto;">
	        <i style="color:#FF0000">&#10007;</i>
	      </div>
	      <h1 style="color:#FF0000">Error</h1> 
	      <p>We're sorry but chat service is currently offline!</p>
	     </div>
	</body>
</html>
<%
}
%>



	    		
	    		