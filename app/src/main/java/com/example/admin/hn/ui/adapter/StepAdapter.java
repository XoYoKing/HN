package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.example.admin.hn.R;
import com.example.admin.hn.model.StepInfo;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;


/**
 * Created by geek on 2017/3/15.
 * 物流查看
 */
public class StepAdapter extends CommonAdapter<StepInfo> {
    //印章数据
    private List<StepInfo> list;

    public StepAdapter(Context context, int layoutId, List<StepInfo> datas) {
        super(context, layoutId, datas);
        this.list = datas;
    }

    @Override
    protected void convert(ViewHolder viewHolder, StepInfo info, int position) {
        ImageView iv_step = viewHolder.getView(R.id.iv_step);
        View step_top_line = viewHolder.getView(R.id.step_top_line);
        View step_bottom_line = viewHolder.getView(R.id.step_bottom_line);
        viewHolder.setText(R.id.tv_content, info.getContent());
        viewHolder.setText(R.id.tv_time, info.getTime());
        if (position == 0) {
            iv_step.setSelected(true);
            step_top_line.setVisibility(View.INVISIBLE);
        } else {
            iv_step.setSelected(false);
            step_top_line.setVisibility(View.VISIBLE);
        }
        iv_step.requestLayout();
        if (position == list.size() - 1) {
            step_bottom_line.setVisibility(View.INVISIBLE);
        } else {
            step_bottom_line.setVisibility(View.VISIBLE);
        }
    }
}

