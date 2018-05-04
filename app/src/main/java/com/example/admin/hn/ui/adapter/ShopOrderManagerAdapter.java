package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.model.OrderInfo;
import com.example.admin.hn.model.OrderManagerInfo;
import com.example.admin.hn.ui.fragment.shop.bean.ShopOrderInfo;
import com.example.admin.hn.ui.shop.StepActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;


/**
 * Created by hjy on 2017/3/10.
 * 订单管理
 */
public class ShopOrderManagerAdapter extends CommonAdapter<ShopOrderInfo> {


    public ShopOrderManagerAdapter(Context context, int layoutId, List<ShopOrderInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ShopOrderInfo shopOrderInfo, int position) {

    }

}
