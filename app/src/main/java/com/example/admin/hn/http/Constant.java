package com.example.admin.hn.http;

/**
 * Created by geek on 2016/6/21.
 * 全局属性
 */
public class Constant {
    /**
     * The  CONNECTEXCEPTION.
     */
    public static final String CONNECT_EXCEPTION = "网络连接失败，请稍后再试";

    /**
     * The  SOCKETEXCEPTION.
     */
    public static final String SOCKET_EXCEPTION = "网络异常，请稍后再试";

    /**
     * The  SOCKETTIMEOUTEXCEPTION.
     */
    public static final String SOCKET_TIMEOUT_EXCEPTION = "网络连接超时，请稍后再试";

    /**
     * 资源未找到.
     */
    public static final String NOT_FOUND_EXCEPTION = "请求资源无效404";

    /**
     * 没有权限访问资源.
     */
    public static final String FORBIDDEN_EXCEPTION = "没有权限访问资源";

    /**
     * The  REMOTESERVICEEXCEPTION.
     */
    public static final String SERVICE_UNAVAILABLE = "服务器正在维护，请稍后再试";

    /**
     * The  UNKNOWNHOSTEXCEPTION.
     */
    public static final String UNKNOWN_HOST_EXCEPTION = "连接服务器失败，请稍后再试";

    /**
     * The  ANALYSIS_DATA_EXCEPTION.
     */
    public static final String ANALYSIS_DATA_EXCEPTION = "服务器响应异常，请稍后再试";

    /**
     * The  DATA_EXCEPTION.
     */
    public static final String DATA_EXCEPTION = "服务器响应异常，请稍后再试";
    /**
     * 其他异常.
     */
    public static final String UNTREATED_EXCEPTION = "未处理的异常";


    /**
     * 已加载全部
     */
    public static final String LOADED = "已加载全部";
    /**
     * 正在加载
     */
    public static final String LOADING = "正在加载...";

    //船舶物料管理 待选搜索
    public static final int POP_NOT_MATERIAL = 800;
    //船舶物料管理 审核申领搜索
    public static final int POP_SHIP_AUDITING = 900;
    //电子海图订单管理 搜索
    public static final int POP_ORDER_MANAGER = 400;
    //电子海图库存管理 搜索
    public static final int POP_ORDER_INVENTORY = 500;

}