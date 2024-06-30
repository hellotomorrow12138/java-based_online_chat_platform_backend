package com.chat.Mapper;

import com.chat.Model.Contents;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface contents_mapper {
    List<Contents> get_contentsListAll(Contents contents);
    @Select("SELECT * FROM contents WHERE id = #{userId}")
    List<Contents> getContentsById(@Param("userId") Integer userId);
    @Select("SELECT * FROM contents WHERE title like concat('%',#{__inputContents},'%')")
    List<Contents> searchContents(@Param("__inputContents") String __inputContents);

    void insert_contentsListAll(Contents contents);


}
