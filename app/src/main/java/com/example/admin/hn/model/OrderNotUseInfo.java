package com.example.admin.hn.model;

/**
 * 船舶资料管理 订单为未选择状态下数据对象
 */
public class OrderNotUseInfo {
    public String id;//资料id
    public boolean isSelect;//是否选中
    public int quantity=1;//购买数量 默认为1
    public int storage_amount;//库存
    public String code;//资料编号
    public String publis_at;//出版日期
    public String chs_name;//中文名
    public String eng_name;//英文名

    @Override
    public String toString() {
        return "OrderNotUseInfo{" +
                "id='" + id + '\'' +
                ", isSelect=" + isSelect +
                ", quantity=" + quantity +
                ", storage_amount=" + storage_amount +
                ", code='" + code + '\'' +
                ", publis_at='" + publis_at + '\'' +
                ", chs_name='" + chs_name + '\'' +
                ", eng_name='" + eng_name + '\'' +
                '}';
    }
}
