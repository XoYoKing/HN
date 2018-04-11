package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.example.admin.hn.R;
import com.example.admin.hn.model.OrderInfo;
import com.example.admin.hn.widget.AlertDialog;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * Created by duantao
 *
 * @date on 2017/7/31 15:35
 */
public class MaterialSelectAdapter extends CommonAdapter<OrderInfo.Order> {


    private  Context context;

    public MaterialSelectAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder viewHolder, OrderInfo.Order item, int position) {

        viewHolder.setOnClickListener(R.id.tv_cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog dialog = new AlertDialog(context);
                dialog.setBtCancel("取消");
                dialog.setBtConfirm("确定");
                dialog.showDialog("删除订单", "是否确认删除此订单", new AlertDialog.DialogOnClickListener() {
                    @Override
                    public void onPositiveClick() {
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

}
