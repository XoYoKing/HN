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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duantao
 *
 * @date on 2017/8/7 15:48
 */
public class MessageAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<EightEntity> list;
    private LayoutInflater inflater;
    private MessageCenterActivity activity;

    public MessageAdapter(MessageCenterActivity mContext,List<EightEntity> list){
        this.activity = mContext;
        this.list = (ArrayList<EightEntity>) list;

    }

    public ArrayList<EightEntity> getList() {
        return list;
    }
    public void setList(ArrayList<EightEntity> list) {
        this.list = list;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int arg0, View convertView, ViewGroup arg2) {
        inflater = LayoutInflater.from(activity);
        holder h = null;
        if(convertView == null){
            h = new holder();
            convertView = inflater.inflate(R.layout.eight_item, null);
            h.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            h.tv_message_time = (TextView) convertView.findViewById(R.id.tv_message_time);
            h.tv_message_state= (TextView) convertView.findViewById(R.id.tv_message_state);
            h.tv_message_content= (TextView) convertView.findViewById(R.id.tv_message_content);
            h.tv_ship_name= (TextView) convertView.findViewById(R.id.tv_ship_name);


            convertView.setTag(h);
        }else{
            h = (holder) convertView.getTag();
        }

        final EightEntity entity = list.get(arg0);
        h.tv_message_time.setText(entity.getNoticetime());
        h.tv_message_state.setText(entity.getMessagestate());
        h.tv_message_content.setText(entity.getNoticecontent());
        h.tv_ship_name.setText(entity.getShipname());

        if(activity.allCb.getVisibility() == View.VISIBLE){
            h.checkBox.setVisibility(View.VISIBLE);
        }else{
            h.checkBox.setVisibility(View.GONE);
        }

        h.checkBox.setChecked(entity.isChecked());
        h.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

        return convertView;
    }

    class holder{
        private CheckBox checkBox;
        private TextView tv_message_time;
        private TextView tv_message_state;
        private TextView tv_message_content;
        private TextView tv_ship_name;
    }

    public  int selectConut=0;
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

}