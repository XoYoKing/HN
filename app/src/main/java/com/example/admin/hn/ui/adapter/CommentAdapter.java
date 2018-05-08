package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.widget.RatingBar;

import com.example.admin.hn.R;
import com.example.admin.hn.ui.fragment.shop.bean.CommentInfo;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 评论
 */

public class CommentAdapter extends CommonAdapter<CommentInfo>{

    public CommentAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, CommentInfo info, int position) {
        RatingBar ratingBar = holder.getView(R.id.ratingBar);
        ratingBar.setRating(info.score);
        holder.setText(R.id.tv_userName, info.memberName + "");
        holder.setText(R.id.tv_content, info.content + "");
//        holder.setText(R.id.tv_newsTime,info.da)
    }
}
