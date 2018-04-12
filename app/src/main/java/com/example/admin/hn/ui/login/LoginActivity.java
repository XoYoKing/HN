package com.example.admin.hn.ui.login;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.admin.hn.MainActivity;
import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.base.HNApplication;
import com.example.admin.hn.base.MyApplication;
import com.example.admin.hn.http.OkHttpUtil;
import com.example.admin.hn.model.ServerResponse;
import com.example.admin.hn.model.ShipInfo;
import com.example.admin.hn.ui.account.AboutActivity;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolString;
import com.example.admin.hn.volley.RequestListener;
import com.example.admin.hn.widget.LoadingFragment;
import com.example.admin.hn.widget.ProgersssDialog;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * @author duantao
 * @date on 2017/7/28 14:24
 * @describe 登陆
 */
public class LoginActivity extends BaseActivity {


    private static final String TAG = "LoginActivity";

    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.text_tile_right)
    TextView mTextright;
    @Bind(R.id.et_login_name)
    EditText etName;
    @Bind(R.id.et_login_password)
    EditText etPassword;
    @Bind(R.id.cb_agree_protocol)
    CheckBox mCbAgreeProtocol;
    @Bind(R.id.iv_password)
    ImageView iv_password;
    @Bind(R.id.rl_logins)
    RelativeLayout rl_logins;
    @Bind(R.id.sp_environment)
    Spinner environment;

    boolean eyeOpen = false;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;
//    private int status = 1;
    private boolean isTest;//是否是测试环境  默认为生产环境

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initTitleBar();
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                //申请WRITE_EXTERNAL_STORAGE权限
                JPushInterface.requestPermission(this);
            }
        }
        initData();
    }

    @Override
    public void initData() {
        super.initData();
        //进入APP的时候默认为生产环境
        HNApplication.mApp.setTestAmbient(isTest);
        String username = HNApplication.mApp.getUserName();
        String password = HNApplication.mApp.getPassWord();
        String switches = HNApplication.mApp.getSwitche();
        if (isTest) {
            if (switches.equals("1")) {
                mCbAgreeProtocol.setChecked(true);
                etPassword.setText(password);
                etName.setText(username);
                login(username, password);
            } else if (switches.equals("2")) {
                etName.setText(username);
            }
        } else {
            if (switches.equals("1")) {
                mCbAgreeProtocol.setChecked(true);
                etPassword.setText(password);
                etName.setText(username);
                login(username, password);
            } else if (switches.equals("2")) {
                etName.setText(username);
            }
        }

        //数据
        data_list = new ArrayList<>();
        data_list.add("生产环境");
        data_list.add("测试环境");
        //适配器
        arr_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        environment.setAdapter(arr_adapter);
        environment.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {//生产环境
                    Api.BASE_URL = "http://222.66.158.231:9000/";
                    isTest = false;
                } else if (position == 1) {//测试环境
                    Api.BASE_URL = "http://10.18.4.31:9000/";
                    isTest = true;
                }
                HNApplication.mApp.setTestAmbient(isTest);
                String username = HNApplication.mApp.getUserName();
                String password =  HNApplication.mApp.getPassWord();
                String switches =  HNApplication.mApp.getSwitche();
                if (isTest) {
                    if (switches.equals("1")) {
                        mCbAgreeProtocol.setChecked(true);
                        etPassword.setText(password);
                        etName.setText(username);
                        login(username, password);
                    } else if (switches.equals("2")) {
                        etName.setText(username);
                    }
                } else {
                    if (switches.equals("1")) {
                        mCbAgreeProtocol.setChecked(true);
                        etPassword.setText(password);
                        etName.setText(username);
                        login(username, password);
                    } else if (switches.equals("2")) {
                        etName.setText(username);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void initTitleBar() {
        textTitle.setText(R.string.btn_login);
        mTextright.setText(R.string.title_register);
        iv_password.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {
                if (eyeOpen) {
                    //密码 TYPE_CLASS_TEXT 和 TYPE_TEXT_VARIATION_PASSWORD 必须一起使用
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    etPassword.setSelection(etPassword.length());
                    iv_password.setImageResource(R.drawable.eye_off);
                    eyeOpen = false;
                } else {
                    //明文
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    etPassword.setSelection(etPassword.length());
                    iv_password.setImageResource(R.drawable.eye_on);
                    eyeOpen = true;
                }
            }

        });
    }

    public static void startActivity(Activity activity){
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    @OnClick({R.id.tv_forget_password, R.id.text_title_back, R.id.btn_login_enter, R.id.text_tile_right})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_forget_password:
                FindPasswordActivity.startActivity(this);
                break;
            case R.id.text_tile_right:
                RegisterActivity.startActivity(this);
                break;
            case R.id.btn_login_enter:
                if (!ToolString.isNoBlankAndNoNull(etName.getText().toString())) {
                    ToolAlert.showToast(LoginActivity.this, "请输入用户名或手机号", true);
                    return;
                }
                if (!ToolString.isNoBlankAndNoNull(etPassword.getText().toString())) {
                    ToolAlert.showToast(LoginActivity.this, "请输入密码", true);
                    return;
                }
                login(etName.getText().toString(), etPassword.getText().toString());
                break;
        }
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            System.exit(0);
            return true;
        } else
            return super.onKeyDown(keyCode, event);
    }

    public void login(String username, String password) {
        params.put("username", username);
        params.put("phonenumber", password);
        http.postJson(Api.BASE_URL + Api.LOGIN, params,"正在登录...", new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.i(TAG, json);
                if (GsonUtils.isSuccess(json)) {
                    ServerResponse serverResponse = GsonUtils.jsonToBean(json, ServerResponse.class);
                    ShipInfo shipInfo = GsonUtils.jsonToBean(json, ShipInfo.class);
                    //保存登录信息
                    saveLoginInfo(serverResponse,mCbAgreeProtocol.isChecked());
                    ArrayList<ShipInfo.ship> list = new ArrayList<>();
                    for (int i = 0; i < shipInfo.getDocuments().size(); i++) {
                        list.add(new ShipInfo.ship(shipInfo.getDocuments().get(i).getShipnumber(), shipInfo.getDocuments().get(i).getShipname()));
                    }
                    Intent intent_homepage = new Intent(LoginActivity.this, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("list", list);
                    intent_homepage.putExtras(bundle);
                    startActivity(intent_homepage);
                    finish();
                }else {
                    ToolAlert.showToast(LoginActivity.this, GsonUtils.getError(json), false);
                }
            }

            @Override
            public void requestError(String message) {
                ToolAlert.showToast(LoginActivity.this, message, false);
            }
        });

    }

    public void saveLoginInfo(ServerResponse serverResponse,boolean isCheck) {
        if (isCheck) {
            HNApplication.mApp.setSwitche("1");//记住密码
        }else {
            HNApplication.mApp.setSwitche("2");//不记住密码
        }
        HNApplication.mApp.setPassWord(etPassword.getText().toString());
        HNApplication.mApp.setUserName(serverResponse.getUsername());
        HNApplication.mApp.setPhone(serverResponse.getPhonenumber());
        HNApplication.mApp.setUserId(serverResponse.getUserid());
        HNApplication.mApp.setEmail(serverResponse.getEmail());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
