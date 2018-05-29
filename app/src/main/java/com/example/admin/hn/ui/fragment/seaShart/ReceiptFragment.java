package com.example.admin.hn.ui.fragment.seaShart;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.RelativeLayout;

import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseFragment;
import com.example.admin.hn.http.Constant;
import com.example.admin.hn.model.ApplyedInfo;

import com.example.admin.hn.ui.adapter.ReceiptAdapter;
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
 * @describe 回执
 */
public class ReceiptFragment extends BaseFragment implements OnRefreshListener,OnLoadmoreListener {

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
    private ArrayList<ApplyedInfo> list = new ArrayList<>();
    private ReceiptAdapter adapter;
    private View view;
    private int page = 1;
    private int rows = 10;
    private String url = Api.BASE_URL + Api.GET_RECEIVE_ORDER;

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
        ToolRefreshView.setRefreshLayout(activity, refreshLayout, this, this);
        adapter = new ReceiptAdapter(activity, R.layout.item_receipt_layout, list);
        recycleView.setLayoutManager(new LinearLayoutManager(activity));
        recycleView.addItemDecoration(new SpaceItemDecoration(10,20,0,0));
        recycleView.setAdapter(adapter);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint() && isFirstHttp) {
            isFirstHttp = false;
            sendHttp();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser  && isFirstHttp) {
            isFirstHttp = false;
            isRefresh = true;
            sendHttp();
        }
    }

    @Override
    public void initData() {
    }


    @Override
    public void initTitleBar() {
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
        params.put("page", page+"");
        params.put("rows", rows+"");
        http.postJson(url, params, progressTitle, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.e("领用单列表", json);
                progressTitle = null;
                if (GsonUtils.isSuccess(json)) {
                    TypeToken typeToken=new TypeToken<List<ApplyedInfo>>(){};
                    List<ApplyedInfo> receives = (List<ApplyedInfo>) GsonUtils.jsonToList(json, typeToken, "receives");
                    if (ToolString.isEmptyList(receives)) {
                        if (isRefresh) {
                            list.clear();
                        }
                        list.addAll(receives);
                    }
                }else {
                    ToolAlert.showToast(activity, GsonUtils.getError(json));
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
        isRefresh = true;
        page = 1;
        sendHttp();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==1001){
            //从详情页面返回
            isRefresh = true;
            sendHttp();
        }
    }

}
