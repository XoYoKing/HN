package com.example.admin.hn.ui.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.model.OrderInfo;
import com.example.admin.hn.ui.adapter.AuditingApplyingAdapter;
import com.example.admin.hn.ui.adapter.ShipApplyingAdapter;
import com.example.admin.hn.utils.SpaceItemDecoration;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolRefreshView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 审核申请中详情列表
 *
 * @author Administrator
 */
public class AuditingApplyingActivity extends BaseActivity implements OnRefreshListener,OnLoadmoreListener {
    @Bind(R.id.text_title_back)
    TextView textTitleBack;
    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.text_tile_right)
    TextView text_tile_right;
    @Bind(R.id.recycleView)
    RecyclerView recycleView;
    private int id;
    private String title;
    private RefreshLayout refreshLayout;
    private AuditingApplyingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_layout);
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
        text_tile_right.setText("通过");
    }
    private ArrayList<OrderInfo.Order> list = new ArrayList<>();
    @Override
    public void initView() {
        //下拉刷新
        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        ToolRefreshView.setRefreshLayout(this, refreshLayout, this, this);
        adapter = new AuditingApplyingAdapter(this, R.layout.item_auditing_applying_adapter, list);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.addItemDecoration(new SpaceItemDecoration(10,10,0,0));
        recycleView.setAdapter(adapter);
    }

    /**
     *
     */
    public static void startActivity(Context context, int id, String title) {
        Intent intent = new Intent(context, AuditingApplyingActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    @OnClick({R.id.text_title_back,R.id.text_tile_right})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_title_back:
                finish();
            break;
            case R.id.text_tile_right:
                submit();
                break;

        }
    }

    private void submit() {
        ToolAlert.showToast(this, "提交", false);
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
//        data();
        adapter.notifyDataSetChanged();
        refreshlayout.finishLoadmore(1000);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
//        data();
        refreshlayout.finishRefresh(1000);
    }
}
