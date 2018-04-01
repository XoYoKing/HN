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
public class OrderAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<OrderInfo.Order> groups;
    private LayoutInflater mLayoutInflater;
    private String statu;

    public OrderAdapter(Context mContext, ArrayList<OrderInfo.Order> groups,String statu) {
        this.mContext = mContext;
        this.groups = groups;
        this.statu = statu;
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
//        if (statu.equals("2")){
//            if (groups.get(position).getStatus().toString().equals("待审核")){
        viewHolder.number.setText("船舶名称: " + groups.get(position).getShipname());
        viewHolder.time.setText("提交日期: " + groups.get(position).getOrdertime());
        viewHolder.status.setText("订单状态: "+groups.get(position).getStatus());
//            }
//        }else if (statu.equals("1")){
//            if (!groups.get(position).getStatus().toString().equals("待审核")){
//                viewHolder.number.setText("订单编号: "+groups.get(position).getOrdernumber());
//                viewHolder.time.setText("提交日期: "+groups.get(position).getOrdertime());
//                viewHolder.status.setText("订单状态: "+groups.get(position).getStatus());
//            }
//        }

//        if (statu.equals("1")){
//            viewHolder.time.setText(R.string.submission_date);
//        }else {
//            viewHolder.time.setText(R.string.date_generated);
//        }
        return convertView;
    }

    static class ViewHolder {
        TextView number;
        TextView time;
        TextView status;
    }

}
