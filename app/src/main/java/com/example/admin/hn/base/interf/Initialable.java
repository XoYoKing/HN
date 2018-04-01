package com.example.admin.hn.base.interf;

/**
 * Created by Jaycee on 15/11/30.
 * E-mail：jayceeok@foxmail.com
 */
public interface Initialable {
    //这个界面设置可视化控件
    void initView();

    //这个方法里面初始化数据
    void initData();

    //初始化顶部标题栏
    void initTitleBar();

}
