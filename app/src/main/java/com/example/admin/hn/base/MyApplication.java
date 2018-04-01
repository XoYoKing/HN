package com.example.admin.hn.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import com.example.admin.hn.MainActivity;
import com.example.admin.hn.http.OkHttpUtil;
import com.example.admin.hn.utils.CrashHandler;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import java.net.CookieManager;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;


/**
 * @author duantao
 * @date on 2017/7/26 16:06
 * @describe
 */
public class MyApplication extends Application {

    private static Context context;
    // 用于存放倒计时时间
    public static Map<String, Long> map;
    public Session session;//生产环境
    public static MyApplication mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        //去除弹窗
        mApp = this;
        Thread.setDefaultUncaughtExceptionHandler(new MyUnCaughtExceptionHandler());
        context = getApplicationContext();
        //初始化本地数据存储
        this.session = new SharedPreferencesSession(this);
        //截取异常
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
        //cookie策略 让OkHttp接受所有的cookie
        CookieManager cookieManager = new CookieManager();
        OkHttpUtil.setCookie(cookieManager);
        //统一日志管理
        Logger.init()               // default tag : PRETTYLOGGER or use just init()
                .setMethodCount(3)            // default 2
                .hideThreadInfo()             // default it is shown
                .setLogLevel(LogLevel.NONE);  // default : LogLevel.FULL
        //极光推送
//        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
//        JPushInterface.init(this);            // 初始化 JPush
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Context getContext() {
        return context;
    }

    //不要在UnCaughtExceptionHandler中新开一个线程，会抛出异常
    class MyUnCaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            ex.printStackTrace();
            // do some work here
            Intent intent = new Intent(MyApplication.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MyApplication.this.startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    public void setUserName(String userName) {
        this.session.putString("username", userName);
    }

    public void setPassWord(String password) {
        this.session.putString("password", password);
    }

    public void setSwitche(String switche) {
        this.session.putString("switche", switche);

    }

    public String getUserName() {
        return this.session.getString("username",null);

    }

    public String getPassWord() {
        return this.session.getString("password",null);

    }

    public String getSwitche() {
        return this.session.getString("switche","1");
    }
}
