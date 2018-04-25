package com.example.admin.hn.ui.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.base.HNApplication;
import com.example.admin.hn.http.OkHttpUtil;
import com.example.admin.hn.model.OrderInfo;
import com.example.admin.hn.model.ShoppingCartInfo;
import com.example.admin.hn.ui.adapter.ShopCartAdapter;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolRefreshView;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 购物车
 *
 * @author Administrator
 */
public class ShopCartActivity extends BaseActivity implements OnRefreshListener,OnLoadmoreListener{
    @Bind(R.id.text_title_back)
    TextView textTitleBack;
    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.text_tile_right)
    TextView text_tile_right;
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
    @Bind(R.id.img_pay_all)
    ImageView img_pay_all;
    @Bind(R.id.rl_pay)
    RelativeLayout rl_pay;
    @Bind(R.id.rl_del)
    RelativeLayout rl_del;

    private ArrayList<OrderInfo.Order> list = new ArrayList<>();

    private ShopCartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        ButterKnife.bind(this);
        initTitleBar();
        initView();
        initData();
    }

    @Override
    public void initTitleBar() {
        textTitle.setText("购物车");
        textTitleBack.setBackgroundResource(R.drawable.btn_back);
        text_tile_right.setText("编辑");
    }
    /**
     *
     */
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ShopCartActivity.class);
        context.startActivity(intent);
    }

    @OnClick({R.id.text_title_back,R.id.text_tile_right,R.id.img_pay_all,R.id.go_pay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_title_back:
                finish();
            break;
            case R.id.text_tile_right:
                if (rl_pay.getVisibility() == View.VISIBLE) {
                    text_tile_right.setText("完成");
                    rl_pay.setVisibility(View.GONE);
                    rl_del.setVisibility(View.VISIBLE);
                }else {
                    text_tile_right.setText("编辑");
                    rl_pay.setVisibility(View.VISIBLE);
                    rl_del.setVisibility(View.GONE);
                }
                break;
            case R.id.img_pay_all:
                img_pay_all.setSelected(!img_pay_all.isSelected());
                break;
            case R.id.go_pay:
                FirmOrderActivity.startActivity(context, new ArrayList<ShoppingCartInfo>());
                break;
        }
    }

    @Override
    public void initView() {
        //下拉刷新
        ToolRefreshView.setRefreshLayout(context,refreshLayout,this,this);
        recycleView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ShopCartAdapter(this, R.layout.item_shopping_cart, list);
        recycleView.setAdapter(adapter);
    }


    @Override
    public void initData() {
        for (int i = 0; i < 10; i++) {
            list.add(new OrderInfo.Order());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {

    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {

    }
}
