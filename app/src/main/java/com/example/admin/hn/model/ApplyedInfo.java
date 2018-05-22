package com.example.admin.hn.model;

import java.io.Serializable;

/**
 * Created by hjy on 2016/11/5.
 * 申请单 已完成
 */
public class ApplyedInfo implements Serializable {
    public String totalamount;//领用总数
    public String receiveno;//领用编号
    public double money;//折扣后总金额
    public String receivedate;//领用日期
    public String operator;//经办人
    public double tax;//税额
    public String is_receipt;//回执情况
    public String remark;//说明（待定）

}
