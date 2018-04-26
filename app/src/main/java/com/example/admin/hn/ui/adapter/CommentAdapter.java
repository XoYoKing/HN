package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.example.admin.hn.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 评论
 */

public class CommentAdapter extends CommonAdapter{

    public CommentAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, Object o, int position) {

    }
}
