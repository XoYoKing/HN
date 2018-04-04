package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.HashMap;
import java.util.List;

/**
 * Created by duantao
 *
 * @date on 2017/10/20 13:48
 */
public class ShipAdapter extends CommonAdapter<HashMap<String, Object>>{
    public ShipAdapter(Context context, int layoutId, List<HashMap<String, Object>> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, HashMap<String, Object> item, int position) {
        viewHolder.setText(R.id.tv_name,item.get("name").toString());
        viewHolder.setChecked(R.id.cb_status,(Boolean)item.get("boolean"));
    }
}
