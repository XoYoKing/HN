package com.example.admin.hn.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hjy on 2016/11/5.
 */
public class ScreenTypeInfo implements Serializable {

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ScreenTypeInfo(String name) {
        this.name = name;
    }

    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public ScreenTypeInfo(String name, List<String> data) {
        this.name = name;
        this.data = data;
    }
}
