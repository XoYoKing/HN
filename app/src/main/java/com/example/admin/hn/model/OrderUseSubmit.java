package com.example.admin.hn.model;

/**
 * 船舶资料管理 订单为未选择状态下提交数据对象
 */
public class OrderUseSubmit {
    public int quantity;//配置数量
    public String docId;//资料id
    public String code;//资料编号
    public String shipId;//船舶id
    public String publishTime;//出版日期
    public String distribute_no;//分配编号（岸端分配）
    public String source;//状态（区分是自选还是 岸端分配）

    public OrderUseSubmit(int quantity, String docId, String code, String shipId, String publishTime, String distribute_no, String source) {
        this.quantity = quantity;
        this.docId = docId;
        this.code = code;
        this.shipId = shipId;
        this.publishTime = publishTime;
        this.distribute_no = distribute_no;
        this.source = source;
    }
}
