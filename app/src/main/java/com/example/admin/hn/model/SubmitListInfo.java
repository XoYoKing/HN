package com.example.admin.hn.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by WIN10 on 2018/4/16.
 * 提交待选列表数据
 */

public class SubmitListInfo implements Serializable{
    public String userId;//用户ID
    public List<?> documents;//数据集合

    public SubmitListInfo(String userId, List<?> list) {
        this.userId = userId;
        this.documents = list;
    }
}
