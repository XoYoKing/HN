
package com.example.admin.hn.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.base.BaseFragment;
import com.example.admin.hn.mViewPager.CycleViewPager;
import com.example.admin.hn.mViewPager.ViewFactory;
import com.example.admin.hn.model.BannerInfo;
import com.example.admin.hn.model.HomeInfo;
import com.example.admin.hn.model.HomeItem;
import com.example.admin.hn.model.ScreenTypeInfo;
import com.example.admin.hn.ui.account.HtmlActivity;
import com.example.admin.hn.ui.adapter.ScreenTypeAdapter;
import com.example.admin.hn.ui.adapter.ShopHomeListAdapter;
import com.example.admin.hn.ui.shop.OrderManagerActivity;
import com.example.admin.hn.ui.shop.ShopCartActivity;
import com.example.admin.hn.utils.GlideImageLoader;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolAppUtils;
import com.example.admin.hn.utils.ToolRefreshView;
import com.example.admin.hn.utils.ToolString;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 首页
 */
public class OneFragment extends BaseFragment  {
    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.text_tile_del)
    TextView text_tile_del;
    @Bind(R.id.banner)
    Banner bannerGuideContent;
    @Bind(R.id.iv_shopCart)
    ImageView iv_shopCart;//购物车


    private View view;

    //首页应用列表
    private List<HomeInfo> homeInfos = new ArrayList<>();
    //首页banner图列表
    private List<BannerInfo> bannerList = new ArrayList<>();

    private RecyclerView recycleView;
    private ShopHomeListAdapter adapter;
    private NestedScrollView scroll;
    private RefreshLayout refreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shop_home, container, false);
        ButterKnife.bind(this, view);
        initTitleBar();
        initView();
        initData();
        return view;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initTitleBar() {
        textTitle.setText("商城");
        text_tile_del.setVisibility(View.VISIBLE);
        text_tile_del.setText("订单管理");
    }

    /**
     * 设置首页应用列表
     *
     * @param data
     */
    private void setHomeAdapter(List data) {
        if (homeInfos.size() > 0) {
            homeInfos.clear();
        }
        homeInfos.addAll(data);
    }

    /**
     * 加载导航banner
     */
    private void sendBanner() {

    }

    @OnClick({R.id.iv_shopCart,R.id.text_tile_del})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_shopCart:
                ShopCartActivity.startActivity(activity);
                break;
            case R.id.text_tile_del:
                OrderManagerActivity.startActivity(activity,0);
                break;
        }
    }


    @Override
    public void initView() {
        //下拉刷新
        refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        ToolRefreshView.setRefreshLayout(activity,refreshLayout);
        //监听
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(1000);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                adapter.notifyDataSetChanged();
                refreshlayout.finishLoadmore(1000);
            }
        });

        scroll = (NestedScrollView) view.findViewById(R.id.scroll);
        recycleView = (RecyclerView) view.findViewById(R.id.recycleView);
        recycleView.setNestedScrollingEnabled(false);
        recycleView.setFocusable(false);
        recycleView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new ShopHomeListAdapter(activity, homeInfos);
        recycleView.setAdapter(adapter);
        initHomeData();
    }

    private List<BannerInfo> mActivityListBean = new ArrayList<>();
    /**
     * 设置banner
     *
     * @param slideList
     */
    private void initBanner(List<BannerInfo> slideList) {
        if (slideList!=null && slideList.size()>0) {
            if (mActivityListBean.size() > 0) {
                mActivityListBean.clear();
            }
            mActivityListBean.addAll(slideList);
            bannerGuideContent.setVisibility(View.VISIBLE);
            //设置banner样式
            bannerGuideContent.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);//设置不显示指示器   BannerConfig.CIRCLE_INDICATOR//圆点指示器
            //设置图片加载器
            bannerGuideContent.setImageLoader(new GlideImageLoader(R.mipmap.ic_launcher));
            //设置图片集合
            bannerGuideContent.setImages(mActivityListBean);
            //设置banner动画效果
            bannerGuideContent.setBannerAnimation(Transformer.DepthPage);
            //设置自动轮播，默认为true
            bannerGuideContent.isAutoPlay(true);
            //设置轮播时间
            bannerGuideContent.setDelayTime(5000);
            //设置指示器位置（当banner模式中有指示器时）
            bannerGuideContent.setIndicatorGravity(BannerConfig.CENTER);//修改小圆点位置
            bannerGuideContent.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    BannerInfo bannerBean = mActivityListBean.get(position);
                    if (ToolString.isNoBlankAndNoNull(bannerBean.link)) {
                        HtmlActivity.startActivity(activity, bannerBean.link);
                    }
                }
            });
            //banner设置方法全部调用完毕时最后调用
            bannerGuideContent.start();
        }
    }

    private void initHomeData(){
        for (int i = 1; i < 4; i++) {
            HomeInfo info = new HomeInfo();
            info.data= new ArrayList<>();

            for (int j = 0; j < 5; j++) {
                info.data.add(new HomeItem());
            }
            info.type = i;
            homeInfos.add(info);
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


}
