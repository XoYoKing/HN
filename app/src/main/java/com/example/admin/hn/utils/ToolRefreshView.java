package com.example.admin.hn.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.admin.hn.base.HNApplication;
import com.example.admin.hn.http.Constant;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

/**
 * Created by WIN10 on 2018/3/30.
 * 列表刷新布局处理
 */

public class ToolRefreshView {

    public static void setRefreshLayout(Context context, RefreshLayout refreshLayout, OnRefreshListener onRefreshListener) {
        refreshLayout.setDisableContentWhenLoading(true);
        refreshLayout.setDisableContentWhenRefresh(true);
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        //设置 Header 为 Material风格
        refreshLayout.setRefreshHeader(new MaterialHeader(context).setShowBezierWave(true));
        //设置 Footer 为 球脉冲
//		refreshLayout.setRefreshFooter(new BallPulseFooter(context).setSpinnerStyle(SpinnerStyle.Scale));
        refreshLayout.setOnRefreshListener(onRefreshListener);
    }
    public static void setRefreshLayout(Context context, RefreshLayout refreshLayout, OnRefreshListener onRefreshListener,OnLoadmoreListener onLoadmoreListener) {
//        refreshLayout.setDisableContentWhenLoading(true);
//        refreshLayout.setDisableContentWhenRefresh(true);
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        refreshLayout.setEnableAutoLoadmore(false);
        //设置 Header 为 Material风格
        refreshLayout.setRefreshHeader(new MaterialHeader(context).setShowBezierWave(true));
        //设置 Footer 为 球脉冲
//		refreshLayout.setRefreshFooter(new BallPulseFooter(context).setSpinnerStyle(SpinnerStyle.Scale));
        refreshLayout.setOnRefreshListener(onRefreshListener);
        refreshLayout.setOnLoadmoreListener(onLoadmoreListener);
    }

    public static void setRefreshLayout(Context context,RefreshLayout refreshLayout) {
        refreshLayout.setDisableContentWhenLoading(true);
        refreshLayout.setDisableContentWhenRefresh(true);
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        //设置 Header 为 Material风格
        refreshLayout.setRefreshHeader(new MaterialHeader(context).setShowBezierWave(true));
        //设置 Footer 为 球脉冲
//		refreshLayout.setRefreshFooter(new BallPulseFooter(context).setSpinnerStyle(SpinnerStyle.Scale));
    }

    /**
     * 以列表形式展示数据时回调的
     *
     * @param isNetwork   是否显示网络加载失败
     * @param adapter     列表适配器
     * @param noData_img  没有请求到数据
     * @param network_img 网络加载失败的图片
     */
    public static void hintView(BaseAdapter adapter, boolean isNetwork, View network, ImageView noData_img, ImageView network_img) {
        adapter.notifyDataSetChanged();
        if (adapter.getCount() == 0) {
            network.setVisibility(View.VISIBLE);
            if (isNetwork) {
                network_img.setVisibility(View.VISIBLE);
                noData_img.setVisibility(View.GONE);
            } else {
                network_img.setVisibility(View.GONE);
                noData_img.setVisibility(View.VISIBLE);
            }
        } else {
            network.setVisibility(View.GONE);
        }
    }

    /**
     * 以列表形式展示数据时回调的
     *
     * @param isNetwork   是否显示网络加载失败
     * @param adapter     列表适配器
     * @param noData_img  没有请求到数据
     * @param network_img 网络加载失败的图片
     */
    public static void hintView(BaseAdapter adapter, RefreshLayout refreshLayout, boolean isNetwork, View network, ImageView noData_img, ImageView network_img) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadmore();
        adapter.notifyDataSetChanged();
        if (adapter.getCount() == 0) {
            network.setVisibility(View.VISIBLE);
            if (isNetwork) {
                network_img.setVisibility(View.VISIBLE);
                noData_img.setVisibility(View.GONE);
            } else {
                network_img.setVisibility(View.GONE);
                noData_img.setVisibility(View.VISIBLE);
            }
        } else {
            network.setVisibility(View.GONE);
        }
    }
    /**
     * 以列表形式展示数据时回调的
     *
     * @param isNetwork   是否显示网络加载失败
     * @param adapter     列表适配器
     * @param noData_img  没有请求到数据
     * @param network_img 网络加载失败的图片
     */
    public static void hintView(RecyclerView.Adapter adapter, boolean isNetwork, View network, ImageView noData_img, ImageView network_img) {
        adapter.notifyDataSetChanged();
        if (adapter.getItemCount() == 0) {
            network.setVisibility(View.VISIBLE);
            if (isNetwork) {
                network_img.setVisibility(View.VISIBLE);
                noData_img.setVisibility(View.GONE);
            } else {
                network_img.setVisibility(View.GONE);
                noData_img.setVisibility(View.VISIBLE);
            }
        } else {
            network.setVisibility(View.GONE);
        }
    }

    /**
     * 以列表形式展示数据时回调的
     *
     * @param isNetwork   是否显示网络加载失败
     * @param adapter     列表适配器
     * @param noData_img  没有请求到数据
     * @param network_img 网络加载失败的图片
     */
    public static void hintView(RecyclerView.Adapter adapter, RefreshLayout refreshLayout, boolean isNetwork, View network, ImageView noData_img, ImageView network_img) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadmore();
        adapter.notifyDataSetChanged();
        if (adapter.getItemCount() == 0) {
            network.setVisibility(View.VISIBLE);
            if (isNetwork) {
                network_img.setVisibility(View.VISIBLE);
                noData_img.setVisibility(View.GONE);
            } else {
                network_img.setVisibility(View.GONE);
                noData_img.setVisibility(View.VISIBLE);
            }
        } else {
            network.setVisibility(View.GONE);
        }
    }

    /**
     * 判断是否加载更多
     * @param currPage
     * @param totalPage
     * @return
     */
    public static boolean isLoadMore(int currPage,int totalPage){
        if(currPage >totalPage){
            //当前页数大于总页数
            ToolAlert.showToast(HNApplication.mApp.context, Constant.LOADED);
            return false;
        }
        return true;
    }
}
