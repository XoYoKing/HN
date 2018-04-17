package com.example.admin.hn.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseActivity;
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
 * @date on 2017/8/1 12:01
 * @describe 忘记密码
 */
public class FindPasswordActivity extends BaseActivity {


    private static final String TAG = "FindPasswordActivity";
    @Bind(R.id.text_title_back)
    TextView mTextTitleBack;
    @Bind(R.id.text_title)
    TextView mTextTitle;
    @Bind(R.id.tb_send_validate_code)
    TimeButton mTbSendValidateCode;
    @Bind(R.id.et_login_call)
    EditText etphone;
    @Bind(R.id.et_validate_code)
    EditText etcode;
    @Bind(R.id.et_new_pay_password)
    EditText etpassword;
    @Bind(R.id.et_confirm_new_pay_password)
    EditText etpasswords;
    @Bind(R.id.et_email)
    EditText etemail;
    private String url_findpassword = Api.BASE_URL + Api.FINDPASSWORD;
    private String url_email = Api.BASE_URL + Api.EMAIL;
    private String phoneNumber;
    private String email;
    private String newPassword;
    private String repeatPassword;
    private String emailCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        ButterKnife.bind(this);
        mTbSendValidateCode.onCreate(savedInstanceState);
        initTitleBar();

    }

    @Override
    public void initTitleBar() {
        super.initTitleBar();
        mTextTitle.setText(R.string.title_reset_login_password);
        mTextTitleBack.setBackgroundResource(R.drawable.btn_back);
    }

    public static void startActivity(Context context){
        Intent intent = new Intent(context, FindPasswordActivity.class);
        context.startActivity(intent);
    }

    @OnClick({R.id.text_title_back, R.id.tv_confirm_change_pay_password, R.id.tb_send_validate_code})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_title_back:
                finish();
                break;
            case R.id.tv_confirm_change_pay_password:
                getTextValue(true);
                if (checkValue()) {
                    changePassword();
                }
                break;
            case R.id.tb_send_validate_code:
                getTextValue(false);
                if (!ToolString.isEmpty(phoneNumber)) {
                    ToolAlert.showToast(context, "请输入手机号码");
                    return ;
                }
                if (!ToolString.isEmpty(email)) {
                    ToolAlert.showToast(context, "请输入邮箱地址");
                    return ;
                }
                sendCode();
                break;
        }
    }

    private void changePassword() {
        Map map = new HashMap();
        map.put("password", newPassword);
        map.put("phonenumber", phoneNumber);
        map.put("email", email);
        map.put("emailcode", emailCode);
        map.put("repeatpassword", repeatPassword);
        http.postJson(url_findpassword, map, "正在修改...", new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                if (GsonUtils.isSuccess(json)) {
                    finish();
                }
                ToolAlert.showToast(context, GsonUtils.getError(json));
            }

            @Override
            public void requestError(String message) {
                ToolAlert.showToast(context, message);
            }
        });
    }

    /**
     * 获取输入框的参数
     * @param is  是否需要获取登录密码和邮箱验证码
     */
    private void getTextValue(boolean is) {
        if (is) {
            newPassword = etpassword.getText().toString();
            repeatPassword = etpasswords.getText().toString();
            emailCode = etcode.getText().toString();
        }
        phoneNumber = etphone.getText().toString();
        email = etemail.getText().toString();
    }

    //校验参数是否为空
    private boolean checkValue(){
        if (!ToolString.isEmpty(phoneNumber)) {
            ToolAlert.showToast(context, "请输入手机号码");
            return false;
        }
        if (!ToolString.isEmpty(email)) {
            ToolAlert.showToast(context, "请输入邮箱地址");
            return false;
        }
        if (!ToolString.isEmpty(emailCode)) {
            ToolAlert.showToast(context, "请输入邮箱验证码");
            return false;
        }
        if (!ToolString.isEmpty(newPassword)) {
            ToolAlert.showToast(context, "请输入新密码");
            return false;
        }
        if (!ToolString.isEmpty(newPassword)) {
            ToolAlert.showToast(context, "请输入确认密码");
            return false;
        }
        return true;
    }
    /**
     * 发送短信验证码
     */
    private void sendCode() {
        Map maps = new HashMap();
        maps.put("phonenumber", phoneNumber);
        maps.put("email", email);
        http.postJson(url_email, maps, "发送中...", new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.e("发送邮箱验证码",json);
                mTbSendValidateCode.startTime();
                ToolAlert.showToast(context, "验证码已发送至"+ email +"邮箱,请注意查收。", false);
            }
            @Override
            public void requestError(String message) {
                ToolAlert.showToast(context, message, false);
            }
        });
    }

    @Override
    public void initView() {
        super.initView();
        mTbSendValidateCode.setTextAfter("s")
                .setTextBefore("发送验证码")
                .setLenght(60 * 1000)
                .setTextSize(14);
    }

    @Override
    protected void onDestroy() {
        mTbSendValidateCode.onDestroy();
        super.onDestroy();
    }

}
