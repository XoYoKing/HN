package com.example.admin.hn.ui.adapter;

import android.content.Context;

import com.example.admin.hn.R;
import com.example.admin.hn.model.ApplyedDetailInfo;
import com.example.admin.hn.utils.AbDateUtil;
import com.example.admin.hn.utils.AbMathUtil;
import com.example.admin.hn.utils.ToolString;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by duantao
 * 已完成
 * @date on 2017/7/31 15:35
 */
public class ShipApplyedAdapter extends CommonAdapter<ApplyedDetailInfo> {

    public ShipApplyedAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder,final ApplyedDetailInfo info, int position) {
        viewHolder.setText(R.id.tv_type, info.category_name+"");
        viewHolder.setText(R.id.tv_ship_name, info.shipname+"");
        viewHolder.setText(R.id.tv_numberNo, info.code + "");
        if (ToolString.isEmpty(info.chs_name)) {
            viewHolder.setText(R.id.tv_chinese_name, info.chs_name + "");
        }else {
            viewHolder.setText(R.id.tv_chinese_name, info.eng_name + "");
        }
        viewHolder.setText(R.id.tv_use_number, info.receivenum + "");
        viewHolder.setText(R.id.tv_inventory, info.returnnum + "");
        viewHolder.setText(R.id.tv_date, AbDateUtil.getStringByFormat(info.publish_at,AbDateUtil.dateFormatYMD)  + "");
        viewHolder.setText(R.id.tv_univalent, AbMathUtil.roundStr(info.sale, 2) + "");
    }

}
