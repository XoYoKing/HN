package com.example.admin.hn.ui.fragment.seaShart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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
import com.example.admin.hn.http.Constant;
import com.example.admin.hn.model.OrderInfo;
import com.example.admin.hn.model.OrderUseInfo;
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
import com.zhy.adapter.recyclerview.CommonAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author duantao
 * @date on 2017/7/26 16:04
 * @describe 已选择
 */
public class MaterialManagerFragment extends BaseFragment implements OnRefreshListener,OnLoadmoreListener{

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
    private boolean isRefresh = true;
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
        recycleView.addItemDecoration(new SpaceItemDecoration(10,20,0,0));
        recycleView.setAdapter(adapter);
    }


    @Override
    public void initData() {
        initBroadcastReceiver();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && screen == 2) {
            sendHttp();
        }
    }

    @Override
    public void initTitleBar() {
        Bundle bundle = getArguments();
        screen = Integer.parseInt(bundle.getString("type"));
    }


    @OnClick({ R.id.network_img})
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
        params.put("shipId", MainActivity.list.get(0).getShipid() + "");
        http.postJson(url, params, progressTitle, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                progressTitle = null;
                Logger.e("已选列表", json);
                if (GsonUtils.isSuccess(json)) {
                    TypeToken typeToken= new TypeToken<List<OrderUseInfo>>() {
                    };
                    List<OrderUseInfo> data = (List<OrderUseInfo>) GsonUtils.jsonToList(json, typeToken);
                    if (ToolString.isEmptyList(data)) {
                        if (isRefresh) {
                            list.clear();
                        }
                        list.addAll(data);
                    }
                } else {
                    if (page != 1) {
                        ToolAlert.showToast(getActivity(),Constant.LOADED);
                    }
                }
                ToolRefreshView.hintView(adapter,refreshLayout,false,network,noData_img,network_img);
            }

            @Override
            public void requestError(String message) {
                ToolAlert.showToast(activity, message);
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

    private void initBroadcastReceiver(){
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

    private void submit() {
        ToolAlert.showToast(activity, "已选-提交", false);
        Logger.e("size", adapter.getDatas().size()+ "");
    }
}
