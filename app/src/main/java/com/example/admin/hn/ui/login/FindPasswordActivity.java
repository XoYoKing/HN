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
                if (etpassword.getText().toString().length()==0||etphone.getText().toString().length()==0||
                        etemail.getText().toString().length()==0||etcode.getText().toString().length()==0||
                        etpasswords.getText().toString().length()==0){
                    ToolAlert.showToast(FindPasswordActivity.this, "填写内容不能为空", false);

                }else {

                    Map map = new HashMap();
                    map.put("password", etpassword.getText().toString());
                    map.put("phonenumber", etphone.getText().toString());
                    map.put("email", etemail.getText().toString());
                    map.put("emailcode", etcode.getText().toString());
                    map.put("repeatpassword", etpasswords.getText().toString());
                    String jsonStr = GsonUtils.mapToJson(map);
                    Logger.i(TAG, jsonStr);
                    try {
                        OkHttpUtil.postJsonData2Server(FindPasswordActivity.this,
                                url_findpassword,
                                jsonStr,
                                new OkHttpUtil.MyCallBack() {
                                    @Override
                                    public void onFailure(Request request, IOException e) {
                                        ToolAlert.showToast(FindPasswordActivity.this, "服务器异常,请稍后再试", false);

                                    }

                                    @Override
                                    public void onResponse(String json) {
                                        Logger.i(TAG, json);
                                        ServerResponse serverResponse = GsonUtils
                                                .jsonToBean(json,
                                                        ServerResponse.class
                                                );
                                        ToolAlert.showToast(FindPasswordActivity.this, serverResponse.getMessage(), false);
                                        finish();
                                    }

                                });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.tb_send_validate_code:
                if(etphone.getText().toString().length()==0||etemail.getText().toString().length()==0){
                   ToolAlert.showToast(FindPasswordActivity.this, "邮箱及手机号为必填项", false);
                }else {
                    Map maps = new HashMap();
                    maps.put("phonenumber", etphone.getText().toString());
                    maps.put("email", etemail.getText().toString());
                    String json = GsonUtils.mapToJson(maps);
                    Logger.i(TAG, json);
                    try {
                        OkHttpUtil.postJsonData2Server(FindPasswordActivity.this,
                                url_email,
                                json,
                                new OkHttpUtil.MyCallBack() {
                                    @Override
                                    public void onFailure(Request request, IOException e) {
                                        ToolAlert.showToast(FindPasswordActivity.this, "服务器异常,请稍后再试", false);
                                    }

                                    @Override
                                    public void onResponse(String json) {
//                                    Logger.i(TAG, json);
//                                    ServerResponse serverResponse = GsonUtils
//                                            .jsonToBean(json,
//                                                    ServerResponse.class
//                                            );
//                                    ToolAlert.showToast(FindPasswordActivity.this, serverResponse.getMessage(), false);
                                    }
                                });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
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
