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
                Map maps = new HashMap();
                maps.put("phonenumber", mTvConfirmChangeBindPhone.getText().toString());
                maps.put("email", mEtPleaseInputNewTelephone.getText().toString());
                String json = GsonUtils.mapToJson(maps);
                Logger.i(TAG, json);
                try {
                    OkHttpUtil.postJsonData2Server(ChangeBindPhoneNumberActivity.this,
                            url_email,
                            json,
                            new OkHttpUtil.MyCallBack() {
                                @Override
                                public void onFailure(Request request, IOException e) {
                                    ToolAlert.showToast(ChangeBindPhoneNumberActivity.this, "服务器异常,请稍后再试", false);
                                }

                                @Override
                                public void onResponse(String json) {
//                                    Logger.i(TAG, json);
//                                    ServerResponse serverResponse = GsonUtils
//                                            .jsonToBean(json,
//                                                    ServerResponse.class
//                                            );
//                                    ToolAlert.showToast(ChangeBindPhoneNumberActivity.this, serverResponse.getMessage(), false);
                                }
                            });
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_confirm_change_bind_phone:
                Map map = new HashMap();
                map.put("password", mEtPleaseInputPayPassword.getText().toString());
                map.put("phonenumber", mTvConfirmChangeBindPhone.getText().toString());
                map.put("email", mEtPleaseInputNewTelephone.getText().toString());
                map.put("userid", HNApplication.mApp.getUserId());
                map.put("emailcode", mEtPleaseInputValidateCode.getText().toString());
                String jsonStr = GsonUtils.mapToJson(map);
                Logger.i(TAG, jsonStr);
                try {
                    OkHttpUtil.postJsonData2Server(ChangeBindPhoneNumberActivity.this,
                            url_changephone,
                            jsonStr,
                            new OkHttpUtil.MyCallBack() {
                                @Override
                                public void onFailure(Request request, IOException e) {
                                }

                                @Override
                                public void onResponse(String json) {
                                    Logger.i(TAG, json);
                                    ServerResponse serverResponse = GsonUtils
                                            .jsonToBean(json,
                                                    ServerResponse.class
                                            );
                                    ToolAlert.showToast(ChangeBindPhoneNumberActivity.this, serverResponse.getMessage(), false);
                                    finish();
                                }

                            });
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }


}
