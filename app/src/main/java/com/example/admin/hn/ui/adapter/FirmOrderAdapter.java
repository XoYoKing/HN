package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.example.admin.hn.R;
import com.example.admin.hn.model.ShoppingCartInfo;
import com.example.admin.hn.utils.ToolViewUtils;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;



/**
 * Created by hjy on 2016/11/5.
 * 确认订单
 */
public class FirmOrderAdapter extends CommonAdapter<ShoppingCartInfo> {

    public FirmOrderAdapter(Context context, int layoutId, List<ShoppingCartInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, ShoppingCartInfo info, int position) {
//        viewHolder.setText(R.id.goods_price, "￥" + info.getReal_price());
//        viewHolder.setText(R.id.goods_name, info.getGoods_name());
//        viewHolder.setText(R.id.goods_number, "X " + info.getGoods_num());
//        ToolViewUtils.glideImageList(info.getImgUrl(), (ImageView) viewHolder.getView(R.id.goods_icon), R.drawable.delete);
    }

}
