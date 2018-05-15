package com.example.admin.hn.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.example.admin.hn.R;
import com.example.admin.hn.model.SearchInfo;
import com.example.admin.hn.ui.shop.SearchActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;


import java.util.List;



public class SearchAdapter extends CommonAdapter<SearchInfo> {

    private SearchActivity activity;
    public SearchAdapter(Context context, int layoutId, List<SearchInfo> datas) {
        super(context, layoutId, datas);
        activity = (SearchActivity) context;
    }

    @Override
    protected void convert(ViewHolder viewHolder, final SearchInfo info, int position) {
        viewHolder.setText(R.id.search_item, info.getName());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.select(info.getName());
            }
        });
    }

}
