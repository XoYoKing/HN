package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.model.ApplyingDetailInfo;
import com.example.admin.hn.model.ApplyingInfo;
import com.example.admin.hn.model.OrderInfo;
import com.example.admin.hn.utils.AbDateUtil;
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
 *
 * @date on 2017/7/31 15:35
 */
public class AuditingApplyingAdapter extends CommonAdapter<ApplyingDetailInfo> {

    private final String applyno;
    private boolean isDeleteAble=true;

    public AuditingApplyingAdapter(Context context,String applyno, int layoutId, List datas) {
        super(context, layoutId, datas);
        this.applyno =applyno;
    }

    @Override
    protected void convert(ViewHolder viewHolder,final ApplyingDetailInfo info, final int position) {
        viewHolder.setText(R.id.tv_type, info.categoryname+"");
        viewHolder.setText(R.id.tv_ship_name, info.shipname + "");
        viewHolder.setText(R.id.tv_name, info.docname + "");
        viewHolder.setText(R.id.tv_numberNo, info.code + "");
        viewHolder.setText(R.id.tv_date, info.publishat  + "");
        viewHolder.setText(R.id.tv_number, info.quantity + "");
        viewHolder.setOnClickListener(R.id.tv_cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolAlert.dialog(mContext, "删除订单", "是否确认除此订单", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        removeHttp(info,position);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    private String url = Api.BASE_URL + Api.MODIFY_APPLY_DETAIL;
    private void removeHttp(ApplyingDetailInfo info , final int position) {
        Map map = new HashMap();
        map.put("applyNo", applyno + "");
        map.put("docId", info.docid + "");
        IRequest.postJson(mContext, url, map, "正在删除...", new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.e("删除订单", json);
                if (GsonUtils.isSuccess(json)) {
                    ToolAlert.showToast(mContext, "删除成功");
                    remove(position);
                }else {
                    ToolAlert.showToast(mContext, GsonUtils.getError(json));
                }
            }

            @Override
            public void requestError(String message) {
                ToolAlert.showToast(mContext,message);
            }
        });
    }

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
}
