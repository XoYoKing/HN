package com.example.admin.hn.ui.fragment.seaShart;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.example.admin.hn.MainActivity;
import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseFragment;
import com.example.admin.hn.base.HNApplication;
import com.example.admin.hn.http.Constant;
import com.example.admin.hn.http.OkHttpUtil;
import com.example.admin.hn.model.InventoryInfo;
import com.example.admin.hn.ui.account.InventoryActivity;
import com.example.admin.hn.ui.adapter.InventoryAdapter;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolRefreshView;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author duantao
 * @date on 2017/7/26 16:04
 * @describe 库存列表
 */
public class InventoryFragment extends BaseFragment implements OnRefreshListener,OnLoadmoreListener{

    private String TAG = "InventoryFragment";

    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.network_disabled)
    RelativeLayout network;
    @Bind(R.id.network_img)
    ImageView network_img;
    @Bind(R.id.noData_img)
    ImageView noData_img;

    private ArrayList<InventoryInfo.Inventory> list = new ArrayList<>();
    private InventoryAdapter adapter;
    private View view;
    private int status = 2;
    private int page = 1;
    private String url_inventory = Api.BASE_URL + Api.INVENTORY;
    private String url_chart = Api.BASE_URL + Api.CHART;
    private ArrayList<HashMap<String, String>> datas = new ArrayList<>();
    private RefreshLayout refreshLayout;
    private String endDate;
    private String startDate;
    private int screen=2;


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
        adapter = new InventoryAdapter(getActivity(), datas);
        listView.setAdapter(adapter);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        endDate = sdf.format(new Date());
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        startDate = sdf.format(c.getTime());

        //下拉刷新
        refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        ToolRefreshView.setRefreshLayout(activity,refreshLayout,this,this);
    }

    @Override
    public void initData() {
        //list点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent_order;
                intent_order = new Intent(getActivity(), InventoryActivity.class);
                intent_order.putExtra("number", list.get(position).getOrdernumber());
                intent_order.putExtra("shipname", list.get(position).getShipname());
                startActivity(intent_order);
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint() && isFirstHttp) {
            isFirstHttp = false;
            data("1", "", 0);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isFirstHttp) {
            isFirstHttp = false;
            data("1", "", 0);
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
                data("1", "", 0);
                refreshLayout.finishRefresh(1000);
                break;
        }
    }

    public void data(String status, String content, final int Loadmore) {
        //status 1(查询该用户海图) 2(船舶名称) 3(海图名称)
        Map map = new HashMap();
        map.put("userid", HNApplication.mApp.getUserId());
        map.put("status", status);
        if (Loadmore == 0) {
            map.put("page", "1");
        } else {
            map.put("page", page);
        }
        //1 全部数据2 海图状态为未过期3 海图状态为已过期
        map.put("screen", screen);
        map.put("endtime", endDate);
        map.put("starttime", startDate);
        map.put("shipname", content);
        map.put("haituname", content);
        String jsonStr = GsonUtils.mapToJson(map);
        Logger.i(TAG, jsonStr);
        try {
            OkHttpUtil.postJsonData2Server(getActivity(), url_inventory, jsonStr,progressTitle,
                    new OkHttpUtil.MyCallBack() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            ToolAlert.showToast(getActivity(), "服务器异常,请稍后再试", false);
                            ToolRefreshView.hintView(adapter, true, network, noData_img, network_img);
                        }

                        @Override
                        public void onResponse(String json) {
                            progressTitle = null;
                            Logger.i(TAG, json);
                            InventoryInfo inventoryInfo = GsonUtils.jsonToBean(json, InventoryInfo.class
                            );
                            if (inventoryInfo.getStatus().equals("error")) {
                                if (page == 1) {
                                    ToolAlert.showToast(getActivity(), inventoryInfo.getMessage(), false);
                                    datas.clear();
                                    adapter.notifyDataSetChanged();
                                } else {
                                    ToolAlert.showToast(getActivity(), "已全部加载完成", false);
                                }
                            } else {
                                list.clear();
                                if (inventoryInfo.getDocuments().size() == 0) {
                                    datas.clear();
                                } else {
                                    for (int i = 0; i < inventoryInfo.getDocuments().size(); i++) {
                                        list.add(new InventoryInfo.Inventory(inventoryInfo.getDocuments().get(i).getPeriod(), inventoryInfo.getDocuments().get(i).getWholesaleprices(), inventoryInfo.getDocuments().get(i).getCname(), inventoryInfo.getDocuments().get(i).getProductType(), inventoryInfo.getDocuments().get(i).getProductNumber(), inventoryInfo.getDocuments().get(i).getScale(), inventoryInfo.getDocuments().get(i).getOrdertime(), inventoryInfo.getDocuments().get(i).getVersion(), inventoryInfo.getDocuments().get(i).getProductTitle(), inventoryInfo.getDocuments().get(i).getOrdernumber(), inventoryInfo.getDocuments().get(i).getGuideprice(), inventoryInfo.getDocuments().get(i).getShipnumber(), inventoryInfo.getDocuments().get(i).getRoutename(), inventoryInfo.getDocuments().get(i).getShipname(), inventoryInfo.getDocuments().get(i).getVersionunit()));
                                    }
                                    if (Loadmore == 0) {
                                        datas.clear();
                                    }
                                    for (int i = 0; i < list.size(); i++) {
                                        HashMap<String, String> item = new HashMap<String, String>();
                                        item.put("cname", list.get(i).getCname());
                                        item.put("shipname", list.get(i).getShipname());
                                        item.put("ordertime", list.get(i).getOrdertime());
                                        item.put("productTitle", list.get(i).getProductTitle());
                                        item.put("ProductType", list.get(i).getProductType());
                                        item.put("productNumber", list.get(i).getProductNumber());
                                        item.put("version", list.get(i).getVersion());
                                        item.put("shipnumber", list.get(i).getShipnumber());
                                        item.put("period", list.get(i).getPeriod());
                                        datas.add(item);
                                    }
                                }
                            }
                            ToolRefreshView.hintView(adapter, false, network, noData_img, network_img);
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constant.POP_ORDER_INVENTORY && data!=null) {
            String name = data.getStringExtra("name");
            startDate = data.getStringExtra("start");
            endDate = data.getStringExtra("end");
            Logger.i("request", name +"--"+startDate+"--" + endDate);
            if (name != null) {
                data("1", "", 0);
            }else {
                data("1", "", 0);
            }
        }
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        page = page + 1;
        data("1", "", 1);
        adapter.notifyDataSetChanged();
        refreshlayout.finishLoadmore(1000);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 1;
        data("1", "", 0);
        adapter.notifyDataSetChanged();
        refreshlayout.finishRefresh(1000);
    }
}
