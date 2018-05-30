package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.view.View;

import com.example.admin.hn.R;
import com.example.admin.hn.model.ApplyedInfo;
import com.example.admin.hn.model.OrderInfo;
import com.example.admin.hn.ui.account.ShipApplyedActivity;
import com.example.admin.hn.utils.AbDateUtil;
import com.example.admin.hn.utils.AbMathUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 领用单
 * @date on 2017/7/31 15:35
 */
public class ApplyedAdapter extends CommonAdapter<ApplyedInfo> {

    public ApplyedAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, final ApplyedInfo info, int position) {
        viewHolder.setText(R.id.tv_name, info.operator + "");
        viewHolder.setText(R.id.tv_sum_money, AbMathUtil.roundStr(info.money, 2) + "");
        viewHolder.setText(R.id.tv_numberNo, info.receiveno + "");
        viewHolder.setText(R.id.tv_date, AbDateUtil.getStringByFormat(info.receivedate, AbDateUtil.dateFormatYMD) + "");
        viewHolder.setText(R.id.tv_number, info.totalamount + "");
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShipApplyedActivity.startActivity(mContext, info.receiveno);
            }
        });
    }

}
