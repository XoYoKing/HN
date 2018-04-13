package com.example.admin.hn.ui.fragment.seaShart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.admin.hn.MainActivity;
import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseFragment;
import com.example.admin.hn.base.HNApplication;
import com.example.admin.hn.http.Constant;
import com.example.admin.hn.http.OkHttpUtil;
import com.example.admin.hn.model.OrderInfo;
import com.example.admin.hn.model.ShipInfo;
import com.example.admin.hn.ui.account.ShipActivity;
import com.example.admin.hn.ui.account.ShipApplyingActivity;
import com.example.admin.hn.ui.adapter.ShipAdapter;
import com.example.admin.hn.ui.adapter.ShipApplyingAdapter;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolRefreshView;
import com.example.admin.hn.volley.RequestListener;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.okhttp.Request;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.io.IOException;
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
 * @describe 申请中
 */
public class ShipSelectFragment extends BaseFragment implements OnRefreshListener, OnLoadmoreListener {

    private static final String TAG = "ShipApplyingFragment";

    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.network_disabled)
    RelativeLayout network;
    @Bind(R.id.network_img)
    ImageView network_img;
    @Bind(R.id.noData_img)
    ImageView noData_img;

    private List<ShipInfo.ship> listship = new ArrayList<>();
    private List<HashMap<String, Object>> list = new ArrayList<>();
    private List<ShipInfo.ship> listStr = new ArrayList<>();
    private List<String> lists = new ArrayList<>();
    private List<String> listss = new ArrayList<>();
    private ShipAdapter adapter;
    private View view;
    private int page = 1;
    private String url_ship = Api.BASE_URL + Api.SHIPINQUIRY;
    private String url_shipselection = Api.BASE_URL + Api.SHIPSELECTION;
    private RefreshLayout refreshLayout;
    private int type;//区分是否选择  0 未选择 1 已选择
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver br;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list_layout, container, false);
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
        adapter = new ShipAdapter(activity, R.layout.ship_adapter, list);
        listView.setAdapter(adapter);
    }


    @Override
    public void initData() {
        Bundle bundle = getArguments();
        type = Integer.parseInt(bundle.getString("type"));
        if (type == 0) {
            //在未选择中注册广播
            initBroadcastReceiver();
            data();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ViewHolder viewCache = (ViewHolder) view.getTag();
                    CheckBox checkBox = viewCache.getView(R.id.cb_status);
                    checkBox.toggle();
                    list.get(position).put("boolean", checkBox.isChecked());
                    adapter.notifyDataSetChanged();
                    if (checkBox.isChecked()) {//被选中状态
                        lists.add(list.get(position).get("name").toString());
                        listss.add(list.get(position).get("number").toString());
                    } else {//从选中状态转化为未选中
                        listss.remove(list.get(position).get("number").toString());
                        lists.remove(list.get(position).get("name").toString());
                    }
                }
            });
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (type == 1) {
                list.clear();
                for (int i = 0; i < MainActivity.list.size(); i++) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("name", MainActivity.list.get(i).getShipname());
                    map.put("boolean", true);//初始化为未选
                    map.put("number", MainActivity.list.get(i).getShipnumber());
                    list.add(map);
                }//初始化数据
                adapter.notifyDataSetChanged();
            }
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
                refreshLayout.finishRefresh(1000);
                if (type == 0) {
                    data();
                }
                break;
        }
    }

    private void data() {
        http.postJson(url_ship, params, progressTitle, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.i(TAG, json);
                if (GsonUtils.isSuccess(json)) {
                    ShipInfo shipInfo = GsonUtils.jsonToBean(
                            json, ShipInfo.class
                    );
                    for (int i = 0; i < shipInfo.getDocuments().size(); i++) {
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("name", shipInfo.getDocuments().get(i).getShipname());
                        map.put("boolean", false);//初始化为未选
                        map.put("number", shipInfo.getDocuments().get(i).getShipnumber());
                        list.add(map);
                    }
                }else {
                    ToolAlert.showToast(activity, GsonUtils.getError(json), false);
                }
                ToolRefreshView.hintView(adapter, false, network, noData_img, network_img);
            }
            @Override
            public void requestError(String message) {
                ToolAlert.showToast(activity,message, false);
                ToolRefreshView.hintView(adapter, true, network, noData_img, network_img);
            }
        });

    }

    private void shipSelection(ShipInfo shipInfo) {
        http.postJson(url_shipselection, shipInfo, progressTitle, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.i(TAG, json);
                if (GsonUtils.isSuccess(json)) {
                    ToolAlert.showToast(activity, "提交成功", false);
                    MainActivity.list = new ArrayList<>(listStr);
                    activity.finish();
                }else {
                    ToolAlert.showToast(activity,GsonUtils.getError(json), false);
                }
            }

            @Override
            public void requestError(String message) {
                ToolAlert.showToast(activity, message, false);
            }
        });

    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        page = page + 1;
        adapter.notifyDataSetChanged();
        refreshlayout.finishLoadmore(1000);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 1;
        refreshlayout.finishRefresh(1000);
    }

    private void initBroadcastReceiver() {
        localBroadcastManager = LocalBroadcastManager.getInstance(activity);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_SHIP_SELECT_FRAGMENT);
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    int status = intent.getIntExtra("status", 0);
                    if (status == 1) {
                        //点击提交
                        submit();
                    } else if (status == 2) {
                        //点击全选
                        allSelect();
                    } else if (status == 3) {
                        //点击取消
                        cancel();
                    }
                }
            }
        };
        localBroadcastManager.registerReceiver(br, intentFilter);
    }

    private void cancel() {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).put("boolean", false);
            adapter.notifyDataSetChanged();
            listss.remove(list.get(i).get("number").toString());
            lists.remove(list.get(i).get("name").toString());
        }//初始化数据
    }

    private void allSelect() {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).put("boolean", true);
            adapter.notifyDataSetChanged();
            lists.add(list.get(i).get("name").toString());
            listss.add(list.get(i).get("number").toString());
        }//初始化数据
    }

    private void submit() {
        for (int i = 0; i < listss.size(); i++) {
            listStr.add(new ShipInfo.ship(listss.get(i), lists.get(i)));
        }
        if (listStr.size() == 0) {
            ToolAlert.showToast(activity, "请选择船舶后再提交", false);
        } else {
            ShipInfo shipInfo = new ShipInfo(HNApplication.mApp.getUserId(), listStr);
            shipSelection(shipInfo);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (localBroadcastManager != null)
            localBroadcastManager.unregisterReceiver(br);
    }
}
