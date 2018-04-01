package com.example.admin.hn.model;

import java.util.List;

/**
 * Created by duantao
 *
 * @date on 2017/9/14 14:54
 */
public class ChartInfo {


    private List<Chart> Documents;

    private String status;

    private String message;

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

    public List<Chart> getDocuments() {
        return Documents;
    }

    public void setDocuments(List<Chart> documents) {
        Documents = documents;
    }

    public static class Chart {
        private String ordernumber;
        private String productNumber;
        private String updatetime;
        private String shipname;

        public String getOrdernumber() {
            return ordernumber;
        }

        public void setOrdernumber(String ordernumber) {
            this.ordernumber = ordernumber;
        }

        public String getProductNumber() {
            return productNumber;
        }

        public void setProductNumber(String productNumber) {
            this.productNumber = productNumber;
        }

        public String getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
        }

        public String getShipname() {
            return shipname;
        }

        public void setShipname(String shipname) {
            this.shipname = shipname;
        }

        public Chart(String ordernumber, String shipname, String updatetime, String productNumber) {
            this.ordernumber = ordernumber;
            this.shipname = shipname;
            this.updatetime = updatetime;
            this.productNumber = productNumber;
        }
    }




}
