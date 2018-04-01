package com.example.admin.hn.model;

import java.util.List;

/**
 * Created by duantao
 *
 * @date on 2017/9/14 14:54
 */
public class OrderInfo {


    private List<Order> Documents;

    private String message;
    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Order> getDocuments() {
        return Documents;
    }

    public void setDocuments(List<Order> documents) {
        Documents = documents;
    }

    public static class Order {
        private String ordernumber;
        private String ordertime;
        private String status;
        private String shipname;
        private String cname;
        private String orderstatus;
        private String currency;
        private String coord;

        public String getCoord() {
            return coord;
        }

        public void setCoord(String coord) {
            this.coord = coord;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getCname() {
            return cname;
        }

        public void setCname(String cname) {
            this.cname = cname;
        }

        public String getOrderstatus() {
            return orderstatus;
        }

        public void setOrderstatus(String orderstatus) {
            this.orderstatus = orderstatus;
        }

        public String getShipname() {
            return shipname;
        }

        public void setShipname(String shipname) {
            this.shipname = shipname;
        }

        public String getOrdernumber() {
            return ordernumber;
        }

        public void setOrdernumber(String ordernumber) {
            this.ordernumber = ordernumber;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getOrdertime() {
            return ordertime;
        }

        public void setOrdertime(String ordertime) {
            this.ordertime = ordertime;
        }

        public Order(String ordernumber, String ordertime, String status,String shipname) {
            this.ordernumber = ordernumber;
            this.ordertime = ordertime;
            this.status = status;
            this.shipname = shipname;
        }

        private String money;
        private String linename;

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getLinename() {
            return linename;
        }

        public void setLinename(String linename) {
            this.linename = linename;
        }
    }




}
