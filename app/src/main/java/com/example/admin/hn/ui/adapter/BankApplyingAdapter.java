package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.example.admin.hn.R;
import com.example.admin.hn.model.OrderInfo;
import com.example.admin.hn.ui.account.AuditingApplyingActivity;
import com.example.admin.hn.utils.ToolAlert;
import com.orhanobut.logger.Logger;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 *
 * @date on 2017/7/31 15:35
 */
public class BankApplyingAdapter extends CommonAdapter<OrderInfo.Order> {

    private boolean isDeleteAble = true;

    public BankApplyingAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, OrderInfo.Order item, final int position) {
        viewHolder.setOnClickListener(R.id.linear1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuditingApplyingActivity.startActivity(mContext, 1, "");
            }
        });
        viewHolder.setOnClickListener(R.id.bt_submit, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolAlert.dialog(mContext, "审核确认", "是否确认通过此订单", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        pass(position);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuditingApplyingActivity.startActivity(mContext, 1, "");
            }
        });
    }

    //审核通过
    private void pass(int position) {
        if (isDeleteAble) {//此时为增加动画效果，刷新部分数据源，防止删除错乱
            isDeleteAble = false;//初始值为true,当点击删除按钮以后，休息0.5秒钟再让他为
            //true,起到让数据源刷新完成的作用
            mDatas.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(300);//休息
                        isDeleteAble = true;//可点击按钮
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
