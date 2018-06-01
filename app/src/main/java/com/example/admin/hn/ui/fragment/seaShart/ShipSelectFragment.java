package com.example.admin.hn.ui.fragment.seaShart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
import com.example.admin.hn.model.ShipInfo;
import com.example.admin.hn.model.SubmitShipInfo;
import com.example.admin.hn.ui.account.ShipSelectActivity;
import com.example.admin.hn.ui.adapter.ShipAdapter;
import com.example.admin.hn.ui.shop.ShopCartActivity;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolRefreshView;
import com.example.admin.hn.utils.ToolString;
import com.example.admin.hn.volley.RequestListener;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author duantao
 * @date on 2017/7/26 16:04
 * @describe 船舶选择
 */
public class ShipSelectFragment extends BaseFragment implements OnRefreshListener, OnLoadmoreListener {

    private static final String TAG = "ShipSelectFragment";

    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.network_disabled)
    RelativeLayout network;
    @Bind(R.id.network_img)
    ImageView network_img;
    @Bind(R.id.noData_img)
    ImageView noData_img;
    @Bind(R.id.refreshLayout)
    RefreshLayout refreshLayout;

    private List<ShipInfo.Ship> list = new ArrayList<>();
    private List<ShipInfo.Ship> list_select = new ArrayList<>();
    private List<String> list_id = new ArrayList<>();
    private ShipAdapter adapter;
    private View view;
    private int page = 1;
    private int rows = 20;
    private String url_ship = Api.BASE_URL + Api.QUERY_SHIP;
    private String url_select = Api.BASE_URL + Api.SHIPSELECTION;
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver br;
    private ShipSelectActivity activity;
    private int type;//区分是否选择  0 未选择 1 已选择
    private boolean isSingle;//区分是单选还是多选  true是单选  默认false是多选
    private boolean isClick;//区分是否可以点击选中 true可以 false不可以
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list_layout, container, false);
        ButterKnife.bind(this, view);
        activity = (ShipSelectActivity) getActivity();
        initTitleBar();
        initView();
        initData();
        return view;
    }


    @Override
    public void initView() {
        //下拉刷新
        Bundle bundle = getArguments();
        type = Integer.parseInt(bundle.getString("type"));
        ToolRefreshView.setRefreshLayout(activity, refreshLayout, this, this);
        isSingle = HNApplication.mApp.getUserType() == 1;//如果是船舶用户就是单选
        isClick = type == 0 ? true : false;
        adapter = new ShipAdapter(activity, R.layout.ship_adapter, list,isSingle,isClick);
        listView.setAdapter(adapter);
    }


    @Override
    public void initData() {
        if (type == 0) {
            //在未选择中注册广播
            initBroadcastReceiver();
            sendHttp();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && type == 1) {
            list.clear();
            list.addAll(MainActivity.list);
            adapter.notifyDataSetChanged();
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
                if (type == 0) {
                    sendHttp();
                }
                break;
        }
    }

    /**
     * 获取船舶列表
     */
    private void sendHttp() {
        params.put("page", page + "");
        params.put("rows", rows + "");
        http.postJson(url_ship, params, progressTitle, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.i("船舶列表", json);
                if (GsonUtils.isSuccess(json)) {
                    progressTitle = null;
                    ShipInfo shipInfo = GsonUtils.jsonToBean(json, ShipInfo.class);
                    if (ToolString.isEmptyList(shipInfo.ships)) {
                        if (isRefresh) {
                            list.clear();
                        }
                        list.addAll(shipInfo.ships);
                    }
                }else {
                    ToolAlert.showToast(activity, GsonUtils.getError(json));
                }
                ToolRefreshView.hintView(adapter, refreshLayout,false, network, noData_img, network_img);
            }
            @Override
            public void requestError(String message) {
                ToolAlert.showToast(activity,message);
                ToolRefreshView.hintView(adapter, refreshLayout, true, network, noData_img, network_img);
            }
        });

    }

    /**
     * 提交选择的船舶列表
     * @param shipInfo
     */
    private void shipSelection(SubmitShipInfo shipInfo) {
        http.postJson(url_select, shipInfo, "提交中...", new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.i(TAG, json);
                if (GsonUtils.isSuccess(json)) {
                    ToolAlert.showToast(activity, "提交成功", false);
                    MainActivity.list = new ArrayList<>(list_select);
                    activity.setCurrentItem(1);

                    //刷新船位船舶数据
                    Intent intent = new Intent(Constant.ACTION_MAP_FRAGMENT);
                    LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
                }else {
                    ToolAlert.showToast(activity,GsonUtils.getError(json));
                }
            }

            @Override
            public void requestError(String message) {
                ToolAlert.showToast(activity, message);
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
        if (!ToolString.isEmptyList(list)) {
            ToolAlert.showToast(activity, "船舶列表为空");
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            list.get(i).isSelect = false;
            adapter.notifyDataSetChanged();
        }//初始化数据
    }

    private void allSelect() {
        if (!ToolString.isEmptyList(list)) {
            ToolAlert.showToast(activity, "船舶列表为空");
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            list.get(i).isSelect = true;
            adapter.notifyDataSetChanged();
        }//初始化数据
    }

    private void submit() {
        if (list_id.size() > 0) {
            list_id.clear();
            list_select.clear();
        }
        for (int i = 0; i < list.size(); i++) {
            ShipInfo.Ship ship = list.get(i);
            if (ship.isSelect) {
                list_select.add(new ShipInfo.Ship(ship.shipid, ship.shipname,ship.isSelect));
                list_id.add(ship.shipid);
            }
        }
        if (list_select.size() == 0) {
            ToolAlert.showToast(activity, "请选择船舶后再提交");
        } else {
            SubmitShipInfo shipInfo = new SubmitShipInfo(HNApplication.mApp.getUserId(), list_id);
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
