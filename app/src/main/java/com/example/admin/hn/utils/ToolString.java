package com.example.admin.hn.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * * 字符串工具类
 *@author duantao
 *created at 16/4/13 下午2:44
 */
public class ToolString {

    /**
     * 获取UUID
     *
     * @return 32UUID小写字符串
     */
    public static String gainUUID() {
        String strUUID = UUID.randomUUID().toString();
        strUUID = strUUID.replaceAll("-", "").toLowerCase();
        return strUUID;
    }


    /**
     * 判断字符串是否非空非null
     *
     * @param strParm 需要判断的字符串
     * @return 真假
     */
    public static boolean isNoBlankAndNoNull(String strParm) {
        return !((strParm == null) || (strParm.equals("")));
    }
    /**
     * 判断字符串是否非空非null
     *
     * @param strParm 需要判断的字符串
     * @return 真假
     */
    public static boolean isEmpty(String strParm) {
        return !((strParm == null) || (strParm.equals("")));
    }

    /**
     * 判断集合是否非空非null
     *
     * @param list 需要判断的集合
     * @return 真假
     */
    public static boolean isEmptyList(List list) {
        return !((list == null) || (list.size()==0));
    }


    /**
     * 将流转成字符串
     *
     * @param is 输入流
     * @throws Exception
     */
    public static String convertStreamToString(InputStream is)
            throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }


    /**
     * 将文件转成字符串
     *
     * @param file 文件
     * @throws Exception
     */
    public static String getStringFromFile(File file) throws Exception {
        FileInputStream fin = new FileInputStream(file);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
    }


    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /*-----------------------------------

    笨方法：String s = "你要去除的字符串";

            1.去除空格：s = s.replace('\\s','');

            2.去除回车：s = s.replace('\n','');

    这样也可以把空格和回车去掉，其他也可以照这样做。

    注：\n 回车(\u000a)
    \t 水平制表符(\u0009)
    \s 空格(\u0008)
    \r 换行(\u000d)*/

    /**
     * @param number
     * @return
     * @Title : filterNumber
     * @Type : FilterStr
     * @Description : 过滤出数字
     */
    public static String filterNumber(String number) {
        if (number != null)
            number = number.replaceAll("[^(0-9)]", "");
        return number;
    }

    // 校验Tag Alias 只能是数字,英文字母和中文
    public static boolean isValidTagAndAlias(String s) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_!@#$&*+=.|]+$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    /**
     * 网络是否连接
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    /**
     * 拼接收货人详细地址
     */
    public static String getAddress(String province, String city, String district, String address) {
        return province + " " + city + " " + district + " " + address;
    }

    /**
     * 拼接收货人省市区地址
     */
    public static String getAddress(String province, String city, String district) {
        return province + " " + city + " " + district + " ";
    }
}
