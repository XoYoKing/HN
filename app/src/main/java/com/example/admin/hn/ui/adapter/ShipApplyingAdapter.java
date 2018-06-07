package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.view.View;

import com.example.admin.hn.R;
import com.example.admin.hn.model.ApplyingInfo;
import com.example.admin.hn.ui.account.ShipApplyingActivity;
import com.example.admin.hn.utils.AbDateUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by duantao
 * 申请中
 * @date on 2017/7/31 15:35
 */
public class ShipApplyingAdapter extends CommonAdapter<ApplyingInfo> {

    public ShipApplyingAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder,final ApplyingInfo info, int position) {
        viewHolder.setText(R.id.tv_name, info.shipname+"");
        viewHolder.setText(R.id.tv_status, info.status + "");
        viewHolder.setText(R.id.tv_numberNo, info.applyno + "");
        viewHolder.setText(R.id.tv_date, AbDateUtil.getStringByFormat(info.applydate,AbDateUtil.dateFormatYMD)  + "");
        viewHolder.setText(R.id.tv_number, info.amount + "");
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShipApplyingActivity.startActivity(mContext, info.applyno,info.status);
            }
        });
    }

}
