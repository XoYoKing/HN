package com.example.admin.hn.model;

import java.io.Serializable;

/**
 * Created by hjy on 2016/11/5.
 * 申请单 申请中
 */
public class ApplyingInfo implements Serializable {

    public String applydate;//申请日期
    public String applyno;//申请编号
    public int row_num;//
    public String status;//订单状态
    public String shipname;//船舶名称
    public int amount;//申请总数

}
