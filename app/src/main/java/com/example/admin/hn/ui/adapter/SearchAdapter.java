package com.example.admin.hn.ui.adapter;

import android.content.Context;

import com.example.admin.hn.R;
import com.example.admin.hn.model.SearchInfo;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;



public class SearchAdapter extends CommonAdapter<SearchInfo> {

    public SearchAdapter(Context context, int layoutId, List<SearchInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, SearchInfo info, int position) {
        viewHolder.setText(R.id.search_item, info.getName());
    }


}
