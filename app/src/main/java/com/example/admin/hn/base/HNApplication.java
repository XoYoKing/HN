package com.example.admin.hn.base;

import android.app.Application;
import android.content.Context;

import com.example.admin.hn.R;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by WIN10 on 2018/3/27.
 */

public class HNApplication extends Application {
    public static HNApplication mApp;
    public Context context;
    public Session session;
    public Session test_session;
    public static HashMap<String, Long> map;


    @Override
    public void onCreate() {
        super.onCreate();
        //初始化本地数据存储
        mApp = this;
        context = getApplicationContext();
        initOkHttp();
        initGallery();//初始化Gallery
        this.session = new SharedPreferencesSession(this);
        this.test_session = new SharedPreferencesSession(this,"configs");
    }
    public static String getAPPName() {
        return mApp.getApplicationContext().getResources().getString(R.string.app_name);
    }
    private void initOkHttp() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
//                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

    private void initGallery() {
//        ImagePicker imagePicker = ImagePicker.getInstance();
//        imagePicker.setImageLoader(new GlideLoader());   //设置图片加载器
//        imagePicker.setShowCamera(true);  //显示拍照按钮
//        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
//        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
//        imagePicker.setSelectLimit(1);    //选中数量限制
//        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
//        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
//        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
//        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
//        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }

    //退出登录
    public void logout(){
        //只清除生产环境的密碼,保留测试环境密碼
        this.session.putString("switche", "2");
        this.session.putString("password", "");
        this.session.putBoolean("isTest", false);

        setEmail("");
        setUserId("");
        setPhone("");
        setCompanyId("");
    }

    public void setUserName(String userName) {
        if (isTestAmbient()) {
            this.test_session.putString("username", userName);
        }else {
            this.session.putString("username", userName);
        }
    }

    public void setPassWord(String password) {
        if (isTestAmbient()) {
            this.test_session.putString("password", password);
        }else {
            this.session.putString("password", password);
        }
    }

    public void setPhone(String phone) {
        if (isTestAmbient()) {
            this.test_session.putString("phone", phone);
        }else {
            this.session.putString("phone", phone);
        }
    }

    public void setSwitche(String switche) {
        if (isTestAmbient()) {
            this.test_session.putString("switche", switche);
        }else {
            this.session.putString("switche", switche);
        }
    }

    public String getUserName() {
        if (isTestAmbient()) {
            return this.test_session.getString("username","");
        }
        return this.session.getString("username","");
    }


    public void setEmail(String email) {
        if (isTestAmbient()) {
            this.test_session.putString("email", email);
        }else {
            this.session.putString("email", email);
        }
    }

    public void setCompanyId(String companyId) {
        if (isTestAmbient()) {
            this.test_session.putString("companyId", companyId);
        }else {
            this.session.putString("companyId", companyId);
        }
    }

    public String getCompanyId() {
        if (isTestAmbient()) {
            return this.test_session.getString("companyId",null);
        }
        return this.session.getString("companyId",null);
    }


    public String getEmail() {
        if (isTestAmbient()) {
            return this.test_session.getString("email","");
        }
        return this.session.getString("email","");
    }

    public void setUserId(String userId) {
        if (isTestAmbient()) {
            this.test_session.putString("userId", userId);
        }else {
            this.session.putString("userId", userId);
        }
    }

    public String getUserId() {
        if (isTestAmbient()) {
            return this.test_session.getString("userId",null);
        }
        return this.session.getString("userId",null);
    }


    public String getPassWord() {
        if (isTestAmbient()) {
            return this.test_session.getString("password","");
        }
        return this.session.getString("password","");

    }

    public String getSwitche() {
        if (isTestAmbient()) {
            return this.test_session.getString("switche","");
        }
        return this.session.getString("switche","");
    }

    public String getPhone() {
        if (isTestAmbient()) {
            return this.test_session.getString("phone","");
        }
        return this.session.getString("phone","");
    }

    //设置用户选择的环境  true=测试环境 false=生产环境
    public void setTestAmbient(boolean isTest) {
        this.session.putBoolean("isTest", isTest);
    }

    public boolean isTestAmbient() {
        return this.session.getBoolean("isTest", false);
    }

    public String getShipName() {
        if (isTestAmbient()) {
            return this.test_session.getString("shipName","");
        }
        return this.session.getString("shipName","");
    }

    public void setShipName(String shipName) {
        if (isTestAmbient()) {
            this.test_session.putString("shipName", shipName);
        }else {
            this.session.putString("shipName", shipName);
        }
    }

    /**
     * 获取消息数量
     * @return
     */
    public int getMsgNumber() {
        if (isTestAmbient()) {
            return this.test_session.getInt("msgNumber",0);
        }
        return this.session.getInt("msgNumber",0);
    }

    /**
     * 设置消息shul
     * @param msgNumber
     */
    public void setMsgNumber(int msgNumber) {
        if (isTestAmbient()) {
            this.test_session.putInt("msgNumber", msgNumber);
        }else {
            this.session.putInt("msgNumber", msgNumber);
        }
    }
}
