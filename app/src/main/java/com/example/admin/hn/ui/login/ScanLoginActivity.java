package com.example.admin.hn.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.volley.RequestListener;
import com.orhanobut.logger.Logger;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author duantao
 * @date on 2017/7/28 14:24
 * @describe 扫码登陆
 */
public class ScanLoginActivity extends BaseActivity {

    private static final String TAG = "ScanLoginActivity";

    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.text_title_back)
    TextView mTextTitleBack;
    @Bind(R.id.tv_result)
    TextView tv_result;

    private String scanResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_login);
        ButterKnife.bind(this);
        initTitleBar();
        initData();
    }

    @Override
    public void initData() {
        super.initData();
        scanResult = getIntent().getStringExtra("scanResult");
        tv_result.setText(scanResult + "");
    }

    @Override
    public void initTitleBar() {
        textTitle.setText("扫描结果");
        mTextTitleBack.setBackgroundResource(R.drawable.btn_back);
    }

    public static void startActivity(Activity activity,String scanResult){
        Intent intent = new Intent(activity, ScanLoginActivity.class);
        intent.putExtra("scanResult", scanResult);
        activity.startActivity(intent);
    }

    @OnClick({R.id.bt_cancel, R.id.bt_login,R.id.text_title_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_title_back:
                finish();
                break;
            case R.id.bt_login:
                ToolAlert.showToast(context, "登录");
                break;
            case R.id.bt_cancel:
                ToolAlert.showToast(context, "取消");
                break;
        }
    }

    public void login() {
        http.postJson(Api.BASE_URL + Api.LOGIN, params,"正在登录...", new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.i(TAG, json);
                if (GsonUtils.isSuccess(json)) {

                }else {
                    ToolAlert.showToast(ScanLoginActivity.this, GsonUtils.getError(json), false);
                }
            }

            @Override
            public void requestError(String message) {
                ToolAlert.showToast(ScanLoginActivity.this, message, false);
            }
        });

    }
}
