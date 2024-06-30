package com.chat.Controller;

import com.chat.Model.Contents;
import com.chat.Model.Store;
import com.chat.Model.User;
import com.chat.Mapper.message_mapper;
import com.chat.Mapper.store_mapper;
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
@RequestMapping("/store")
public class Store_Controller {
    @GetMapping("/show_store")
    public ResponseEntity<List<Store>> show_store() {
        try {
            Store __store = new Store();

            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlsessionfactory = new SqlSessionFactoryBuilder().build(inputStream);
            SqlSession sqlsession = sqlsessionfactory.openSession();
            store_mapper __Sm = sqlsession.getMapper(store_mapper.class);

            List<Store> __storeList = __Sm.get_storeList(__store);
            return ResponseEntity.ok(__storeList);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 更改返回错误处理，避免返回null
        }
    }
    @PostMapping("/selected_products")
    public ResponseEntity<String> selected_products(@RequestParam("id")Integer __id,
                                            @RequestParam("user_name")String __user_name) {
        try {
            User __user = new User();
            __user.setHas_store(__id);
            __user.setUser_name(__user_name);

            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlsessionfactory = new SqlSessionFactoryBuilder().build(inputStream);
            SqlSession sqlsession = sqlsessionfactory.openSession();
            message_mapper __Sm = sqlsession.getMapper(message_mapper.class);

            __Sm.updateUser(__user);
            sqlsession.commit();
            return new ResponseEntity<>("ok", HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("no", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/favorite_products")
    public ResponseEntity<List<Store>> favorite_products(@RequestParam("user_name")String __user_name) {
        try {
            User __user = new User();
            __user.setUser_name(__user_name);

            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlsessionfactory = new SqlSessionFactoryBuilder().build(inputStream);
            SqlSession sqlsession = sqlsessionfactory.openSession();
            store_mapper __Sm = sqlsession.getMapper(store_mapper.class);

            Integer __hasStore_id = __Sm.username_hasStore(__user);

            List<Store> __hasStore_userStore = __Sm.user_getStore(__hasStore_id);

            return ResponseEntity.ok(__hasStore_userStore);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 更改返回错误处理，避免返回null
        }
    }
}
