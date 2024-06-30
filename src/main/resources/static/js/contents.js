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
			currentNickname = loginForm_nickname;
		} else {
			window.location.href = "../index.html";
		}
	}).fail(function(xhr, status, error) {
		console.error("Error occurred while fetching username:", error);
	});
});

document.addEventListener('DOMContentLoaded', function() {
	loadContents(); // 页面加载时首次加载用户列表
	document.getElementById('refreshButton').addEventListener('click', loadContents); // 绑定刷新按钮事件
});
// 刷新函数
function loadContents() {
	fetch('http://127.0.0.1:8080/contents/loadContents') // 替换为你的API地址
		.then(response => response.json())
		.then(data => {
			const tableBody = document.getElementById('tableBody');
			tableBody.innerHTML = ''; // 清空现有表格内容，准备更新
			data.forEach(user => {
				const row = `
			        <tr>
			            <td>${user.id}</td>
			            <td class="title-cell">${user.title}</td> <!-- 添加类名 -->
			            <td>${user.created_user}</td>
			            <td>${user.text}</td>
			            <td>${user.status}</td>
			        </tr>
			    `;
				tableBody.insertAdjacentHTML('beforeend', row);
			});

			// 之后，为所有具有"title-cell"类名的元素添加点击事件监听器
			document.querySelectorAll('.title-cell').forEach(cell => {
				cell.addEventListener('click', function() {
					const userId = this.closest('tr').querySelector('td:first-child')
						.textContent; // 假设第一列是用户ID
					click_contentsDiv.style.display = 'none';
					fetchArticleFullContent(userId);
				});
			});
		})
		.catch(error => console.error('Error loading users:', error));
}
// 新增函数来获取文章全貌
function fetchArticleFullContent(userId) {
	fetch(`http://127.0.0.1:8080/contents/load-article/${userId}`) // 假定接口需要文章ID作为路径参数
		.then(response => {
			if (!response.ok) {
				throw new Error(`HTTP error! Status: ${response.status}`);
			}
			return response.json();
		})
		.then(article => {
			// 处理获取到的文章全貌数据，例如显示在页面上的某个div中
			const articleDisplay = document.getElementById('articleDisplay');
			articleDisplay.innerHTML = '';
			article.forEach(user => {
				const row = `
			        <h1 class="article-title">${user.title || '未知标题'}</h1>
			        <h2 class="article-title">作者: ${user.created_user || '未知作者'}</h2>
			        <div class="article-content"><p>${user.text || '无内容'}</p></div>
			    `;
				articleDisplay.insertAdjacentHTML('beforeend', row);
				articleDisplay.style.display = 'block';
			});

		})
		.catch(error => {
			console.error('Error fetching article content:', error);
		});
}

function search_contentsForm() {
	var input_contents = document.getElementById('contents_search').value;
	if (input_contents.trim() !== "") {
		search_contents(input_contents)
			.then(function(contents_response) {
				console.log(contents_response);
				if (!Array.isArray(contents_response)) {
					throw new Error("从send_contents获得的数据不是数组");
				}
				const articleDisplay = document.getElementById('articleDisplay');
				articleDisplay.innerHTML = '';
				contents_response.forEach(user => {
					const row = `
					<tr>
                        <th>${user.id}</th>
                        <th class="title-cell">${user.title}</th>
                        <th>${user.created_user}</th>
                        <th>${user.text}</th>
                        <th>${user.status}</th>
					</tr>
                    `;
					articleDisplay.insertAdjacentHTML('beforeend', row);
					articleDisplay.style.display = 'block';
				});
				document.querySelectorAll('.title-cell').forEach(cell => {
					cell.addEventListener('click', function() {
						const userId = this.closest('tr').querySelector('td:first-child')
							.textContent; // 假设第一列是用户ID
						click_contentsDiv.style.display = 'none';
						fetchArticleFullContent(userId);
					});
				});
			})
			.catch(function(error) {
				alert('处理错误：' + error);
			});
	} else {
		alert("请不要留空");
	}
}

function search_contents(input_contents) {
	return new Promise(function(resolve, reject) {
		var xhr = new XMLHttpRequest();
		var url = 'http://127.0.0.1:8080/contents/search_contents';
		var params = 'input_contents=' + encodeURIComponent(input_contents);
		xhr.open('POST', url, true);
		xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
		xhr.onreadystatechange = function() {
			if (xhr.readyState === XMLHttpRequest.DONE) {
				if (xhr.status === 200) {
					try {
						var response = JSON.parse(xhr.responseText);
						if (!Array.isArray(response)) {
							throw new Error("返回的数据不是数组");
						}
						resolve(response);
					} catch (e) {
						reject("解析响应数据出错：" + e);
					}
				} else {
					reject(xhr.status);
				}
			}
		};
		xhr.send(params);
	});
}

function add_contents() {
	var add_title = document.getElementById('input-title').value;
	var add_contents = document.getElementById('input-contents').value;
	if (add_title !== "") {
		if (add_contents !== "") {
			var add_user = currentNickname;
			send_inputContents(add_title, add_contents, add_user)
				.then(function(response) {
					if (response.ok) {
						alert('提交成功');
					} else {
						alert('提交成功');
					}
				})
				.catch(function(error) {
					alert('服务器错误：' + error);
				});
		} else {
			alert("内容不得为空");
		}
	} else {
		alert("标题不得为空");
	}
}
function send_inputContents(add_title, add_contents, add_user) {
    return new Promise((resolve, reject) => { // 使用箭头函数简化代码
		var xhr = new XMLHttpRequest();
		var url = '/contents/send_inputContents';
		var params = 'add_title=' + encodeURIComponent(add_title) + '&add_contents=' + encodeURIComponent(add_contents) + '&add_user=' + encodeURIComponent(add_user);
		xhr.open('POST', url, true);
		xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
		xhr.onload  = function() {
			if (xhr.readyState === XMLHttpRequest.DONE) {
				if (xhr.status === 200) {
					resolve(xhr.statusText);
				} else {
					reject(xhr.status);
				}
			}
		};
		xhr.onerror = () => reject(xhr.statusText);
		xhr.send(params);
	});
}
