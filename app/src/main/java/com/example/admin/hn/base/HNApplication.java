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

//    public void setUserName(String userName) {
//        this.session.putString("username", userName);
//    }
//
//    public void setPassWord(String password) {
//        this.session.putString("password", password);
//    }
//
//    public void setSwitche(String switche) {
//        this.session.putString("switche", switche);
//
//    }
//
//    public String getUserName() {
//        return this.session.getString("username","");
//
//    }
//
//    public String getPassWord() {
//        return this.session.getString("password","");
//
//    }
//
//    public String getSwitche() {
//        return this.session.getString("switche","1");
//    }
//
    public void setUserName(String userName,boolean isTest) {
        if (isTest) {
            this.test_session.putString("username", userName);
        }else {
            this.session.putString("username", userName);
        }
    }

    public void setPassWord(String password,boolean isTest) {
        if (isTest) {
            this.test_session.putString("password", password);
        }else {
            this.session.putString("password", password);
        }
    }

    public void setSwitche(String switche,boolean isTest) {
        if (isTest) {
            this.test_session.putString("switche", switche);
        }else {
            this.session.putString("switche", switche);
        }
    }

    public String getUserName(boolean isTest) {
        if (isTest) {
            return this.test_session.getString("username","");
        }
        return this.session.getString("username","");
    }

    public String getPassWord(boolean isTest) {
        if (isTest) {
            return this.test_session.getString("password","");
        }
        return this.session.getString("password","");

    }

    public String getSwitche(boolean isTest) {
        if (isTest) {
            return this.test_session.getString("switche","");
        }
        return this.session.getString("switche","");
    }


}
