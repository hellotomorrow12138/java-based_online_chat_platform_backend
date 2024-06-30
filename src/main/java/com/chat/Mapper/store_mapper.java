package com.chat.Mapper;

import com.chat.Model.Store;
import com.chat.Model.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface store_mapper {
    List<Store> get_storeList(Store store);
    Integer username_hasStore(User user);
    List<Store> user_getStore(Integer store);

}
