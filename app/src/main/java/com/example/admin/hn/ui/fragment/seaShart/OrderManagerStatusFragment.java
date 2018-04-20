package com.example.admin.hn.ui.fragment.seaShart;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.admin.hn.utils.SpaceItemDecoration;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolRefreshView;
import com.example.admin.hn.utils.ToolString;
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

    @Bind(R.id.recycleView)
    RecyclerView recycleView;
    @Bind(R.id.network_disabled)
    RelativeLayout network;
    @Bind(R.id.network_img)
    ImageView network_img;
    @Bind(R.id.noData_img)
    ImageView noData_img;

    private ArrayList<OrderInfo.Order> list = new ArrayList<>();
    private OrderAdapter adapter;
    private View view;
    //搜索条件  1(查询该用户全部订单) 2(根据船舶名称)3(船舶编号)4(订单号) 默认查询全部
    private int status = 1;
    private int page = 1;//当前页面
    private int screen = 1;//订单状态
    //screen 1 ：审核   2:已完成  3：失败
    private String url_order = Api.BASE_URL + Api.ORDER;
    private RefreshLayout refreshLayout;
    private String shipName = "";//搜索内容
    private String endDate;//开始时间
    private String startDate;//结束时间
    private int childItem;

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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        endDate = sdf.format(new Date());
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        startDate = sdf.format(c.getTime());

        //下拉刷新
        refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        ToolRefreshView.setRefreshLayout(activity,refreshLayout,this,this);

        adapter = new OrderAdapter(getActivity(), R.layout.order_adapter, list);
        recycleView.setLayoutManager(new LinearLayoutManager(activity));
        recycleView.addItemDecoration(new SpaceItemDecoration(10,20,0,0));
        recycleView.setAdapter(adapter);
    }


    @Override
    public void initData() {

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
                data(status, shipName, 0);
                break;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint() && isFirstHttp && http != null) {
            isFirstHttp = false;
            data(status, shipName, 0);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && http != null) {
            isFirstHttp = false;
            data(status, shipName, 0);
        }
    }

    public void data(final int status, String shipName, final int Loadmore) {
        params.put("ordernumber", shipName);
        params.put("starttime", startDate);
        params.put("endtime", endDate);
        params.put("shipnumber", shipName);
        params.put("shipname", shipName);
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
                }else {
                    if (page != 1) {
                        ToolAlert.showToast(getActivity(),Constant.LOADED);
                    }
                }
                ToolRefreshView.hintView(adapter,refreshLayout, false, network, noData_img, network_img);
            }

            @Override
            public void requestError(String message) {
                ToolAlert.showToast(activity, message, false);
                ToolRefreshView.hintView(adapter,refreshLayout, true, network, noData_img, network_img);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constant.POP_ORDER_MANAGER && data!=null) {
            shipName = data.getStringExtra("name");
            startDate = data.getStringExtra("start");
            endDate = data.getStringExtra("end");
            childItem = data.getIntExtra("childItem",0);
            if (ToolString.isEmpty(shipName)) {
                status = 2;
            }else {
                status = 1;
            }
            progressTitle = "正在加载...";
            if (screen == childItem+1) {
                if (shipName != null) {
                    data(status, shipName, 0);
                }else {
                    data(status, "", 0);
                }
            }
        }
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        page = page + 1;
        data(status, shipName, 1);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 1;
        data(status, shipName, 0);
    }
}
