package com.example.admin.hn.ui.fragment.shop.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by WIN10 on 2018/5/3.
 * 商城 订单管理
 */

public class ShopCartInfo implements Serializable {
    public boolean isSelect;//是否选中
    public boolean isEditSelect;//是否编辑状态下的选中
    public int buyNumber=1;//购买数量
    public int inventory=10;//库存
    public int qty=10;//库存

}
