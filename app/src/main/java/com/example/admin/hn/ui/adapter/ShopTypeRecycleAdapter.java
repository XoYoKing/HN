package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.model.HomeTypeInfo;
import com.example.admin.hn.utils.ToolViewUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;


public class ShopTypeRecycleAdapter extends CommonAdapter<HomeTypeInfo> {

    public ShopTypeRecycleAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, HomeTypeInfo info, int position) {
//        ToolViewUtils.glideImageList(info.getImgUrl(), (ImageView) holder.getView(R.id.type_icon),  R.drawable.load_fail);
//        holder.setText(R.id.type_name,info.getName());
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                GoodsListActivity.startActivity(context, info);
//            }
//        });
    }
}
