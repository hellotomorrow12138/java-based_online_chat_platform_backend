$(document).ready(function() {
	// 检查登录状态
	$.get("/getUsername", function(loginForm_nickname) {
		if (loginForm_nickname !== "no" && loginForm_nickname !== "") { // 简化判断逻辑
			// 用户已登录
			$("#usernameDisplay").text("欢迎，" + loginForm_nickname + "！");
			// 尝试获取并显示Session ID
			$.get("/getSessionId", function(loginForm_SessionId) {
				$("#sessionIdDisplay").text("当前Session ID: " + loginForm_SessionId);
			}).fail(function(xhr, status, error) {
				console.error("获取Session ID失败:", error);
			});
		} else {
			// 用户未登录，重定向到首页或其他适当操作
			window.location.href = "../index.html";
		}
		// 登录状态检查完毕后加载内容
		loadArticles();
	}).fail(function(xhr, status, error) {
		console.error("获取用户名失败:", error);
		// 可能需要重定向或提示用户
	});
});

function loadArticles() {
	fetch('http://127.0.0.1:8080/contents/loadContents') // 假定是正确的API路径
		.then(response => response.json())
		.then(article => {
			// 使用获取的文章数据填充到页面元素
			$('#articleTitle').text(article.title);
			$('#author-date').text(`作者：${article.created_user}`);
			$('#articleContent').html(article.text); // 注意：使用html()方法可以插入HTML内容，但需注意XSS攻击风险
		})
		.catch(error => console.error('加载文章详情失败:', error));
}