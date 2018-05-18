package com.example.admin.hn.model;

/**
 * 船舶资料管理 订单为未选择状态下提交数据对象
 */
public class SubmitSelect {
    public int buyNum;//配置数量
    public String docId;//资料ID
    public String distributeNo;//资料编号

    public SubmitSelect(int buyNum, String docId, String distributeNo) {
        this.buyNum = buyNum;
        this.docId = docId;
        this.distributeNo = distributeNo;
    }
}
