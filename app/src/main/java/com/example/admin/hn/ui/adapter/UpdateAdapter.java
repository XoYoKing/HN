package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.model.UpdateInfo;

import java.util.ArrayList;

/**
 * Created by duantao
 *
 * @date on 2017/11/10 10:35
 */
public class UpdateAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<UpdateInfo.update> groups;
    private LayoutInflater mLayoutInflater;

    public UpdateAdapter(Context mContext, ArrayList<UpdateInfo.update> groups) {
        this.mContext = mContext;
        this.groups = groups;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return groups.size();
    }

    @Override
    public Object getItem(int position) {
        return groups.get(position);
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
            convertView = mLayoutInflater.inflate(R.layout.order_adapter, null);
            viewHolder.number = (TextView) convertView
                    .findViewById(R.id.tv_number);
            viewHolder.time = (TextView) convertView
                    .findViewById(R.id.tv_time);
            viewHolder.status = (TextView) convertView
                    .findViewById(R.id.tv_status);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.number.setText("船舶名称: "+groups.get(position).getShipname());
        viewHolder.time.setText("更新包大小: "+groups.get(position).getSize()+"kb");
        viewHolder.status.setText("更新日期: "+groups.get(position).getUpdatetime());
        return convertView;
    }

    static class ViewHolder {
        TextView number;
        TextView time;
        TextView status;
    }
}
