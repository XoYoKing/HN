package com.example.admin.hn.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Created by WIN10 on 2018/3/30.
 * 列表刷新布局处理
 */

public class ToolRefreshView {

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

}
