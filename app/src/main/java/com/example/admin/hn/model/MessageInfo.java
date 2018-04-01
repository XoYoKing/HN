package com.example.admin.hn.model;

import java.util.List;

/**
 * Created by duantao
 *
 * @date on 2017/9/27 14:43
 */
public class MessageInfo {
    private List<Messageinfo> Documents;

    public List<Messageinfo> getDocuments() {
        return Documents;
    }

    public void setDocuments(List<Messageinfo> documents) {
        Documents = documents;
    }

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

    public static class Messageinfo {
        private String linename;
        private String noticetime;
        private String orderdetails;
        private String messagestate;
        private String noticecontent;
        private String ordernumber;
        private String messageid;
        private String shipname;

        public Messageinfo(String linename, String orderdetails, String noticetime, String messagestate, String ordernumber, String noticecontent, String messageid, String shipname) {
            this.linename = linename;
            this.orderdetails = orderdetails;
            this.noticetime = noticetime;
            this.messagestate = messagestate;
            this.ordernumber = ordernumber;
            this.noticecontent = noticecontent;
            this.messageid = messageid;
            this.shipname = shipname;
        }

        public String getLinename() {
            return linename;
        }

        public void setLinename(String linename) {
            this.linename = linename;
        }

        public String getNoticetime() {
            return noticetime;
        }

        public void setNoticetime(String noticetime) {
            this.noticetime = noticetime;
        }

        public String getOrderdetails() {
            return orderdetails;
        }

        public void setOrderdetails(String orderdetails) {
            this.orderdetails = orderdetails;
        }

        public String getMessagestate() {
            return messagestate;
        }

        public void setMessagestate(String messagestate) {
            this.messagestate = messagestate;
        }

        public String getNoticecontent() {
            return noticecontent;
        }

        public void setNoticecontent(String noticecontent) {
            this.noticecontent = noticecontent;
        }

        public String getOrdernumber() {
            return ordernumber;
        }

        public void setOrdernumber(String ordernumber) {
            this.ordernumber = ordernumber;
        }

        public String getShipname() {
            return shipname;
        }

        public void setShipname(String shipname) {
            this.shipname = shipname;
        }

        public String getMessageid() {
            return messageid;
        }

        public void setMessageid(String messageid) {
            this.messageid = messageid;
        }
    }
}
