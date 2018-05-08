package com.example.admin.hn.ui.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.model.GoodsInfo;
import com.example.admin.hn.model.GoodsListInfo;
import com.example.admin.hn.model.ShoppingCartInfo;
import com.example.admin.hn.ui.adapter.AllTabAdapter;
import com.example.admin.hn.ui.fragment.shop.CommentFragment;
import com.example.admin.hn.ui.fragment.shop.GoodsFragment;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.volley.RequestListener;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by hjy on 2016/11/5.
 * 商品详情
 */
public class GoodsDetailActivity extends BaseActivity {

    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    private String url= Api.SHOP_BASE_URL+Api.GET_GOODS_DETAIL;
    private GoodsListInfo.Goods info;
    private GoodsInfo goodsInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        ButterKnife.bind(this);
        initTitleBar();
        initView();
        initData();
    }

    /**
     *
     * @param context
     */
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, GoodsDetailActivity.class);
        context.startActivity(intent);
    }
    /**
     *
     * @param context
     */
    public static void startActivity(Context context, GoodsListInfo.Goods info) {
        Intent intent = new Intent(context, GoodsDetailActivity.class);
        intent.putExtra("info", info);
        context.startActivity(intent);
    }

    @Override
    public void initTitleBar() {

    }

    @Override
    public void initView() {
        info = (GoodsListInfo.Goods) getIntent().getSerializableExtra("info");
    }


    @Override
    public void initData() {
        sendHttp();
    }

    @OnClick({R.id.iv_back,R.id.confirm_bid,R.id.add_shopping_cart})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.add_shopping_cart:

                break;
            case R.id.confirm_bid:
                FirmOrderActivity.startActivity(context, new ArrayList<ShoppingCartInfo>());
                break;
        }
    }

    private void sendHttp(){
//        params.put("spuId", info.spuId + "");
        http.get(url+info.id , params, progressTitle, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.e("商品详情", json);
                if (GsonUtils.isShopSuccess(json)) {
                    goodsInfo = GsonUtils.jsonToBean2(json, GoodsInfo.class);
                    setValue();
                }else {
                    ToolAlert.showToast(context, GsonUtils.getError(json));
                }
            }

            @Override
            public void requestError(String message) {
                ToolAlert.showToast(context,message);
            }
        });
    }

    private void setValue() {
        AllTabAdapter adapter = new AllTabAdapter(this, viewPager);
        adapter.addTab("商品", goodsInfo, GoodsFragment.class);
        adapter.addTab("详情", info.id,GoodsFragment.class);
        adapter.addTab("评价", info.id, CommentFragment.class);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
    }

}
