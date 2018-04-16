package com.example.admin.hn.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.Toast;

/**
*  @date on 2017/7/26 16:04
*  @author duantao
*  @describe 
*/
public class ToolAlert {
    public static void showToast(Context mContext, String msg, boolean longOrShort) {
        if (msg == null) return;
        Toast toast = Toast.makeText(mContext, msg,
                longOrShort ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        mContext = null;
    }

    public static void showToast(Context mContext, String msg) {
        if (msg == null) return;
        showToast(mContext, msg, false);
    }


    public static void showToast(Context context, int info, boolean longOrShort) {
        Toast.makeText(context, info,
                longOrShort ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
        context = null;
    }


    /**
     * 弹出对话框
     *
     * title 对话框标题
     * msg 对话框内容
     * okBtnListenner 确定按钮点击事件
     * cancelBtnListenner 取消按钮点击事件
     */
    public static AlertDialog dialog(Context context, String msg) {
        return dialog(context, "", msg);
    }


    /**
     * 弹出对话框
     *
     * title 对话框标题
     * msg 对话框内容
     * okBtnListenner 确定按钮点击事件
     * cancelBtnListenner 取消按钮点击事件
     */
    public static AlertDialog dialog(Context context, String title, String msg) {
        return dialog(context, title, msg, null);
    }


    /**
     * 弹出对话框
     *
     * title 对话框标题
     * msg 对话框内容
     * okBtnListenner 确定按钮点击事件
     * cancelBtnListenner 取消按钮点击事件
     */
    public static AlertDialog dialog(Context context, String title, String msg, DialogInterface.OnClickListener okBtnListenner) {
        return dialog(context, title, msg, okBtnListenner, null);
    }


    /**
     * 弹出对话框
     *
     * title 对话框标题
     * msg 对话框内容
     * okBtnListenner 确定按钮点击事件
     * cancelBtnListenner 取消按钮点击事件
     */
    public static AlertDialog dialog(Context context, String title, String msg, DialogInterface.OnClickListener okBtnListenner, DialogInterface.OnClickListener cancelBtnListenner) {
        return dialog(context, null, title, msg, okBtnListenner,
                cancelBtnListenner);
    }


    /**
     * 弹出对话框
     *
     * title 对话框标题
     * msg 对话框内容
     * okBtnListenner 确定按钮点击事件
     * cancelBtnListenner 取消按钮点击事件
     */
    public static AlertDialog dialog(Context context, Drawable icon, String title, String msg) {
        return dialog(context, icon, title, msg, null);
    }


    /**
     * 弹出对话框
     *
     * title 对话框标题
     * msg 对话框内容
     * okBtnListenner 确定按钮点击事件
     * cancelBtnListenner 取消按钮点击事件
     */
    public static AlertDialog dialog(Context context, Drawable icon, String title, String msg, DialogInterface.OnClickListener okBtnListenner) {
        return dialog(context, icon, title, msg, okBtnListenner, null);
    }


    /**
     * 弹出对话框
     *
     * @param icon 标题图标
     * @param title 对话框标题
     * @param msg 对话框内容
     * @param okBtnListenner 确定按钮点击事件
     * @param cancelBtnListenner 取消按钮点击事件
     */
    public static AlertDialog dialog(Context context, Drawable icon, String title, String msg, DialogInterface.OnClickListener okBtnListenner, DialogInterface.OnClickListener cancelBtnListenner) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        if (null != icon) {
            dialogBuilder.setIcon(icon);
        }
        if (ToolString.isNoBlankAndNoNull(title)) {
            dialogBuilder.setTitle(title);
        }
        dialogBuilder.setMessage(msg);
        if (null != okBtnListenner) {
            dialogBuilder.setPositiveButton(android.R.string.ok,
                    okBtnListenner);
        }
        if (null != cancelBtnListenner) {
            dialogBuilder.setNegativeButton(android.R.string.cancel,
                    cancelBtnListenner);
        }
        dialogBuilder.create();
        return dialogBuilder.show();
    }
    //
    ///**
    // * 弹出一个自定义布局对话框
    // *
    // * @param context 上下文
    // * @param view 自定义布局View
    // */
    //public static AlertDialog dialog(Context context, View view) {
    //	final AlertDialog.Builder builder = new AlertDialog.Builder(context);
    //	builder.setView(view);
    //	return builder.show();
    //}
    //
    ///**
    // * 弹出一个自定义布局对话框
    // *
    // * @param context 上下文
    // * @param resId 自定义布局View对应的layout id
    // */
    //public static AlertDialog dialog(Context context, int resId) {
    //	LayoutInflater inflater = LayoutInflater.from(context);
    //	View view = inflater.inflate(resId, null);
    //	AlertDialog.Builder builder = new AlertDialog.Builder(context);
    //	builder.setView(view);
    //	return builder.show();
    //}
    //
    ///**
    // * 弹出较短的Toast消息
    // *
    // * @param msg 消息内容
    // */
    //public static void toastShort(String msg) {
    //	Toast.makeText(MApplication.gainContext(), msg, Toast.LENGTH_SHORT).show();
    //}
    //
    ///**
    // * 弹出较短的Toast消息
    // *
    // * @param msg 消息内容
    // */
    //public static void toastShort(Context context, String msg) {
    //	Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    //}
    //
    ///**
    // * 弹出较长的Toast消息
    // *
    // * @param msg 消息内容
    // */
    //public static void toastLong(String msg) {
    //	Toast.makeText(MApplication.gainContext(), msg, Toast.LENGTH_LONG).show();
    //}
    //
    ///**
    // * 弹出较长的Toast消息
    // *
    // * @param msg 消息内容
    // */
    //public static void toastLong(Context context, String msg) {
    //	Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    //}
    //
    ///**
    // * 弹出自定义时长的Toast消息
    // *
    // * @param msg 消息内容
    // */
    //public static void toast(String msg, int duration) {
    //	Toast.makeText(MApplication.gainContext(), msg, duration).show();
    //}
    //
    ///**
    // * 弹出自定义时长的Toast消息
    // *
    // * @param msg 消息内容
    // */
    //public static void toast(Context context, String msg, int duration) {
    //	Toast.makeText(context, msg, duration).show();
    //}
    //
    ///**
    // * 显示居中黑色的Toast
    // *
    // * @param context
    // * @param msg
    // */
    //public static void showCenterText(Context context, String msg) {
    //	ToolToast.showShort(context, msg);
    //}

    private void showToast(CharSequence charSequence,Activity activity) {
        Toast.makeText(activity, charSequence,
                Toast.LENGTH_SHORT).show();
    }

}
