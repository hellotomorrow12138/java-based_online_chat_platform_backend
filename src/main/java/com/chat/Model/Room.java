package com.chat.Model;

public class Room {
    private Integer id;
    public  Integer getId(){return id;}
    public  void setId(Integer id){this.id=id;}

    private String room_name;
    public String getRoom_name() {
        return room_name;
    }
    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

//    private String room_maxpeople;
//    public String getRoom_maxpeople() {
//        return room_maxpeople;
//    }
//    public void setRoom_maxpeople(String room_maxpeople) {
//        this.room_maxpeople = room_maxpeople;
//    }

    @Override
    public String toString() {
        return room_name;
    }
}
