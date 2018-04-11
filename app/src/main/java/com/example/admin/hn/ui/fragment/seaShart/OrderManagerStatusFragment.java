package com.example.admin.hn.ui.fragment.seaShart;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
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
import com.example.admin.hn.model.OrderInfo;
import com.example.admin.hn.ui.account.OrderActivity;
import com.example.admin.hn.ui.adapter.OrderAdapter;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolRefreshView;
import com.example.admin.hn.volley.RequestListener;
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
 * @describe 订单狀态管理
 */
public class OrderManagerStatusFragment extends BaseFragment implements OnRefreshListener,OnLoadmoreListener {

    private static final String TAG = "OrderManagerStatusFragment";

    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.network_disabled)
    RelativeLayout network;
    @Bind(R.id.network_img)
    ImageView network_img;
    @Bind(R.id.noData_img)
    ImageView noData_img;

    private ArrayList<OrderInfo.Order> list = new ArrayList<>();
    private OrderAdapter adapter;
    private View view;
    private String str = "";//搜索条件
    //搜索条件1(查询该用户全部订单) 2(根据船舶名称)3(船舶编号)4(订单号)
    private int status = 1;
    private int page = 1;//当前页面
    private int screen = 1;//订单状态
    //screen 1 ：审核   2:已完成  3：失败
    private String url_order = Api.BASE_URL + Api.ORDER;
    private RefreshLayout refreshLayout;
    private String endDate;
    private String startDate;

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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        endDate = sdf.format(new Date());
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        startDate = sdf.format(c.getTime());

        //下拉刷新
        refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        ToolRefreshView.setRefreshLayout(activity,refreshLayout,this,this);

        adapter = new OrderAdapter(getActivity(), R.layout.order_adapter, list);
        listView.setAdapter(adapter);
    }


    @Override
    public void initData() {
        data(1, str, 0);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent_order;
                intent_order = new Intent(getActivity(), OrderActivity.class);
                intent_order.putExtra("shipname", list.get(position).getShipname());
                intent_order.putExtra("number", list.get(position).getOrdernumber());
                startActivity(intent_order);
            }
        });
        listView.setTextFilterEnabled(true);
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
                data(1, str, 0);
                refreshLayout.finishRefresh(1000);
                break;
        }
    }

    public void data(final int status, String down, final int Loadmore) {
        params.put("ordernumber", down);
        params.put("starttime", startDate);
        params.put("endtime", endDate);
        params.put("shipnumber", down);
        params.put("userid", HNApplication.mApp.getUserId());
        params.put("shipname", down);
        params.put("status", status);
        if (Loadmore == 0) {
            params.put("page", "1");
        } else {
            params.put("page", page);
        }
        params.put("screen", screen);
        http.postJson(url_order, params, progressTitle, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.i("json", json);
                progressTitle = null;
                if (GsonUtils.isSuccess(json)) {
                    OrderInfo orderInfo = GsonUtils.jsonToBean(json, OrderInfo.class
                    );
                    if (Loadmore == 0) {
                        list.clear();
                    }
                    if (orderInfo.getDocuments().size() == 0) {
                        list.clear();
                    } else {
                        for (int i = 0; i < orderInfo.getDocuments().size(); i++) {
                            list.add(new OrderInfo.Order(orderInfo.getDocuments().get(i).getOrdernumber(), orderInfo.getDocuments().get(i).getOrdertime(), orderInfo.getDocuments().get(i).getStatus(), orderInfo.getDocuments().get(i).getShipname()));
                        }
                        HNApplication.mApp.setShipName(list.get(0).getShipname());
                    }
                    adapter.notifyDataSetChanged();
                }else {
                    if (page == 1) {
                        list.clear();
                    } else {
                        ToolAlert.showToast(getActivity(), "已全部加载完成", false);
                    }
                }
                ToolRefreshView.hintView(adapter, false, network, noData_img, network_img);
            }

            @Override
            public void requestError(String message) {
                ToolAlert.showToast(activity, message, false);
                ToolRefreshView.hintView(adapter, true, network, noData_img, network_img);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constant.POP_ORDER_MANAGER && data!=null) {
            String name = data.getStringExtra("name");
            startDate = data.getStringExtra("start");
            endDate = data.getStringExtra("end");
            Logger.i("request", name +"--"+startDate+"--" + endDate);
            progressTitle = "正在加载...";
            if (name != null) {
                str = name;
                data(2, str, 0);
            }else {
                data(1, "", 0);
            }
        }
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        page = page + 1;
        data(status, str, 1);
        adapter.notifyDataSetChanged();
        refreshlayout.finishLoadmore(1000);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 1;
        data(1, str, 0);
        refreshlayout.finishRefresh(1000);
    }
}
