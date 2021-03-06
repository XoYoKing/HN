package com.example.admin.hn.ui.fragment.shop.bean;



import java.io.Serializable;
import java.util.List;

/**
 * Created by WIN10 on 2018/5/3.
 * 商城 订单管理
 */

public class SubmitGoodsInfo  implements Serializable {

    public String memberName;
    public String memberId;
    public String orderSource;
    public String status;
    public int isCancel;
    public double orderAmount;
    public String receiverAddr;
    public String receiverName;
    public String receiverPhone;
    public String receiverNote;
    public List<Goods> item;

    public static class Goods{
        public String goodsName;//商品名称
        public String goodsId;//商品ID
        public String spuId;//SPUID
        public String goodsSpec;//产品规格
        public String goodsFullSpecs;//商品规格
        public int qty;//购买数量
        public double goodsPrice;//商品价格
        public double amount;//商品总金额
        public int isComment;//是否已评论
    }

}
