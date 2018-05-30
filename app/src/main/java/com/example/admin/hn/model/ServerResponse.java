package com.example.admin.hn.model;

import java.util.List;

/**
 * 服务器返回
 * Created by Jaycee on 16/4/15.
 * E-mail：jayceeok@foxmail.com
 */
public class ServerResponse {

    /**
     * status : 200
     * msg : success
     */
    private int id;
    private String status;
    private String msg;
    private String orderId;
    private String key;
    private String token;
    private String data;
    private String amount;
    private String poundage ;
    private String outOfPocket ;
    private String systemswitch;
    private String message;
    private String phonenumber;
    private String username;
    private String companyid;
    private int mobile_user_type;
    private List<ShipInfo.Ship> myShip;

    public int getMobileusertype() {
        return mobile_user_type;
    }

    public void setMobileusertype(int mobileusertype) {
        this.mobile_user_type = mobileusertype;
    }

    public List<ShipInfo.Ship> getMyShip() {
        return myShip;
    }

    public void setMyShip(List<ShipInfo.Ship> myShip) {
        this.myShip = myShip;
    }

    public String getCompanyid() {
        return companyid;
    }
    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    private String userid;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSystemswitch() {
        return systemswitch;
    }

    public void setSystemswitch(String systemswitch) {
        this.systemswitch = systemswitch;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPoundage() {
        return poundage;
    }

    public void setPoundage(String poundage) {
        this.poundage = poundage;
    }

    public String getOutOfPocket() {
        return outOfPocket;
    }

    public void setOutOfPocket(String outOfPocket) {
        this.outOfPocket = outOfPocket;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    /**
     * email : 625435051@qq.com
     */

    private String email;
    /**
     * phone : 15852855880
     */

    private String phone;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() { return status;}

    public void setStatus(String status) { this.status = status;}

    public String getMsg() { return msg;}

    public void setMsg(String msg) { this.msg = msg;}

    public String getEmail() { return email;}

    public void setEmail(String email) { this.email = email;}

    public String getPhone() { return phone;}

    public void setPhone(String phone) { this.phone = phone;}



}
