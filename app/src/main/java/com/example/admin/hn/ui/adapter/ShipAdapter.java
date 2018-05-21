package com.example.admin.hn.ui.adapter;

import android.content.Context;

import com.example.admin.hn.R;
import com.example.admin.hn.model.ShipInfo;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * Created by duantao
 *
 * @date on 2017/10/20 13:48
 */
public class ShipAdapter extends CommonAdapter<ShipInfo.Ship>{
    public ShipAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, ShipInfo.Ship item, int position) {
        viewHolder.setText(R.id.tv_name,item.shipname);
        viewHolder.setChecked(R.id.cb_status,item.isSelect);
    }
}
