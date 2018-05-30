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
import com.example.admin.hn.model.OrderNotUseInfo;
import com.example.admin.hn.model.OrderNotUseSubmit;
import com.example.admin.hn.model.SubmitListInfo;
import com.example.admin.hn.ui.adapter.MaterialNotSelectAdapter;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author duantao
 * @date on 2017/7/26 16:04
 * @describe 未选择 新品推荐
 */
public class MaterialNewManagerFragment extends BaseFragment implements OnRefreshListener,OnLoadmoreListener{

    private static final String TAG = "新品推荐";

    @Bind(R.id.recycleView)
    RecyclerView recycleView;
    @Bind(R.id.network_disabled)
    RelativeLayout network;
    @Bind(R.id.network_img)
    ImageView network_img;
    @Bind(R.id.noData_img)
    ImageView noData_img;

    private ArrayList<OrderNotUseInfo> list = new ArrayList<>();
    private MaterialNotSelectAdapter adapter;
    private View view;
    private String code;//资料编号
    private String cname;//中文名称
    private int page = 1, rows = 10;
    private int type;
    private String url = Api.BASE_URL + Api.GET_DOCUMENTS;
    private String submit_url = Api.BASE_URL + Api.SUBMIT_DOCUMENTS;
    private RefreshLayout refreshLayout;
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver br;
    private List<OrderNotUseSubmit> submits = new ArrayList<>();

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
        adapter = new MaterialNotSelectAdapter(activity, R.layout.item_not_material_layout, list);
        recycleView.setLayoutManager(new LinearLayoutManager(activity));
        recycleView.addItemDecoration(new SpaceItemDecoration(10,20,0,0));
        recycleView.setAdapter(adapter);
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
    public void initData() {
        initBroadcastReceiver();
    }

    private void sendHttp() {
        Map params = new HashMap();
        if (ToolString.isEmpty(code)) {
            params.put("code", code);
        }
        if (ToolString.isEmpty(cname)) {
            params.put("cname", cname);
        }
        params.put("page", page);
        params.put("rows", rows+"");
        params.put("newbooksign", "Y");
        http.postJson(url, params, progressTitle, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                progressTitle = null;
                Logger.i("新品推荐", json);
                if (GsonUtils.isSuccess(json)) {
                    TypeToken typeToken = new TypeToken<List<OrderNotUseInfo>>() {
                    };
                    List<OrderNotUseInfo> data = (List<OrderNotUseInfo>) GsonUtils.jsonToList(json, typeToken);
                    if (isRefresh) {
                        list.clear();
                    }
                    if (ToolString.isEmptyList(data)) {
                        list.addAll(data);
                    }
                }else {
                    if (page != 1) {
                        ToolAlert.showToast(getActivity(),Constant.LOADED);
                    }
                }
                ToolRefreshView.hintView(adapter,refreshLayout,false,network,noData_img,network_img);
            }
            @Override
            public void requestError(String message) {
                ToolAlert.showToast(activity,message,false);
                ToolRefreshView.hintView(adapter,refreshLayout,true,network,noData_img,network_img);
            }
        });
    }


    @Override
    public void initTitleBar() {
        Bundle bundle = getArguments();
        type = Integer.parseInt(bundle.getString("type"));
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constant.POP_NEW_MATERIAL && data != null) {
            code = data.getStringExtra("dataNumber");
            cname = data.getStringExtra("chineseName");
            isRefresh = true;
            //发送请求
            sendHttp();
        }
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        isRefresh = false;
        page = page + 1;
        sendHttp();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        isRefresh = true;
        page = 1;
        sendHttp();
    }


    private void initBroadcastReceiver(){
        localBroadcastManager = LocalBroadcastManager.getInstance(activity);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_MATERIAL_NEW_MANAGER_FRAGMENT);
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

    //提交数据
    private void submit() {
        if (submits.size() > 0) {
            submits.clear();
        }
        final List<OrderNotUseInfo> selectList = adapter.getSelectList();
        if (ToolString.isEmptyList(selectList)) {
            for (int i = 0; i < selectList.size(); i++) {
                OrderNotUseInfo notUseInfo = selectList.get(i);
                OrderNotUseSubmit useSubmit = new OrderNotUseSubmit(notUseInfo.quantity,notUseInfo.id, notUseInfo.code, notUseInfo.publis_at, MainActivity.list.get(0).shipid
                );
                submits.add(useSubmit);
            }
            if (!ToolString.isEmptyList(selectList)) {
                ToolAlert.showToast(activity, "请选择需要提交的资料", false);
                return;
            }
            SubmitListInfo submitListInfo = new SubmitListInfo(HNApplication.mApp.getUserId(), submits);
            http.postJson(submit_url, submitListInfo, "提交中...", new RequestListener() {
                @Override
                public void requestSuccess(String json) {
                    Logger.e("待选提交结果",json);
                    if (GsonUtils.isSuccess(json)) {
                        selectList.clear();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(br);
    }
}
