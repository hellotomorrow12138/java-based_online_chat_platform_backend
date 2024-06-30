let globalRoomIds = [];
let globalRoomNames = [];
document.addEventListener('DOMContentLoaded', function() {
	fetch('http://127.0.0.1:8080/api/room_controller')
		.then(response => {
			if (!response.ok) {
				throw new Error(`网络请求错误，状态码：${response.status}`);
			}
			return response.json();
		})
		.then(menuList => {
			menuList.forEach(item => {
				// 先存储房间ID和房间名
				globalRoomIds.push(item.id);
				globalRoomNames.push(item.room_name);
				// 然后继续创建列表项并追加到子菜单中
				$('<li><a href="./chat.html?room_id=' + encodeURIComponent(item.id) +
					'" class="room-link">' + item.room_name + '</a></li>').appendTo(
					'.chatrooms-submenu');
			});
		})
		.catch(error => {
			console.error('获取菜单列表失败:', error);
		});
});
let currentRoomId = "";
let currentNickname = "";
let currentSessionId = "";
$(document).ready(function() {
	// 使用Promise处理异步操作
	new Promise((resolve, reject) => {
			// 检查登录状态
			$.get("/getUsername")
				.done(function(loginForm_nickname) {
					if (loginForm_nickname !== "no" && loginForm_nickname !== "") {
						resolve(loginForm_nickname);
					} else {
						window.location.href = "../index.html"; // 用户未登录，重定向
						reject(new Error("User is not logged in."));
					}
				})
				.fail(function(xhr, status, error) {
					reject(error); // 获取用户名失败
				});
		})
		.then((loginForm_nickname) => {
			// 已登录，继续获取Session ID
			return new Promise((resolve, reject) => {
				$.get("/getSessionId")
					.done(function(loginForm_SessionId) {
						$("#sessionIdDisplay").text("当前Session ID: " + loginForm_SessionId);
						$("#usernameDisplay").text("欢迎，" + loginForm_nickname + "！");
						currentNickname = loginForm_nickname;
						currentSessionId = loginForm_SessionId;
						resolve(); // 成功获取Session ID
					})
					.fail(function(xhr, status, error) {
						reject(error); // 获取Session ID失败
					});
			});
		})
		.then(() => {
			// 从URL中提取room_id参数
			const urlParams = new URLSearchParams(window.location.search);
			currentRoomId = urlParams.get('room_id');
			if (currentRoomId) {
				// 向后端发送请求获取房间名
				$.get(`/api/getRoomName?roomId=${encodeURIComponent(currentRoomId)}`)
					.done(function(roomName) {
						// 成功获取房间名
						document.getElementById('chatNickname').textContent = roomName;
						// 确保所有必要数据已准备就绪
						if (currentNickname && currentSessionId) {
							initializeWebSocketConnection(currentNickname, currentRoomId);
						} else {
							console.warn('昵称或Session ID未准备好，无法初始化WebSocket连接');
						}
					})
					.fail(function(xhr, status, error) {
						console.error('获取房间名失败:', error);
					});
			} else {
				console.error('未在URL中找到room_id参数');
			}
		})
});

var socket;
function initializeWebSocketConnection(nickname, roomId) {
	var socketUrl = "ws://127.0.0.1:8080/socket/" + encodeURIComponent(nickname) + "/" + encodeURIComponent(roomId);
	socket = new WebSocket(socketUrl);
	socket.onmessage = function(msg) {
		var data = JSON.parse(msg.data);
		if (data.userlist) {
			document.getElementById('userCount').textContent = data.userlist.length;
			var userList = document.getElementById('userList');
			userList.innerHTML = "";
			data.userlist.forEach(function(user) {
				var userDiv = document.createElement('div');
				userDiv.textContent = user;
				userList.appendChild(userDiv);
			});
		} else {
			// 将currentNickname传递给appendChatMessage
			appendChatMessage(data, currentNickname); // 调用新的函数来添加聊天消息
		}
	};
}

function sendMessage() {
	var text = document.getElementById('text').value;
	if (text.trim() !== "") {
		// 在消息对象中新增roomId字段
		var message = {
			name: currentNickname,
			msg: text,
			roomId: currentRoomId // 添加当前房间ID
		};
		socket.send(JSON.stringify(message));
		document.getElementById('text').value = ""; // 清空输入框
	}
}
$(document).ready(function() {
	// 绑定发送按钮的点击事件
	document.getElementById('sendButton').addEventListener('click', function() {
		sendMessage(); // 点击按钮时调用sendMessage函数
	});
	// 新增：允许用户在按下回车键时也能发送消息
	document.getElementById('text').addEventListener('keydown', function(event) {
		if (event.key === "Enter") {
			event.preventDefault(); // 阻止默认行为（如表单提交）
			sendMessage(); // 直接调用sendMessage函数发送消息
		}
	});
});


function appendChatMessage(data) {
	var chatHistory = document.getElementById('chatHistory');
	var messageDiv = document.createElement('div');
	var messageText = document.createElement('div');
	var userAvatar = document.createElement('img'); // 创建img元素
	messageDiv.style.display = "flex";
	messageText.textContent = data.name + ": " + data.msg;
	messageDiv.style.margin = "10px 0"; // 添加间距以分隔自己的消息
	messageDiv.style.padding = "10px"; // 添加填充以更好地区分消息
	messageDiv.style.borderRadius = "10px"; // 圆角
	var avatarPath = "../images/" + encodeURIComponent(data.name) + ".png";
	userAvatar.src = avatarPath;
	userAvatar.className = "user-avatar"; // 添加一个class
	userAvatar.style.width = '40px'; // 设置宽度
	userAvatar.style.height = '40px'; // 设置高度
	userAvatar.style.borderRadius = '50%'; // 制成圆形
	userAvatar.style.objectFit = 'cover'; // 图片填充且保持比例
	messageDiv.appendChild(userAvatar); // 将头像添加到消息div中
	messageDiv.style.alignItems = "center";
	if (data.name === currentNickname) {
		// 为自己的聊天消息添加样式
		messageDiv.style.backgroundColor = "#007bff"; // 蓝色背景颜色
		messageDiv.style.color = "#fff"; // 白色文本颜色
	} else {
		// 为其他用户的聊天消息添加样式
		messageDiv.style.backgroundColor = "#f2f2f2"; // 浅灰色背景颜色
		messageDiv.style.color = "#000"; // 黑色文本颜色
	}
	messageDiv.appendChild(messageText);
	chatHistory.appendChild(messageDiv);
}
