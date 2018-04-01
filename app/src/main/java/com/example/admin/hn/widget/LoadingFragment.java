package com.example.admin.hn.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.admin.hn.R;


/**
 * 网络请求进度显示布局
 */
public class LoadingFragment extends AlertDialog {
    private TextView vLoading_text;
    private String mMsg = "正在加载···";
    private Context context;

    private LoadingFragment(Context context) {
        super(context, R.style.MyDialogStyle);
        try {
            this.context = context;
            this.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView(context);
        this.setCanceledOnTouchOutside(false);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_loading, null, false);
        vLoading_text = (TextView) view.findViewById(R.id.loading_text);
        vLoading_text.setText(mMsg);
        setContentView(view);
    }

    private void setMsg(String msg) {
        if (msg != null && vLoading_text != null) {
            this.mMsg = msg;
            vLoading_text.setText(mMsg);
        }
    }

    /**
     * 显示 网络请求时加载数据的布局
     *
     * @param context
     * @param progressTitle
     */
    public static LoadingFragment showLoading(Context context, String progressTitle) {
        if (progressTitle == null) return null;
        if (context == null || ((Activity) context).isFinishing()) return null;
        LoadingFragment dialog = new LoadingFragment(context);
        dialog.setMsg(progressTitle);
        return dialog;
    }

    /**
     * 关闭已显示的dialog
     *
     * @param context
     */
    public void dismiss(Context context) {
        if (context != null) {
            try {
                if (this.isShowing()) {
                    this.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭已显示的dialog
     *
     * @param dialog
     */
    public static void dismiss(LoadingFragment dialog) {
        if (dialog != null) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
