package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.model.AddressInfo;
import com.example.admin.hn.ui.shop.CreateAddressActivity;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.volley.IRequest;
import com.example.admin.hn.volley.RequestListener;
import com.example.admin.hn.recycleView.BaseViewHolder;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by hjy on 2016/11/5.
 * 选择地址
 */
public class SelectAddressAdapter extends RecyclerView.Adapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private int layoutId;
    private List<AddressInfo> list;

    public SelectAddressAdapter(Context context, int layoutId, List<AddressInfo> datas) {
        this.mContext = context;
        this.layoutId = layoutId;
        this.list = datas;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = new BaseViewHolder(mInflater.inflate(layoutId, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BaseViewHolder viewHolder = (BaseViewHolder) holder;
       final AddressInfo info = list.get(position);
        if (info.isSelect) {
            viewHolder.getView(R.id.select_address).setVisibility(View.VISIBLE);
        } else {
            viewHolder.getView(R.id.select_address).setVisibility(View.GONE);
        }
        if (info.isDefaul == 1) {
            viewHolder.getView(R.id.tv_default).setVisibility(View.VISIBLE);
        } else {
            viewHolder.getView(R.id.tv_default).setVisibility(View.GONE);
        }
        viewHolder.setText(R.id.goods_receipt_phone, info.phone);
        viewHolder.setText(R.id.goods_receipt_name, info.receiverName + "");
        viewHolder.setText(R.id.tv_address, info.areaName + " " + info.receiverAddr);
        viewHolder.setOnClickListener(R.id.img_edit, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAddressActivity.startActivity(mContext, info);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    private String url_del = Api.SHOP_BASE_URL + Api.GET_DELETE_ADDRESS;

    public void delAddress(final int position) {
        AddressInfo info = list.get(position);
        Map map = new HashMap();
        map.put("id", info.id);
        IRequest.post(mContext, url_del, map, "正在删除...", new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.e("删除地址", json);
                if (GsonUtils.isShopSuccess(json)) {
                    ToolAlert.showToast(mContext, "删除成功");
                    remove(position);
                } else {
                    ToolAlert.showToast(mContext, GsonUtils.getError(json));
                }
            }

            @Override
            public void requestError(String message) {
                ToolAlert.showToast(mContext, message);
            }
        });
    }

    private boolean isDeleteAble = true;

    public void remove(int position) {
        if (isDeleteAble) {//此时为增加动画效果，刷新部分数据源，防止删除错乱
            isDeleteAble = false;//初始值为true,当点击删除按钮以后，休息0.5秒钟再让他为
            //true,起到让数据源刷新完成的作用
            list.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(300);//休息
                        isDeleteAble = true;//可点击按钮
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
