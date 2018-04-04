package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.model.EightEntity;
import com.example.admin.hn.ui.account.MessageCenterActivity;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duantao
 *
 * @date on 2017/8/7 15:48
 */
public class MessageAdapter extends CommonAdapter<EightEntity> {

    private ArrayList<EightEntity> list;
    private MessageCenterActivity activity;

    public void setList(ArrayList<EightEntity> list) {
        this.list = list;
    }

    public  int selectConut=0;

    public MessageAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
        this.activity = (MessageCenterActivity) context;
        this.list = (ArrayList<EightEntity>) datas;
    }

    public void selectConut() {
        selectConut=0;
        for (EightEntity entity : list) {
            if(entity.isChecked()){
                selectConut++;
            }
        }
    }

    public boolean changeState(){
        for(EightEntity entity:list){
            if(entity.isChecked()){
                return true;
            }
        }
        return false;
    }

    @Override
    protected void convert(ViewHolder h, final EightEntity entity, int position) {
        CheckBox checkBox = h.getView(R.id.checkBox);
        h.setText(R.id.tv_message_time,entity.getNoticetime());
        h.setText(R.id.tv_message_state,entity.getMessagestate());
        h.setText(R.id.tv_message_content,entity.getNoticecontent());
        h.setText(R.id.tv_ship_name,entity.getShipname());

        if(activity.allCb.getVisibility() == View.VISIBLE){
            checkBox.setVisibility(View.VISIBLE);
        }else{
            checkBox.setVisibility(View.GONE);
        }
        checkBox.setChecked(entity.isChecked());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                if(!isChecked){
                    activity.allCb.setChecked(false);
                }
                entity.setChecked(isChecked);
                notifyDataSetChanged();
                if(isChecked){
                    selectConut();
                    if(selectConut==list.size()){
                        activity.allCb.setChecked(true);
                    }
                }
                if(changeState()){
                    activity.bSubmit.setEnabled(true);
                    activity.bSubmit.setBackgroundColor(Color.parseColor("#ff3933"));
                }else{
                    activity.bSubmit.setEnabled(false);
                    activity.bSubmit.setBackgroundColor(Color.parseColor("#B1B1B1"));
                }
            }
        });
    }
}