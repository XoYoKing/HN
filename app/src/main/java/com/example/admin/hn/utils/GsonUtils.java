package com.example.admin.hn.utils;

import com.example.admin.hn.http.Constant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Gson解析、转化工具
 * Created by Jaycee on 16/1/20.
 * E-mail：jayceeok@foxmail.com
 */
public class GsonUtils {
    private static final String TAG ="GsonUtils" ;

    /**
     * json字符串解析为对象
     *
     * @param jsonResult
     * @param clz
     * @param <T>
     *
     * @return
     */
    public static <T> T jsonToBean(String jsonResult, Class<T> clz) {
        Gson gson = new Gson();
        T t = gson.fromJson(jsonResult, clz);
        return t;
    }

    /**
     * json字符串解析为对象
     *
     * @param jsonResult
     * @param clz
     * @param <T>
     *
     * @return
     */
    public static <T> T jsonToBean2(String jsonResult, Class<T> clz) {
        try {
            Gson gson = new Gson();
            JSONObject object = new JSONObject(jsonResult);
            JSONObject data = object.getJSONObject("data");
            T t = gson.fromJson(data.toString(), clz);
            return t;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json字符串解析为list集合
     *
     * @param jsonResult
     * @param typeToken
     *
     * @return
     */
    public static List<?> jsonToList(String jsonResult,TypeToken typeToken) {
        List<?> list = null;
        try {
            Gson gson = new Gson();
            JSONObject object = new JSONObject(jsonResult);
            JSONArray jsonArray = object.getJSONArray("Documents");
            list = gson.fromJson(jsonArray.toString(), typeToken.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * json字符串解析为list集合
     *
     * @param jsonResult
     * @param typeToken
     *
     * @return
     */
    public static List<?> jsonToList(String jsonResult,TypeToken typeToken,String key) {
        List<?> list = null;
        try {
            Gson gson = new Gson();
            JSONObject object = new JSONObject(jsonResult);
            JSONArray jsonArray = object.getJSONArray(key);
            list = gson.fromJson(jsonArray.toString(), typeToken.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * map数据组装成json字符串
     *
     * @param map
     * @param <T>
     *
     * @return
     */
    public static <T> String mapToJson(Map<String, T> map) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(map);
        return jsonStr;
    }

    /**
     * 将java对象转换成json字符串
     *
     * @param bean
     *
     * @return
     */
    public static String beanToJson(Object bean) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(bean);
        return jsonStr.toString();
    }


    public static String transMapToString(Map map) {
        Map.Entry entry;
        StringBuffer sb = new StringBuffer();
        for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
            entry = (Map.Entry) iterator.next();
            sb.append(entry.getKey().toString()).append("=").append(
                    null == entry.getValue() ? "" : entry.getValue().toString()
            ).append(iterator.hasNext() ? "&" : "");
        }
        return sb.toString();
    }

    /**
     * 转加密后的字符串给服务器解析,否则服务器那边request接收的值所有的“+”变成空格
     *
     * @param paramString
     *
     * @return
     */
    public static String toURLEncoded(String paramString) {
        if (paramString == null || paramString.equals("")) {
            Logger.i(TAG, "toURLEncoded error:" + paramString);
            return "";
        }
        try {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLEncoder.encode(str, "UTF-8");
            return str;
        } catch (Exception localException) {
        }
        return "";
    }

    /**
     * 判断网络请求结果
     *
     * @return 成功=true  失敗=false
     */
    public static boolean isSuccess(String json) {
        try {
            //船舶解析
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.optString("status");
            return "success".equals(status);
        } catch (Exception e) {

        }
        return false;
    }
    /**
     * 判断网络请求结果
     *
     * @return 成功=true  失敗=false
     */
    public static boolean isShopSuccess(String json) {
        try {
            //商城解析
            JSONObject jsonObject = new JSONObject(json);
            return jsonObject.optBoolean("success");
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return false;
    }


    /**
     * 获取网络请求的错误信息
     *
     * @return
     */
    public static String getError(String json) {
        String message = Constant.DATA_PARSING_EXCEPTION;
        try {
            JSONObject jsonObject = new JSONObject(json);
            message = jsonObject.optString("message");
            return message;
        } catch (Exception e) {
        }
        return message;
    }


}
