package com.chat.Model;

public class User {
    private Integer id;
    public  Integer getId(){return id;}
    public  void setId(Integer id){this.id=id;}

    private String user_name;
    public String getUser_name() {
        return user_name;
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    private String user_password;
    public String getUser_password() {
        return user_password;
    }
    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    private Integer vip;
    public  Integer getVip(){return vip;}
    public  void setVip(Integer vip){this.vip=vip;}

    private Integer has_store;
    public  Integer getHas_store(){return has_store;}
    public  void setHas_store(Integer has_store){this.has_store=has_store;}
    @Override
    public String toString() {
        return user_name + ',' + user_password + ',' + vip + ',' + has_store;
    }
}
