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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 商品分类列表
 *
 * @author Administrator
 */
public class ShopTypeListActivity extends BaseActivity {
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
        setContentView(R.layout.activity_shop_type);
        ButterKnife.bind(this);
        initTitleBar();
        initData();
    }

    @Override
    public void initTitleBar() {
        textTitle.setText("商品分类");
        textTitleBack.setBackgroundResource(R.drawable.btn_back);
    }
    /**
     *
     */
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ShopTypeListActivity.class);
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
        AllTabAdapter adapter = new AllTabAdapter(this, viewPager);
//        adapter.addTab("海宁海图", HnShopFragment.class);
//        adapter.addTab("纸质海图", ShopFragment.class);
//        adapter.addTab("电脑配件", HnShopFragment.class);
//        adapter.addTab("船舶物料", HnShopFragment.class);
//        adapter.addTab("手机数码", HnShopFragment.class);
//        adapter.addTab("个人化妆", HnShopFragment.class);
//        adapter.addTab("大家电", HnShopFragment.class);
//        adapter.addTab("家用产品", HnShopFragment.class);
//        adapter.addTab("扶贫产品", HnShopFragment.class);
        viewPager.setOffscreenPageLimit(8);
        tabLayout.setupWithViewPager(viewPager);
    }
}
