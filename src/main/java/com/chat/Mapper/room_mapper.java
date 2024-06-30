package com.chat.Mapper;

import com.chat.Model.Room;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface room_mapper {
    List<Room> get_room(Room room);
    @Select("SELECT room_name FROM room WHERE id = #{roomId}")
    String getRoomNameById(@Param("roomId") Integer roomId);
}
