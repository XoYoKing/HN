package com.example.admin.hn.model;

import java.util.List;

/**
 * 船舶资料管理 订单为领用状态下数据对象
 */
public class SubmitOrderUseInfo {

    public String Userid;
    public List<OrderUseInfo> Documents;

    public SubmitOrderUseInfo(String userid, List<OrderUseInfo> documents) {
        Userid = userid;
        Documents = documents;
    }

    public static class OrderUseInfo {
        public int number;//数量
        public String dataNumber;//资料编号
        public String date;//出版日期

        public OrderUseInfo(int number, String dataNumber, String date) {
            this.number = number;
            this.dataNumber = dataNumber;
            this.date = date;
        }
    }
}
