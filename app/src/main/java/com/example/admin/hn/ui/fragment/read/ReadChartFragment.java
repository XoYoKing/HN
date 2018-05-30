package com.example.admin.hn.ui.fragment.read;

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
import com.example.admin.hn.model.ArticleInfo;
import com.example.admin.hn.model.LibraryTypeInfo;
import com.example.admin.hn.ui.adapter.SeaMapAdapter;

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
 * @describe 海图资料目录
 */
public class ReadChartFragment extends BaseFragment implements OnRefreshListener, OnLoadmoreListener {

    private static final String TAG = "ReadDrawFragment";
    @Bind(R.id.recycleView)
    RecyclerView recycleView;
    @Bind(R.id.network_disabled)
    RelativeLayout network;
    @Bind(R.id.network_img)
    ImageView network_img;
    @Bind(R.id.noData_img)
    ImageView noData_img;

    private ArrayList<ArticleInfo> list = new ArrayList<>();
    private SeaMapAdapter adapter;
    private View view;
    private int page, rows = 10, totalPage;
    private String url = Api.SHOP_BASE_URL + Api.GET_PUB_LIST;
    private RefreshLayout refreshLayout;
    private String name;
    private LibraryTypeInfo typeInfo;
    private String type;

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
        adapter = new SeaMapAdapter(getActivity(), R.layout.item_sea_map_layout, list);
        recycleView.setFocusable(false);
        recycleView.addItemDecoration(new SpaceItemDecoration(10,20,0,0));
        recycleView.setLayoutManager(new LinearLayoutManager(activity));
        recycleView.setAdapter(adapter);
    }

    @Override
    public void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            typeInfo = (LibraryTypeInfo) bundle.getSerializable("obj");
            type = bundle.getString("type");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint() && isFirstHttp && http != null) {
            isFirstHttp = false;
            sendHttp();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isFirstHttp && http != null) {
            isFirstHttp = false;
            sendHttp();
        }
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
        params.put("categoryId", typeInfo.id+"");
        params.put("rows", rows+"");
        params.put("page", page + "");
        if (ToolString.isEmpty(name)) {
            params.put("pubTitle_like", name);
        }else {
            params.remove("pubTitle_like");
        }
        http.get(url, params, progressTitle, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.e("文库列表", json);
                if (GsonUtils.isShopSuccess(json)) {
                    TypeToken typeToken = new TypeToken<List<ArticleInfo>>() {
                    };
                    List<ArticleInfo> data = (List<ArticleInfo>) GsonUtils.jsonToList2(json, typeToken, "content");
                    totalPage = GsonUtils.getTotalPage(json);
                    if (isRefresh) {
                        list.clear();
                    }
                    if (ToolString.isEmptyList(data)) {
                        list.addAll(data);
                    }
                } else {
                    ToolAlert.showToast(activity, GsonUtils.getError(json));
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constant.POP_LIB_TYPE && data != null) {
            isRefresh = true;
            name = data.getStringExtra("name");
            String currentItem = data.getIntExtra("currentItem", 0)+"";
            if (currentItem.equals(type)) {
                //发送请求
                sendHttp();
            }
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 0;
        isRefresh = true;
        sendHttp();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        page = page + 1;
        isRefresh = false;
        if (ToolRefreshView.isLoadMore(refreshlayout, page, totalPage)) {
            sendHttp();
        }
    }
}
