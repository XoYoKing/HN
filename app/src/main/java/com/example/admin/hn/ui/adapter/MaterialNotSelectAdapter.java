package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.example.admin.hn.R;
import com.example.admin.hn.model.OrderInfo;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * Created by duantao
 *
 * @date on 2017/7/31 15:35
 */
public class MaterialNotSelectAdapter extends CommonAdapter<OrderInfo.Order> {

    public MaterialNotSelectAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, OrderInfo.Order item, int position) {
        ImageView view = viewHolder.getView(R.id.img_select);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
