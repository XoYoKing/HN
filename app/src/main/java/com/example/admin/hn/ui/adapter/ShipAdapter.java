package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;

import com.example.admin.hn.R;
import com.example.admin.hn.model.ShipInfo;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * Created by duantao
 *
 * @date on 2017/10/20 13:48
 */
public class ShipAdapter extends CommonAdapter<ShipInfo.Ship>{
    private boolean isSingle;//区分是单选还是多选 true是单选  默认false是多选
    private boolean isClick;//区分是否可以点击选中 true可以 false不可以
    public ShipAdapter(Context context, int layoutId, List datas,boolean isSingle,boolean isClick) {
        super(context, layoutId, datas);
        this.isSingle = isSingle;
        this.isClick = isClick;
    }

    @Override
    protected void convert(ViewHolder viewHolder, final ShipInfo.Ship item, final int position) {
        final CheckBox checkBox = viewHolder.getView(R.id.cb_status);
        viewHolder.setText(R.id.tv_name,item.shipname);
        viewHolder.setChecked(R.id.cb_status,item.isSelect);
        if (isClick) {
            viewHolder.setOnClickListener(R.id.ll, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isSingle) {
                        for (int i = 0; i < mDatas.size(); i++) {
                            ShipInfo.Ship ship = mDatas.get(i);
                            if (position == i) {
                                //把当前设置为选中
                                ship.isSelect = true;
                            }else {
                                //其他设置为 未选中
                                ship.isSelect = false;
                            }
                        }
                    }else {
                        //多选
                        item.isSelect = !checkBox.isChecked();
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }
}
