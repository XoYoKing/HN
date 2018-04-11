package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.view.View;

import com.example.admin.hn.MainActivity;
import com.example.admin.hn.R;
import com.example.admin.hn.model.OrderInfo;
import com.example.admin.hn.ui.account.AuditingApplyingActivity;
import com.example.admin.hn.widget.AlertDialog;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * Created by duantao
 *
 * @date on 2017/7/31 15:35
 */
public class BankApplyingAdapter extends CommonAdapter<OrderInfo.Order> {
    private Context context;
    public BankApplyingAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder viewHolder, OrderInfo.Order item, int position) {
        viewHolder.setOnClickListener(R.id.linear1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuditingApplyingActivity.startActivity(context, 1, "");
            }
        });
        viewHolder.setOnClickListener(R.id.bt_submit, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog dialog = new AlertDialog(context);
                dialog.setBtCancel("取消");
                dialog.setBtConfirm("确定");
                dialog.showDialog("审核确认", "是否确认通过此订单", new AlertDialog.DialogOnClickListener() {
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
