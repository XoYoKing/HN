package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.example.admin.hn.R;
import com.example.admin.hn.model.OrderInfo;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolString;
import com.example.admin.hn.widget.AlertDialog;
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
        final EditText tv_buy_number = viewHolder.getView(R.id.tv_buy_number);
        viewHolder.setOnClickListener(R.id.tv_cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolAlert.dialog(mContext, "删除订单", "是否确认删除此订单", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        remove(position);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }
        });
//        tv_buy_number.setText(item.buyNumber+"");
        tv_buy_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ToolString.isEmpty(s.toString())) {
                    int number = Integer.parseInt(s.toString());
//                    if (number > item.inventory) {
//                        ToolAlert.showToast(mContext, "库存不足！", false);
//                        tv_buy_number.setText(item.inventory + "");
//                    }
                }
            }
        });
    }

    private boolean isDeleteAble=true;
    public void remove(int position) {
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
