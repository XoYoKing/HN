package com.example.admin.hn.ui.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.ui.adapter.AllTabAdapter;
import com.example.admin.hn.ui.fragment.shop.HnShopFragment;
import com.example.admin.hn.ui.fragment.shop.ShopFragment;
import com.example.admin.hn.ui.fragment.shop.ShopOrderManagerFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 订单管理
 *
 * @author Administrator
 */
public class OrderManagerActivity extends BaseActivity {
    @Bind(R.id.text_title_back)
    TextView textTitleBack;
    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tablaout);
        ButterKnife.bind(this);
        initTitleBar();
        initData();
    }

    @Override
    public void initTitleBar() {
        textTitle.setText("订单管理");
        textTitleBack.setBackgroundResource(R.drawable.btn_back);
    }
    /**
     *
     */
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, OrderManagerActivity.class);
        context.startActivity(intent);
    }

    @OnClick(R.id.text_title_back)
    public void onClick(View v) {
        if (v.getId() == R.id.text_title_back) {
            finish();
        }
    }

    /**
     * NEW("待付款"),              0
      PREPARE("待发货"),          1
      SEND("待收货"),             2
      NOEVAL("待评价"),           3
      FINISH("已完成");           4
     */
    @Override
    public void initData() {
        AllTabAdapter adapter = new AllTabAdapter(this, viewPager);
        adapter.addTab("待付款","0", ShopOrderManagerFragment.class);
        adapter.addTab("待发货","1", ShopOrderManagerFragment.class);
        adapter.addTab("待收货","2", ShopOrderManagerFragment.class);
        adapter.addTab("待评价","3", ShopOrderManagerFragment.class);
        adapter.addTab("已完成","4", ShopOrderManagerFragment.class);
        viewPager.setOffscreenPageLimit(5);
        tabLayout.setupWithViewPager(viewPager);
    }
}
