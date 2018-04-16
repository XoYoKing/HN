package com.example.admin.hn.model;

/**
 * 船舶资料管理 订单为未选择状态下提交数据对象
 */
public class OrderUseSubmit {
    public int quantity;//配置数量
    public String code;//资料编号
    public String publishTime;//出版日期
    public String shipId;//船舶

    public OrderUseSubmit(int quantity, String code, String publishTime, String shipId) {
        this.quantity = quantity;
        this.code = code;
        this.publishTime = publishTime;
        this.shipId = shipId;
    }
}
