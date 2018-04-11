package com.example.admin.hn.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.admin.hn.MainActivity;
import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.base.HNApplication;
import com.example.admin.hn.http.OkHttpUtil;
import com.example.admin.hn.model.InventoryInfo;
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
*  @date on 2017/8/2 10:50
*  @author duantao
*  @describe 未过期库存详细页面
*/
public class InventoryActivity extends BaseActivity {

    private static String TAG = "InventoryActivity";

    @Bind(R.id.text_title_back)
    TextView mTextTitleBack;
    @Bind(R.id.text_title)
    TextView mTextTitle;
    @Bind(R.id.btn_confirm)
    Button mBtconfirm;
    @Bind(R.id.tv_order_rate)
    TextView tv_order_rate;
    @Bind(R.id.tv_shipname)
    TextView tv_shipname;
    @Bind(R.id.tv_order_rate1)
    TextView tv_order_rate1;
    @Bind(R.id.tv_order_rat2e)
    TextView tv_order_rat2e;
    @Bind(R.id.tv_endtime)
    TextView tv_endtime;
    @Bind(R.id.tv_left_time)
    TextView tv_left_time;
    @Bind(R.id.tv_left_price)
    TextView tv_left_price;


    private String number;
    private String shipname;
    private String url_inventory = Api.BASE_URL+Api.INVENTORY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        ButterKnife.bind(this);
        initTitleBar();
        initView();

    }

    @OnClick({R.id.text_title_back,R.id.btn_confirm})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_title_back:
                finish();
                break;
            case R.id.btn_confirm:
                finish();
                break;
        }
    }



    @Override
    public void initView() {
        super.initView();
        Intent intent= getIntent();
        number = intent.getStringExtra("number");
        shipname= intent.getStringExtra("shipname");
        Map map = new HashMap();
        map.put("ordernumber", number);
        map.put("starttime", "");
        map.put("endtime", "");
        map.put("shipnumber", "");
        map.put("userid", HNApplication.mApp.getUserId());
        map.put("shipname", "");
        map.put("status", "4");
        map.put("page", "1");
        String jsonStr = GsonUtils.mapToJson(map);
        Logger.i(TAG, jsonStr);
        try {
            OkHttpUtil.postJsonData2Server(InventoryActivity.this,
                    url_inventory,
                    jsonStr,
                    new OkHttpUtil.MyCallBack() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            ToolAlert.showToast(InventoryActivity.this, "服务器异常,请稍后再试", false);

                        }

                        @Override
                        public void onResponse(String json) {
                            Logger.i(TAG, json);
                            InventoryInfo inventoryInfo = GsonUtils.jsonToBean(
                                    json, InventoryInfo.class
                            );

                            tv_order_rate.setText(number);
                            tv_shipname.setText(shipname);
                            tv_order_rate1.setText(inventoryInfo.getDocuments().get(0).getProductNumber());
//                            tv_order_rat2e.setText(inventoryInfo.getDocuments().get(0).getEffstartTime());
//                            tv_endtime.setText(inventoryInfo.getDocuments().get(0).getEffEndTime());
//                            tv_left_time.setText(inventoryInfo.getDocuments().get(0).getInLibraryTime());
//                            tv_left_price.setText(inventoryInfo.getDocuments().get(0).getMoney());


                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void initTitleBar() {
        super.initTitleBar();
        mTextTitleBack.setBackgroundResource(R.drawable.btn_back);
        mTextTitle.setText(R.string.title_inventory);
    }



}
