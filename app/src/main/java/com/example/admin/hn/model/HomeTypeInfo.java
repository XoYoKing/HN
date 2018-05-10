package com.example.admin.hn.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hjy on 2016/11/5.
 * 分类
 */
public class HomeTypeInfo implements Serializable {

    public SitMenu sitMenu;//一级分类
    public List<Children> children;//二级分类

    public class SitMenu implements Serializable {
        //一级分类
        public String id;// "id": 1,
        public String menuNames;//  "menuNames": "服饰鞋帽",
        public String menuImage;//   "menuImage": "1111",
        public String parentMenuId;// "parentMenuId":1,
        public String menuData;// json字符串(list集合) 三级分类

        @Override
        public String toString() {
            return "SitMenu{" +
                    "id='" + id + '\'' +
                    ", menuNames='" + menuNames + '\'' +
                    ", menuImage='" + menuImage + '\'' +
                    ", parentMenuId='" + parentMenuId + '\'' +
                    ", menuData='" + menuData + '\'' +
                    '}';
        }
    }

    public class Children implements Serializable {
        //二级分类
        public SitMenu sitMenu;
    }


}
