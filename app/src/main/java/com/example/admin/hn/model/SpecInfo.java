package com.example.admin.hn.model;

import java.io.Serializable;

/**
 * Created by hjy on 2016/11/5.
 */
public class SpecInfo implements Serializable {

    private String item_id;
    private String spec_name;//标题名称
    private String item;//内容
    private String src;//图片地址
    private int check_id;//被选中的

    public int getCheck_id() {
        return check_id;
    }

    public void setCheck_id(int check_id) {
        this.check_id = check_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getSpec_name() {
        return spec_name;
    }

    public void setSpec_name(String spec_name) {
        this.spec_name = spec_name;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    @Override
    public String toString() {
        return "SpecInfo{" +
                "item_id='" + item_id + '\'' +
                ", spec_name='" + spec_name + '\'' +
                ", item='" + item + '\'' +
                ", src='" + src + '\'' +
                ", check_id=" + check_id +
                '}';
    }
}
