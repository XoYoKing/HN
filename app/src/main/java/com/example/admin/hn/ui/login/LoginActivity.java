package com.example.admin.hn.ui.login;

import android.Manifest;
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
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolString;
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
    private String url_login = Api.BASE_URL + Api.LOGIN;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;
    private int status = 1;
    private LoadingFragment loading;

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
        String username = HNApplication.mApp.getUserName(status == 1 ? false : true);
        String password = HNApplication.mApp.getPassWord(status == 1 ? false : true);
        String switches = HNApplication.mApp.getSwitche(status == 1 ? false : true);
        if (status == 1) {
            if (switches.equals("1")) {
                mCbAgreeProtocol.setChecked(true);
                etPassword.setText(password);
                etName.setText(username);
                login(username, password, "1");
            } else if (switches.equals("2")) {
                etName.setText(username);
            }
        } else {
            if (switches.equals("1")) {
                mCbAgreeProtocol.setChecked(true);
                etPassword.setText(password);
                etName.setText(username);
                login(username, password, "1");
            } else if (switches.equals("2")) {
                etName.setText(username);
            }
        }

        //数据
        data_list = new ArrayList<String>();
        data_list.add("生产环境");
        data_list.add("测试环境");

        //适配器
        arr_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        environment.setAdapter(arr_adapter);
        environment.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Api.BASE_URL = "http://222.66.158.231:9000/";
                    status = 1;
                } else if (position == 1) {
                    Api.BASE_URL = "http://10.18.4.31:9000/";
                    status = 2;
                }
                String username = HNApplication.mApp.getUserName(status == 1 ? false : true);
                String password =  HNApplication.mApp.getPassWord(status == 1 ? false : true);
                String switches =  HNApplication.mApp.getSwitche(status == 1 ? false : true);
                if (status == 1) {
                    if (switches.equals("1")) {
                        mCbAgreeProtocol.setChecked(true);
                        etPassword.setText(password);
                        etName.setText(username);
                        login(username, password, "1");
                    } else if (switches.equals("2")) {
                        etName.setText(username);
                    }
                } else {
                    if (switches.equals("1")) {
                        mCbAgreeProtocol.setChecked(true);
                        etPassword.setText(password);
                        etName.setText(username);
                        login(username, password, "1");
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
//        textTitleBack.setBackgroundResource(R.drawable.btn_back);
//        tv.setBackgroundResource(R.drawable.btn_environment);
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


    @OnClick({R.id.tv_forget_password, R.id.text_title_back, R.id.btn_login_enter, R.id.text_tile_right})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_forget_password:
                Intent intent_Password = new Intent(LoginActivity.this, FindPasswordActivity.class);
                startActivity(intent_Password);
                break;
            case R.id.text_tile_right:
                Intent intent_register = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent_register);
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
                login(etName.getText().toString(), etPassword.getText().toString(), "2");
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

    public void login(String username, String password, final String str) {
        Map map = new HashMap();
        map.put("username", username);
        map.put("phonenumber", password);
        String jsonStr = GsonUtils.mapToJson(map);
        Logger.i(TAG, jsonStr);
        try {
            loading = LoadingFragment.showLoading(context, "正在登陆...");
            OkHttpUtil.postJsonData2Server(LoginActivity.this,
                    Api.BASE_URL + Api.LOGIN,
                    jsonStr,
                    new OkHttpUtil.MyCallBack() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            loading.dismiss();
                            ToolAlert.showToast(LoginActivity.this, "服务器异常,请稍后再试", false);
                        }

                        @Override
                        public void onResponse(String json) {
                            loading.dismiss();
                            Logger.i(TAG, json);
                            ServerResponse serverResponse = GsonUtils
                                    .jsonToBean(json,
                                            ServerResponse.class
                                    );
                            ShipInfo shipInfo = GsonUtils
                                    .jsonToBean(json,
                                            ShipInfo.class
                                    );
                            if (serverResponse.getStatus().equals("success")) {
                                if (str.equals("2")) {
                                    if (mCbAgreeProtocol.isChecked()) {
                                        saveLoginInfo(etName.getText().toString(), etPassword.getText().toString(), "1", status);
                                    } else {
                                        saveLoginInfo(etName.getText().toString(), etPassword.getText().toString(), "2", status);
                                    }
                                }
                                ArrayList<ShipInfo.ship> list = new ArrayList<ShipInfo.ship>();
                                for (int i = 0; i < shipInfo.getDocuments().size(); i++) {
                                    list.add(new ShipInfo.ship(shipInfo.getDocuments().get(i).getShipnumber(), shipInfo.getDocuments().get(i).getShipname()));
                                }
//                                ToolAlert.showToast(LoginActivity.this, serverResponse.getMessage(), false);
                                Intent intent_homepage = new Intent(LoginActivity.this, MainActivity.class);
                                intent_homepage.putExtra("phonenumber", serverResponse.getPhonenumber());
                                intent_homepage.putExtra("username", serverResponse.getUsername());
                                intent_homepage.putExtra("email", serverResponse.getEmail());
                                intent_homepage.putExtra("userid", serverResponse.getUserid());
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("list", list);
                                intent_homepage.putExtras(bundle);
                                startActivity(intent_homepage);
                                finish();
                            } else {
                                ToolAlert.showToast(LoginActivity.this, serverResponse.getMessage(), false);
                            }

                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void saveLoginInfo(String username, String password, String switches, int i) {
        HNApplication.mApp.setUserName(username, i == 1 ? false : true);
        HNApplication.mApp.setSwitche(switches,i == 1 ? false : true);
        HNApplication.mApp.setPassWord(password,i == 1 ? false : true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoadingFragment.dismiss(loading);
    }
}
