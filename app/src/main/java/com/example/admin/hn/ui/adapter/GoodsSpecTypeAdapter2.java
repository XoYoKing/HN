package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.model.GoodsInfo;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 商品规格适配器
 */
public class GoodsSpecTypeAdapter2 extends CommonAdapter<GoodsInfo.SpecInfo> {
    private Context context;
    private List<GoodsInfo.SpecInfo> spec_data;
    private Map<Integer, String> select_id ;


    public void setSelect_id(Map<Integer, String> select_id) {
        this.select_id = select_id;
    }

    private OnSelectSpecListener selectSpecListener;

    public void setSelectSpecListener(OnSelectSpecListener selectSpecListener) {
        this.selectSpecListener = selectSpecListener;
    }

    public GoodsSpecTypeAdapter2(Context context, int layoutId, List datas, Map<Integer, String> select_id) {
        super(context, layoutId, datas);
        this.context = context;
        this.spec_data = datas;
        if (select_id == null) {
            this.select_id = new HashMap<>();
        }else {
            this.select_id = select_id;
        }
    }


    @Override
    protected void convert(ViewHolder viewHolder, final GoodsInfo.SpecInfo info, final int position) {
        viewHolder.setText(R.id.tv_spec_type, info.spec.specName+"");
        RecyclerView recycleView = viewHolder.getView(R.id.recycleView);
        recycleView.setNestedScrollingEnabled(false);
        recycleView.setFocusable(false);
        recycleView.setLayoutManager(new GridLayoutManager(mContext, 3));
        MyItemAdapter itemRecycleAdapter = new MyItemAdapter(mContext, R.layout.goods_space_item, info.specItem,position);
        recycleView.setAdapter(itemRecycleAdapter);
    }


    class MyItemAdapter extends CommonAdapter<GoodsInfo.SpecInfo.SpecItem> {

        private List<GoodsInfo.SpecInfo.SpecItem> data;
        private int groupItem;//父下标

        public MyItemAdapter(Context context, int layoutId, List datas,int groupItem) {
            super(context, layoutId, datas);
            this.data = datas;
            this.groupItem = groupItem;
        }

        @Override
        protected void convert(ViewHolder holder, GoodsInfo.SpecInfo.SpecItem specItem, final int position) {
            TextView space_item_name = holder.getView(R.id.space_item_name);
            space_item_name.setText(specItem.specItemName + "");
            if (specItem.isSelect) {
                space_item_name.setSelected(true);
            }else {
                space_item_name.setSelected(false);
            }
            if (data.size() > 1) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeData(groupItem,position);
                        reckon(select_id);
                    }
                });
            }
        }

        public void changeData(int group,int child) {
            for (int i = 0; i < data.size(); i++) {
                if (child != i) {
                    //把非当前点击的都改变成非选中状态
                    data.get(i).isSelect=false;
                }else {
                    GoodsInfo.SpecInfo.SpecItem specItem = data.get(i);
                    specItem.isSelect = true;
                    select_id.put(group, specItem.specItemId);
                }
            }
            notifyDataSetChanged();
        }

        /**
         * 计算选中的
         */
        private void reckon(Map<Integer, String> map) {
            Iterator iterator = map.entrySet().iterator();
            StringBuffer buffer = new StringBuffer();
            while (iterator.hasNext()) {
                Map.Entry<Integer, String> entry = (Map.Entry<Integer, String>) iterator.next();
                String value = entry.getValue();
                buffer.append(value).append(",");
            }
            String key = buffer.substring(0, buffer.length() - 1);
            if (selectSpecListener != null) {
                selectSpecListener.onSelectSpecListener(key.substring(0, key.length()));
            }
        }
    }

    public interface OnSelectSpecListener {
        void onSelectSpecListener(String spec);
    }
}
