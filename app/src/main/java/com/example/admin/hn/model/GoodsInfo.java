package com.example.admin.hn.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by geek on 2016/11/1.
 * 商品
 */
public class GoodsInfo implements Serializable {

    public Spu spu;//商品spu
    public Goods goods;//商品
    public List<SpecInfo> spec;//規格

    public class Spu implements Serializable{
        public String id;//商品ID
        public String storeId;//店铺ID
        public String categoryId;//商品分类ID
        public String goodsName;//商品名称
        public String brandId;//品牌id
        public String brandName;//品牌名称
        public String unitName;//商品数量单位
        public String descriptionUrl;//商品详情地址
        public String usp;//买点
        public double goodsFreight;//运费
        public double freightWeight;//重量
        public double freightVolume;//体积
        public int collectCount;//收藏数量
        public int reviewCount;//评论数量
        public int orderCount;//销量
    }

    public class Goods implements Serializable{
        public String storeId;//店铺ID
        public String spuId;//商品SPUID
        public double goodsPrice;//商品价格
        public String goodsSpec;//商品规格
        public String goodsFullSpecs;//商品规格
        public int qty;//库存
        public String imageUrl;//图片
        public String mainAttrId;//商品属性ID(映射第一个属性)
        public String specItemsIds;//规格iDS，逗号分隔
    }

    public class SpecInfo implements Serializable{
        public List<SpecItem> specItem;//
        public Spec spec;

        public class Spec implements Serializable{
            public String id;//
            public String specId;//
            public String specName;//
        }

        public class SpecItem implements Serializable{
            public String id;//
            public String specId;//
            public String specName;//
            public String specItemId;//
            public String specItemName;//
            public String selectId;//被选中的id
            public boolean isSelect;//是否选中
        }
    }
}
