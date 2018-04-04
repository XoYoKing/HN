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
import com.example.admin.hn.ui.shop.StepActivity;

import java.util.List;


/**
 * Created by hjy on 2017/3/10.
 * 订单管理
 */
public class ShopOrderManagerAdapter extends RecyclerView.Adapter<ShopOrderManagerAdapter.OrderCarHolder> {

    private List<OrderInfo.Order> list;
    private Context context;

    public ShopOrderManagerAdapter(List<OrderInfo.Order> list) {
        this.list = list;
    }


    @Override
    public OrderCarHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View item = LayoutInflater.from(context).inflate(R.layout.item_shop_order_manager, null);
        item.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        return new OrderCarHolder(item);
    }

    @Override
    public void onBindViewHolder(OrderCarHolder holder, final int position) {
//        final OrderManagerInfo info = list.get(holder.getAdapterPosition());
//        holder.all_goods_money.setText("￥" + info.getGoods_price() + "");
//        holder.goods_title.setText(info.getGoods_name() + "");
//        holder.single_goods_money.setText("￥" + info.getSell_price() + "");
//        holder.goods_amount.setText("x " + info.getGoods_num() + "");
//        holder.shipping_price.setText("(" + "含运费￥" + info.getShipping_price() + ")");
//        holder.all_goods_amount.setText("共" + info.getGoods_num() + "件商品");
//        AbImageUtil.glideImageList(info.getImgUrl(), holder.goods_img, R.drawable.loading_img, R.drawable.load_fail);
//
//        if (info.getUseType() == 1) {
//            holder.cancel_order.setText("取消订单");
//            holder.pay.setText("付款");
//            holder.cancel_order.setVisibility(View.VISIBLE);
//        } else if (info.getUseType() == 2) {
//            holder.btn_linear.setVisibility(View.GONE);
//        } else if (info.getUseType() == 3) {
//            holder.cancel_order.setVisibility(View.VISIBLE);
//            holder.cancel_order.setText("查看物流");
//            holder.pay.setText("确认收货");
//        } else if (info.getUseType() == 4) {
//            holder.pay.setText("继续购买");
//            holder.cancel_order.setVisibility(View.INVISIBLE);
//        }
//        holder.pay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setPayOnClick(info, position);
//            }
//        });
        holder.cancel_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setCancelOnClick(info, position);
                StepActivity.startActivity(context);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void setPayOnClick(final OrderManagerInfo info, final int position) {
        if (info.getUseType() == 1) {
//            AbToastUtil.showToast(context, "付款成功");
        } else if (info.getUseType() == 3) {
//            AbToastUtil.showToast(context, "确认收货成功");
        } else if (info.getUseType() == 4) {
//            GoodsDetailsActivity.startActivity(context);
        }

    }

    private void setCancelOnClick(final OrderManagerInfo info, final int position) {
        if (info.getUseType() == 3) {
//            StepActivity.startActivity(context);
        } else {
//            View view = View.inflate(context, R.layout.delete_order_is_true_dailog, null);
//            Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
//            Button btn_sure = (Button) view.findViewById(R.id.btn_sure);
//            btn_cancel.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    DialogUtil.dismiss();
//                }
//            });
//
//            btn_sure.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    DialogUtil.dismiss();
//                    setCancelHttp(info, position);
//                    list.remove(position);
//                    notifyItemRemoved(position);
//                    notifyItemChanged(position);
//
//                }
//            });
//            DialogUtil.showAlertDialog(view);
        }

    }

    class OrderCarHolder extends RecyclerView.ViewHolder {
        private ImageView goods_img;
        private TextView all_goods_money, goods_title, single_goods_money, goods_amount, shipping_price, all_goods_amount;
        private Button cancel_order, pay;
        private LinearLayout btn_linear;

        public OrderCarHolder(View itemView) {
            super(itemView);
            goods_img = (ImageView) itemView.findViewById(R.id.goods_img);
            all_goods_money = (TextView) itemView.findViewById(R.id.all_goods_money);
            goods_title = (TextView) itemView.findViewById(R.id.goods_title);
            single_goods_money = (TextView) itemView.findViewById(R.id.single_goods_money);
            goods_amount = (TextView) itemView.findViewById(R.id.goods_amount);
            shipping_price = (TextView) itemView.findViewById(R.id.shipping_price);
            all_goods_amount = (TextView) itemView.findViewById(R.id.all_goods_amount);
            cancel_order = (Button) itemView.findViewById(R.id.cancel_order);
            pay = (Button) itemView.findViewById(R.id.pay);
            btn_linear = (LinearLayout) itemView.findViewById(R.id.btn_linear);
        }
    }

    /**
     * 取消订单的请求
     *
     * @param info
     * @param position
     */
    public void setCancelHttp(final OrderManagerInfo info, final int position) {
//        IRequest http = new IRequest(context);
//        RequestParams params = new RequestParams();
//        params.put("order_id", info.getOrder_id() + "");
//        http.post(Config.URL_CANCELORDER, params, "正在取消...", new RequestListener() {
//            @Override
//            public void requestSuccess(String json) {
//                AbLogUtil.e("setCancelHttp", json);
//                if (AbJsonUtil.isSuccess(json)) {
//                    setCancelOnClick(info, position);
//                } else {
//                    AbToastUtil.showToast(context, AbJsonUtil.getError(json));
//                }
//
//            }
//
//            @Override
//            public void requestError(String message) {
//                AbToastUtil.showToast(context, message);
//            }
//        });

    }
}
