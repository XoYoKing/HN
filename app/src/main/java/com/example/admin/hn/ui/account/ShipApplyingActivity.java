package com.example.admin.hn.ui.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.model.OrderInfo;
import com.example.admin.hn.ui.adapter.ShipApplyingAdapter;
import com.example.admin.hn.utils.ToolRefreshView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 申请中详情列表
 *
 * @author Administrator
 */
public class ShipApplyingActivity extends BaseActivity implements OnRefreshListener,OnLoadmoreListener {
    @Bind(R.id.text_title_back)
    TextView textTitleBack;
    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.listView)
    ListView listView;
    private int id;
    private String title;
    private RefreshLayout refreshLayout;
    private ShipApplyingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_layout);
        ButterKnife.bind(this);
        initTitleBar();
        initView();
        initData();
    }

    @Override
    public void initTitleBar() {
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        title = intent.getStringExtra("title");
        textTitle.setText("申请详情");
        textTitleBack.setBackgroundResource(R.drawable.btn_back);
    }
    private ArrayList<OrderInfo.Order> list = new ArrayList<>();
    @Override
    public void initView() {
        //下拉刷新
        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        ToolRefreshView.setRefreshLayout(this, refreshLayout, this, this);
        adapter = new ShipApplyingAdapter(this, R.layout.item_ship_applying_adapter, list);
        listView.setAdapter(adapter);
    }

    /**
     *
     */
    public static void startActivity(Context context, int id, String title) {
        Intent intent = new Intent(context, ShipApplyingActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    @OnClick(R.id.text_title_back)
    public void onClick(View v) {
        if (v.getId() == R.id.text_title_back) {
            finish();
        }
    }

    @Override
    public void initData() {
        super.initData();
        data();
    }


    public void data() {
        for (int i = 0; i < 10; i++) {
            list.add(new OrderInfo.Order());
        }
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        data();
        adapter.notifyDataSetChanged();
        refreshlayout.finishLoadmore(1000);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        data();
        refreshlayout.finishRefresh(1000);
    }
}
