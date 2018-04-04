package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.model.ScreenTypeInfo;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

/**
 * Created by geek on 2016/7/5.
 * 筛选适配器
 */
public class ScreenTypeAdapter extends BaseExpandableListAdapter {
    private List<ScreenTypeInfo> groups;
    private Context context;
    private TextView group_title;
    private TextView group_time;
    private ImageView right_group;

    /**
     * 更新状态
     *
     * @param groupPosition
     */
    public void update(int groupPosition) {
        ScreenTypeInfo info = groups.get(groupPosition);
        notifyDataSetChanged();
    }

    public void addList(List<ScreenTypeInfo> list, boolean isRefresh) {
        if (isRefresh) {
            groups.clear();
        }
        groups.addAll(list);
        notifyDataSetChanged();
    }


    public ScreenTypeAdapter(Context context, List<ScreenTypeInfo> groups) {
        this.groups = groups;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).getName();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.screen_type_group_item_layout, null);
        ScreenTypeInfo info = groups.get(groupPosition);
        group_title = (TextView) view.findViewById(R.id.group_title);
        group_title.setText(info.getName());
        right_group = (ImageView) view.findViewById(R.id.right_group_img);
        if (isExpanded) {
            right_group.setImageResource(R.drawable.more_up);
        } else {
            right_group.setImageResource(R.drawable.right);
        }
        return view;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.screen_type_child_item_layout, null);
        TagFlowLayout screen_type = (TagFlowLayout) view.findViewById(R.id.screen_type);
        ScreenTypeInfo info = groups.get(groupPosition);
        screen_type.setAdapter(new TagAdapter<String>(info.getData()) {
            @Override
            public View getView(com.zhy.view.flowlayout.FlowLayout parent, int position, String s) {
                TextView bt = (TextView) View.inflate(context, R.layout.item_screen_type, null);
                bt.setText(s);
                return bt;
            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }
}
