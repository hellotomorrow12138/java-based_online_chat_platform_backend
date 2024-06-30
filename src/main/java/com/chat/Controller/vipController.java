package com.chat.Controller;

import com.chat.Mapper.message_mapper;
import com.chat.Mapper.store_mapper;
import com.chat.Mapper.vip_mapper;
import com.chat.Model.Store;
import com.chat.Model.User;
import com.chat.Model.Vip;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/vip")
public class vipController {
    @PostMapping("/select_vipLevel")
    public ResponseEntity<List<Vip>> selected_products(@RequestParam("user_name")String __user_name) {
        try {
            User __user = new User();
            __user.setUser_name(__user_name);

            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlsessionfactory = new SqlSessionFactoryBuilder().build(inputStream);
            SqlSession sqlsession = sqlsessionfactory.openSession();
            vip_mapper __Vm = sqlsession.getMapper(vip_mapper.class);
            Integer __vipLevel = __Vm.username_vipLevel(__user);
            List<Vip> __vipLevel_userName = __Vm.user_getVip(__vipLevel);
            return ResponseEntity.ok(__vipLevel_userName);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 更改返回错误处理，避免返回null
        }
    }
}
