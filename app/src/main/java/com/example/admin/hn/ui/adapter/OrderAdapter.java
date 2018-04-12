package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.admin.hn.R;
import com.example.admin.hn.model.OrderInfo;
import com.example.admin.hn.ui.account.OrderActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 *
 * @date on 2017/7/31 15:35
 */
public class OrderAdapter extends CommonAdapter<OrderInfo.Order> {

    public OrderAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder,final OrderInfo.Order order, int position) {
        holder.setText(R.id.tv_number,"船舶名称: " + order.getShipname());
        holder.setText(R.id.tv_time,"提交日期: " + order.getOrdertime());
        holder.setText(R.id.tv_status, "订单状态: " + order.getStatus());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderActivity.startActivity(mContext, order.getShipname(),order.getOrdernumber());
            }
        });
    }
}
