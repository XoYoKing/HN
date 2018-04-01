package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.model.ChartInfo;

import java.util.ArrayList;

/**
 * Created by duantao
 *
 * @date on 2017/8/2 16:09
 */
public class ChartAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ChartInfo.Chart> groups;
    private LayoutInflater mLayoutInflater;


    public ChartAdapter(Context mContext, ArrayList<ChartInfo.Chart> groups) {
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
            convertView = mLayoutInflater.inflate(R.layout.chart_adapter, null);
            viewHolder.number = (TextView) convertView
                    .findViewById(R.id.tv_number);
            viewHolder.time = (TextView) convertView
                    .findViewById(R.id.tv_time);
//            viewHolder.status = (TextView) convertView
//                    .findViewById(R.id.tv_status);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.number.setText("船舶名称："+groups.get(position).getShipname());
        viewHolder.time.setText("更新日期："+groups.get(position).getUpdatetime());
        return convertView;
    }

    static class ViewHolder {
        TextView number;
        TextView time;
//        TextView status;
    }

}
