package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.hn.R;

import com.example.admin.hn.model.OrderUseInfo;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolString;
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
public class MaterialNotSelectAdapter extends CommonAdapter<OrderUseInfo.OrderUser> {

    private List< OrderUseInfo.OrderUser> selectList = new ArrayList<>();

    //获取选中的数据对象
    public List<OrderUseInfo.OrderUser> getSelectList() {
        return selectList;
    }

    public MaterialNotSelectAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, final OrderUseInfo.OrderUser item, int position) {
        final ImageView img_select = viewHolder.getView(R.id.img_select);
        final EditText tv_buy_number = viewHolder.getView(R.id.tv_buy_number);
        viewHolder.setText(R.id.tv_inventory, item.inventory + "");
        viewHolder.setText(R.id.tv_date, item.date + "");
        viewHolder.setText(R.id.tv_data_number, item.dateNumber + "");
        img_select.setSelected(item.isSelect);
        img_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.isSelect) {
                    item.isSelect = false;
                    selectList.remove(item);
                }else {
                    item.isSelect = true;
                    add(item, tv_buy_number);
                }
                img_select.setSelected(item.isSelect);
            }
        });

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
                    if (number > item.inventory) {
                        ToolAlert.showToast(mContext, "库存不足！", false);
                        tv_buy_number.setText(item.inventory + "");
                    }
                }
            }
        });
    }

    private void add(OrderUseInfo.OrderUser item,TextView tv_buy_number) {
        int number = 0;//goum购买数量默认为1
        try {
            String tv_number = tv_buy_number.getText().toString();
            number = Integer.parseInt(tv_number);
            Logger.i("number", number + "");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            number = 1;
        }
        item.buyNumber = number;
        Logger.i("  item.buyNumber", item.buyNumber + "");
        selectList.add(item);
    }

}
