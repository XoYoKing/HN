package com.example.admin.hn.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by WIN10 on 2018/4/16.
 * 提交已选列表数据
 */

public class SubmitSelectInfo implements Serializable{
    public String userId;//用户ID
    public String shipId;//船舶ID
    public List<?> docs;//数据集合

    public SubmitSelectInfo(String userId,String shipId, List<?> list) {
        this.userId = userId;
        this.shipId = shipId;
        this.docs = list;
    }
}
