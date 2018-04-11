package com.example.admin.hn.base;

import android.app.Application;

/**
 * Created by WIN10 on 2018/3/27.
 */

public class HNApplication extends Application {
    public static HNApplication mApp;
    public Session session;
    public Session test_session;
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化本地数据存储
        mApp = this;
        this.session = new SharedPreferencesSession(this);
        this.test_session = new SharedPreferencesSession(this,"configs");
    }

    //退出登录
    public void logout(){
        //只清除生产环境的数据,保留测试环境数据
        this.session.putString("switche", "2");
        this.session.putString("password", "");
        this.session.putBoolean("isTest", false);
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

    //设置用户选择的环境  true=测试环境 false=生产环境
    public void setTestAmbient(boolean isTest) {
        this.session.putBoolean("isTest", isTest);
    }

    public boolean isTestAmbient() {
        return this.session.getBoolean("isTest", false);
    }


}
