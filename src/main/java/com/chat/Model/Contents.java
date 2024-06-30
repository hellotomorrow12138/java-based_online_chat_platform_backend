package com.chat.Model;

public class Contents {
    private Integer id;
    public  Integer getId(){return id;}
    public  void setId(Integer id){this.id=id;}

    private String title;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    private Integer status;
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }

    private String created_user;
    public String getCreated_user() {
        return created_user;
    }
    public void setCreated_user(String created_user) {
        this.created_user = created_user;
    }

    private String text;
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return id+','+title+','+status+','+created_user+','+text;
    }
}
