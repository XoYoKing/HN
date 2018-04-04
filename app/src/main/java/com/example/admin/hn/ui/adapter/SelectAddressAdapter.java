package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.view.View;

import com.example.admin.hn.R;
import com.example.admin.hn.model.AddressInfo;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

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
//        viewHolder.setChecked(R.id.select_address, info.isSelect());
//        viewHolder.setText(R.id.goods_receipt_phone, info.getMobile());
//        viewHolder.setText(R.id.goods_receipt_name, "收货人：" + info.getConsignee());
//        viewHolder.setText(R.id.tv_address, AbStringUtil.getAddress(info.getProvince(), info.getCity(), info.getDistrict(), info.getAddress()));
//        viewHolder.setOnClickListener(R.id.select_linear, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (selectAddressClick != null) {
//                    selectAddressClick.selectAddressClick(info);
//                }
//            }
//        });
    }

    public interface OnSelectAddressClick {
        void selectAddressClick(AddressInfo info);
    }

}
