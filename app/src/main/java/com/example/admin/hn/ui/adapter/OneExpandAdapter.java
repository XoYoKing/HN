package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 点击item展开隐藏部分,再次点击收起
 * 只可展开一条记录
 * 
 * @author WangJ
 * @date 2016.01.31
 */
public class OneExpandAdapter extends CommonAdapter<HashMap<String, String>> {

	private int currentItem = -1; //用于记录点击的 Item 的 position，是控制 item 展开的核心

	public OneExpandAdapter(Context context, int layoutId, List datas) {
		super(context, layoutId, datas);
	}

	@Override
	protected void convert(ViewHolder holder, HashMap<String, String> item, int position) {
		// 注意：我们在此给响应点击事件的区域（我的例子里是 showArea 的线性布局）添加Tag，为了记录点击的 position，我们正好用 position 设置 Tag
		LinearLayout showArea = holder.getView(R.id.layout_showArea);
		RelativeLayout hideArea = holder.getView(R.id.layout_hideArea);
		showArea.setTag(position);
		holder.setText(R.id.textview_number,"资料号" + item.get("dataNumber"));
		holder.setText(R.id.textview_time,"海图版本号：" + item.get("version"));
		//资料号
		holder.setText(R.id.tv_time,item.get("chartTitle"));
		holder.setText(R.id.tv_title,item.get("Price"));
		holder.setText(R.id.tv_num,item.get("cycle"));

		//根据 currentItem 记录的点击位置来设置"对应Item"的可见性（在list依次加载列表数据时，每加载一个时都看一下是不是需改变可见性的那一条）
		if (currentItem == position) {
			hideArea.setVisibility(View.VISIBLE);
		} else {
			hideArea.setVisibility(View.GONE);
		}
		showArea.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				//用 currentItem 记录点击位置
				int tag = (Integer) view.getTag();
				if (tag == currentItem) { //再次点击
					currentItem = -1; //给 currentItem 一个无效值
				} else {
					currentItem = tag;
				}
				//通知adapter数据改变需要重新加载
				notifyDataSetChanged(); //必须有的一步
			}
		});
	}
}
