package com.example.admin.hn.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by geek on 2016/11/1.
 * 商品
 */
public class GoodsInfo implements Serializable {

    public List<Goods> content;//商品列表
    public int totalPages;//总页数
    public boolean last;//

    public class Goods implements Serializable{
        public String spuId;//商品ID
        public String storeId;//店铺ID
        public String categoryId;//商品分类ID
        public String goodsName;//商品名称
        public double goodsPrice;//商品价格
        public String brandId;//品牌id
        public String brandName;//品牌名称
        public String unitName;//商品数量单位
        public String descriptionUrl;//商品详情地址
        public String imageUrl;//图片地址
        public String goodsFullSpecs;//规格全称
        public String usp;//买点
        public double goodsFreight;//运费
        public double freightWeight;//重量
        public double freightVolume;//体积
        public int collectCount;//收藏数量
        public int reviewCount;//评论数量
        public int orderCount;//销量


    }

}
