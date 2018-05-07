package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.view.View;

import com.example.admin.hn.R;
import com.example.admin.hn.model.AddressInfo;
import com.example.admin.hn.ui.shop.CreateAddressActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;


/**
 * Created by hjy on 2016/11/5.
 * 选择地址
 */
public class SelectAddressAdapter extends CommonAdapter<AddressInfo> {
    private OnSelectAddressClick selectAddressClick;

    public SelectAddressAdapter(Context context, int layoutId, List<AddressInfo> datas) {
        super(context, layoutId, datas);
    }

    public void setSelectAddressClick(OnSelectAddressClick selectAddressClick) {
        this.selectAddressClick = selectAddressClick;
    }

    @Override
    protected void convert(ViewHolder viewHolder, final AddressInfo info, int position) {
//        viewHolder.setChecked(R.id.select_address, info.isDefaul!=1);
        if (info.isDefaul == 1) {
            viewHolder.getView(R.id.select_address).setVisibility(View.VISIBLE);
        }else {
            viewHolder.getView(R.id.select_address).setVisibility(View.GONE);
        }
        viewHolder.setText(R.id.goods_receipt_phone, info.phone);
        viewHolder.setText(R.id.goods_receipt_name, info.receiverName+"");
        viewHolder.setText(R.id.tv_address,info.areaName+" "+info.receiverAddr);
        viewHolder.setOnClickListener(R.id.select_linear, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectAddressClick != null) {
                    selectAddressClick.selectAddressClick(info);
                }
            }
        });
        viewHolder.setOnClickListener(R.id.img_edit, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAddressActivity.startActivity(mContext,info);
            }
        });
    }

    public interface OnSelectAddressClick {
        void selectAddressClick(AddressInfo info);
    }

}
