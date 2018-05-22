package com.example.admin.hn.ui.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.model.ApplyedDetailInfo;
import com.example.admin.hn.model.ApplyingDetailInfo;
import com.example.admin.hn.model.ApplyingInfo;
import com.example.admin.hn.model.OrderInfo;
import com.example.admin.hn.ui.adapter.ShipApplyedAdapter;
import com.example.admin.hn.ui.adapter.ShipApplyingAdapter;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.SpaceItemDecoration;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolRefreshView;
import com.example.admin.hn.utils.ToolString;
import com.example.admin.hn.volley.RequestListener;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 领用成功详情列表
 *
 * @author Administrator
 */
public class ShipApplyedActivity extends BaseActivity implements OnRefreshListener,OnLoadmoreListener {
    @Bind(R.id.text_title_back)
    TextView textTitleBack;
    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.recycleView)
    RecyclerView recycleView;
    @Bind(R.id.network_disabled)
    RelativeLayout network;
    @Bind(R.id.network_img)
    ImageView network_img;
    @Bind(R.id.noData_img)
    ImageView noData_img;
    @Bind(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    private int page = 1;
    private int rows = 10;
    private String url = Api.BASE_URL + Api.GET_RECEIVE_ORDER_DETAIL;
    private ShipApplyedAdapter adapter;
    private String receiveNo;
    private ArrayList<ApplyedDetailInfo> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_layout);
        ButterKnife.bind(this);
        initTitleBar();
        initView();
        initData();
    }

    @Override
    public void initTitleBar() {
        Intent intent = getIntent();
        receiveNo = intent.getStringExtra("receiveNo");
        textTitle.setText("领用详情");
        textTitleBack.setBackgroundResource(R.drawable.btn_back);
    }

    @Override
    public void initView() {
        //下拉刷新
        ToolRefreshView.setRefreshLayout(this, refreshLayout, this, this);
        adapter = new ShipApplyedAdapter(this, R.layout.item_ship_applyed_adapter, list);
        recycleView.addItemDecoration(new SpaceItemDecoration(10, 20, 0, 0));
        recycleView.setLayoutManager(new LinearLayoutManager(context));
        recycleView.setAdapter(adapter);
    }

    /**
     *
     */
    public static void startActivity(Context context, String receiveNo) {
        Intent intent = new Intent(context, ShipApplyedActivity.class);
        intent.putExtra("receiveNo", receiveNo);
        context.startActivity(intent);
    }

    @OnClick(R.id.text_title_back)
    public void onClick(View v) {
        if (v.getId() == R.id.text_title_back) {
            finish();
        }
    }

    @Override
    public void initData() {
        super.initData();
        sendHttp();
    }


    public void sendHttp() {
        params.put("page", page + "");
        params.put("rows", rows+"");
        params.put("receiveNo", receiveNo);
        http.postJson(url, params, progressTitle, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.e("领用单详情", json);
                progressTitle = null;
                if (GsonUtils.isSuccess(json)) {
                    TypeToken typeToken = new TypeToken<List<ApplyedDetailInfo>>() {
                    };
                    List<ApplyedDetailInfo> applys = (List<ApplyedDetailInfo>) GsonUtils.jsonToList(json, typeToken, "receiveDetail");
                    if (ToolString.isEmptyList(applys)) {
                        if (isRefresh) {
                            list.clear();
                        }
                        list.addAll(applys);
                    }
                } else {
                    ToolAlert.showToast(context, GsonUtils.getError(json));
                }
                ToolRefreshView.hintView(adapter, refreshLayout, false, network, noData_img, network_img);
            }

            @Override
            public void requestError(String message) {
                ToolAlert.showToast(context, message);
                ToolRefreshView.hintView(adapter, refreshLayout, true, network, noData_img, network_img);
            }
        });
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        page = page + 1;
        isRefresh = false;
        sendHttp();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 1;
        isRefresh = true;
        sendHttp();
    }
}