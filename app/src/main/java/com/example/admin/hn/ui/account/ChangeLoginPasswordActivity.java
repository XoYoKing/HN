package com.example.admin.hn.ui.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.admin.hn.MainActivity;
import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.base.HNApplication;
import com.example.admin.hn.http.OkHttpUtil;
import com.example.admin.hn.model.ServerResponse;
import com.example.admin.hn.ui.login.FindPasswordActivity;
import com.example.admin.hn.ui.login.LoginActivity;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolString;
import com.example.admin.hn.volley.RequestListener;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
*  @date on 2017/8/1 16:54
*  @author duantao
*  @describe 修改密码
*/
public class ChangeLoginPasswordActivity extends BaseActivity {

    private static final String TAG = "ChangeLoginPasswordActivity";
    @Bind(R.id.text_title_back)
    TextView textTitleBack;
    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.et_old_password)
    EditText et_old_password;
    @Bind(R.id.et_new_password)
    EditText et_new_password;
    @Bind(R.id.et_repeat_password)
    EditText et_repeat_password;
    @Bind(R.id.tv_account_code)
    TextView tv_account_code;
    @Bind(R.id.tv_account_phone)
    TextView tv_account_phone;
    private String url_changepassword = Api.BASE_URL + Api.CHANGEPASSWORD;
    private String oldPassword;
    private String newPassword;
    private String repeatPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_login_password);
        ButterKnife.bind(this);
        initTitleBar();
        initView();
    }


    @Override
    public void initTitleBar() {
        textTitleBack.setBackgroundResource(R.drawable.btn_back);
        textTitle.setText(R.string.title_change_password);
        tv_account_code.setText(HNApplication.mApp.getUserName());
        tv_account_phone.setText(HNApplication.mApp.getPhone());
    }

    public static void startActivity(Context context){
        Intent intent = new Intent(context, ChangeLoginPasswordActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void initView() {
        super.initView();
    }


    @OnClick({R.id.text_title_back, R.id.bt_confirm})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_title_back:
                finish();
                break;
            case R.id.bt_confirm:
                if (checkTextValue()) {
                    sureUpdate();
                }
                break;
        }
    }

    private boolean checkTextValue(){
        oldPassword = et_old_password.getText().toString();
        newPassword = et_new_password.getText().toString();
        repeatPassword = et_repeat_password.getText().toString();
        if (!ToolString.isEmpty(oldPassword)) {
            ToolAlert.showToast(context, "请输入原登录密码", false);
            return false;
        }
        if (!ToolString.isEmpty(newPassword)) {
            ToolAlert.showToast(context, "请输入新登录密码", false);
            return false;
        }
        if (!ToolString.isEmpty(repeatPassword)) {
            ToolAlert.showToast(context, "请输入确认密码", false);
            return false;
        }
        if (oldPassword.equals(newPassword)) {
            ToolAlert.showToast(context, "原密码与新密码一致", false);
            return false;
        }
        if (!newPassword.equals(repeatPassword)) {
            ToolAlert.showToast(context, "新密码与重复密码不一致", false);
            return false;
        }
        return true;
    }

    private void sureUpdate() {
        params.put("oldpassword", oldPassword);
        params.put("password", newPassword);
        params.put("repeatpassword", repeatPassword);
        http.postJson(url_changepassword, params, "修改中...", new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                if (GsonUtils.isSuccess(json)) {
                    //密码修改成功需要重新登录
                    HNApplication.mApp.logout();
                    LoginActivity.startActivity(ChangeLoginPasswordActivity.this);
                }
                ToolAlert.showToast(context,GsonUtils.getError(json),false);
            }

            @Override
            public void requestError(String message) {
                ToolAlert.showToast(context,message,false);
            }
        });
    }

}
