package com.example.admin.hn.model;

/**
 * 船舶资料管理 订单为已选状态下数据对象
 */
public class OrderUseInfo {
    public String id;//资料id
    public String code;//资料编号
    public String chs_name;//中文名
    public String eng_name;//英文名
    public int quantity;//购买数量
    public int storage_amount;//库存
    public String publish_at;//出版日期
    public String category_name;//资料类别
    public String ship_name;//船舶名称
    public String distribute_no;//分配编号（岸端分配）
    public String source;//状态（区分是自选还是 岸端分配）

    @Override
    public String toString() {
        return "OrderUseInfo{" +
                "code='" + code + '\'' +
                ", chs_name='" + chs_name + '\'' +
                ", eng_name='" + eng_name + '\'' +
                ", quantity=" + quantity +
                ", storage_amount=" + storage_amount +
                ", publish_at='" + publish_at + '\'' +
                ", category_name='" + category_name + '\'' +
                ", ship_name='" + ship_name + '\'' +
                ", distribute_no='" + distribute_no + '\'' +
                ", source='" + source + '\'' +
                '}';
    }
}
