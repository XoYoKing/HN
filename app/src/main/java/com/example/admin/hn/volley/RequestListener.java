package com.example.admin.hn.volley;

/**
 * Created by Administrator on 2015/3/11.
 */
public interface RequestListener {

    /**
     * 成功
     */
    void requestSuccess(String json);

    /**
     * 错误
     */
    void requestError(String message);

}
