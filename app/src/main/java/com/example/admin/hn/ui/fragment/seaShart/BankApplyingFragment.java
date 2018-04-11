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
import com.example.admin.hn.http.Constant;
import com.example.admin.hn.http.OkHttpUtil;
import com.example.admin.hn.model.OrderInfo;
import com.example.admin.hn.ui.account.AuditingApplyingActivity;
import com.example.admin.hn.ui.account.ShipApplyingActivity;
import com.example.admin.hn.ui.adapter.BankApplyingAdapter;
import com.example.admin.hn.ui.adapter.MaterialSelectAdapter;
import com.example.admin.hn.ui.adapter.ShipApplyingAdapter;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolRefreshView;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.okhttp.Request;
import com.zhy.adapter.abslistview.CommonAdapter;

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
 * @describe 申请中
 */
public class BankApplyingFragment extends BaseFragment implements OnRefreshListener,OnLoadmoreListener{

    private static final String TAG = "BankApplyingFragment";

    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.network_disabled)
    RelativeLayout network;
    @Bind(R.id.network_img)
    ImageView network_img;
    @Bind(R.id.noData_img)
    ImageView noData_img;
    private String str = "";

    private ArrayList<OrderInfo.Order> list = new ArrayList<>();
    private CommonAdapter adapter;
    private View view;
    //是否审核1已审核2未审核
    private String statu = "1";
    //搜索条件1(查询该用户全部订单) 2(根据船舶名称)3(船舶编号)4(订单号)
    private int status = 1;
    private int page = 1;
    private int screen = 1;
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
        ToolRefreshView.setRefreshLayout(activity, refreshLayout, this, this);
        adapter = new BankApplyingAdapter(activity, R.layout.item_bank_applying_layout, list);
        listView.setAdapter(adapter);
    }


    @Override
    public void initData() {
        data(1, "", 0);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToolAlert.showToast(activity,""+position,false);
                AuditingApplyingActivity.startActivity(activity, 1, "");
            }
        });
    }


    @Override
    public void initTitleBar() {
        Bundle bundle = getArguments();
        screen = Integer.parseInt(bundle.getString("type"));
    }


    @OnClick({ R.id.network_img})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.network_img:
                network_img.setVisibility(View.GONE);
                data(1, str, 0);
                refreshLayout.finishRefresh(1000);
                break;
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }


    public void data(final int status, String down, final int Loadmore) {
        for (int i = 0; i < 10; i++) {
            list.add(new OrderInfo.Order());
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constant.POP_SHIP_AUDITING && data != null) {
            String name = data.getStringExtra("name");
            startDate = data.getStringExtra("start");
            endDate = data.getStringExtra("end");
            Logger.i("request", name +"--"+startDate+"--" + endDate);
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
