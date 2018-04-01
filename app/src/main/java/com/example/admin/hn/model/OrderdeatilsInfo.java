package com.example.admin.hn.model;

import java.util.List;

/**
 * Created by duantao
 *
 * @date on 2017/10/30 14:35
 */
public class OrderdeatilsInfo {

    private String status;
    private String message;
    private List<deatils> charts;
    private Order order;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<deatils> getCharts() {
        return charts;
    }

    public void setCharts(List<deatils> charts) {
        this.charts = charts;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderdeatilsInfo(String status, String message, List<deatils> charts, Order order) {
        this.status = status;
        this.message = message;
        this.charts = charts;
        this.order = order;
    }

    private static class Order{
        private String Date;
        private String money;

        public String getDate() {
            return Date;
        }

        public void setDate(String date) {
            Date = date;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public Order(String date, String money) {
            Date = date;
            this.money = money;
        }
    }

    public static class deatils{
        private String dataNumber;
        private String chartTitle;
        private String version;
        private String Number;
        private String Price;
        private String cycle;

        public deatils(String dataNumber, String price, String number, String version, String chartTitle, String cycle) {
            this.dataNumber = dataNumber;
            Price = price;
            Number = number;
            this.version = version;
            this.chartTitle = chartTitle;
            this.cycle = cycle;
        }

        public String getDataNumber() {
            return dataNumber;
        }

        public void setDataNumber(String dataNumber) {
            this.dataNumber = dataNumber;
        }

        public String getChartTitle() {
            return chartTitle;
        }

        public void setChartTitle(String chartTitle) {
            this.chartTitle = chartTitle;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getNumber() {
            return Number;
        }

        public void setNumber(String number) {
            Number = number;
        }

        public String getPrice() {
            return Price;
        }

        public void setPrice(String price) {
            Price = price;
        }

        public String getCycle() {
            return cycle;
        }

        public void setCycle(String cycle) {
            this.cycle = cycle;
        }
    }
}
