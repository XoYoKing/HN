package com.example.admin.hn.model;

import java.io.Serializable;

/**
 * Created by hjy on 2016/11/5.
 */
public class CityInfo implements Serializable {
    //    "areaName": "淮安市",
//            "areaJp": "HAS",
//            "areaQp": "HUAIANSHI",
//            "areaRegion": "华东",
//            "areaRegionId": 3,
//            "areaLevel": null,
//            "areaDeep": 2,
//            "areaParentName": "江苏省",
//            "areaParentId": "10"
    public String id;//城市ID
    public String areaQp;//城市缩写
    public String areaJp;//城市全拼
    public String areaName;//城市名称
    public String areaDeep;//地区深度
    public String areaParentName;//父地区的名称
    public String areaParentId;//父地区的ID
    public String areaRegionId;//所属区域ID
    public String areaRegion;//所属区域名称

    @Override
    public String toString() {
        return "CityInfo{" +
                "id='" + id + '\'' +
                ", areaQp='" + areaQp + '\'' +
                ", areaJp='" + areaJp + '\'' +
                ", areaName='" + areaName + '\'' +
                ", areaDeep='" + areaDeep + '\'' +
                ", areaParentName='" + areaParentName + '\'' +
                ", areaParentId='" + areaParentId + '\'' +
                ", areaRegionId='" + areaRegionId + '\'' +
                ", areaRegion='" + areaRegion + '\'' +
                '}';
    }
}
