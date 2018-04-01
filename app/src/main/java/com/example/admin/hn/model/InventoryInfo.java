package com.example.admin.hn.model;

import java.util.List;

/**
 * Created by duantao
 *
 * @date on 2017/9/14 14:54
 */
public class InventoryInfo {

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

    private List<Inventory> Documents;

    public List<Inventory> getDocuments() {
        return Documents;
    }

    public void setDocuments(List<Inventory> documents) {
        Documents = documents;
    }

    public InventoryInfo(String message, String status, List<Inventory> documents) {
        this.message = message;
        this.status = status;
        Documents = documents;
    }

    public static class Inventory {
        private String period;
        private String wholesaleprices;
        private String cname;

        private String  ProductType;
        private String  scale;
        private String  productNumber;
        private String  ordertime;
        private String  version;
        private String  productTitle;

        private String  ordernumber;
        private String  guideprice;
        private String  shipnumber;
        private String  routename;
        private String  versionunit;
        private String  shipname;

        public Inventory(String period, String wholesaleprices, String cname, String productType, String productNumber, String scale, String ordertime, String version, String productTitle, String ordernumber, String guideprice, String shipnumber, String routename, String shipname, String versionunit) {
            this.period = period;
            this.wholesaleprices = wholesaleprices;
            this.cname = cname;
            ProductType = productType;
            this.productNumber = productNumber;
            this.scale = scale;
            this.ordertime = ordertime;
            this.version = version;
            this.productTitle = productTitle;
            this.ordernumber = ordernumber;
            this.guideprice = guideprice;
            this.shipnumber = shipnumber;
            this.routename = routename;
            this.shipname = shipname;
            this.versionunit = versionunit;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public String getWholesaleprices() {
            return wholesaleprices;
        }

        public void setWholesaleprices(String wholesaleprices) {
            this.wholesaleprices = wholesaleprices;
        }

        public String getCname() {
            return cname;
        }

        public void setCname(String cname) {
            this.cname = cname;
        }

        public String getProductType() {
            return ProductType;
        }

        public void setProductType(String productType) {
            ProductType = productType;
        }

        public String getScale() {
            return scale;
        }

        public void setScale(String scale) {
            this.scale = scale;
        }

        public String getProductNumber() {
            return productNumber;
        }

        public void setProductNumber(String productNumber) {
            this.productNumber = productNumber;
        }

        public String getOrdertime() {
            return ordertime;
        }

        public void setOrdertime(String ordertime) {
            this.ordertime = ordertime;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getOrdernumber() {
            return ordernumber;
        }

        public void setOrdernumber(String ordernumber) {
            this.ordernumber = ordernumber;
        }

        public String getProductTitle() {
            return productTitle;
        }

        public void setProductTitle(String productTitle) {
            this.productTitle = productTitle;
        }

        public String getGuideprice() {
            return guideprice;
        }

        public void setGuideprice(String guideprice) {
            this.guideprice = guideprice;
        }

        public String getShipnumber() {
            return shipnumber;
        }

        public void setShipnumber(String shipnumber) {
            this.shipnumber = shipnumber;
        }

        public String getRoutename() {
            return routename;
        }

        public void setRoutename(String routename) {
            this.routename = routename;
        }

        public String getVersionunit() {
            return versionunit;
        }

        public void setVersionunit(String versionunit) {
            this.versionunit = versionunit;
        }

        public String getShipname() {
            return shipname;
        }

        public void setShipname(String shipname) {
            this.shipname = shipname;
        }
    }




}
