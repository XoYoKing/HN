package com.example.admin.hn.model;

import java.io.Serializable;

/**
 * Created by hjy on 2016/11/5.
 * 生成的订单
 */
public class ShoppingCartInfo implements Serializable {
    private String id;//订单号
    private String goods_name;//商品名称
    private String goods_id;//商品id
    private String imgUrl;//图片地址
    private int goods_num;//购买数量
    private double real_price;//实际购买金额
    private int real_integral;//实际购买积分
    private int selected;//是否被选中 0未选中 1被选中
    private int store_count;//库存

    public int getStore_count() {
        return store_count;
    }

    public void setStore_count(int store_count) {
        this.store_count = store_count;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public int getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(int goods_num) {
        this.goods_num = goods_num;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public double getReal_price() {
        return real_price;
    }

    public void setReal_price(double real_price) {
        this.real_price = real_price;
    }

    public int getReal_integral() {
        return real_integral;
    }

    public void setReal_integral(int real_integral) {
        this.real_integral = real_integral;
    }

}
