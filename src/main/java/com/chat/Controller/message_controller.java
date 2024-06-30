package com.chat.Controller;

import com.chat.Model.User;
import com.chat.Mapper.message_mapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
public class message_controller {
    @PostMapping("/messageController")
    public ResponseEntity<String> user_controller(@RequestParam("user_name")String __user_name,
                                                  @RequestParam("user_password")String __user_password,
                                                  @RequestParam("check")Boolean __check,
                                                  HttpServletRequest __request) {
        try {
            User __user = new User();
            __user.setId(8);
            __user.setUser_name(__user_name);
            __user.setUser_password(__user_password);
            String __resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(__resource);
            SqlSessionFactory sqlsessionfactory = new SqlSessionFactoryBuilder().build(inputStream);
            SqlSession sqlsession = sqlsessionfactory.openSession();
            message_mapper __userMapper = sqlsession.getMapper(message_mapper.class);
//            System.out.println("user:"+__user_name+" "+__user_password);


            if(__check) {
                List<User> __getUsers = __userMapper.get_user(__user);
                for (User __user_mapper : __getUsers) {
                    if (__user_mapper.getUser_name().equals(__user_name) && __user_mapper.getUser_password().equals(__user_password)) {
                        HttpSession __session = __request.getSession(); // 获取session
                        __session.setAttribute("loggedInUsername", __user_mapper.getUser_name()); // 将用户数据(键名)存入session
//                        System.out.println("user:"+__session.getId());
                        return new ResponseEntity<>("ok",HttpStatus.OK);
                    }
                }
            }else{
                __userMapper.reg_user(__user);
                sqlsession.commit();
                return new ResponseEntity<>("okk", HttpStatus.OK);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("no", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @GetMapping("/getSessionId")
    public String getSessionId(HttpSession session) {
        return session.getId(); // 直接返回Session ID
    }
    @GetMapping ("/getUsername")
    public String getUsername(HttpSession session) {
        // 假设登录时已将用户名存入session，键为"loggedInUsername"
        Object usernameObj = session.getAttribute("loggedInUsername");
//        System.out.println("has joined "+usernameObj);
        if (usernameObj != null) {
            return (String) usernameObj;
        } else {
            return "no"; // 或者处理为null的情况，比如返回"未登录"
        }
    }
}
