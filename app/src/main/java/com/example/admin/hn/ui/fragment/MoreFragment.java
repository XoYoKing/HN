package com.example.admin.hn.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseFragment;
import com.example.admin.hn.base.HNApplication;
import com.example.admin.hn.http.OkHttpUtil;
import com.example.admin.hn.model.AppUpdateInfo;
import com.example.admin.hn.ui.account.AboutActivity;
import com.example.admin.hn.ui.account.ChangeBindPhoneNumberActivity;
import com.example.admin.hn.ui.account.ChangeLoginPasswordActivity;
import com.example.admin.hn.ui.account.ChartUpdateActivity;
import com.example.admin.hn.ui.account.ShipSelectActivity;
import com.example.admin.hn.ui.login.LoginActivity;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.volley.RequestListener;
import com.example.admin.hn.widget.ProgressDialog;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * @author duantao
 * @date on 2017/7/26 16:04
 * @describe 更多
 */
public class MoreFragment extends BaseFragment {
    private static final String TAG = "MoreFragment";
    @Bind(R.id.tv_top_title)
    TextView title;
    @Bind(R.id.lv_more_function)
    ListView mLvMoreFunction;
    @Bind(R.id.btn_Sign_out)
    Button mBtSignOut;
    @Bind(R.id.text_tile_right)
    FrameLayout right;
    @Bind(R.id.iv_search)
    ImageView search;


    private MyBaseAdapter myAdapter;
    private int[] imageIds = {R.drawable.right, R.drawable.right, R.drawable.right, R.drawable.right, R.drawable.right, R.drawable.right};
    private String[] functionDesc;
    private String url_update = Api.BASE_URL + Api.UPDATE;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        ButterKnife.bind(this, view);
        initView();
        initData();
        myAdapter = new MyBaseAdapter();
        mLvMoreFunction.setAdapter(myAdapter);
        return view;
    }

    @Override
    public void initView() {
        title.setText("更多");
        right.setVisibility(View.GONE);
        search.setVisibility(View.GONE);
    }

    @Override
    public void initTitleBar() {

    }

    @Override
    public void initData() {
        super.initData();
        functionDesc = this.getResources().getStringArray(R.array.function_desc);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    private class MyBaseAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return functionDesc.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }


        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View arg1, ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_listview_morefragment, parent, false
            );
            ImageView extend_arrow = (ImageView) view.findViewById(
                    R.id.iv_extend_arrow
            );
            TextView fun_desc = (TextView) view.findViewById(
                    R.id.tv_fun_desc
            );
            extend_arrow.setImageResource(imageIds[position]);
            fun_desc.setText(functionDesc[position]);
            return view;
        }
    }

    @OnItemClick(R.id.lv_more_function)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            ChartUpdateActivity.startActivity(activity);
        } else if (position == 1) {
            ShipSelectActivity.startActivity(activity);
        } else if (position == 2) {
            ChangeLoginPasswordActivity.startActivity(activity);
        } else if (position == 3) {
            ChangeBindPhoneNumberActivity.startActivity(activity);
        } else if (position == 4) {
            checkVersion();
        } else if (position == 5){
            AboutActivity.startActivity(activity);
        }

    }

    private void checkVersion() {
        params.put("type", "1");
        http.postJson(url_update, params, progressTitle, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.i(TAG, json);
                if (GsonUtils.isSuccess(json)) {
                    AppUpdateInfo appUpdateInfo = GsonUtils.jsonToBean(json, AppUpdateInfo.class
                    );
                    if (Double.valueOf(getAppVersionName(getActivity())) < Double.valueOf(appUpdateInfo.getDocuments().get(appUpdateInfo.getDocuments().size()-1).getVersionnumber())) {
                        showNoticeDialog(getActivity(), appUpdateInfo.getDocuments().get(0).getDownloadurl());
                    } else {
                        ToolAlert.showToast(getActivity(), "当前已是最新版本", false);
                    }
                }else {
                    ToolAlert.showToast(getActivity(), GsonUtils.getError(json), false);
                }
            }

            @Override
            public void requestError(String message) {
                ToolAlert.showToast(getActivity(), message, false);
            }
        });
    }

    @OnClick(R.id.btn_Sign_out)
    public void onClick(View v) {
        //清除 当前环境的数据
        ToolAlert.dialog(activity, "退出登陆", "是否确定退出登陆", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HNApplication.mApp.logout();
                LoginActivity.startActivity(activity);
                dialog.dismiss();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

    }

    static ProgressDialog progersssDialog;

    /**
     * 显示软件更新对话框
     */

    public static void showNoticeDialog(final Context context, final String url) {
        new AlertDialog.Builder(context).setTitle(R.string.soft_update_title).setCancelable(false)//设置对话框标题
                .setMessage(R.string.soft_update_info)//设置显示的内容
                .setPositiveButton(
                        R.string.soft_update_updatebtn,
                        new DialogInterface.OnClickListener() {//添加确定按钮


                            @Override

                            public void onClick(DialogInterface dialog, int which) {
                                //确定按钮的响应事件
                                ToolAlert.showToast(context, "下载中", false);
                                progersssDialog = new ProgressDialog(context);
//                                // TODO Auto-generated method stub
                                OkHttpUtil.dowloadProgress(context, Api.BASE_URL + url, progersssDialog);

                            }

                        }
                ).setNegativeButton(
                R.string.soft_update_later, new DialogInterface.OnClickListener() {
                    //添加返回按钮

                    @Override

                    public void onClick(DialogInterface dialog, int which) {//响应事件
                        // TODO Auto-generated method stub
                        dialog.dismiss();

                    }

                }
        ).show();//在按键响应事件中显示此对话框
    }


    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

}
