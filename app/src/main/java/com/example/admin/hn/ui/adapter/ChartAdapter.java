package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.model.ChartInfo;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * Created by duantao
 *
 * @date on 2017/8/2 16:09
 */
public class ChartAdapter extends CommonAdapter<ChartInfo.Chart> {

    public ChartAdapter(Context context, int layoutId, List<ChartInfo.Chart> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, ChartInfo.Chart item, int position) {
        viewHolder.setText(R.id.tv_number,"船舶名称："+item.getShipname());
        viewHolder.setText(R.id.tv_time,"更新日期："+item.getUpdatetime());
    }

}
