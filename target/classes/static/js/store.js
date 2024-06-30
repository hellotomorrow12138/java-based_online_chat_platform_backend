let currentNickname = "";
$(document).ready(function() {
	// 获取用户名并检查登录状态
	$.get("/getUsername", function(loginForm_nickname) {
		if (loginForm_nickname !== "no" && loginForm_nickname !== "") {
			currentNickname = loginForm_nickname;
			// 显示欢迎信息及Session ID
			$("#usernameDisplay").text("欢迎，" + currentNickname + "！");
			$.get("/getSessionId", function(loginForm_SessionId) {
				$("#sessionIdDisplay").text("当前Session ID: " + loginForm_SessionId);
			}).fail(function(xhr, status, error) {
				console.error(error);
			});
			// 查询并显示收藏商品
			showFavorites_store(currentNickname);
		} else {
			window.location.href = "../index.html"; // 未登录重定向
		}
	}).fail(function(xhr, status, error) {
		console.error("Error fetching username:", error);
	});
});

function showFavorites_store(user_name) {
    $.ajax({
        url: `/store/favorite_products?user_name=` + encodeURIComponent(user_name),
        type: "GET",
        dataType: "json",
        success: function(data) {
            if (data && data.length > 0) {
                let favoritesHTML = "<p>购物车：</p>";
                data.forEach(function(item) {
                    // 修改此处以匹配实际数据结构，假设您想展示商品ID、名称和售价
                    favoritesHTML += `
					<div class="product">
					    <img src="../images/store/${item.goods_name}.jpg" alt="${item.goods_name}">
					    <h3>商品名称：${item.goods_name}</h3>
					    <p>花费: ${item.goods_sell}</p>
					</div>
					`;
                });
                $("#showStore").html(favoritesHTML);
            } else {
                $("#showStore").text("该用户暂无收藏商品。");
            }
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.error("Error fetching favorite products:", textStatus, errorThrown);
            $("#showStore").text("查询收藏商品时出错，请稍后再试。");
        }
    });
}

$(document).ready(function() {
	$.ajax({
		url: "/store/show_store",
		type: "GET",
		dataType: "json",
		success: function(data) {
			var productsHTML = "";
			data.forEach(function(product) {
				// 确保所有单选按钮共享同一个name属性，实现互斥选择
				productsHTML += `
                    <div class="product">
                        <input type="radio" name="productSelect" class="product-radio" data-id="${product.id}">
                        <img src="../images/store/${product.goods_name}.jpg" alt="${product.goods_name}">
                        <h3>${product.goods_name}</h3>
                        <p>价格: ${product.goods_sell}</p>
                    </div>
                `;
			});
			$("#products").html(productsHTML);
			// 由于现在是单选，无需遍历选中的checkbox，直接获取第一个选中的即可
			$("#submitSelection").click(function() {
				var selectedProductId = $('.product-radio:checked').data('id');
				if (selectedProductId) {
					// 发送勾选的商品ID到后端
					sendSelectedProductsToBackend(selectedProductId);
				} else {
					alert("请至少选择一项商品！");
				}
			});
		},
		error: function(jqXHR, textStatus, errorThrown) {
			console.error("Error fetching products:", textStatus, errorThrown);
		}
	});
});

function sendSelectedProductsToBackend(id) {
	// 直接将id作为查询参数附加到URL后面
	$.ajax({
		url: "/store/selected_products?user_name=" + encodeURIComponent(currentNickname) + "&id=" + encodeURIComponent(id), 
		type: "POST",
		contentType: "application/json; charset=utf-8", // 保持原本设定，虽然这次不通过请求体发送数据，但保持content-type为json是一种习惯做法
		dataType: "json",
		success: function(response) {
			try {
				var responseData = JSON.parse(response); // 尝试解析，如果确实是JSON
				console.log("成功发送至后端:", responseData);
				alert("已成功提交选择的商品！");
			} catch (e) {
				// 如果不是JSON，直接处理文本响应
				console.log("后端返回非JSON响应:", response);
				// 	alert(response); // 或者其他处理逻辑
			}
		},
		error: function(jqXHR, textStatus, errorThrown) {
			console.error("Error sending selections to backend:", textStatus, errorThrown);
		}
	});
}