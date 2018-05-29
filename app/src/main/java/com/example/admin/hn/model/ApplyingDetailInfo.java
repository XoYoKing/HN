package com.example.admin.hn.model;

import java.io.Serializable;

/**
 * Created by hjy on 2016/11/5.
 * 申请单 申请中 申请详情
 */
public class ApplyingDetailInfo implements Serializable {
    public String publishat;//出版日期
    public String code;//资料号
    public String docid;//资料ID
    public int quantity;//购买数量
    public String categoryname;//资料类别
    public String docname;//资料名称
    public String shipname;//船舶名称
}
