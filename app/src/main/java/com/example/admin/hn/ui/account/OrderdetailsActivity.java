package com.example.admin.hn.ui.account;

import android.content.Context;
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
import com.example.admin.hn.volley.RequestListener;
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
    private OneExpandAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetails);
        ButterKnife.bind(this);
        initTitleBar();
        initData();
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
        adapter = new OneExpandAdapter(OrderdetailsActivity.this, R.layout.item_2, datas);
        lvProduct.setAdapter(adapter);
        data();
    }


    @Override
    public void initTitleBar() {
        super.initTitleBar();
        mTextTitleBack.setBackgroundResource(R.drawable.btn_back);
        mTextTitle.setText("订单明细");
    }

    /**
     *
     * @param context
     */
    public static void startActivity(Context context,String ordernumber) {
        Intent intent = new Intent(context, OrderdetailsActivity.class);
        intent.putExtra("ordernumber", ordernumber);
        context.startActivity(intent);
    }

    private void data() {
        Intent intent = getIntent();
        ordernumber = intent.getStringExtra("ordernumber");
        Map map = new HashMap();
        map.put("ordernumber", ordernumber);
        map.put("Userid", HNApplication.mApp.getUserId());
        http.postJson(url_orderdetail, map, progressTitle, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.i(TAG, json);
                if (GsonUtils.isSuccess(json)) {
                    OrderdeatilsInfo orderInfo = GsonUtils.jsonToBean(
                            json, OrderdeatilsInfo.class
                    );
                    datas = new ArrayList<>();
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
                    adapter.notifyDataSetChanged();
                } else {
                    ToolAlert.showToast(context, GsonUtils.getError(json));
                    finish();
                }
            }

            @Override
            public void requestError(String message) {
                ToolAlert.showToast(context, message);
            }
        });
    }

}
