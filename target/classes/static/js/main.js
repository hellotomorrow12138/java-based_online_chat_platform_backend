$(document).ready(function() {
	// 首先尝试获取用户名，以此间接判断用户登录状态
	$.get("/getUsername", function(loginForm_nickname) {
		if (loginForm_nickname !== "no" && loginForm_nickname !== "") { // 假设 "no" 或空字符串表示未登录
			// 用户已登录，继续获取并显示Session ID及用户名
			$.get("/getSessionId", function(loginForm_SessionId) {
				$("#sessionIdDisplay").text("当前Session ID: " + loginForm_SessionId);
			}).fail(function(xhr, status, error) {
				console.error(error);
			});
			// 显示用户名
			$("#usernameDisplay").text("欢迎，" + loginForm_nickname + "！");
		// 	document.getElementById('chatNickname').textContent = loginForm_nickname;
		}else{
			window.location.href = "../index.html";
		}
	}).fail(function(xhr, status, error) {
		console.error("Error occurred while fetching username:", error);
	});
});
