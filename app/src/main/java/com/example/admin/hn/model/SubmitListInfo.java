package com.example.admin.hn.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by WIN10 on 2018/4/16.
 * 提交列表数据
 */

public class SubmitListInfo implements Serializable{
    public String Userid;//用户ID
    public List<?> Documents;//数据集合

    public SubmitListInfo(String userId, List<?> list) {
        this.Userid = userId;
        this.Documents = list;
    }
}
