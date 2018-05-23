package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.http.Constant;
import com.example.admin.hn.ui.fragment.shop.bean.PayOrderInfo;
import com.example.admin.hn.ui.fragment.shop.bean.ShopOrderInfo;
import com.example.admin.hn.ui.shop.GoodsDetailActivity;
import com.example.admin.hn.ui.shop.PayActivity;
import com.example.admin.hn.ui.shop.StepActivity;
import com.example.admin.hn.ui.shop.SubmitCommentActivity;
import com.example.admin.hn.utils.AbMathUtil;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.SpaceItemDecoration;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolString;
import com.example.admin.hn.utils.ToolViewUtils;
import com.example.admin.hn.volley.IRequest;
import com.example.admin.hn.volley.RequestListener;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品订单管理
 */
public class ShopOrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_TYPE1 = 1;//当有只有一条数据的时候显示的布局
    public static final int TYPE_TYPE2 = 2;//当有多条数据的时候显示的布局
    private Context mContext;
    private List<ShopOrderInfo> list;

    public ShopOrderListAdapter(Context mContext, List<ShopOrderInfo> list) {
        this.mContext = mContext;
        this.list = list;
    }


    @Override
    public int getItemViewType(int position) {
        ShopOrderInfo shopOrderInfo = list.get(position);
        if (ToolString.isEmptyList(shopOrderInfo.orderItems)) {
            int size = shopOrderInfo.orderItems.size();
            if (size > 1) {
                return TYPE_TYPE2;
            } else {
                return TYPE_TYPE1;
            }
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_TYPE1) {
            return new HolderType1(LayoutInflater.from(mContext).inflate(R.layout.item_shop_order_manager, parent, false));
        } else if (viewType == TYPE_TYPE2) {
            return new HolderType2(LayoutInflater.from(mContext).inflate(R.layout.item_shop_order_recycle, parent, false));
        }
        return new HolderType1(LayoutInflater.from(mContext).inflate(R.layout.item_shop_order_manager, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HolderType1) {
            bindType1((HolderType1) holder, position);
        } else if (holder instanceof HolderType2) {
            bindType2((HolderType2) holder, position);
        }
    }

    private boolean isDeleteAble=true;
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

    private void bindType1(HolderType1 holder,final int position) {
        final ShopOrderInfo info = list.get(position);
        List<ShopOrderInfo.OrderItems> orderItems = info.orderItems;
        if (ToolString.isEmptyList(orderItems)) {
            final ShopOrderInfo.OrderItems item = orderItems.get(0);
            ToolViewUtils.glideImageList(item.imgUrl,holder.goods_img,R.drawable.load_fail);
            holder.goods_title.setText(item.goodsName + "");
            holder.ll_goods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GoodsDetailActivity.startActivity(mContext,item.id);
                }
            });
        }
        setOrderValue(holder, position, info);
    }

    /**
     * 设置订单数据信息
     * @param holder
     * @param position
     * @param info
     */
    private void setOrderValue(HolderType holder, final int position, final ShopOrderInfo info) {
        holder.all_goods_number.setText("共"+1+"件商品");
        holder.all_goods_amount.setText("￥" + AbMathUtil.roundStr(info.orderAmount, 2));
//        holder.tv_freight.setText("（含运费￥" + AbMathUtil.roundStr(info.freight, 2) + "）");
        if (Constant.SHOP_ORDER_STATUS_NEW==info.status) {
            holder.lock_logistics.setVisibility(View.GONE);
            holder.pay.setVisibility(View.VISIBLE);
            holder.pay.setText("去支付");
            holder.tv_status.setText("等待付款");
        }else if (Constant.SHOP_ORDER_STATUS_PREPARE==info.status) {
            holder.lock_logistics.setVisibility(View.VISIBLE);
            holder.pay.setVisibility(View.GONE);
            holder.tv_status.setText("等待发货");
        }else if (Constant.SHOP_ORDER_STATUS_SEND==info.status) {
            holder.lock_logistics.setVisibility(View.VISIBLE);
            holder.pay.setVisibility(View.VISIBLE);
            holder.pay.setText("确认收货");
            holder.tv_status.setText("等待收货");
        }else if (Constant.SHOP_ORDER_STATUS_NOEVAL==info.status) {
            holder.lock_logistics.setVisibility(View.GONE);
            holder.pay.setVisibility(View.VISIBLE);
            holder.pay.setText("评价");
            holder.tv_status.setText("等待评价");
        }else if(Constant.SHOP_ORDER_STATUS_FINISH==info.status){
            holder.lock_logistics.setVisibility(View.GONE);
            holder.pay.setVisibility(View.VISIBLE);
            holder.pay.setText("再次购买");
            holder.tv_status.setText("已完成");
        }
        holder.pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constant.SHOP_ORDER_STATUS_NOEVAL==info.status) {
                    //去待评价
                    SubmitCommentActivity.startActivity(mContext,info);
                }else if (Constant.SHOP_ORDER_STATUS_NEW==info.status) {
                    //去支付
                    PayOrderInfo payOrderInfo = new PayOrderInfo();
                    payOrderInfo.orderAmount = info.orderAmount;
                    payOrderInfo.orderNo = info.orderNo;
                    payOrderInfo.paymentNo = info.paymentNo;
                    PayActivity.startActivity(mContext,payOrderInfo);
                }else if (Constant.SHOP_ORDER_STATUS_PREPARE==info.status) {
                    //待发货
                    updateOrder(position,info);
                }else if (Constant.SHOP_ORDER_STATUS_SEND==info.status) {
                    //待收货
                    updateOrder(position,info);
                }
            }
        });
        holder.lock_logistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StepActivity.startActivity(mContext);
            }
        });
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolAlert.dialog(mContext, "删除订单", "是否确认删除此订单", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        remove(position);
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


    private void bindType2(HolderType2 holder,final int position) {
        final ShopOrderInfo info = list.get(position);
        holder.item_recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        ItemRecycleAdapter itemRecycleAdapter = new ItemRecycleAdapter(TYPE_TYPE1, info.orderItems);
        holder.item_recyclerView.setAdapter(itemRecycleAdapter);
        setOrderValue(holder, position, info);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HolderType extends RecyclerView.ViewHolder {
        private ImageView iv_delete;
        private TextView tv_status,all_goods_number, tv_freight, all_goods_amount;
        private Button lock_logistics, pay;
        private LinearLayout ll_goods;

        public HolderType(View itemView) {
            super(itemView);
            iv_delete = (ImageView) itemView.findViewById(R.id.iv_delete);
            all_goods_number = (TextView) itemView.findViewById(R.id.all_goods_number);
            tv_freight = (TextView) itemView.findViewById(R.id.tv_freight);
            all_goods_amount = (TextView) itemView.findViewById(R.id.all_goods_amount);
            lock_logistics = (Button) itemView.findViewById(R.id.lock_logistics);
            tv_status = (TextView) itemView.findViewById(R.id.tv_status);
            pay = (Button) itemView.findViewById(R.id.pay);
            ll_goods= (LinearLayout) itemView.findViewById(R.id.ll_goods);
        }
    }

    /**
     * 只有一件商品的时候
     */
    public class HolderType1 extends HolderType {
        private ImageView goods_img;
        private TextView  goods_title, single_goods_money, goods_number;
        private LinearLayout ll_goods;

        public HolderType1(View itemView) {
            super(itemView);
            goods_img = (ImageView) itemView.findViewById(R.id.goods_img);
            goods_title = (TextView) itemView.findViewById(R.id.goods_title);
            single_goods_money = (TextView) itemView.findViewById(R.id.single_goods_money);
            goods_number = (TextView) itemView.findViewById(R.id.goods_number);
            ll_goods= (LinearLayout) itemView.findViewById(R.id.ll_goods);
        }
    }

    /**
     * 存在多件商品的时候
     */
    public class HolderType2 extends HolderType {
        private RecyclerView item_recyclerView;

        public HolderType2(View itemView) {
            super(itemView);
            item_recyclerView = (RecyclerView) itemView.findViewById(R.id.item_recyclerView);
            item_recyclerView.setNestedScrollingEnabled(false);
            item_recyclerView.setFocusable(false);
            item_recyclerView.addItemDecoration(new SpaceItemDecoration(0, 0, 15, 15));
        }
    }

    private class ItemRecycleAdapter extends RecyclerView.Adapter {
        private int type;
        private List data;

        public ItemRecycleAdapter(int type, List data) {
            this.type = type;
            this.data = data;
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (type) {
                case TYPE_TYPE1:
                    return new HolderType(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_order_img, parent, false));
                default:
                    return null;
            }

        }

        class HolderType extends RecyclerView.ViewHolder {
            public ImageView img;
            View itemView;

            HolderType(View itemView) {
                super(itemView);
                this.itemView = itemView;
                img = (ImageView) itemView.findViewById(R.id.img);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof HolderType) {
                bindType((HolderType) holder, position);
            }
        }

        private void bindType(HolderType holder, int position) {
            final ShopOrderInfo.OrderItems info = (ShopOrderInfo.OrderItems) data.get(position);
            ToolViewUtils.glideImageList(info.imgUrl,holder.img,R.drawable.load_fail);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GoodsDetailActivity.startActivity(mContext,info.id);
                }
            });
        }
    }

    private String order_url = Api.SHOP_BASE_URL + Api.GET_SAVE_ORDER;
    private void updateOrder(final int position,ShopOrderInfo orderInfo) {
        Map map = new HashMap();
        map.put("id", orderInfo.id+"");
        map.put("paymentType", orderInfo.paymentType+"");
        map.put("paymentNo", orderInfo.paymentNo+"");
        map.put("status", (orderInfo.status+1)+"");
        IRequest.post(mContext, order_url, map, "正在修改", new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.e("修改订单", json);
                if (GsonUtils.isShopSuccess(json)) {
                    remove(position);
                } else {
                    ToolAlert.showToast(mContext,GsonUtils.getError(json));
                }
            }

            @Override
            public void requestError(String message) {
                ToolAlert.showToast(mContext,message);
            }
        });
    }
}
