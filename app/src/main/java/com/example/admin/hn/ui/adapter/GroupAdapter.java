package com.example.admin.hn.ui.adapter;


import android.content.Context;

import com.example.admin.hn.R;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

public class GroupAdapter extends CommonAdapter<String> {
	public GroupAdapter(Context context, int layoutId, List datas) {
		super(context, layoutId, datas);
	}

	@Override
	protected void convert(ViewHolder viewHolder, String item, int position) {
		viewHolder.setText(R.id.tv_group_item, item);
	}
}