package com.chat.Mapper;

import com.chat.Model.Store;
import com.chat.Model.User;
import com.chat.Model.Vip;

import java.util.List;

public interface vip_mapper {
    Integer username_vipLevel(User user);
//    List<Vip> get_vipList(Vip vip);
    List<Vip> user_getVip(Integer vip);
}
