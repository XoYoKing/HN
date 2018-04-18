package com.example.admin.hn.model;

/**
 * 船舶资料管理 订单为未选择状态下提交数据对象
 */
public class OrderNotUseSubmit {
    public int quantity;//配置数量
    public String docId;//资料ID
    public String code;//资料编号
    public String publishTime;//出版日期
    public String shipId;//船舶id

    public OrderNotUseSubmit(int quantity,String id, String code, String publishTime, String shipId) {
        this.docId = id;
        this.quantity = quantity;
        this.code = code;
        this.publishTime = publishTime;
        this.shipId = shipId;
    }
}
