package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.example.admin.hn.R;
import com.example.admin.hn.model.OrderInfo;
import com.example.admin.hn.model.OrderNotUseInfo;
import com.example.admin.hn.model.OrderUseInfo;
import com.example.admin.hn.utils.AbDateUtil;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolString;
import com.example.admin.hn.utils.ToolViewUtils;
import com.example.admin.hn.widget.AlertDialog;
import com.example.admin.hn.widget.ExtendedEditText;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by ty
 *
 * @date on 2017/7/31 15:35
 */
public class MaterialSelectAdapter extends CommonAdapter<OrderUseInfo> {


    public MaterialSelectAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder,final OrderUseInfo item, final int position) {
        final ExtendedEditText tv_buy_number = viewHolder.getView(R.id.tv_buy_number);
//        viewHolder.setText(R.id.tv_type, item.category_name + "");
        viewHolder.setText(R.id.tv_ship_name, item.ship_name + "");
        viewHolder.setText(R.id.tv_name, item.chs_name + "");
        viewHolder.setText(R.id.tv_number, item.code + "");
        viewHolder.setText(R.id.tv_date, AbDateUtil.getStringByFormat(item.publish_at,AbDateUtil.dateFormatYMD)  + "");
//        viewHolder.setText(R.id.tv_inventory, item.storage_amount + "");
        tv_buy_number.setText(item.quantity+"");
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
        ToolViewUtils.setSelection(tv_buy_number);
        tv_buy_number.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tv_buy_number.addTextChangedListener(new MyTextWatcher(tv_buy_number, item));
                }else {
                    tv_buy_number.setText(item.quantity+"");//当失去焦点的时候设置输入框的值
                    tv_buy_number.clearTextChangedListeners();
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

    private class MyTextWatcher implements TextWatcher{
        private OrderUseInfo info;
        private EditText tv_buy_number;

        public MyTextWatcher(EditText tv_buy_number, OrderUseInfo item) {
            this.tv_buy_number = tv_buy_number;
            this.info = item;
        }
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
                if (number == 0) {
                    info.quantity = 1;
                    tv_buy_number.setText(info.quantity + "");
                    ToolViewUtils.setSelection(tv_buy_number);
                }else {
                    info.quantity = number;
                }
            }
        }
    }
}
