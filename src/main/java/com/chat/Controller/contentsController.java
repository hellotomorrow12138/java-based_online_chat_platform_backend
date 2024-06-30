package com.chat.Controller;

import com.chat.Model.Contents;
import com.chat.Mapper.contents_mapper;
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
@RequestMapping("/contents")
public class contentsController {
    @GetMapping("/loadContents")
    public ResponseEntity<List<Contents>> get_contents() {
        try {
            Contents __contents = new Contents();
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlsessionfactory = new SqlSessionFactoryBuilder().build(inputStream);
            SqlSession sqlsession = sqlsessionfactory.openSession();
            contents_mapper __Cm = sqlsession.getMapper(contents_mapper.class);
            List<Contents> __contentsList = __Cm.get_contentsListAll(__contents);
            sqlsession.close();
            return ResponseEntity.ok(__contentsList);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 更改返回错误处理，避免返回null
        }
    }
    @GetMapping("/load-article/{userId}")
    public ResponseEntity<List<Contents>> id_contents(@PathVariable  Integer userId){
        try {
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlsessionfactory = new SqlSessionFactoryBuilder().build(inputStream);
            SqlSession sqlsession = sqlsessionfactory.openSession();
            contents_mapper __Cm = sqlsession.getMapper(contents_mapper.class);
            List<Contents> __contentsList = __Cm.getContentsById(userId);
            sqlsession.close();
            return ResponseEntity.ok(__contentsList);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 更改返回错误处理，避免返回null
        }
    }
    @PostMapping("/search_contents")
    public ResponseEntity<List<Contents>> searchContents(@RequestParam("input_contents")String __inputContents){
        try {
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlsessionfactory = new SqlSessionFactoryBuilder().build(inputStream);
            SqlSession sqlsession = sqlsessionfactory.openSession();
            contents_mapper __Cm = sqlsession.getMapper(contents_mapper.class);

            List<Contents> __contentsList = __Cm.searchContents(__inputContents);
            sqlsession.close();
            System.out.println(__inputContents+__contentsList);
            return ResponseEntity.ok(__contentsList);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 更改返回错误处理，避免返回null
        }
    }
    @PostMapping("/send_inputContents")
    public ResponseEntity<String> addContents(@RequestParam("add_title")String __addTitle,
                                                      @RequestParam("add_contents")String __addContents,
                                                      @RequestParam("add_user")String __addUser){
        try {
            Contents __contents = new Contents();
            __contents.setId(1);
            __contents.setTitle(__addTitle);
            __contents.setStatus(1);
            __contents.setCreated_user(__addUser);
            __contents.setText(__addContents);

            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlsessionfactory = new SqlSessionFactoryBuilder().build(inputStream);
            SqlSession sqlsession = sqlsessionfactory.openSession();
            contents_mapper __Cm = sqlsession.getMapper(contents_mapper.class);

            __Cm.insert_contentsListAll(__contents);
            sqlsession.commit();
            sqlsession.close();
            return ResponseEntity.ok("ok");
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("no", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

