package com.example.admin.hn.model;

import java.io.Serializable;

/**
 * Created by WIN10 on 2018/3/30.
 */

public class HomeItem implements Serializable {
//    {\"image\":\"imageurl\",\"name\":\"数码1\",\"operate\":\"5\",\"value\":\"3\"}
    /**
     * NONE("无操作"), // 无操作   operate=0
     * URL("链接地址"), // 链接地址  operate=1
     * KEYWORD("关键字"), // 关键字   operate=2
     * GOODS("商品编号"), // 商品编号 operate=3
     * STORE("店铺编号"), // 店铺编号 operate=4
     * CATEGORY("商品分类"), // 商品分类 operate=5
     * CHANNEL("频道编号"), // 频道编号 operate=6
     * BRAND_LIST("品牌列表"), // 品牌列表 operate=7
     */
    public String image;//图片地址
    public String name;//名称
    public String operate;//操作
    public String value;//
}
