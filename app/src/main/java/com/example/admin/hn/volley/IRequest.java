package com.example.admin.hn.volley;

import android.content.Context;

import com.example.admin.hn.base.HNApplication;
import com.example.admin.hn.utils.AbDateUtil;
import com.orhanobut.logger.Logger;

import java.util.Map;


/**
 * 网络请求
 */
public class IRequest {

    private final Context context;

    public IRequest(Context context) {
        this.context = context;
    }

//    ====================================推荐使用start================================================

    private static String toString(Map params) {
        try {
            StringBuffer bf = new StringBuffer();
            //过滤一些默认参数
            for (Object key : params.keySet()) {
                if ("platform".equals(key)) {
                    continue;
                } else if ("time".equals(key)) {
                    continue;
                } else if ("ct_name".equals(key)) {
                    continue;
                } else if ("appVersion".equals(key)) {
                    continue;
                }
                bf.append(key).append("=").append(params.get(key)).append("，");
            }
            return "{ " + bf.toString().substring(0, bf.length() - 1) + " }";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     *
     * @param params
     */
    private static void setParams(Map params) {
//        params.put("timestamp", AbDateUtil.getCurrentDate(AbDateUtil.dateFormatYMDHMS));
        params.put("userid", HNApplication.mApp.getUserId());//船舶用户ID 默认传递
        params.put("memberId", "1");//商城用户ID 默认传递
        Logger.i("请求参数", params.toString());
    }

    /**
     * 返回String post
     *
     * @param url
     * @param params
     * @param l
     */
    public void post(String url, Map params, RequestListener l) {
        if (params != null) {
            setParams(params);
        }
        RequestManager.post(url, context, params, l);
    }

    /**
     * 返回String 带进度条 post
     *
     * @param url
     * @param params
     * @param progressTitle
     * @param l
     */
    public void post(String url, Map params,
                     String progressTitle, RequestListener l) {
        if (params != null) {
            setParams(params);
        }
        RequestManager.post(url, context, params, progressTitle, l);
    }


    /**
     *  返回String 不带进度条
     * （参数以json形式传递）
     *
     * @param url
     * @param params
     * @param l
     */
    public void postJson(String url, Map params, RequestListener l) {
        if (params != null) {
            setParams(params);
        }
        Logger.i("url", url);
        RequestManager.postJson(url, context, params, l);
    }

    /**
     *  返回String 带进度条
     * （参数以json形式传递）
     *
     * @param url
     * @param params
     * @param l
     */
    public void postJson(String url, Map params, String progressTitle,
                         RequestListener l) {
        if (params != null) {
            setParams(params);
        }
        RequestManager.postJson(url, context, params, progressTitle, l);
    }

    /**
     *  返回String 带进度条
     * （参数以json形式传递）
     *
     * @param url
     * @param params
     * @param l
     */
    public void get(String url, Map params, String progressTitle,
                         RequestListener l) {
        if (params != null) {
            setParams(params);
        }
        RequestManager.get(url, context, params, progressTitle, l);
    }

    /**
     *  返回String 带进度条
     * （参数以Java对象形式传递）
     * @param url
     * @param params
     * @param l
     */
    public void postJson(String url, Object params, String progressTitle,
                         RequestListener l) {
        RequestManager.postJson(url, context, params, progressTitle, l);
    }

    /**
     *  返回String 不带进度条
     * （参数以Form表单形式传递）
     *
     * @param url
     * @param params
     * @param l
     */
    public void postFrom(String url, Map params, RequestListener l) {
        if (params != null) {
            setParams(params);
        }
        RequestManager.postForm(url, context, params, l);
    }

    /**
     * 理财投资 返回String 带进度条
     * （参数以Form表单形式传递）
     *
     * @param url
     * @param params
     * @param l
     */
    public void postFrom(String url, Map params, String progressTitle,
                         RequestListener l) {
        if (params != null) {
            setParams(params);
        }
        RequestManager.postForm(url, context, params, progressTitle, l);
    }

//  =======================================推荐使用end=============================================

}
