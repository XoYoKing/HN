package com.example.admin.hn.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.admin.hn.R;
import com.example.admin.hn.base.HNApplication;

/**
 * Created by WIN10 on 2018/3/30.
 * 圖片工具类
 */

public class ToolViewUtils {

     /**
     * 在listView中获取图片
     * 使用glide 加载图片
     *
     * @param imgUrl
     * @param imageView
     * @param resId
     */
    public static void glideImageList(String imgUrl, final ImageView imageView, final int resId) {
        Glide.with(HNApplication.mApp)
                .load(imgUrl)
                .error(resId)//加载失败显示的图片
                .into(imageView);
    }
}
