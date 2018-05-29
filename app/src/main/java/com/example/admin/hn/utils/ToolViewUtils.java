package com.example.admin.hn.utils;

import android.text.Selection;
import android.text.Spannable;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.HNApplication;
import com.orhanobut.logger.Logger;


/**
 * Created by WIN10 on 2018/3/30.
 * 圖片工具类
 */

public class ToolViewUtils {

     /**
      *
      * 商城加载图片
     * 在listView中获取图片
     * 使用glide 加载图片
     *
     * @param imgUrl
     * @param imageView
     * @param resId
     */
    public static void glideImageList(String imgUrl, final ImageView imageView, final int resId) {
        String url;
        if (imgUrl!=null && (imgUrl.startsWith("http") || imgUrl.startsWith("https"))) {
            url = imgUrl;
        }else {
            url= Api.SHOP_BASE_URL.substring(0, Api.SHOP_BASE_URL.length() - 1)+imgUrl;
        }
        Glide.with(HNApplication.mApp)
                .load(url)
                .error(resId)//加载失败显示的图片
                .into(imageView);
    }

    /**
     *
     * 使用glide 加载图片
     * 船舶加载图片
     * @param imgUrl
     * @param imageView
     * @param resId
     */
    public static void glideImage(String imgUrl, final ImageView imageView, final int resId) {
        String url;
        if (imgUrl!=null && (imgUrl.startsWith("http") || imgUrl.startsWith("https"))) {
            url = imgUrl;
        }else {
            url= Api.BASE_URL.substring(0, Api.BASE_URL.length() - 1)+imgUrl;
        }
        Glide.with(HNApplication.mApp)
                .load(url)
                .error(resId)//加载失败显示的图片
                .into(imageView);
    }

    /**
     * 将光标设置到文本后面
     * @param editText
     */
    public static void setSelection(EditText editText){
        editText.postInvalidate();
        // 切换后将EditText光标置于末尾
        CharSequence charSequence = editText.getText();
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
    }
}
