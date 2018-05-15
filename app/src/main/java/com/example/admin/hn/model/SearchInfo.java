package com.example.admin.hn.model;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by hjy on 2016/11/5.
 */
public class SearchInfo extends DataSupport implements Serializable {

    private long id;
    private String name;
    private long date;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
