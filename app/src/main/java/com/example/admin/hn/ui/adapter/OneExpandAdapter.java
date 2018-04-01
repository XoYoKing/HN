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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 点击item展开隐藏部分,再次点击收起
 * 只可展开一条记录
 * 
 * @author WangJ
 * @date 2016.01.31
 */
public class OneExpandAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<HashMap<String, String>> list;
	private int currentItem = -1; //用于记录点击的 Item 的 position，是控制 item 展开的核心

	public OneExpandAdapter(Context context,
			ArrayList<HashMap<String, String>> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_2, parent, false);
			holder = new ViewHolder();
			holder.showArea = (LinearLayout) convertView.findViewById(R.id.layout_showArea);
			holder.textview_number = (TextView) convertView
					.findViewById(R.id.textview_number);
			holder.textview_time = (TextView) convertView
					.findViewById(R.id.textview_time);
			holder.tv_time = (TextView) convertView
					.findViewById(R.id.tv_time);
			holder.tv_title = (TextView) convertView
					.findViewById(R.id.tv_title);
			holder.tv_num = (TextView) convertView
					.findViewById(R.id.tv_num);
			holder.hideArea = (RelativeLayout) convertView.findViewById(R.id.layout_hideArea);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		HashMap<String, String> item = list.get(position);
		
		// 注意：我们在此给响应点击事件的区域（我的例子里是 showArea 的线性布局）添加Tag，为了记录点击的 position，我们正好用 position 设置 Tag
		holder.showArea.setTag(position);
		
		holder.textview_number.setText("资料号" + item.get("dataNumber"));
		holder.textview_time.setText("海图版本号：" + item.get("version"));
		//资料号
		holder.tv_time.setText(item.get("chartTitle"));
		holder.tv_title.setText(item.get("Price"));
		holder.tv_num.setText(item.get("cycle"));

		//根据 currentItem 记录的点击位置来设置"对应Item"的可见性（在list依次加载列表数据时，每加载一个时都看一下是不是需改变可见性的那一条）
		if (currentItem == position) {
			holder.hideArea.setVisibility(View.VISIBLE);
		} else {
			holder.hideArea.setVisibility(View.GONE);
		}

		holder.showArea.setOnClickListener(new OnClickListener() {

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
		return convertView;
	}

	private static class ViewHolder {
		private LinearLayout showArea;

		private TextView textview_number;
		private TextView textview_time;
		private TextView tv_time;
		private TextView tv_title;
		private TextView tv_num;

		private RelativeLayout hideArea;
	}
}
