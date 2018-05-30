package com.example.admin.hn.ui.login;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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
import com.example.admin.hn.base.PermissionsListener;
import com.example.admin.hn.model.ServerResponse;
import com.example.admin.hn.model.ShipInfo;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolString;
import com.example.admin.hn.utils.ToolViewUtils;
import com.example.admin.hn.volley.RequestListener;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


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
    private boolean isTest;//是否是测试环境  默认为生产环境
    private boolean isRequesting;//为了避免在onResume中多次请求权限
    private String[] permissions = {Manifest.permission.READ_PHONE_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onResume() {
        super.onResume();
        if (!isRequesting) {
            requestPermissions(permissions, mListener);
            isRequesting = true;
        }
    }
    private PermissionsListener mListener = new PermissionsListener() {
        @Override
        public void onGranted() {
            isRequesting = false;
        }

        @Override
        public void onDenied(List<String> deniedPermissions, boolean isNeverAsk) {
            if (!isNeverAsk) {//请求权限没有全被勾选不再提示然后拒绝
                ToolAlert.dialog(context, "权限申请", "为了能正常使用\"" + HNApplication.mApp.getAPPName() + "\"，请授予所需权限", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        requestPermissions(permissions, mListener);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            } else {//全被勾选不再提示
                ToolAlert.dialog(context, "权限申请",
                        "\"" + HNApplication.mApp.getAPPName() + "\"缺少必要权限\n请手动授予\"" +  HNApplication.mApp.getAPPName() + "\"访问您的权限",
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                        isRequesting = false;
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initTitleBar();
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
        ToolViewUtils.setSelection(etName);
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
                    Api.SHOP_BASE_URL = "http://172.16.0.10:8990/";
                    isTest = false;
                } else if (position == 1) {//测试环境
                    Api.BASE_URL = "http://10.18.4.31:9000/";
                    Api.SHOP_BASE_URL = "http://172.16.0.3:8990/";
//                    Api.SHOP_BASE_URL = "http://10.17.107.65:8990/";
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
                    //保存登录信息
                    saveLoginInfo(serverResponse,mCbAgreeProtocol.isChecked());
                    ArrayList<ShipInfo.Ship> list = new ArrayList<>();
                    for (int i = 0; i < serverResponse.getMyShip().size(); i++) {
                        ShipInfo.Ship ship = serverResponse.getMyShip().get(i);
                        list.add(new ShipInfo.Ship(ship.shipid,ship.shipname,true));
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
        HNApplication.mApp.setCompanyId(serverResponse.getCompanyid());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
