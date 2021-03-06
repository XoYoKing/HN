package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.model.GoodsInfo;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 商品规格适配器
 */
public class GoodsSpecTypeAdapter extends CommonAdapter<GoodsInfo.SpecInfo> {
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

    public GoodsSpecTypeAdapter(Context context, int layoutId, List datas,Map<Integer, String> select_id) {
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
        TagFlowLayout flow_spec_type = viewHolder.getView(R.id.flow_spec_type);
        MyAdapter myAdapter = new MyAdapter(info.specItem, position);
        flow_spec_type.setAdapter(myAdapter);
        for (int i = 0; i < info.specItem.size(); i++) {
            GoodsInfo.SpecInfo.SpecItem specItem = info.specItem.get(i);
            if (specItem.isSelect) {
                myAdapter.setSelectedList(i);
            }
        }
    }

    class MyAdapter extends TagAdapter<GoodsInfo.SpecInfo.SpecItem> {
        private List<GoodsInfo.SpecInfo.SpecItem> data;
        private int groupItem;//父下标
        public MyAdapter(List<GoodsInfo.SpecInfo.SpecItem> datas,int groupItem) {
            super(datas);
            this.data = datas;
            this.groupItem = groupItem;
        }

        @Override
        public View getView(FlowLayout parent,final int position, GoodsInfo.SpecInfo.SpecItem specItem) {
            TextView textView = (TextView) View.inflate(context, R.layout.goods_space_item, null);
            textView.setText(specItem.specItemName + "");
            if (data.size() > 1) {
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeData(groupItem,position);
                        reckon(select_id);
                    }
                });
            }
            return textView;
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
