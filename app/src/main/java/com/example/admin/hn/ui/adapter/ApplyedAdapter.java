package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.view.View;

import com.example.admin.hn.model.OrderInfo;
import com.example.admin.hn.ui.account.ShipApplyedActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 领用单
 * @date on 2017/7/31 15:35
 */
public class ApplyedAdapter extends CommonAdapter<OrderInfo.Order> {

    public ApplyedAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, OrderInfo.Order item, int position) {

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShipApplyedActivity.startActivity(mContext, 1, "");
            }
        });
    }

}
