package com.example.admin.hn.mViewPager;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.example.admin.hn.R;
import com.example.admin.hn.utils.ToolViewUtils;

/**
 * ImageView创建工厂
 */
public class ViewFactory {

    /**
     * 获取ImageView视图的同时加载显示url
     *
     * @return
     */
    public static ImageView getImageView(Context context, String url) {
        ImageView imageView = (ImageView) LayoutInflater.from(context).inflate(
                R.layout.view_banner, null);
        ToolViewUtils.glideImageList(url, imageView, R.drawable.delete);
        return imageView;
    }
}
