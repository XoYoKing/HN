package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CheckBox;

import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.model.AddressInfo;
import com.example.admin.hn.ui.shop.CreateAddressActivity;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.volley.IRequest;
import com.example.admin.hn.volley.RequestListener;
import com.orhanobut.logger.Logger;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by hjy on 2016/11/5.
 * 地址管理
 */
public class ManageAddressAdapter extends CommonAdapter<AddressInfo> {
    private Context context;
    private List<AddressInfo> list;
    private OnDelListener onDelListener;

    public ManageAddressAdapter(Context context, int layoutId, List<AddressInfo> datas) {
        super(context, layoutId, datas);
        this.context = context;
        this.list = datas;
    }

    public void setOnDelListener(OnDelListener onDelListener) {
        this.onDelListener = onDelListener;
    }

    @Override
    protected void convert(ViewHolder viewHolder, final AddressInfo info, final int position) {
        viewHolder.setText(R.id.goods_receipt_phone, info.phone+"");
        viewHolder.setText(R.id.goods_receipt_name, info.receiverName);
        viewHolder.setText(R.id.tv_address, info.areaName+" "+info.receiverAddr);
        CheckBox select_default_address = viewHolder.getView(R.id.select_default_address);
        if (info.isDefaul==0) {
            select_default_address.setChecked(true);
            select_default_address.setEnabled(false);
            select_default_address.setSelected(true);
        } else {
            select_default_address.setChecked(false);
            select_default_address.setEnabled(true);
            select_default_address.setSelected(false);
        }
        viewHolder.setOnClickListener(R.id.edit_address, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAddressActivity.startActivity(context, info);
            }
        });

        viewHolder.setOnClickListener(R.id.delete_address, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDelTip(position);
            }
        });
        viewHolder.setOnClickListener(R.id.select_default_address, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressInfo info = list.get(position);
                setDefaultAddress(position, info);
            }
        });
    }

    private void alertDelTip(final int position) {
        ToolAlert.dialog(context, "删除收货地址", "确认要删除此地址吗?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delAddress(position);
                dialog.dismiss();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }
//
    private String url_del = Api.SHOP_BASE_URL + Api.GET_DELETE_ADDRESS;

    private void delAddress(final int position) {
        AddressInfo info = list.get(position);
        Map map = new HashMap();
        map.put("id", info.id);
        IRequest.post(mContext,url_del, map, "正在删除...", new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.e("删除地址", json);
                if (GsonUtils.isShopSuccess(json)) {
                    ToolAlert.showToast(context, "删除成功");
                    if (onDelListener != null) {
                        onDelListener.onDelListener();
                    }
                    remove(position);
                } else {
                    ToolAlert.showToast(context, GsonUtils.getError(json));
                }
            }

            @Override
            public void requestError(String message) {
                ToolAlert.showToast(context, message);
            }
        });
    }
    private boolean isDeleteAble=true;
    public void remove(int position) {
        if (isDeleteAble) {//此时为增加动画效果，刷新部分数据源，防止删除错乱
            isDeleteAble = false;//初始值为true,当点击删除按钮以后，休息0.5秒钟再让他为
            //true,起到让数据源刷新完成的作用
            mDatas.remove(position);
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

    private void setDefaultAddress(final int position, AddressInfo info) {
        Map params = new HashMap();
        params.put("address_id", info.id);
//        IRequest.post(context, Config.URL_SET_DEFAULT_ADDRESS, params, "正在设置...", new RequestListener() {
//            @Override
//            public void requestSuccess(String json) {
//                if (AbJsonUtil.isSuccess(json)) {
//                    for (int i1 = 0; i1 < list.size(); i1++) {
//                        AddressInfo info1 = list.get(i1);
//                        if (i1 != position) {
//                            info1.setIs_default("0");
//                        } else {
//                            info1.setIs_default("1");
//                        }
//                        notifyDataSetChanged();
//                    }
//                } else {
//                    AbToastUtil.showToast(context, AbJsonUtil.getError(json));
//                }
//            }
//
//            @Override
//            public void requestError(String message) {
//                AbToastUtil.showToast(context, message);
//            }
//        });
    }

    public interface OnDelListener {
        void onDelListener();
    }
}
