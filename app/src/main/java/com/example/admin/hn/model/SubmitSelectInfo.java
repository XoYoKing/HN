package com.example.admin.hn.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by WIN10 on 2018/4/16.
 * 提交已选列表数据
 */

public class SubmitSelectInfo implements Serializable{
    public String userid;//用户ID
    public String shipid;//船舶ID
    public List<?> docs;//数据集合

    public SubmitSelectInfo(String userId,String shipid, List<?> list) {
        this.userid = userId;
        this.shipid = shipid;
        this.docs = list;
    }
}
