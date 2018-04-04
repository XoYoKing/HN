package com.example.admin.hn.model;

import java.io.Serializable;

/**
 * Created by hjy on 2016/11/5.
 * 分类
 */
public class HomeTypeInfo implements Serializable {

    private String name;//标题
    private String imgUrl;//图片地址
    private int id;//分类ID

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "HomeTypeInfo{" +
                "name='" + name + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", id=" + id +
                '}';
    }
}
