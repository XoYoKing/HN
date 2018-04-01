package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.model.OrderInfo;

import java.util.ArrayList;

/**
 * Created by duantao
 *
 * @date on 2017/7/31 15:35
 */
public class MagazineAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<OrderInfo.Order> groups;
    private LayoutInflater mLayoutInflater;
    private String statu;

    public MagazineAdapter(Context mContext, ArrayList<OrderInfo.Order> groups ) {
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
            convertView = mLayoutInflater.inflate(R.layout.item_magazine_layout, null);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    static class ViewHolder {
        TextView number;
        TextView time;
        TextView status;
    }

}
