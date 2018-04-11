package com.example.admin.hn.ui.account;

import android.content.Intent;
import android.graphics.Paint;
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
import com.example.admin.hn.model.OrderInfo;
import com.example.admin.hn.utils.GsonUtils;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
*  @date on 2017/8/7 13:53
*  @author duantao
*  @describe 未提交订单详情
*/
public class NotsubmittedOrderActivity extends BaseActivity {

    private static String TAG = "NotsubmittedOrderActivity";

    @Bind(R.id.text_title_back)
    TextView mTextTitleBack;
    @Bind(R.id.tv_left_route)
    TextView mTroute;
    @Bind(R.id.text_title)
    TextView mTextTitle;
    @Bind(R.id.btn_confirm)
    Button mBtconfirm;

    private String ordernumber;
    private String url_order = Api.BASE_URL + Api.ORDER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notsubmitted_order);
        ButterKnife.bind(this);
        initView();
        initTitleBar();
        initData();
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
        mTroute.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
    }


    @Override
    public void initTitleBar() {
        super.initTitleBar();
        mTextTitleBack.setBackgroundResource(R.drawable.btn_back);
        mTextTitle.setText(R.string.title_my_order);
    }

    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        ordernumber = intent.getStringExtra("number");
        Map map = new HashMap();
        map.put("ordernumber", ordernumber);
        map.put("starttime", "");
        map.put("endtime", "");
        map.put("shipnumber", ordernumber);
        map.put("userid", HNApplication.mApp.getUserId());
        map.put("shipname", "");
        map.put("status", "4");
        map.put("page", "1");
        String jsonStr = GsonUtils.mapToJson(map);
        Logger.i(TAG, jsonStr);
        try {
            OkHttpUtil.postJsonData2Server(NotsubmittedOrderActivity.this,
                    url_order,
                    jsonStr,
                    new OkHttpUtil.MyCallBack() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                        }

                        @Override
                        public void onResponse(String json) {
                            Logger.i(TAG, json);
//                            String jsonStr = json.replaceAll( "\\\\", "");
//                            Logger.i(TAG, jsonStr);
                            OrderInfo orderInfo = GsonUtils.jsonToBean(
                                    json, OrderInfo.class
                            );
//                            tv_order_rate.setText(ordernumber);
//                            tv_order_time.setText(orderInfo.getDocuments().get(0).getOrdertime());
//                            tv_order_rat2e.setText(orderInfo.getDocuments().get(0).getMoney());
//                            tv_statu.setText(orderInfo.getDocuments().get(0).getStatus());
//                            mTroute.setText(orderInfo.getDocuments().get(0).getLinename());
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
