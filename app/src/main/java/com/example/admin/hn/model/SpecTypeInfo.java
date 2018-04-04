package com.example.admin.hn.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hjy on 2016/11/5.
 * 规格参数
 */
public class SpecTypeInfo implements Serializable {

    private String title;//标题
    private List<SpecInfo> data;//数据集合

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SpecInfo> getData() {
        return data;
    }

    public void setData(List<SpecInfo> data) {
        this.data = data;
    }

}
