package com.chat.Model;

public class Store {
    private Integer id;
    public  Integer getId(){return id;}
    public  void setId(Integer id){this.id=id;}

    private String goods_name;
    public String getGoods_name() {
        return goods_name;
    }
    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    private Double goods_sell;
    public Double getGoods_sell() {
        return goods_sell;
    }
    public void setGoods_sell(Double goods_sell) {
        this.goods_sell = goods_sell;
    }
    @Override
    public String toString() {
        return id+','+goods_name+','+goods_sell;
    }

}
