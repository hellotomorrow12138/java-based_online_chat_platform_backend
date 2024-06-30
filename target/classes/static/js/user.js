let currentNickname = "";
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
			document.getElementById('user_name').innerText = loginForm_nickname;
			
			var avatarUrl = "../images/" + encodeURIComponent(loginForm_nickname) + ".png";
			// 获取并设置头像
			var imgElement = document.createElement('img');
			imgElement.src = avatarUrl; // 设置图片源
			imgElement.classList.add('profile-pic'); // 添加样式类
			// 将创建的img元素添加到DOM中
			document.querySelector('.profile-card').appendChild(imgElement);
			currentNickname = loginForm_nickname;
			vipLevel_user()
		} else {
			window.location.href = "../index.html";
		}
	}).fail(function(xhr, status, error) {
		console.error("Error occurred while fetching username:", error);
	});
	
});
function vipLevel_user() {
	$.ajax({
		url: "/vip/select_vipLevel?user_name=" + encodeURIComponent(currentNickname),
		type: "POST",
		// contentType: "application/json; charset=utf-8", // 保持原本设定，虽然这次不通过请求体发送数据，但保持content-type为json是一种习惯做法
		// dataType: "json",
		success: function(vipInfoArray) {
		    console.log("Received VIP Info:", vipInfoArray);
		    if (vipInfoArray.length > 0 && vipInfoArray[0].hasOwnProperty('vip_name')) {
		        document.getElementById('vip_level').innerText = vipInfoArray[0].vip_name;
		    } else {
		        console.error("VIP Name not found in the response.");
		    }
		},
		error: function(jqXHR, textStatus, errorThrown) {
			console.error("Error sending selections to backend:", textStatus, errorThrown);
		}
	});
	
}
