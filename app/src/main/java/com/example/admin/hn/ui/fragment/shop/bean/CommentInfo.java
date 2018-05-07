package com.example.admin.hn.ui.fragment.shop.bean;

import java.io.Serializable;

/**
 * Created by WIN10 on 2018/5/3.
 * 商城 评价列表
 */

public class CommentInfo implements Serializable {
    public String storeId;//店铺ID
    public String storeName;//店铺名称
    public String memberId;//用户ID
    public String memberName;//用户名称
    public String goodsId;//商品ID(SKU)
    public String spuId;//商品SPU
    public String goodsSpec;//产品规格
    public String goodsFullSpecs;//产品规格全称
    public String goodsName;//商品名称
    public double goodsPrice;//商品价格
    public String score;//商品评分
    public String content;//商品评价内容
    public String recontent;//回复评价内容
    public double amount;//商品总金额
}
