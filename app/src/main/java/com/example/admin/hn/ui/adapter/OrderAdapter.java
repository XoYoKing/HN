package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.model.OrderInfo;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duantao
 *
 * @date on 2017/7/31 15:35
 */
public class OrderAdapter extends CommonAdapter<OrderInfo.Order> {

    public OrderAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, OrderInfo.Order item, int position) {
        viewHolder.setText(R.id.tv_number,"船舶名称: " + item.getShipname());
        viewHolder.setText(R.id.tv_time,"提交日期: " + item.getOrdertime());
        viewHolder.setText(R.id.tv_status, "订单状态: " + item.getStatus());
    }

}
