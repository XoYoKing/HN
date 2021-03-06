package com.example.admin.hn.ui.fragment.shop.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

/**
 * Created by WIN10 on 2018/5/3.
 * 商城 订单管理
 */

public class ShopCartInfo extends DataSupport implements Serializable {

    @Column(unique = true)
    private long id;
    private boolean select;//是否选中
    private boolean editSelect;//是否编辑状态下的选中
    private int buyNumber;//购买数量
    private int qty;//库存
    private String goodsId;//商品ID
    private String spuId;//SPUID
    private String goodsName;//商品名称
    private String goodsSpec;//商品规格
    private String goodsFullSpecs;//商品规格
    private String goodsFullName;//商品标题
    private String usp;//商品买点
    private double goodsPrice;//商品价格
    private double goodsFreight;//运费
    private String imageUrl;//商品图片
    private List<String> currGoodsSpecItemsIds;//当前规格参数

    public String getGoodsSpec() {
        return goodsSpec;
    }

    public void setGoodsSpec(String goodsSpec) {
        this.goodsSpec = goodsSpec;
    }

    public double getGoodsFreight() {
        return goodsFreight;
    }

    public void setGoodsFreight(double goodsFreight) {
        this.goodsFreight = goodsFreight;
    }

    public String getGoodsFullName() {
        return goodsFullName;
    }

    public void setGoodsFullName(String goodsFullName) {
        this.goodsFullName = goodsFullName;
    }

    public String getGoodsFullSpecs() {
        return goodsFullSpecs;
    }

    public void setGoodsFullSpecs(String goodsFullSpecs) {
        this.goodsFullSpecs = goodsFullSpecs;
    }

    public List<String> getCurrGoodsSpecItemsIds() {
        return currGoodsSpecItemsIds;
    }

    public void setCurrGoodsSpecItemsIds(List<String> currGoodsSpecItemsIds) {
        this.currGoodsSpecItemsIds = currGoodsSpecItemsIds;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsp() {
        return usp;
    }

    public void setUsp(String usp) {
        this.usp = usp;
    }


    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean isEditSelect() {
        return editSelect;
    }

    public void setEditSelect(boolean editSelect) {
        this.editSelect = editSelect;
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
