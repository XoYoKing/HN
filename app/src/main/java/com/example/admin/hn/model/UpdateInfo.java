package com.example.admin.hn.model;

import java.util.List;

/**
 * Created by duantao
 *
 * @date on 2017/11/10 10:57
 */
public class UpdateInfo {
    private List<update> Documents;

    private String message;
    private String status;

    public List<update> getDocuments() {
        return Documents;
    }

    public void setDocuments(List<update> documents) {
        Documents = documents;
    }

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

    public static class update {
        private String shipname;
        private String shipstatus;
        private String download;
        private String size;
        private String updatetime;
        private String ordertime;
        private String shipnumber;

        public update(String shipname, String shipnumber, String ordertime, String updatetime, String size, String download, String shipstatus) {
            this.shipname = shipname;
            this.shipnumber = shipnumber;
            this.ordertime = ordertime;
            this.updatetime = updatetime;
            this.size = size;
            this.download = download;
            this.shipstatus = shipstatus;
        }

        public String getShipname() {
            return shipname;
        }

        public void setShipname(String shipname) {
            this.shipname = shipname;
        }

        public String getOrdertime() {
            return ordertime;
        }

        public void setOrdertime(String ordertime) {
            this.ordertime = ordertime;
        }

        public String getShipnumber() {
            return shipnumber;
        }

        public void setShipnumber(String shipnumber) {
            this.shipnumber = shipnumber;
        }

        public String getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getDownload() {
            return download;
        }

        public void setDownload(String download) {
            this.download = download;
        }

        public String getShipstatus() {
            return shipstatus;
        }

        public void setShipstatus(String shipstatus) {
            this.shipstatus = shipstatus;
        }
    }
}
