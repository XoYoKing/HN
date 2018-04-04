package com.example.admin.hn.utils;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;

import com.example.admin.hn.base.HNApplication;
import com.example.admin.hn.base.MyApplication;

/**
 * Created by WIN10 on 2018/3/30.
 */

public class ToolAppUtils {
    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public static void backgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return yinsujun 2015-8-20 下午5:34:18
     */
    public static int getWindowWith(Context context) {
        WindowManager windowManager = (WindowManager) HNApplication.mApp.getSystemService(Context.WINDOW_SERVICE);
        @SuppressWarnings("deprecation")
        int width = windowManager.getDefaultDisplay().getWidth();
        return width;
    }
}
