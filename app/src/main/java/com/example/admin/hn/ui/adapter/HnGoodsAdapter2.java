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

public class HnGoodsAdapter2 extends RecyclerView.Adapter<HnGoodsAdapter2.MyHolder>{

    private final LayoutInflater mInflater;
    private final List list;

    public HnGoodsAdapter2(Context context, List list) {
        mInflater = LayoutInflater.from(context);
        this.list = list;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_hnshop_layout, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        private ImageView mall_img;
        private TextView shopping_title;
        private TextView integral_mall_amount;
        private View view;

        public MyHolder(View itemView) {
            super(itemView);
        }
    }
}
