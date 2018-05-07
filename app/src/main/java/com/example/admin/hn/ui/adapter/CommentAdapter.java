package com.example.admin.hn.ui.adapter;

import android.content.Context;
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

    }
}
