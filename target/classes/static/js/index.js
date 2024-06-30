function loginForm() {
	var loginForm_nickname = document.getElementById('index_username').value;
	var loginForm_userpassword = md5(document.getElementById('index_password').value);
	var check = true;
	if (loginForm_nickname.trim() !== "" && loginForm_userpassword !== "") {
		messageController(loginForm_nickname, loginForm_userpassword, check)
			.then(function(usercontroller_response) {
				if (usercontroller_response === "ok") {
                    window.location.href = "../index/main.html";
				} else {alert('昵称/密码 错误');}
			})
			.catch(function(error) {alert('昵称/密码 错误' + error);});
	} else {alert("请输入昵称/密码");}
}
// 发送用户名密码验证去后端
function messageController(loginForm_nickname, loginForm_userpassword, check) {
	return new Promise(function(resolve, reject) {
		var xhr = new XMLHttpRequest();
		var url = '/messageController';
		var params = 'user_name=' + encodeURIComponent(loginForm_nickname) + '&user_password=' + encodeURIComponent(loginForm_userpassword) + '&check=' + encodeURIComponent(check);
		xhr.open('POST', url, true);
		xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
		xhr.onreadystatechange = function() {
			if (xhr.readyState === XMLHttpRequest.DONE) {
				if (xhr.status === 200) {
					resolve(xhr.responseText);
				} else {
					reject(xhr.status);
				}
			}
		};
		xhr.send(params);
	});
}
// 注册功能
function registerForm() {
	var nickname = document.getElementById('nickName_register').value;
	var user_password_1 = md5(document.getElementById('userPassword_register_1').value);
	var user_password_2 = md5(document.getElementById('userPassword_register_2').value);
	var check = false;
	if (nickname.trim() !== "" && user_password_1 !== "" && user_password_2 !== "") {
		if (user_password_1 === user_password_2) {
			messageController(nickname, user_password_1, check)
				.then(function(usercontroller_response) {
					if (usercontroller_response === "okk") {alert('注册成功');} 
					else {alert('注册失败，可能服务器已满');}
				})
				.catch(function(error) {alert('服务器错误：' + error);});
		} else {alert("两次密码需一致");}
	} else {alert("昵称/密码不得为空");}
}
