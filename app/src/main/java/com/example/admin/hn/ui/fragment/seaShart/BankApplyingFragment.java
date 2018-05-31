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
import com.example.admin.hn.model.ApplyingInfo;
import com.example.admin.hn.ui.adapter.BankApplyingAdapter;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author duantao
 * @date on 2017/7/26 16:04
 * @describe 申请中
 */
public class BankApplyingFragment extends BaseFragment implements OnRefreshListener,OnLoadmoreListener{

    private static final String TAG = "BankApplyingFragment";

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

    private ArrayList<ApplyingInfo> list = new ArrayList<>();
    private BankApplyingAdapter adapter;
    private View view;
    private int page=1, rows = 10;
    private String url = Api.BASE_URL + Api.GET_VERIFY_APPLY_ORDER;
    private String name;//搜索条件
    private String endDate;
    private String startDate;
    private int type;
    private String status;

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
        adapter = new BankApplyingAdapter(activity, R.layout.item_bank_applying_layout, list);
        recycleView.setLayoutManager(new LinearLayoutManager(activity));
        recycleView.addItemDecoration(new SpaceItemDecoration(10,20,0,0));
        recycleView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void initData() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint() && http != null && isFirstHttp) {
            isFirstHttp = false;
            sendHttp();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && http != null && isFirstHttp) {
            isFirstHttp = false;
            isRefresh = true;
            sendHttp();
        }
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


    public void sendHttp() {
        Map map = new HashMap();
        map.put("page", page);
        map.put("rows", rows+"");
        if (ToolString.isEmpty(startDate)) {
            map.put("startDate", startDate+"");
            map.put("endDate", endDate+"");
        }
        if (ToolString.isEmpty(name)) {
            map.put("shipId", name+"");
        }
        if (ToolString.isEmpty(status)) {
            map.put("status", status+"");
        }
        http.postJson(url, map, progressTitle, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.e("审核管理-申请单", json);
                if (GsonUtils.isSuccess(json)) {
                    TypeToken typeToken=new TypeToken<List<ApplyingInfo>>(){};
                    List<ApplyingInfo> applys = (List<ApplyingInfo>) GsonUtils.jsonToList(json, typeToken, "applys");
                    if (isRefresh) {
                        list.clear();
                    }
                    if (ToolString.isEmptyList(applys)) {
                        list.addAll(applys);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constant.POP_SHIP_AUDITING && data != null) {
            //从搜索页面返回
            name = data.getStringExtra("name");
            status = data.getStringExtra("status");
            startDate = data.getStringExtra("start");
            endDate = data.getStringExtra("end");
            isRefresh = true;
            sendHttp();
        }else if (resultCode==1000){
            //从详情页面返回
            isRefresh = true;
            sendHttp();
        }
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
