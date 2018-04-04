package com.example.admin.hn.model;

/**
 * 订单管理的实体类
 * Created by hjy on 2017/3/10.
 */

public class OrderManagerInfo {

    private String imgUrl;//商品的图片
    private String goods_name;//商品的名称
    private String sell_price;//单个商品的价格
    private String goods_price;//该商品的总价格
    private String goods_num;//该商品的数量
    private String shipping_price;//邮费的价格
    private int useType;//订单状态
    private int order_id;//订单id

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getUseType() {
        return useType;
    }

    public void setUseType(int useType) {
        this.useType = useType;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getShipping_price() {
        return shipping_price;
    }

    public void setShipping_price(String shipping_price) {
        this.shipping_price = shipping_price;
    }

    public String getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(String goods_num) {
        this.goods_num = goods_num;
    }

    public String getSell_price() {
        return sell_price;
    }

    public void setSell_price(String sell_price) {
        this.sell_price = sell_price;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }


    public OrderManagerInfo(String imgUrl, String shipping_price, String goods_num, String goods_price, String sell_price, String goods_name) {
        this.imgUrl = imgUrl;
        this.shipping_price = shipping_price;
        this.goods_num = goods_num;
        this.goods_price = goods_price;
        this.sell_price = sell_price;
        this.goods_name = goods_name;
    }
}
