package com.example.admin.hn.http;

import com.example.admin.hn.utils.ToolString;
import com.orhanobut.logger.Logger;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
//正如前面代码看到的，在创建一个请求时，需要添加一个错误监听onErrorResponse。如果请求发生异常，会返回一个VolleyError实例。  
//以下是Volley的异常列表：  
//AuthFailureError：如果在做一个HTTP的身份验证，可能会发生这个错误。  
//NetworkError：Socket关闭，服务器宕机，DNS错误都会产生这个错误。  
//NoConnectionError：和NetworkError类似，这个是客户端没有网络连接。  
//ParseError：在使用JsonObjectRequest或JsonArrayRequest时，如果接收到的JSON是畸形，会产生异常。  
//SERVERERROR：服务器的响应的一个错误，最有可能的4xx或5xx HTTP状态代码。  
//TimeoutError：Socket超时，服务器太忙或网络延迟会产生这个异常。默认情况下，Volley的超时时间为2.5秒。如果得到这个错误可以使用RetryPolicy。  


public class ErrorHelper {

    /**
     * Returns appropriate message which is to be displayed to the user against
     * the specified error object.
     *
     * @param error
     * @return
     */
    public static String getMessage(Exception error) {
        //获取错误信息里面的状态码
        Logger.e("error", error);
        String code = ToolString.filterNumber(error.getMessage());
        if (!ToolString.isEmpty(code)) {
            //如果没有获取到状态码，就判断错误类型
            if (error instanceof SocketTimeoutException) {
                return Constant.SOCKET_TIMEOUT_EXCEPTION;
            } else if (error instanceof UnknownHostException) {
                return Constant.CONNECT_EXCEPTION;
            }
        } else {
            int responseCode = 0;
            try {
                responseCode = Integer.parseInt(code);
            } catch (Exception e) {

            }
            return handleServerError(responseCode);
        }
        return Constant.SOCKET_EXCEPTION;
    }


    /**
     * Handles the server error, tries to determine whether to show a stock
     * message or to show a message retrieved from the server.
     *
     * @param responseCode 请求的状态码
     * @return
     */
    private static String handleServerError(int responseCode) {
        switch (responseCode) {
            case 403:
                return Constant.FORBIDDEN_EXCEPTION;
            case 404:
                return Constant.NOT_FOUND_EXCEPTION;
            case 502:
                return Constant.SERVICE_UNAVAILABLE;
            case 503:
                return Constant.SERVICE_UNAVAILABLE;
            default:
                return Constant.SOCKET_EXCEPTION;
        }
    }
}