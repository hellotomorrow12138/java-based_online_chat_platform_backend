package com.chat.Model;

public class Vip {
    private Integer id;
    public  Integer getId(){return id;}
    public  void setId(Integer id){this.id=id;}

    private String vip_name;
    public String getVip_name() {
        return vip_name;
    }
    public void setVip_name(String vip_name) {
        this.vip_name = vip_name;
    }

    private Integer vip_level;
    public Integer getVip_level() {
        return vip_level;
    }
    public void setVip_level(Integer vip_level) {
        this.vip_level = vip_level;
    }

    private Integer vip_cost;
    public Integer getVip_cost() {
        return vip_cost;
    }
    public void setVip_cost(Integer vip_cost) {
        this.vip_cost = vip_cost;
    }
    @Override
    public String toString() {
        return id+','+vip_name+','+vip_level+','+vip_cost;
    }
}
