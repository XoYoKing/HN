package com.example.admin.hn.ui.adapter;

/**
 * 投资记录
 */

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.example.admin.hn.R;
import com.example.admin.hn.model.GoodsListInfo;
import com.example.admin.hn.ui.shop.GoodsDetailActivity;
import com.example.admin.hn.utils.AbMathUtil;
import com.example.admin.hn.utils.ToolViewUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;


public class GoodsListAdapter extends CommonAdapter<GoodsListInfo.Goods> {

    public GoodsListAdapter(Context context, int layoutId, List<GoodsListInfo.Goods> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, final GoodsListInfo.Goods info, int position) {
        ImageView goods_icon = viewHolder.getView(R.id.goods_icon);
        viewHolder.setText(R.id.goods_name, info.goodsFullName+"");
        viewHolder.setText(R.id.goods_price, "￥" + AbMathUtil.roundStr(info.goodsPrice, 2));
        viewHolder.setText(R.id.tv_comment,info.reviewCount+ "条评价");
        ToolViewUtils.glideImageList(info.imageUrl, goods_icon,  R.drawable.load_fail);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodsDetailActivity.startActivity(mContext,info.id);
            }
        });
    }
}
