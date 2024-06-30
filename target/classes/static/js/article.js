document.addEventListener('DOMContentLoaded', function() {
    // ... 原有的fetch请求代码保持不变 ...
    fetch('http://127.0.0.1:8080/api/room_controller')
        .then(response => {
            if (!response.ok) {
                throw new Error(`网络请求错误，状态码：${response.status}`);
            }
            return response.json();
        })
        .then(menuList => {
            // 这里我们假设menuList是一个对象数组，每个对象至少包含一个表示房间名称的属性，例如{name: "新房间1"}
            // 遍历新获取的菜单列表数据
            menuList.forEach(item => {
                // 为每个新房间创建列表项并追加到子菜单中
                $('<li><a href="./chat.html?room_id=' + encodeURIComponent(item.id) + '" class="room-link">' + item.room_name + '</a></li>').appendTo('.chatrooms-submenu');
            });
        })
        .catch(error => {
            console.error('获取菜单列表失败:', error);
        });
});
