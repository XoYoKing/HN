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
import com.example.admin.hn.http.OkHttpUtil;
import com.example.admin.hn.model.ServerResponse;
import com.example.admin.hn.ui.login.FindPasswordActivity;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
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
    @Bind(R.id.et_please_input_change_original_login_password)
    EditText etpassword;
    @Bind(R.id.et_please_input_new_login_password)
    EditText etpassword1;
    @Bind(R.id.et_confirm_new_login_password)
    EditText etpassword2;
    @Bind(R.id.tv_account_code)
    TextView tv_account_code;
    @Bind(R.id.tv_account_phone)
    TextView tv_account_phone;

    private String url_changepassword = Api.BASE_URL + Api.CHANGEPASSWORD;


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
        tv_account_code.setText(MainActivity.username);
        tv_account_phone.setText(MainActivity.phonenumber);
    }

    public static void startActivity(Context context){
        Intent intent = new Intent(context, ChangeLoginPasswordActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void initView() {
        super.initView();
    }


    @OnClick({R.id.text_title_back, R.id.btn_confirm_change_login_password})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.text_title_back:
                finish();
                break;


            case R.id.btn_confirm_change_login_password:
                Map map = new HashMap();
                map.put("password", etpassword.getText().toString());
                map.put("oldpassword", etpassword1.getText().toString());
                map.put("userid", MainActivity.USER_ID);
                map.put("repeatpassword", etpassword2.getText().toString());
                String jsonStr = GsonUtils.mapToJson(map);
                Logger.i(TAG, jsonStr);
                try {
                    OkHttpUtil.postJsonData2Server(ChangeLoginPasswordActivity.this,
                            url_changepassword,
                            jsonStr,
                            new OkHttpUtil.MyCallBack() {
                                @Override
                                public void onFailure(Request request, IOException e) {
                                    ToolAlert.showToast(ChangeLoginPasswordActivity.this,"服务器异常,请稍后再试",false);

                                }

                                @Override
                                public void onResponse(String json) {
                                    Logger.i(TAG, json);
                                    ServerResponse serverResponse = GsonUtils
                                            .jsonToBean(json,
                                                    ServerResponse.class
                                            );
                                    ToolAlert.showToast(ChangeLoginPasswordActivity.this, serverResponse.getMessage(), false);
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
