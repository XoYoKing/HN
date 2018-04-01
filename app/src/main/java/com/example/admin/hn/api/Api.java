package com.example.admin.hn.api;

/**
 * Created by duantao
 *
 * @date on 2017/9/11 14:40
 */
public class Api {

//    public final static String BASE_URL = "http://zxs.tunnel.qydev.com/menusystem/";
    public  static  String BASE_URL = "http://222.66.158.231:9000/";

//    public  static  String BASE_URL = "http://58.67.133.129:8080/";
    //注册
    public static final String REGISTER = "menusystem/mobile/registered.action";

    //登录
    public static final String LOGIN = "menusystem/mobile/login.action";

    //订单
    public static final String ORDER = "menusystem/orderinterfsce/getorder.action";

    //库存
    public static final String INVENTORY = "menusystem/orderinterfsce/getstock.action";

    //海图
    public static final String CHART = "menusystem/orderinterfsce/getchart.action";

    //修改密码
    public static final String CHANGEPASSWORD = "menusystem/mobile/updatepassword.action";

    //修改邮箱
    public static final String CHANGEPHONE = "menusystem/mobile/updatephonenumber.action";

    //忘记密码
    public static final String FINDPASSWORD = "menusystem/mobile/forgetpassword.action";

    //注册协议
    public static final String AGREEMENT = "menusystem/HTML/agreement.html";

    //消息通知
    public static final String MESSAGE = "menusystem/mobile/notice.action ";

    //邮箱验证
    public static final String EMAIL = "menusystem/mobile/getVerification.action";

    //消息通知消息刪除
    public static final String MESSAGEDEL = "menusystem/mobile/deletepush.action";

    //订单明细
    public static final String ORDERDETAIL = "menusystem/mobilenew/getOrderDetail.action";

    //船舶查询
    public static final String SHIPINQUIRY = "menusystem/mobile/shipinquiry.action";

    //船舶选择
    public static final String SHIPSELECTION = "menusystem/mobile/shipselection.action";

    //海图更新
    public static final String SHIPPACKAGE = "menusystem/mobile/shippackage.action";

    //app更新
    public static final String UPDATE = "menusystem/mobile/getVersion.action";

    //公司名称
    public static final String COMPANYNAME = "menusystem/mobile/getCompany.action";

    //极光id
    public static final String APPID = "menusystem/mobile/updateAppid.action";

}
