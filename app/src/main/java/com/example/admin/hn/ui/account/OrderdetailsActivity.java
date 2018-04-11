package com.example.admin.hn.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.hn.MainActivity;
import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.base.HNApplication;
import com.example.admin.hn.http.OkHttpUtil;
import com.example.admin.hn.model.OrderdeatilsInfo;
import com.example.admin.hn.ui.adapter.OneExpandAdapter;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 订单明细
 */
public class OrderdetailsActivity extends BaseActivity {

    private static String TAG = "OrderdetailsActivity";

    @Bind(R.id.text_title_back)
    TextView mTextTitleBack;
    @Bind(R.id.text_title)
    TextView mTextTitle;
    @Bind(R.id.text_tile_right)
    TextView mTextright;
    @Bind(R.id.lv_products)
    ListView lvProduct;

    private String ordernumber;
    private String url_orderdetail = Api.BASE_URL+Api.ORDERDETAIL;
    private ArrayList<HashMap<String, String>> datas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetails);
        ButterKnife.bind(this);
        initTitleBar();
        initData();
        data();
    }




    @OnClick(R.id.text_title_back)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_title_back:
                finish();
                break;
        }
    }

    @Override
    public void initData() {

//        //下拉刷新
//        final RefreshLayout refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
//        refreshLayout.setDisableContentWhenLoading(true);
//        refreshLayout.setDisableContentWhenRefresh(true);
//        refreshLayout.setEnableScrollContentWhenLoaded(true);
//        //设置 Header 为 Material风格
//        refreshLayout.setRefreshHeader(new MaterialHeader(this).setShowBezierWave(true));
//        //设置 Footer 为 球脉冲
//        refreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
//        //监听
//        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(RefreshLayout refreshlayout) {
//                refreshlayout.finishRefresh(1000);
//            }
//        });
//        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
//            @Override
//            public void onLoadmore(RefreshLayout refreshlayout) {
//
//                refreshlayout.finishLoadmore(1000);
//            }
//        });
    }


    @Override
    public void initTitleBar() {
        super.initTitleBar();
        mTextTitleBack.setBackgroundResource(R.drawable.btn_back);
        mTextTitle.setText("订单明细");
    }


    private void data() {
        Intent intent = getIntent();
        ordernumber = intent.getStringExtra("ordernumber");
        Map map = new HashMap();
        map.put("ordernumber", ordernumber);
        map.put("Userid", HNApplication.mApp.getUserId());
        String jsonStr = GsonUtils.mapToJson(map);
        Logger.i(TAG, jsonStr);
        try {
            OkHttpUtil.postJsonData2Server(OrderdetailsActivity.this,
                    url_orderdetail,
                    jsonStr,
                    new OkHttpUtil.MyCallBack() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            ToolAlert.showToast(OrderdetailsActivity.this, "网络连接错误,请稍后再试", false);
                        }

                        @Override
                        public void onResponse(String json) {
                            Logger.i(TAG, json);

                            OrderdeatilsInfo orderInfo = GsonUtils.jsonToBean(
                                    json, OrderdeatilsInfo.class
                            );
                            if (("success").equals(orderInfo.getStatus())) {
                                datas = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < orderInfo.getCharts().size(); i++) {
                                    HashMap<String, String> item = new HashMap<String, String>();
                                    item.put("cycle", orderInfo.getCharts().get(i).getCycle());
                                    item.put("Number", orderInfo.getCharts().get(i).getNumber());
                                    item.put("chartTitle", orderInfo.getCharts().get(i).getChartTitle());
                                    item.put("Price", orderInfo.getCharts().get(i).getPrice());
                                    item.put("dataNumber", orderInfo.getCharts().get(i).getDataNumber());
                                    item.put("version", orderInfo.getCharts().get(i).getVersion());
                                    datas.add(item);
                                }
                                OneExpandAdapter adapter = new OneExpandAdapter(OrderdetailsActivity.this,R.layout.item_2, datas);
                                lvProduct.setAdapter(adapter);
                            } else {
                                ToolAlert.showToast(OrderdetailsActivity.this, orderInfo.getMessage(), false);
                                finish();
                            }
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
