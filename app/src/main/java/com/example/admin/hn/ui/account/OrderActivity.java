package com.example.admin.hn.ui.account;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.base.HNApplication;
import com.example.admin.hn.http.OkHttpUtil;
import com.example.admin.hn.model.OrderInfo;
import com.example.admin.hn.ui.shop.FirmOrderActivity;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.volley.RequestListener;
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

/**
 * @author duantao
 * @date on 2017/8/1 14:31
 * @describe 订单详情
 */
public class OrderActivity extends BaseActivity {

    private static String TAG = "OrderActivity";
    @Bind(R.id.text_title_back)
    TextView mTextTitleBack;
    @Bind(R.id.tv_left_route)
    TextView mTroute;
    @Bind(R.id.text_title)
    TextView mTextTitle;
    @Bind(R.id.btn_confirm)
    Button mBtconfirm;
    @Bind(R.id.tv_details)
    TextView tv_details;
    @Bind(R.id.tv_status)
    TextView tv_status;
    @Bind(R.id.tv_ship)
    TextView tv_ship;
    @Bind(R.id.tv_submit)
    TextView tv_submit;

    @Bind(R.id.tv_order_rate)
    TextView tv_order_rate;
    @Bind(R.id.tv_order_time)
    TextView tv_order_time;
    @Bind(R.id.tv_order_rat2e)
    TextView tv_order_rat2e;
    @Bind(R.id.tv_statu)
    TextView tv_statu;
    @Bind(R.id.tv_order_money)
    TextView tv_order_money;

    private String money;
    private String ordernumber;
    private String shipname;
    private String ordertime;
    private String linename;
    private String status;
    private String coord;
    private String url_order = Api.BASE_URL + Api.ORDER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        initTitleBar();
        initView();
        initData();
    }


    @OnClick({R.id.text_title_back, R.id.btn_confirm, R.id.tv_left_route, R.id.tv_details})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_title_back:
                finish();
                break;
            case R.id.btn_confirm:
                finish();
                break;
            case R.id.tv_left_route:
                String url = "http://api.shipxy.com/apicall/location?k=a7222a0180264aa99245ff2b53595a31&kw="+shipname+"&tip=1&track=1";
                HtmlActivity.startActivity(OrderActivity.this,url);
                break;
            case R.id.tv_details:
                OrderdetailsActivity.startActivity(context, ordernumber);
                break;
        }
    }


    @Override
    public void initView() {
        super.initView();
        mTroute.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_details.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

    }


    /**
     *
     * @param context
     */
    public static void startActivity(Context context, String shipname,String ordernumber) {
        Intent intent = new Intent(context, OrderActivity.class);
        intent.putExtra("shipname",shipname);
        intent.putExtra("number", ordernumber);
        context.startActivity(intent);
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
        shipname = intent.getStringExtra("shipname");
        sendHttp();
    }

    private void sendHttp() {
        Map map = new HashMap();
        map.put("ordernumber", ordernumber);
        map.put("starttime", "");
        map.put("endtime", "");
        map.put("shipnumber", ordernumber);
        map.put("userid", HNApplication.mApp.getUserId());
        map.put("shipname", "");
        map.put("status", "3");
        map.put("page", "1");
        map.put("screen", "1");
        http.postJson(url_order, map, progressTitle, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.i(TAG, json);
                if (GsonUtils.isSuccess(json)) {
                    OrderInfo orderInfo = GsonUtils.jsonToBean(json, OrderInfo.class
                    );
                    coord = orderInfo.getDocuments().get(0).getCoord();
                    tv_order_rate.setText(ordernumber);
                    tv_ship.setText(shipname);
                    tv_order_time.setText(orderInfo.getDocuments().get(0).getOrdertime());
                    tv_order_rat2e.setText(orderInfo.getDocuments().get(0).getMoney());
                    tv_statu.setText(orderInfo.getDocuments().get(0).getStatus());
                    tv_submit.setText(orderInfo.getDocuments().get(0).getCname());
//                  mTroute.setText(orderInfo.getDocuments().get(0).getLinename());
                    tv_status.setText(orderInfo.getDocuments().get(0).getOrderstatus());
                    tv_order_money.setText(orderInfo.getDocuments().get(0).getCurrency());
                }else {
                    ToolAlert.showToast(context,GsonUtils.getError(json));
                    finish();
                }
            }

            @Override
            public void requestError(String message) {
                ToolAlert.showToast(context,message);
            }
        });
    }


    /**
     * 提取中括号中内容，忽略中括号中的中括号
     *
     * @param msg
     * @return
     */
    public static List<String> extractMessage(String msg) {
        List<String> list = new ArrayList<String>();
        int start = 0;
        int startFlag = 0;
        int endFlag = 0;
        for (int i = 0; i < msg.length(); i++) {
            if (msg.charAt(i) == '[') {
                startFlag++;
                if (startFlag == endFlag + 1) {
                    start = i;
                }
            } else if (msg.charAt(i) == ']') {
                endFlag++;
                if (endFlag == startFlag) {
                    list.add(msg.substring(start + 1, i));
                }
            }
        }
        return list;
    }

}
