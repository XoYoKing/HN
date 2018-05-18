package com.example.admin.hn.ui.adapter;

import android.content.Context;

import com.example.admin.hn.R;
import com.example.admin.hn.model.ApplyingDetailInfo;
import com.example.admin.hn.utils.AbDateUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by duantao
 * 申请中
 * @date on 2017/7/31 15:35
 */
public class ShipApplyingDetailAdapter extends CommonAdapter<ApplyingDetailInfo> {

    public ShipApplyingDetailAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, ApplyingDetailInfo info, int position) {
        viewHolder.setText(R.id.tv_type, info.categoryname+"");
        viewHolder.setText(R.id.tv_ship_name, info.shipname + "");
        viewHolder.setText(R.id.tv_name, info.docname + "");
        viewHolder.setText(R.id.tv_numberNo, info.code + "");
        viewHolder.setText(R.id.tv_date, AbDateUtil.getStringByFormat(info.publishat,AbDateUtil.dateFormatYMD)  + "");
        viewHolder.setText(R.id.tv_number, info.quantity + "");
    }

}
