package com.chat.Mapper;
import com.chat.Model.User;

import java.util.List;
public interface message_mapper {
    List<User> get_user(User user);
    void reg_user(User user);
    void updateUser(User user);

}
