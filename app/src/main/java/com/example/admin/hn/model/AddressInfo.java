package com.example.admin.hn.model;

import java.io.Serializable;

/**
 * Created by hjy on 2016/11/5.
 * 地址对象
 */
public class AddressInfo implements Serializable {
//    "id": 1,
//            "createAt": 1524470356000,
//            "updateAt": 1524470378000,
//            "createBy": "admin",
//            "updateBy": "admin",
//            "isDelete": 0,
//            "version": 1,
//            "memberId": 1,
//            "receiverName": "hbj",
//            "receiverAddr": "创新大厦",
//            "areaId": 33,
//            "areaName": "天津 天津市 和平区",
//            "phone": "15632374273",
//            "isDefaul": 1

    public String id;//地址ID
    public String memberId;//用户ID
    public String receiverName;//收货人
    public String receiverAddr;//收货地址
    public String areaId;//地区ID
    public String areaName;//区域全称
    public String phone;//联系人电话
    public int isDefaul;//是否默认地址
    public boolean isSelect;//在选择地址时判断是否选中状态

    @Override
    public String toString() {
        return "AddressInfo{" +
                "id='" + id + '\'' +
                ", memberId='" + memberId + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", receiverAddr='" + receiverAddr + '\'' +
                ", areaId='" + areaId + '\'' +
                ", areaName='" + areaName + '\'' +
                ", phone='" + phone + '\'' +
                ", isDefaul=" + isDefaul +
                '}';
    }
}
