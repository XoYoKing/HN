package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.example.admin.hn.R;
import com.example.admin.hn.model.OrderInfo;
import com.example.admin.hn.widget.AlertDialog;
import com.orhanobut.logger.Logger;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by duantao
 *
 * @date on 2017/7/31 15:35
 */
public class MaterialSelectAdapter extends CommonAdapter<OrderInfo.Order> {


    public MaterialSelectAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, OrderInfo.Order item, final int position) {

        viewHolder.setOnClickListener(R.id.tv_cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog dialog = new AlertDialog(mContext);
                dialog.setBtCancel("取消");
                dialog.setBtConfirm("确定");
                dialog.showDialog("删除订单", "是否确认删除此订单", new AlertDialog.DialogOnClickListener() {
                    @Override
                    public void onPositiveClick() {
                        remove(position);
                        dialog.dismiss();
                    }

                    @Override
                    public void onNegativeClick() {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    public void remove(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
        Logger.e("list","size="+mDatas.size()+";position="+position);
    }

}
