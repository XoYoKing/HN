package com.example.admin.hn.ui.fragment.shop.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

/**
 * Created by WIN10 on 2018/5/3.
 * 商城 订单管理
 */

public class ShopCartInfo extends DataSupport implements Serializable {
    private int id;
    private boolean isSelect;//是否选中
    private boolean isEditSelect;//是否编辑状态下的选中
    private int buyNumber;//购买数量
    private int qty;//库存
    private String goodsId;//商品ID
    private String spuId;//SPUID
    private String goodsName;//商品名称
    private String usp;//商品买点
    private double goodsPrice;//商品价格
    private String imageUrl;//商品图片
    private List<String> currGoodsSpecItemsIds;//当前规格参数

    public List<String> getCurrGoodsSpecItemsIds() {
        return currGoodsSpecItemsIds;
    }

    public void setCurrGoodsSpecItemsIds(List<String> currGoodsSpecItemsIds) {
        this.currGoodsSpecItemsIds = currGoodsSpecItemsIds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getUsp() {
        return usp;
    }

    public void setUsp(String usp) {
        this.usp = usp;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isEditSelect() {
        return isEditSelect;
    }

    public void setEditSelect(boolean editSelect) {
        isEditSelect = editSelect;
    }

    public int getBuyNumber() {
        return buyNumber;
    }

    public void setBuyNumber(int buyNumber) {
        this.buyNumber = buyNumber;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getSpuId() {
        return spuId;
    }

    public void setSpuId(String spuId) {
        this.spuId = spuId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
