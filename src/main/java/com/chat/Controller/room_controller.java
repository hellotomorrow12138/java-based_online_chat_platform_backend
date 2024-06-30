package com.chat.Controller;

import com.chat.Model.Room;
import com.chat.Mapper.room_mapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api")
public class room_controller {
    @GetMapping("/getRoomName")
    public String getRoomName(@RequestParam Integer roomId) throws IOException {
        try {
            String __resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(__resource);
            SqlSessionFactory sqlsessionfactory = new SqlSessionFactoryBuilder().build(inputStream);
            SqlSession sqlsession = sqlsessionfactory.openSession();
            room_mapper __roomMapper = sqlsession.getMapper(room_mapper.class);

            String roomName = __roomMapper.getRoomNameById(roomId);
            sqlsession.close();
            return roomName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @GetMapping("/room_controller")
    public List<Room> get_contents() {
        try {
            Room __room = new Room();

            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlsessionfactory = new SqlSessionFactoryBuilder().build(inputStream);
            SqlSession sqlsession = sqlsessionfactory.openSession();
            room_mapper __Rm = sqlsession.getMapper(room_mapper.class);

            List<Room> __roomList = __Rm.get_room(__room);
            return __roomList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
