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

import com.example.admin.hn.MainActivity;
import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.base.HNApplication;
import com.example.admin.hn.model.ApplyingDetailInfo;
import com.example.admin.hn.model.ApplyingInfo;
import com.example.admin.hn.model.OrderInfo;
import com.example.admin.hn.model.OrderNotUseInfo;
import com.example.admin.hn.model.OrderNotUseSubmit;
import com.example.admin.hn.model.SubmitListInfo;
import com.example.admin.hn.ui.adapter.ShipApplyingAdapter;
import com.example.admin.hn.ui.adapter.ShipApplyingDetailAdapter;
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
 * 申请中详情列表
 *
 * @author Administrator
 */
public class ShipApplyingActivity extends BaseActivity implements OnRefreshListener,OnLoadmoreListener {
    @Bind(R.id.text_title_back)
    TextView textTitleBack;
    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.text_tile_right)
    TextView text_tile_right;
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
    private ShipApplyingDetailAdapter adapter;
    private List<OrderNotUseSubmit> submits = new ArrayList<>();
    private ArrayList<ApplyingDetailInfo> list = new ArrayList<>();
    private String url = Api.BASE_URL + Api.GET_APPLY_ORDER_DETAIL;

    private String submit_url = Api.BASE_URL + Api.SUBMIT_DOCUMENTS;
    private int page = 1;
    private String applyno;
    private String status;//船舶用户传递的申请单状态

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
        applyno = intent.getStringExtra("applyno");
        status = intent.getStringExtra("status");
        textTitle.setText("申请详情");
        textTitleBack.setBackgroundResource(R.drawable.btn_back);
        if ("退回".equals(status)) {
            text_tile_right.setText("复制订单");
        }
    }

    @Override
    public void initView() {
        //下拉刷新
        ToolRefreshView.setRefreshLayout(this, refreshLayout, this, this);
        recycleView.addItemDecoration(new SpaceItemDecoration(10, 20, 0, 0));
        recycleView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ShipApplyingDetailAdapter(this, R.layout.item_ship_applying_adapter, list);
        recycleView.setAdapter(adapter);
    }

    /**
     *
     */
    public static void startActivity(Context context, String applyno) {
        Intent intent = new Intent(context, ShipApplyingActivity.class);
        intent.putExtra("applyno", applyno);
        context.startActivity(intent);
    }
    /**
     *
     */
    public static void startActivity(Context context, String applyno,String status) {
        Intent intent = new Intent(context, ShipApplyingActivity.class);
        intent.putExtra("applyno", applyno);
        intent.putExtra("status", status);
        context.startActivity(intent);
    }

    @OnClick({R.id.text_title_back,R.id.network_img,R.id.text_tile_right})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.network_img:
                network_img.setVisibility(View.GONE);
                sendHttp();
                break;
            case R.id.text_title_back:
                finish();
                break;
            case R.id.text_tile_right:
                submit();
                break;
        }
    }

    //提交数据
    private void submit() {
        if (submits.size() > 0) {
            submits.clear();
        }
        if (ToolString.isEmptyList(list)) {
            for (int i = 0; i < list.size(); i++) {
                ApplyingDetailInfo info = list.get(i);
                OrderNotUseSubmit useSubmit = new OrderNotUseSubmit(info.quantity,info.id, info.code, info.publishat, MainActivity.list.get(0).shipid
                );
                submits.add(useSubmit);
            }
            SubmitListInfo submitListInfo = new SubmitListInfo(HNApplication.mApp.getUserId(), submits);
            http.postJson(submit_url, submitListInfo, "提交中...", new RequestListener() {
                @Override
                public void requestSuccess(String json) {
                    Logger.e("申请单提交结果",json);
                    if (GsonUtils.isSuccess(json)) {
                        ToolAlert.showToast(context,"您的订单已重新提交到已选列表，请重新申请！");
                        finish();
                    }else {
                        ToolAlert.showToast(context, GsonUtils.getError(json));
                    }
                }

                @Override
                public void requestError(String message) {
                    ToolAlert.showToast(context, message);
                }
            });
        }else {
            ToolAlert.showToast(context, "资料列表为空");
        }
    }

    @Override
    public void initData() {
        super.initData();
        sendHttp();
    }


    public void sendHttp() {
        params.put("page", page + "");
        params.put("applyNo", applyno);
        http.postJson(url, params, progressTitle, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.e("申请单", json);
                progressTitle = null;
                if (GsonUtils.isSuccess(json)) {
                    TypeToken typeToken=new TypeToken<List<ApplyingDetailInfo>>(){};
                    List<ApplyingDetailInfo> applys = (List<ApplyingDetailInfo>) GsonUtils.jsonToList(json, typeToken, "applysDetail");
                    if (ToolString.isEmptyList(applys)) {
                        if (isRefresh) {
                            list.clear();
                        }
                        list.addAll(applys);
                    }
                }else {
                    ToolAlert.showToast(context, GsonUtils.getError(json));
                }
                ToolRefreshView.hintView(adapter,refreshLayout,false,network,noData_img,network_img);
            }

            @Override
            public void requestError(String message) {
                ToolAlert.showToast(context, message);
                ToolRefreshView.hintView(adapter,refreshLayout,true,network,noData_img,network_img);
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
