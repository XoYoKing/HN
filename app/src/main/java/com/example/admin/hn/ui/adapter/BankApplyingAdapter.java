package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;

import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.model.ApplyingInfo;
import com.example.admin.hn.model.OrderInfo;
import com.example.admin.hn.ui.account.AuditingApplyingActivity;
import com.example.admin.hn.ui.account.ReturnActivity;
import com.example.admin.hn.ui.account.ShipApplyingActivity;
import com.example.admin.hn.utils.AbDateUtil;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolString;
import com.example.admin.hn.volley.IRequest;
import com.example.admin.hn.volley.RequestListener;
import com.orhanobut.logger.Logger;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @date on 2017/7/31 15:35
 */
public class BankApplyingAdapter extends CommonAdapter<ApplyingInfo> {

    private boolean isDeleteAble = true;

    public BankApplyingAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, final ApplyingInfo info, final int position) {
        viewHolder.setText(R.id.tv_name, info.shipname + "");
        viewHolder.setText(R.id.tv_status, info.status + "");
        viewHolder.setText(R.id.tv_numberNo, info.applyno + "");
        viewHolder.setText(R.id.tv_date, AbDateUtil.getStringByFormat(info.applydate, AbDateUtil.dateFormatYMD) + "");
        viewHolder.setText(R.id.tv_number, info.amount + "");
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("待审核".equals(info.status)) {
                    AuditingApplyingActivity.startActivity(mContext, info.applyno);
                } else if ("已审核".equals(info.status)) {
                    ShipApplyingActivity.startActivity(mContext, info.applyno);
                } else if ("退回".equals(info.status)) {
                    ShipApplyingActivity.startActivity(mContext, info.applyno);
                }
            }
        });
        Button bt_submit = viewHolder.getView(R.id.bt_submit);
        Button bt_return = viewHolder.getView(R.id.bt_return);
        if ("待审核".equals(info.status)) {
            bt_submit.setVisibility(View.VISIBLE);
            bt_return.setVisibility(View.VISIBLE);
        } else if ("已审核".equals(info.status)) {
            bt_submit.setVisibility(View.GONE);
            bt_return.setVisibility(View.GONE);
        } else if ("退回".equals(info.status)){
            bt_submit.setVisibility(View.GONE);
            bt_return.setVisibility(View.GONE);
        }
        viewHolder.setOnClickListener(R.id.bt_submit, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolAlert.dialog(mContext, "审核确认", "是否确认通过此订单", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        passHttp(info, position);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }
        });
        viewHolder.setOnClickListener(R.id.bt_return, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReturnActivity.startActivity(mContext, info.applyno);
            }
        });
    }

    private String url = Api.BASE_URL + Api.PASS_APPLY;

    private void passHttp(final ApplyingInfo info, final int position) {
        Map map = new HashMap();
        map.put("applyNo", info.applyno + "");
        IRequest.postJson(mContext, url, map, "正在申请", new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.e("审核通过", json);
                if (GsonUtils.isSuccess(json)) {
                    ToolAlert.showToast(mContext, "审核通过");
                    info.status = "已审核";
                    pass(position);
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

    //审核通过
    private void pass(int position) {
        if (isDeleteAble) {//此时为增加动画效果，刷新部分数据源，防止删除错乱
            isDeleteAble = false;//初始值为true,当点击删除按钮以后，休息0.5秒钟再让他为
            //true,起到让数据源刷新完成的作用
//            mDatas.remove(position);
//            notifyItemRemoved(position);
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
