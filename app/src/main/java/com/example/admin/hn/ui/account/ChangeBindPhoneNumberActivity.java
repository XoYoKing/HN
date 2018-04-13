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
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolString;
import com.example.admin.hn.volley.RequestListener;
import com.example.admin.hn.widget.TimeButton;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author duantao
 * @date on 2017/8/3 14:32
 * @describe 修改邮箱
 */
public class ChangeBindPhoneNumberActivity extends BaseActivity {

    private static final String TAG = "ChangeBindPhoneNumberActivity";

    @Bind(R.id.text_title_back)
    TextView textTitleBack;
    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.tb_send_validate_code)
    TimeButton mTbSendValidateCode;
    @Bind(R.id.tv_bound_telephone)
    TextView mTvBoundTelephone;
    @Bind(R.id.et_please_input_pay_password)
    EditText mEtPleaseInputPayPassword;
    @Bind(R.id.et_please_input_new_telephone)
    EditText mEtPleaseInputNewTelephone;
    @Bind(R.id.et_please_input_validate_code)
    EditText mEtPleaseInputValidateCode;
    @Bind(R.id.et_please_input_pay_phone)
    TextView mTvConfirmChangeBindPhone;
    private String url_changephone = Api.BASE_URL + Api.CHANGEPHONE;
    private String url_email = Api.BASE_URL + Api.EMAIL;
    private String phoneNumber;
    private String email;
    private String password;
    private String emailCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_bind_phone_number);
        ButterKnife.bind(this);
        mTbSendValidateCode.onCreate(savedInstanceState);
        initTitleBar();
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        mTvBoundTelephone.setText(HNApplication.mApp.getEmail());
        mTbSendValidateCode.setTextAfter("s")
                .setTextBefore("发送验证码")
                .setLenght(60 * 1000)
                .setTextSize(14);

    }

    @Override
    public void initTitleBar() {
        textTitleBack.setBackgroundResource(R.drawable.btn_back);
        textTitle.setText(R.string.title_change_bind_email);
    }

    public static void startActivity(Context context){
        Intent intent = new Intent(context, ChangeBindPhoneNumberActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        mTbSendValidateCode.onDestroy();
        super.onDestroy();
    }

    @OnClick({R.id.text_title_back, R.id.tb_send_validate_code, R.id.tv_confirm_change_bind_phone})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_title_back:
                finish();
                break;
            case R.id.tb_send_validate_code:
                getTextValue(false);
                if (!ToolString.isEmpty(phoneNumber)) {
                    ToolAlert.showToast(context, "请输入手机号码", false);
                    return;
                }
                if (!ToolString.isEmpty(email)) {
                    ToolAlert.showToast(context, "请输入邮箱地址", false);
                    return;
                }
                sendCode();
                break;
            case R.id.tv_confirm_change_bind_phone:
                getTextValue(true);
                if (checkValue()) {
                    sureUpdate();
                }
                break;
        }
    }

    /**
     * 获取输入框的参数
     * @param is  是否需要获取登录密码和邮箱验证码
     */
    private void getTextValue(boolean is) {
        if (is) {
            password = mEtPleaseInputPayPassword.getText().toString();
            emailCode = mEtPleaseInputValidateCode.getText().toString();
        }
        phoneNumber = mTvConfirmChangeBindPhone.getText().toString();
        email = mEtPleaseInputNewTelephone.getText().toString();
    }

    //校验参数是否为空
    private boolean checkValue(){
        if (!ToolString.isEmpty(password)) {
            ToolAlert.showToast(context, "请输入登录密码", false);
            return false;
        }
        if (!ToolString.isEmpty(phoneNumber)) {
            ToolAlert.showToast(context, "请输入手机号码", false);
            return false;
        }
        if (!ToolString.isEmpty(email)) {
            ToolAlert.showToast(context, "请输入邮箱地址", false);
            return false;
        }
        if (!ToolString.isEmpty(emailCode)) {
            ToolAlert.showToast(context, "请输入邮箱验证码", false);
            return false;
        }
        return true;
    }

    /**
     * 发送短信验证码
     */
    private void sendCode() {
        Map maps = new HashMap();
        maps.put("phonenumber",phoneNumber);
        maps.put("email",email );
        http.postJson(url_email, maps, "发送中...", new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                if (GsonUtils.isSuccess(json)) {
                    ToolAlert.showToast(context, "验证码已发送至"+email+"邮箱,请注意查收。", false);
                }else {
                    ToolAlert.showToast(context, GsonUtils.getError(json), false);
                }
            }
            @Override
            public void requestError(String message) {
                ToolAlert.showToast(context, message, false);
            }
        });
    }

    /**
     * 确认修改
     */
    private void sureUpdate() {
        Map map = new HashMap();
        map.put("password",password);
        map.put("phonenumber", phoneNumber);
        map.put("email", email);
        map.put("emailcode",emailCode);
        http.postJson(url_changephone, map, "提交中...", new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.i(TAG, json);
                if (GsonUtils.isSuccess(json)) {
                    finish();
                }
                ToolAlert.showToast(context, GsonUtils.getError(json), false);
            }

            @Override
            public void requestError(String message) {
                ToolAlert.showToast(context, message, false);
            }
        });
    }


}
