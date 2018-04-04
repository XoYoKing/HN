package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.hn.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by duantao
 *
 * @date on 2017/8/2 10:27
 *
 * 仓库页面适配器
 */

public class InventoryAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<HashMap<String, String>> list;
    private int currentItem = -1; //用于记录点击的 Item 的 position，是控制 item 展开的核心

    public InventoryAdapter(Context context,
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
            convertView = LayoutInflater.from(context).inflate(R.layout.time3, parent, false);
            holder = new ViewHolder();

            holder.showArea = (LinearLayout) convertView.findViewById(R.id.layout_showArea);
            holder.tv_shipname = (TextView) convertView
                    .findViewById(R.id.tv_shipname);
            holder.textview_number = (TextView) convertView
                    .findViewById(R.id.textview_number);
            holder.textview_time = (TextView) convertView
                    .findViewById(R.id.textview_time);
            holder.tv_number = (TextView) convertView
                    .findViewById(R.id.tv_number);
            holder.tv_time = (TextView) convertView
                    .findViewById(R.id.tv_time);
            holder.tv_chart_title = (TextView) convertView
                    .findViewById(R.id.tv_chart_title);
            holder.tv_title = (TextView) convertView
                    .findViewById(R.id.tv_title);
            holder.tv_money = (TextView) convertView
                    .findViewById(R.id.tv_money);
            holder.tv_num = (TextView) convertView
                    .findViewById(R.id.tv_num);
            holder.hideArea = (LinearLayout) convertView.findViewById(R.id.layout_hideArea);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HashMap<String, String> item = list.get(position);

        // 注意：我们在此给响应点击事件的区域（我的例子里是 showArea 的线性布局）添加Tag，为了记录点击的 position，我们正好用 position 设置 Tag
        holder.showArea.setTag(position);

        holder.tv_shipname.setText("船舶名称：" + item.get("shipname"));
        holder.textview_number.setText("海图标题："+ item.get("productTitle"));
        holder.textview_time.setText("海图类型："+item.get("ProductType"));
        holder.tv_number.setText("船舶编号："+item.get("shipnumber"));
        holder.tv_time.setText("海图编号："+item.get("productNumber"));
        holder.tv_chart_title.setText("提交人："+item.get("cname"));
        holder.tv_title.setText("日期："+item.get("ordertime"));
        holder.tv_money.setText("版本号："+item.get("version"));
        holder.tv_num.setText("周期："+item.get("period"));



        //根据 currentItem 记录的点击位置来设置"对应Item"的可见性（在list依次加载列表数据时，每加载一个时都看一下是不是需改变可见性的那一条）
        if (currentItem == position) {
            holder.hideArea.setVisibility(View.VISIBLE);
        } else {
            holder.hideArea.setVisibility(View.GONE);
        }

        holder.showArea.setOnClickListener(new View.OnClickListener() {

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

        private TextView tv_shipname;
        private TextView textview_number;
        private TextView textview_time;
        private TextView tv_number;
        private TextView tv_time;
        private TextView tv_chart_title;
        private TextView tv_title;
        private TextView tv_money;
        private TextView tv_num;


        private LinearLayout hideArea;
    }
}