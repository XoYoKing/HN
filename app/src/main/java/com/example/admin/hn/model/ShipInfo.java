package com.example.admin.hn.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by duantao
 *
 * @date on 2017/10/30 17:10
 */
public class ShipInfo {
    private String Userid;

    public ShipInfo(String userid, List<ship> documents) {
        Userid = userid;
        Documents = documents;
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }

    private String message;
    private String status;
    private List<ship> Documents;

    public ShipInfo(String message, String status, List<ship> documents) {
        this.message = message;
        this.status = status;
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

    public List<ship> getDocuments() {
        return Documents;
    }

    public void setDocuments(List<ship> documents) {
        Documents = documents;
    }

    public static class ship implements Serializable {
        private String shipnumber;
        private String shipname;

        public ship(String shipnumber, String shipname) {
            this.shipnumber = shipnumber;
            this.shipname = shipname;
        }

        public ship(String shipnumber) {
            this.shipnumber = shipnumber;
        }

        public String getShipnumber() {
            return shipnumber;
        }

        public void setShipnumber(String shipnumber) {
            this.shipnumber = shipnumber;
        }

        public String getShipname() {
            return shipname;
        }

        public void setShipname(String shipname) {
            this.shipname = shipname;
        }
    }
}
