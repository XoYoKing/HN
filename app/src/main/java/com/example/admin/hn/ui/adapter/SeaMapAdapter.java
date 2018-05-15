package com.example.admin.hn.ui.adapter;

import android.content.Context;

import com.example.admin.hn.R;
import com.example.admin.hn.model.ArticleInfo;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by duantao
 *
 * @date on 2017/7/31 15:35
 * 海圖資料
 */
public class SeaMapAdapter extends CommonAdapter<ArticleInfo> {

    public SeaMapAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, ArticleInfo info, int position) {
        viewHolder.setText(R.id.tv_name, info.pubTitle + "");

    }
}
