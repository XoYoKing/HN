package com.example.admin.hn.ui.account;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.base.HNApplication;
import com.example.admin.hn.utils.ToolAppUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author duantao
 * @date on 2017/8/3 16:29
 * @describe 关于
 */
public class AboutActivity extends BaseActivity {

    @Bind(R.id.text_title_back)
    TextView textTitleBack;
    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.tv_versionName)
    TextView tv_versionName;
    @Bind(R.id.tv_account)
    TextView tv_account;
    @Bind(R.id.tv_email)
    TextView tv_email;
    @Bind(R.id.tv_phone)
    TextView tv_phone;
    @Bind(R.id.tv_userType)
    TextView tv_userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        initTitleBar();
        initData();
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void initTitleBar() {
        textTitleBack.setBackgroundResource(R.drawable.btn_back);
        textTitle.setText(R.string.title_about);
    }

    @Override
    public void initData() {
        super.initData();
        tv_account.setText(HNApplication.mApp.getUserName() + "");
        tv_email.setText(HNApplication.mApp.getEmail() + "");
        tv_phone.setText(HNApplication.mApp.getPhone() + "");
        tv_versionName.setText("V"+ToolAppUtils.getVersionName(context) + "");
        if (HNApplication.mApp.getUserType() == 0) {
            tv_userType.setText("其他用户");
        } else if (HNApplication.mApp.getUserType() == 1) {
            tv_userType.setText("船舶用户");
        } else if (HNApplication.mApp.getUserType() == 2) {
            tv_userType.setText("海务主管");
        } else if (HNApplication.mApp.getUserType() == 3) {
            tv_userType.setText("岸端海宁用户");
        }else {
            tv_userType.setText("未知用户类型");
        }

    }

    @OnClick(R.id.text_title_back)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_title_back:
                finish();
                break;
        }
    }


}
