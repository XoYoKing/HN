package com.example.admin.hn.model;

import java.util.List;

/**
 * 船舶资料管理 订单为领用状态下数据对象
 */
public class OrderUseInfo {

    public List<OrderUser> Documents;
    public static class OrderUser {
        public boolean isSelect;//是否选中
        public int buyNumber;//购买数量
        public int inventory;//库存
        public String date;//出版日期
        public String dateNumber;//出版日期

        public OrderUser(boolean isSelect, int buyNumber, int inventory, String date, String dateNumber) {
            this.isSelect = isSelect;
            this.buyNumber = buyNumber;
            this.inventory = inventory;
            this.date = date;
            this.dateNumber = dateNumber;
        }

        @Override
        public String toString() {
            return "OrderUser{" +
                    "isSelect=" + isSelect +
                    ", buyNumber=" + buyNumber +
                    ", inventory=" + inventory +
                    ", date='" + date + '\'' +
                    ", dateNumber='" + dateNumber + '\'' +
                    '}';
        }
    }
}
