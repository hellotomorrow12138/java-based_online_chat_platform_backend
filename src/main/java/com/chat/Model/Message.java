package com.chat.Model;
import lombok.Data;
/**
 * @author KSaMar
 * @version 1.0
 * 信息实体类
 */
@Data
public class Message {
    private String name;
    private String time;
    private String msg;
    private String roomId;
}
