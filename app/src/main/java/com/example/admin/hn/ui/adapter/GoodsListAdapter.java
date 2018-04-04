package com.example.admin.hn.ui.adapter;

/**
 * 投资记录
 */

import android.content.Context;
import android.graphics.Paint;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.model.GoodsInfo;
import com.example.admin.hn.utils.ToolViewUtils;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;


public class GoodsListAdapter extends CommonAdapter<GoodsInfo> {

    public GoodsListAdapter(Context context, int layoutId, List<GoodsInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, GoodsInfo info, int position) {
//        TextView market_price = viewHolder.getView(R.id.market_price);
//        ImageView goods_icon = viewHolder.getView(R.id.goods_icon);
//        viewHolder.setText(R.id.goods_name, info.getGoods_name());
//        viewHolder.setText(R.id.shop_price, "￥" + info.getShop_price());
//        viewHolder.setText(R.id.sales_sum, info.getSales_sum() + "人已购买");
//        market_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//        market_price.getPaint().setAntiAlias(true);
//        market_price.setText("￥" + info.getMarket_price());
//        ToolViewUtils.glideImageList(info.getOriginal_img(), goods_icon,  R.drawable.load_fail);
    }
}
