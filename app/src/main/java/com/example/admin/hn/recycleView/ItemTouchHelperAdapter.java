package com.example.admin.hn.recycleView;

/**
 * Created by WIN10 on 2018/5/16.
 */

public interface ItemTouchHelperAdapter {
    //数据交换
    void onItemMove(int fromPosition,int toPosition);
    //数据删除
    void onItemDelete(int position);
}
