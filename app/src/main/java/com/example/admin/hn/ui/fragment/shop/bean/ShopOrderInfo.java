package com.example.admin.hn.ui.fragment.shop.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by WIN10 on 2018/5/3.
 * 商城 订单管理
 */

public class ShopOrderInfo implements Serializable {
    public String id;//订单ID
    public String storeId;//店铺ID
    public String orderNo;//订单编号
    public String areaId;//地区ID
    public String areaName;//区域全称
    public String receiverAddr;//收货人地址
    public String receiverName;//收货人姓名
    public String receiverPhone;//收货人电话
    public String receiverNote;//收货人备注
    public String memberId;//会员ID
    public String memberName;//会员名称
    public int status;//订单状态
    public double freight;//运费
    public double orderAmount;//订单金额
    public String expressId;//快递公司ID
    public String expressName;//快递公司名称
    public String expressNo;//快递单号
    public String orderDate;//订单时间
    public String paymentDate;//支付时间
    public String paymentType;//支付类型
    public String paymentNo;//支付流水号
    public String sendDate;//发货时间
    public String cancelDate;//取消时间
    public String receiveDate;//收货时间
    public String orderSource;//订单来源
    public int number;//订单中的商品数量
    public List<OrderItems> orderItems;

    public class OrderItems implements Serializable{
        public String id;
        public String orderId;//订单ID
        public String goodsId;//商品ID
        public String spuId;//商品SPU
        public String goodsSpec;//产品规格
        public String goodsFullSpecs;//产品规格全称
        public String goodsName;//商品名称
        public double goodsPrice;//商品单价
        public String qty;//数量
        public String imageUrl;//图片地址
        public double amount;//商品总金额
        public String isComment;//是否已评价
        public String orderSource;//订单来源

        @Override
        public String toString() {
            return "OrderItems{" +
                    "id='" + id + '\'' +
                    ", orderId='" + orderId + '\'' +
                    ", goodsId='" + goodsId + '\'' +
                    ", spuId='" + spuId + '\'' +
                    ", goodsSpec='" + goodsSpec + '\'' +
                    ", goodsFullSpecs='" + goodsFullSpecs + '\'' +
                    ", goodsName='" + goodsName + '\'' +
                    ", goodsPrice=" + goodsPrice +
                    ", qty='" + qty + '\'' +
                    ", imageUrl='" + imageUrl + '\'' +
                    ", amount=" + amount +
                    ", isComment='" + isComment + '\'' +
                    ", orderSource='" + orderSource + '\'' +
                    '}';
        }
    }
}
