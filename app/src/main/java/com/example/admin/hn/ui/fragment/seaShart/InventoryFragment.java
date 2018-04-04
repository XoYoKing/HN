package com.example.admin.hn.ui.fragment.seaShart;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
public class InventoryFragment extends BaseFragment {

    private String TAG = "InventoryListFragment";

    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.startdate)
    TextView startdate;
    @Bind(R.id.enddate)
    TextView enddate;
    @Bind(R.id.ll_hide)
    LinearLayout hide;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_inventory_list, container, false);
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
        Drawable nav_up = getResources().getDrawable(R.drawable.seletor_stock_arrow);
        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        startdate.setText(sdf.format(c.getTime()));
        enddate.setText(date);
        //下拉刷新
        refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        refreshLayout.setDisableContentWhenLoading(true);
        refreshLayout.setDisableContentWhenRefresh(true);
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        //设置 Header 为 Material风格
        refreshLayout.setRefreshHeader(new MaterialHeader(getActivity()).setShowBezierWave(true));
        //设置 Footer 为 球脉冲
        refreshLayout.setRefreshFooter(new BallPulseFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale));
        //监听
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 1;
                data("1", "", 0);
                adapter.notifyDataSetChanged();
                refreshlayout.finishRefresh(1000);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page = page + 1;
                data("1", "", 1);
                adapter.notifyDataSetChanged();
                refreshlayout.finishLoadmore(1000);
            }
        });
        //默认隐藏搜索条件
        hide.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        data("1", "", 0);
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
    public void initTitleBar() {

    }

    @OnClick({R.id.startdate, R.id.enddate, R.id.fl_search, R.id.network_img})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startdate:
                //时间选择器
                TimePickerView pvTime = new TimePickerView.Builder(getActivity(), new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        startdate.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
                        data(status + "", "", 0);
                    }
                }).isCyclic(true).setBackgroundId(0x00FFFFFF).setContentSize(21).setType(new boolean[]{true, true, true, false, false, false}).build();
                pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
                pvTime.show();
                break;
            case R.id.enddate:
                //时间选择器
                TimePickerView pTime = new TimePickerView.Builder(getActivity(), new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        enddate.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
                        data(status + "", "", 0);
                    }
                }).isCyclic(true).setBackgroundId(0x00FFFFFF).setContentSize(21).setType(new boolean[]{true, true, true, false, false, false}).build();
                pTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
                pTime.show();
                break;
            case R.id.fl_search:
                if (hide.getVisibility() == View.GONE) {
                    hide.setVisibility(View.VISIBLE);
                } else {
                    hide.setVisibility(View.GONE);
                }
                break;
            case R.id.network_img:
                network_img.setVisibility(View.GONE);
                data("1", "", 0);
                refreshLayout.finishRefresh(1000);
                break;
        }
    }

    public void data(String str, String content, final int Loadmore) {

        //status 1(查询该用户海图) 2(船舶名称)3(海图名称)
        Map map = new HashMap();
        map.put("userid", MainActivity.USER_ID);
        map.put("status", str);
        if (Loadmore == 0) {
            map.put("page", "1");
        } else {
            map.put("page", page);
        }
        //1 全部数据2 海图状态为未过期3 海图状态为已过期
        map.put("screen", "1");
        map.put("endtime", enddate.getText().toString());
        map.put("starttime", startdate.getText().toString());
        map.put("shipname", content);
        map.put("haituname", content);

        String jsonStr = GsonUtils.mapToJson(map);
        Logger.i(TAG, jsonStr);
        try {
            OkHttpUtil.postJsonData2Server(getActivity(),
                    url_inventory,
                    jsonStr,
                    new OkHttpUtil.MyCallBack() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            ToolAlert.showToast(getActivity(), "服务器异常,请稍后再试", false);
                            ToolRefreshView.hintView(adapter, true, network, noData_img, network_img);
                        }

                        @Override
                        public void onResponse(String json) {
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
        if (resultCode == 700)
            ToolAlert.showToast(activity, TAG + resultCode + "", false);
    }
}
