package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.example.admin.hn.R;
import com.example.admin.hn.ui.fragment.shop.bean.ShopCartInfo;
import com.example.admin.hn.utils.AbMathUtil;
import com.example.admin.hn.utils.ToolViewUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;


import java.util.List;



/**
 * Created by hjy on 2016/11/5.
 * 确认订单
 */
public class FirmOrderAdapter extends CommonAdapter<ShopCartInfo> {

    public FirmOrderAdapter(Context context, int layoutId, List<ShopCartInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, ShopCartInfo info, int position) {
        viewHolder.setText(R.id.goods_name, info.getGoodsFullName()+ "");
        viewHolder.setText(R.id.goods_usp, info.getUsp()+"");
        viewHolder.setText(R.id.goods_price, "￥" + AbMathUtil.roundStr(info.getGoodsPrice(),2));
        viewHolder.setText(R.id.goods_number, "X " + info.getBuyNumber());
        ToolViewUtils.glideImageList(info.getImageUrl(), (ImageView) viewHolder.getView(R.id.goods_img), R.drawable.load_fail);
    }

}
