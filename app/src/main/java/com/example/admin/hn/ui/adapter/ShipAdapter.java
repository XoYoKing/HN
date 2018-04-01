package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.admin.hn.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by duantao
 *
 * @date on 2017/10/20 13:48
 */
public class ShipAdapter extends BaseAdapter{
    private Context mContext;
    private List<HashMap<String, Object>> list;
    private LayoutInflater mLayoutInflater;
    private TextView name;
    private CheckBox status;

    public ShipAdapter(Context mContext, List<HashMap<String, Object>> list) {
        this.mContext = mContext;
        this.list = list;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.ship_adapter, null);
            name = (TextView) convertView
                    .findViewById(R.id.tv_name);
            status = (CheckBox) convertView
                    .findViewById(R.id.cb_status);
            viewHolder.name = name;
            viewHolder.status = status;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            name = viewHolder.name;
            status = viewHolder.status;
        }
        viewHolder.name.setText(list.get(position).get("name").toString());
        viewHolder.status.setChecked((Boolean) list.get(position).get("boolean"));
        return convertView;
    }

    public class ViewHolder {
        TextView name;
        public CheckBox status;
    }

}
