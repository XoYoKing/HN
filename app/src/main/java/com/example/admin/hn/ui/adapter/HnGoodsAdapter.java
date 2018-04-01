package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.admin.hn.R;
import java.util.List;

/**
 * 海宁商品
 */

public class HnGoodsAdapter extends BaseAdapter{

    private final LayoutInflater mInflater;
    private final List list;

    public HnGoodsAdapter(Context context, List list) {
        mInflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHolder viewHolder;
        if (convertView == null) {
            viewHolder = new MyHolder();
            convertView = mInflater.inflate(R.layout.item_hnshop_layout, null);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MyHolder) convertView.getTag();
        }
        return convertView;
    }

    class MyHolder  {

        private ImageView mall_img;
        private TextView shopping_title;
        private TextView integral_mall_amount;
        private View view;

    }
}
