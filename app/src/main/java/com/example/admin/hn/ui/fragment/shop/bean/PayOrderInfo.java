package com.example.admin.hn.ui.fragment.shop.bean;

import java.io.Serializable;

/**
 * Created by WIN10 on 2018/5/17.
 * 支付订单信息
 */

public class PayOrderInfo implements Serializable {
    public String orderNo;//订单号
    public String paymentNo;//支付订单号
    public double orderAmount;//订单支付金额
}
