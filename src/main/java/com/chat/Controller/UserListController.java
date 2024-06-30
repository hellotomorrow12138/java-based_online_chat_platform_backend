package com.chat.Controller;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/list")
public class UserListController {
    @GetMapping("/{username}")
    public JSONObject getUsername(@PathVariable("username") String username) {
        JSONObject jsonObject = new JSONObject();
        boolean isEmpty = WebSocket_controller.sessionMap.isEmpty();
        jsonObject.put("isEmpty", isEmpty);
        jsonObject.put("isExist", false);
        if (!isEmpty) {
            boolean isExist = WebSocket_controller.sessionMap.containsKey(username);
            jsonObject.replace("isExist", isExist);
        }
        return jsonObject;
    }
}
