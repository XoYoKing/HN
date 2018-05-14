package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.hn.R;

import com.example.admin.hn.model.OrderNotUseInfo;
import com.example.admin.hn.model.OrderUseInfo;
import com.example.admin.hn.utils.AbDateUtil;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolString;
import com.example.admin.hn.utils.ToolViewUtils;
import com.example.admin.hn.widget.ExtendedEditText;
import com.orhanobut.logger.Logger;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duantao
 *
 * @date on 2017/7/31 15:35
 */
public class MaterialNotSelectAdapter extends CommonAdapter<OrderNotUseInfo>{

    private List<OrderNotUseInfo> selectList = new ArrayList<>();

    //获取选中的数据对象
    public List<OrderNotUseInfo> getSelectList() {
        return selectList;
    }

    public MaterialNotSelectAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, final OrderNotUseInfo info, int position) {
        ImageView img_select = viewHolder.getView(R.id.img_select);
        final ExtendedEditText tv_buy_number = viewHolder.getView(R.id.tv_buy_number);
//        viewHolder.setText(R.id.tv_inventory, info.storage_amount + "");
        viewHolder.setText(R.id.tv_date,  AbDateUtil.getStringByFormat(info.publis_at,AbDateUtil.dateFormatYMD)  + "");
        if (ToolString.isEmpty(info.chs_name)) {
            viewHolder.setText(R.id.tv_chinese_name, info.chs_name + "");
        }else {
            viewHolder.setText(R.id.tv_chinese_name, info.eng_name + "");
        }
        viewHolder.setText(R.id.tv_data_number, info.code + "");
        img_select.setSelected(info.isSelect);
        tv_buy_number.setText(info.quantity+"");
        ToolViewUtils.setSelection(tv_buy_number);
        viewHolder.itemView.setOnClickListener(new MyOnClickListener(img_select, tv_buy_number, info));
        tv_buy_number.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tv_buy_number.addTextChangedListener(new MyTextWatcher(tv_buy_number, info));
                }else {
                    tv_buy_number.clearTextChangedListeners();
                }
            }
        });
    }

    private void add(OrderNotUseInfo item,TextView tv_buy_number) {
        int number = 0;//goum购买数量默认为1
        try {
            String tv_number = tv_buy_number.getText().toString();
            number = Integer.parseInt(tv_number);
            Logger.i("number", number + "");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            number = 1;
        }
        item.quantity = number;
        selectList.add(item);
    }

    private class MyTextWatcher implements TextWatcher{
        private OrderNotUseInfo info;
        private EditText tv_buy_number;

        public MyTextWatcher(EditText tv_buy_number, OrderNotUseInfo item) {
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
//                if (info.storage_amount!=0 && number > info.storage_amount) {//购买数量大于库存
//                    ToolAlert.showToast(mContext, "库存不足！", false);
//                    tv_buy_number.setText(info.storage_amount + "");
//                    info.quantity = info.storage_amount;
//                    ToolViewUtils.setSelection(tv_buy_number);
//                }else {
//                    if (info.storage_amount == 0) {
//                        info.quantity = 0;
//                    }else {
//
//                    }
//                }
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

    private class MyOnClickListener implements View.OnClickListener{
        private OrderNotUseInfo info;
        private EditText tv_buy_number;
        private ImageView img_select;
        public MyOnClickListener(ImageView img_select, EditText tv_buy_number, OrderNotUseInfo item) {
            this.img_select = img_select;
            this.tv_buy_number = tv_buy_number;
            this.info = item;
        }

        @Override
        public void onClick(View v) {
            if (info.isSelect) {
                info.isSelect = false;
                selectList.remove(info);
            }else {
                info.isSelect = true;
                add(info, tv_buy_number);
            }
            img_select.setSelected(info.isSelect);
        }
    }
}
