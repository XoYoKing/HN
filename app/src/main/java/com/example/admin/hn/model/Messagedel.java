package com.example.admin.hn.model;

import java.util.List;

/**
 * Created by dlg on 2017/10/28.
 */
public class Messagedel {
    public String Userid ;
    public List<Message> Documents;

    public Messagedel(List<Message> documents, String userid) {
        Documents = documents;
        this.Userid = userid;
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        this.Userid = userid;
    }

    public List<Message> getDocuments() {
        return Documents;
    }

    public void setDocuments(List<Message> documents) {
        Documents = documents;
    }

    public static class Message{
        private String Messageid;

        public String getMessageid() {
            return Messageid;
        }

        public void setMessageid(String messageid) {
            Messageid = messageid;
        }

        public Message(String messageid) {
            Messageid = messageid;
        }
    }
}
