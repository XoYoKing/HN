package com.example.admin.hn.ui.fragment.seaShart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.admin.hn.MainActivity;
import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseFragment;
import com.example.admin.hn.base.HNApplication;
import com.example.admin.hn.http.Constant;
import com.example.admin.hn.model.OrderInfo;
import com.example.admin.hn.model.OrderNotUseInfo;
import com.example.admin.hn.model.OrderNotUseSubmit;
import com.example.admin.hn.model.OrderUseInfo;
import com.example.admin.hn.model.OrderUseSubmit;
import com.example.admin.hn.model.SubmitListInfo;
import com.example.admin.hn.model.SubmitSelect;
import com.example.admin.hn.model.SubmitSelectInfo;
import com.example.admin.hn.ui.adapter.MaterialSelectAdapter;
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
 * @author duantao
 * @date on 2017/7/26 16:04
 * @describe 已选择
 */
public class MaterialManagerFragment extends BaseFragment implements OnRefreshListener, OnLoadmoreListener {

    private static final String TAG = "已选择";

    @Bind(R.id.recycleView)
    RecyclerView recycleView;
    @Bind(R.id.network_disabled)
    RelativeLayout network;
    @Bind(R.id.network_img)
    ImageView network_img;
    @Bind(R.id.noData_img)
    ImageView noData_img;

    private ArrayList<OrderUseInfo> list = new ArrayList<>();
    private MaterialSelectAdapter adapter;
    private View view;
    private int page = 1;
    private int screen = 1;
    private String url = Api.BASE_URL + Api.GET_SUBMITTED_DOCUMENTS;
    private RefreshLayout refreshLayout;
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver br;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recycle_layout, container, false);
        ButterKnife.bind(this, view);
        initTitleBar();
        initView();
        initData();
        return view;
    }


    @Override
    public void initView() {
        //下拉刷新
        refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        ToolRefreshView.setRefreshLayout(activity, refreshLayout, this, this);
        adapter = new MaterialSelectAdapter(activity, R.layout.item_material_layout, list);
        recycleView.setLayoutManager(new LinearLayoutManager(activity));
        recycleView.addItemDecoration(new SpaceItemDecoration(10, 20, 0, 0));
        recycleView.setAdapter(adapter);
    }


    @Override
    public void initData() {
        initBroadcastReceiver();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint() && screen == 3) {
            sendHttp();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && screen == 3) {
            isRefresh = true;
            sendHttp();
        }
    }

    @Override
    public void initTitleBar() {
        Bundle bundle = getArguments();
        screen = Integer.parseInt(bundle.getString("type"));
    }


    @OnClick({R.id.network_img})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.network_img:
                network_img.setVisibility(View.GONE);
                sendHttp();
                break;
        }
    }


    public void sendHttp() {
        params.put("page", page);
//        params.put("shipId", MainActivity.list.get(0).getShipid() + "");
        params.put("shipId", "7340");
        http.postJson(url, params, progressTitle, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                progressTitle = null;
                Logger.e("已选列表", json);
                if (GsonUtils.isSuccess(json)) {
                    TypeToken typeToken = new TypeToken<List<OrderUseInfo>>() {
                    };
                    List<OrderUseInfo> data = (List<OrderUseInfo>) GsonUtils.jsonToList(json, typeToken);
                    if (isRefresh) {
                        list.clear();
                    }
                    if (ToolString.isEmptyList(data)) {
                        list.addAll(data);
                    }
                } else {
                    if (page != 1) {
                        ToolAlert.showToast(getActivity(), Constant.LOADED);
                    }
                }
                ToolRefreshView.hintView(adapter, refreshLayout, false, network, noData_img, network_img);
            }

            @Override
            public void requestError(String message) {
                ToolAlert.showToast(activity, message);
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

    private void initBroadcastReceiver() {
        localBroadcastManager = LocalBroadcastManager.getInstance(activity);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_MATERIAL_MANAGER_FRAGMENT);
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    submit();
                }
            }
        };
        localBroadcastManager.registerReceiver(br, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(br);
    }

    private List<SubmitSelect> submits=new ArrayList<>();
    private String submit_url = Api.BASE_URL + Api.SUBMIT_APPLY_ORDER;
    private void submit() {
        if (submits.size() > 0) {
            submits.clear();
        }
        List<OrderUseInfo> selectList = adapter.getDatas();
        if (ToolString.isEmptyList(selectList)) {
            for (int i = 0; i < selectList.size(); i++) {
                OrderUseInfo useInfo = selectList.get(i);
                SubmitSelect useSubmit = new SubmitSelect(useInfo.quantity, useInfo.id, useInfo.distribute_no);
                submits.add(useSubmit);
            }
            if (!ToolString.isEmptyList(selectList)) {
                ToolAlert.showToast(activity, "请选择需要提交的资料", false);
                return;
            }
            SubmitSelectInfo submitListInfo = new SubmitSelectInfo(HNApplication.mApp.getUserId(), "7340", submits);
            http.postJson(submit_url, submitListInfo, "提交中...", new RequestListener() {
                @Override
                public void requestSuccess(String json) {
                    Logger.e("已选提交结果", json);
                    if (GsonUtils.isSuccess(json)) {
                        //刷新列表
                        sendHttp();
                    }
                    ToolAlert.showToast(activity, GsonUtils.getError(json));
                }

                @Override
                public void requestError(String message) {
                    ToolAlert.showToast(activity, message);
                }
            });
        }else {
            ToolAlert.showToast(activity, "请选择资料！");
        }
    }
}
