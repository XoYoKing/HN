package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.model.SpecGoodsPriceInfo;
import com.example.admin.hn.model.SpecInfo;
import com.example.admin.hn.model.SpecTypeInfo;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;
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
public class GoodsSpecTypeAdapter extends CommonAdapter<SpecTypeInfo> {
    private Context context;

    private List<SpecTypeInfo> spec_data;
    private Map<Integer, String> select_id = new HashMap<>();
    private SpecGoodsPriceInfo goodsPriceInfo;


    public GoodsSpecTypeAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
        this.context = context;
        this.spec_data = datas;
        for (int i = 0; i < spec_data.size(); i++) {
            SpecTypeInfo typeInfo = spec_data.get(i);
            SpecInfo specInfo = typeInfo.getData().get(0);
            select_id.put(i, specInfo.getItem_id());
        }
    }

    //被选中的子控件
    private int childPosition;
    private int groupPosition;

    public void changeData(int group, int child) {
        childPosition = child;
        groupPosition = group;
        SpecTypeInfo typeInfo = spec_data.get(group);
        SpecInfo specInfo = typeInfo.getData().get(child);
        specInfo.setCheck_id(child);
        select_id.put(group, specInfo.getItem_id());
        notifyDataSetChanged();
    }

    @Override
    protected void convert(ViewHolder viewHolder, final SpecTypeInfo info, final int position) {
        viewHolder.setText(R.id.tv_spec_type, "选择" + info.getTitle());
        TagFlowLayout flow_spec_type = viewHolder.getView(R.id.flow_spec_type);
        final MyAdapter adapter = new MyAdapter(info.getData(), position);
        flow_spec_type.setAdapter(adapter);
        if (groupPosition == position) {
            adapter.setSelectedList(childPosition);
        } else {
            adapter.setSelectedList(0);
        }
        if (info.getData().size() > 1) {
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
        while (iterator.hasNext()) {
            Map.Entry<Integer, String> entry = (Map.Entry<Integer, String>) iterator.next();
            String value = entry.getValue();
            buffer.append(value).append("_");
        }
        String key = buffer.toString();
        sendHttp(key.substring(0, key.length() - 1));
    }

    private void sendHttp(String key) {
//        RequestParams params = new RequestParams();
//        params.put("key", key);
//        AbLogUtil.e("key", key);
//        IRequest.post(context, Config.URL_GET_GOODS_SPCINFO, params, "加载中...", new RequestListener() {
//            @Override
//            public void requestSuccess(String json) {
//                AbLogUtil.e("json", json);
//                if (AbJsonUtil.isSuccess(json)) {
//                    goodsPriceInfo = (SpecGoodsPriceInfo) AbJsonUtil.fromJson(json, SpecGoodsPriceInfo.class);
//                    if (goodsPriceInfo != null) {
//                        if (selectSpecListener != null) {
//                            selectSpecListener.onSelectSpecListener(goodsPriceInfo);
//                        }
//                    }
//                } else {
//                    AbToastUtil.showToast(context, AbJsonUtil.getError(json));
//                }
//            }
//
//            @Override
//            public void requestError(String message) {
//                AbToastUtil.showToast(context, message);
//            }
//        });
    }

    private OnSelectSpecListener selectSpecListener;

    public OnSelectSpecListener getSelectSpecListener() {
        return selectSpecListener;
    }

    public void setSelectSpecListener(OnSelectSpecListener selectSpecListener) {
        this.selectSpecListener = selectSpecListener;
    }

    public interface OnSelectSpecListener {
        void onSelectSpecListener(SpecGoodsPriceInfo specGoodsPriceInfo);
    }

    class MyAdapter extends TagAdapter<SpecInfo> {

        private int position;

        public MyAdapter(List datas, int groupItem) {
            super(datas);
            this.position = groupItem;
        }

        @Override
        public View getView(FlowLayout parent, int position, SpecInfo s) {
            TextView bt = (TextView) View.inflate(context, R.layout.goods_space_item, null);
            bt.setText(s.getItem());
            return bt;
        }
    }
}
