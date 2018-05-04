package com.example.admin.hn.ui.shop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.http.Constant;
import com.example.admin.hn.model.AddressInfo;
import com.example.admin.hn.model.CityInfo;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolAppUtils;
import com.example.admin.hn.utils.ToolString;
import com.example.admin.hn.utils.ToolViewUtils;
import com.example.admin.hn.volley.RequestListener;
import com.orhanobut.logger.Logger;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by hjy on 2016/11/5.
 * 创建新地址
 */
public class CreateAddressActivity extends BaseActivity {

    @Bind(R.id.text_title_back)
    TextView textTitleBack;
    @Bind(R.id.text_tile_right)
    TextView text_tile_right;
    @Bind(R.id.text_title)
    TextView textTitle;
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver br;
    private String url = Api.SHOP_BASE_URL + Api.GET_CREATE_ADDRESS;
    private String url_del = Api.SHOP_BASE_URL + Api.GET_DELETE_ADDRESS;

    private AddressInfo info;
    private CityInfo provinceInfo;
    private CityInfo cityInfo;
    private CityInfo areaInfo;
    private EditText et_detailed_address;
    private EditText et_consignee_phone;
    private EditText et_consignee_name;
    private RelativeLayout relative_region;
    private Switch cb_default_address;
    private TextView tv_region;
    private String name;
    private String phone;
    private String addressId;
    private String region;
    private String detailed_address;
    private RelativeLayout rl_default_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address);
        ButterKnife.bind(this);
        initTitleBar();
        initView();
        initData();
    }

    /**
     *
     * @param context
     */
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CreateAddressActivity.class);
        context.startActivity(intent);
    }
    public static void startActivity(Context context, AddressInfo info) {
        Intent intent = new Intent(context, CreateAddressActivity.class);
        intent.putExtra("info", info);
        context.startActivity(intent);
    }


    @Override
    public void initTitleBar() {
        textTitle.setText("添加新地址");
        textTitleBack.setBackgroundResource(R.drawable.btn_back);
    }

    @Override
    public void initView() {
        et_detailed_address = (EditText) findViewById(R.id.et_detailed_address);
        et_consignee_phone = (EditText) findViewById(R.id.et_consignee_phone);
        et_consignee_name = (EditText) findViewById(R.id.et_consignee_name);
        relative_region = (RelativeLayout) findViewById(R.id.relative_region);
        rl_default_address = (RelativeLayout) findViewById(R.id.rl_default_address);
        cb_default_address= (Switch) findViewById(R.id.select_address);
        tv_region = (TextView) findViewById(R.id.tv_region);
    }


    @Override
    public void initData() {
        info = (AddressInfo) getIntent().getSerializableExtra("info");
        if (info != null) {
            text_tile_right.setText("删除");
            textTitle.setText("编辑收货地址");
            if (info.isDefaul==0) {//不是默认地址
                rl_default_address.setVisibility(View.VISIBLE);
            }else {
                rl_default_address.setVisibility(View.GONE);
            }
            addressId = info.id;
            et_detailed_address.setText(info.receiverAddr);
            et_consignee_phone.setText(info.phone);
            et_consignee_name.setText(info.receiverName);
            tv_region.setText(info.areaName);
            ToolViewUtils.setSelection(et_consignee_name);
        }
        initBroadcastReceiver();
    }

    @OnClick({R.id.text_title_back, R.id.text_tile_right, R.id.relative_region,R.id.bt_save})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_title_back:
                finish();
                break;
            case R.id.text_tile_right:
                ToolAlert.dialog(context, "", "是否删除此地址", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAddress();
                        dialog.dismiss();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.relative_region:
                ProvinceActivity.startActivity(context, "NewAddress");
                break;
            case R.id.bt_save:
                getTextValue();
                saveAddress();
                break;
        }
    }

    private void deleteAddress() {
        Map map = new HashMap();
        map.put("id", info.id);
        http.post(url_del, map, "正在删除...", new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.e("删除地址", json);
                if (GsonUtils.isShopSuccess(json)) {
                    ToolAlert.showToast(context, "删除成功");
                    finish();
                } else {
                    ToolAlert.showToast(context, GsonUtils.getError(json));
                }
            }

            @Override
            public void requestError(String message) {
                ToolAlert.showToast(context, message);
            }
        });
    }

    private void getTextValue() {
        name = et_consignee_name.getText().toString();
        phone = et_consignee_phone.getText().toString();
        region = tv_region.getText().toString();
        detailed_address = et_detailed_address.getText().toString();
    }


    private void saveAddress() {
        if (!ToolString.isEmpty(name)) {
            ToolAlert.showToast(context, "收货人姓名不能为空");
            return;
        }
        if (!ToolString.isEmpty(phone)) {
            ToolAlert.showToast(context, "收货人手机号不能为空");
            return;
        }
        if (info == null) {
            if (provinceInfo == null) {
                ToolAlert.showToast(context, "请选择区域");
                return;
            }
        }
        if (!ToolString.isEmpty(detailed_address)) {
            ToolAlert.showToast(context, "详细地址不能为空");
            return;
        }
        submit();
    }

    private void submit() {
        if (info != null) {
            params.put("id", info.id);
        }
        params.put("receiverName", name);
        params.put("receiverAddr", detailed_address);
        params.put("areaId",addressId);
        params.put("areaName", region);
        params.put("phone", phone);
        params.put("isDefaul", cb_default_address.isChecked() ? "0" : "1");
        http.post(url, params, "正在保存...", new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.e("保存地址", json);
                if (GsonUtils.isShopSuccess(json)) {
                    finish();
                } else {
                    ToolAlert.showToast(context, GsonUtils.getError(json));
                }
            }

            @Override
            public void requestError(String message) {
                ToolAlert.showToast(context, message);
            }
        });
    }

    private void initBroadcastReceiver(){
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CREATE_ADDRESS_ACTIVITY);
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    areaInfo = (CityInfo) intent.getSerializableExtra("areaInfo");
                    provinceInfo = (CityInfo) intent.getSerializableExtra("provinceInfo");
                    cityInfo = (CityInfo) intent.getSerializableExtra("cityInfo");
                    tv_region.setText(provinceInfo.areaName + " " + cityInfo.areaName + " " + areaInfo.areaName);
                    addressId = areaInfo.id;
                }
            }
        };
        localBroadcastManager.registerReceiver(br, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(br);
    }

}
