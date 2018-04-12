package com.example.admin.hn.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by WIN10 on 2018/4/12.
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    int topSpace,bottomSpace,leftSpace, rightSpace;
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = leftSpace;
        outRect.right = rightSpace;
        outRect.bottom = bottomSpace;
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = topSpace;
        }
    }

    public SpaceItemDecoration(int topSpace,int bottomSpace,int leftSpace,int rightSpace) {
        this.topSpace = topSpace;
        this.bottomSpace = bottomSpace;
        this.leftSpace = leftSpace;
        this.rightSpace = rightSpace;
    }
}