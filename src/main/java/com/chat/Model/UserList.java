package com.chat.Model;
import lombok.Data;

import java.util.List;
/**
 * @author KSaMar
 * @version 1.0
 * 用户列表
 */
@Data
public class UserList {
    private List<String> userlist;
    private List<String> roomid;

}
