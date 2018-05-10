package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.model.GoodsInfo;
import com.example.admin.hn.model.SpecGoodsPriceInfo;
import com.orhanobut.logger.Logger;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.nio.Buffer;
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
    private Map<Integer, String> select_id = new HashMap<>();
    private SpecGoodsPriceInfo goodsPriceInfo;

    public GoodsSpecTypeAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
        this.context = context;
        this.spec_data = datas;
    }

    //被选中的子控件
    private int childPosition;
    private int groupPosition;

    public void changeData(int group, int child) {
        childPosition = child;
        groupPosition = group;
        GoodsInfo.SpecInfo spec = spec_data.get(group);
        GoodsInfo.SpecInfo.SpecItem specItem = spec.specItem.get(child);
        specItem.isSelect = true;
        select_id.put(group, specItem.id);
        notifyDataSetChanged();
    }

    @Override
    protected void convert(ViewHolder viewHolder, final GoodsInfo.SpecInfo info, final int position) {
        viewHolder.setText(R.id.tv_spec_type, info.spec.specName+"");
        TagFlowLayout flow_spec_type = viewHolder.getView(R.id.flow_spec_type);
        final MyAdapter adapter = new MyAdapter(info.specItem, position);
        flow_spec_type.setAdapter(adapter);
        if (groupPosition == position && info.specItem.get(childPosition).isSelect) {
            adapter.setSelectedList(childPosition);
        }
        if (info.specItem.size() > 1) {
            flow_spec_type.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                @Override
                public boolean onTagClick(View view, int item, FlowLayout parent) {
                    changeData(position, item);
                    reckon(select_id);
                    return false;
                }
            });
        }

    }

    /**
     * 计算选中的
     */
    private void reckon(Map<Integer, String> map) {
        Iterator iterator = map.entrySet().iterator();
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        while (iterator.hasNext()) {
            Map.Entry<Integer, String> entry = (Map.Entry<Integer, String>) iterator.next();
            String value = entry.getValue();
            buffer.append(value).append(",");
        }
        String substring = buffer.substring(0, buffer.length() - 1);
        StringBuffer buffer1 = new StringBuffer(substring);
        buffer1.append("]");
        String key = buffer1.toString();
        sendHttp(key);
    }

    private void sendHttp(String key) {
        if (selectSpecListener != null) {
            selectSpecListener.onSelectSpecListener(key.substring(0, key.length()));
        }
    }

    private OnSelectSpecListener selectSpecListener;

    public OnSelectSpecListener getSelectSpecListener() {
        return selectSpecListener;
    }

    public void setSelectSpecListener(OnSelectSpecListener selectSpecListener) {
        this.selectSpecListener = selectSpecListener;
    }

    public interface OnSelectSpecListener {
        void onSelectSpecListener(String spec);
    }

    class MyAdapter extends TagAdapter<GoodsInfo.SpecInfo.SpecItem> {

        private int position;

        public MyAdapter(List datas, int groupItem) {
            super(datas);
            this.position = groupItem;
        }

        @Override
        public View getView(FlowLayout parent, int position, GoodsInfo.SpecInfo.SpecItem s) {
            TextView bt = (TextView) View.inflate(context, R.layout.goods_space_item, null);
            bt.setText(s.specItemName);
            return bt;
        }
    }
}
