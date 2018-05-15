package com.example.admin.hn.model;

import java.io.Serializable;

/**
 * Created by WIN10 on 2018/5/15.
 */

public class ArticleInfo implements Serializable {
    /**
     * "pubTitle": "fjsk",
     * "pubDesc": "fsdf",
     * "pubLabel": null,
     * "pubType": 1,
     * "originalUrl": "/resource/fs/group1/M00/00/01/ChwCW1rzt4qAUkDBAABWAPy7sBk475.xls",
     * "originalName": "新建 Microsoft Excel 工作表.xls",
     * "newUrl": null,
     * "status": null,
     * "reason": null,
     * "collectCount": null,
     * "reviewCount": null
     */

    public String pubTitle;
    public String pubDesc;
    public String pubLabel;
    public String originalUrl;
    public String originalName;
    public String newUrl;
    public String status;
    public String reason;
    public int pubType;
    public int collectCount;
    public int reviewCount;
}
