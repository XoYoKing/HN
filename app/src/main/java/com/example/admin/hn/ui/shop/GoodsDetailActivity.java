package com.example.admin.hn.ui.shop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.http.Constant;
import com.example.admin.hn.model.AddressInfo;
import com.example.admin.hn.model.GoodsInfo;
import com.example.admin.hn.model.GoodsListInfo;
import com.example.admin.hn.model.ShoppingCartInfo;
import com.example.admin.hn.ui.adapter.AllTabAdapter;
import com.example.admin.hn.ui.fragment.shop.CommentFragment;
import com.example.admin.hn.ui.fragment.shop.GoodsFragment;
import com.example.admin.hn.ui.fragment.shop.bean.GoodsDetailFragment;
import com.example.admin.hn.ui.fragment.shop.bean.ShopCartInfo;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolShopCartUtil;
import com.example.admin.hn.utils.ToolString;
import com.example.admin.hn.volley.RequestListener;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

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
    @Bind(R.id.tv_cartNumber)
    TextView tv_cartNumber;
    private String url= Api.SHOP_BASE_URL+Api.GET_GOODS_DETAIL;
    private GoodsListInfo.Goods info;
    private GoodsInfo goodsInfo;
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver br;
    private AddressInfo defaultInfo;//默认地址

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
        initBroadcastReceiver();
        info = (GoodsListInfo.Goods) getIntent().getSerializableExtra("info");
    }

    private ShopCartInfo shopCartInfo;

    @Override
    public void initData() {
        sendHttp();//获取商品详情
        int count = DataSupport.count(ShopCartInfo.class);
        tv_cartNumber.setText(count + "");
    }

    @OnClick({R.id.iv_back,R.id.confirm_bid,R.id.add_shopping_cart,R.id.ll_cart})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.add_shopping_cart:
                ToolShopCartUtil.addShopCartInfo(context,goodsInfo,1);
                break;
            case R.id.confirm_bid:
                FirmOrderActivity.startActivity(context, new ArrayList<ShoppingCartInfo>());
                break;
            case R.id.ll_cart:
                ShopCartActivity.startActivity(context);
                break;
        }
    }

    private void sendHttp(){
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
        adapter.addTab("详情", info.descriptionUrl,GoodsDetailFragment.class);
        adapter.addTab("评价", info.spuId, CommentFragment.class);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        FragmentManager fragmentManager=getSupportFragmentManager();
        for(int index=0;index<fragmentManager.getFragments().size();index++){
            Fragment fragment=fragmentManager.getFragments().get(index); //找到第一层Fragment
            if(fragment!=null)
                handleResult(fragment,requestCode,resultCode,data);
        }
    }
    /**
     * 递归调用，对所有的子Fragment生效
     * @param fragment
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private void handleResult(Fragment fragment,int requestCode,int resultCode,Intent data) {
        fragment.onActivityResult(requestCode, resultCode, data);//调用每个Fragment的onActivityResult
        List<Fragment> childFragment = fragment.getChildFragmentManager().getFragments(); //找到第二层Fragment
        if(childFragment!=null)
            for(Fragment f:childFragment){
                if(f!=null) {
                    handleResult(f, requestCode, resultCode, data);
                }
            }
    }

    private void initBroadcastReceiver(){
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_GOODS_DETAIL_ACTIVITY);
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    int status = intent.getIntExtra("status", 0);
                    if (status == 1) {
                        //从选择地址传递的数据
                        defaultInfo = (AddressInfo) intent.getSerializableExtra("info");
                    }else if (status==2){
                        //更新购物车数量
                        int count = intent.getIntExtra("count", 0);
                        if (count == 0) {
                            tv_cartNumber.setVisibility(View.GONE);
                        }else {
                            tv_cartNumber.setText(count + "");
                            tv_cartNumber.setVisibility(View.VISIBLE);
                        }
                    }else if (status==3){
                        //更新商品信息
                        goodsInfo = (GoodsInfo) intent.getSerializableExtra("goodsInfo");
                    }
                }
            }
        };
        localBroadcastManager.registerReceiver(br, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(br);
    }
}
