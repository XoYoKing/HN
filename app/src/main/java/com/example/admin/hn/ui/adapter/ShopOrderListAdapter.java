package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
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
import com.example.admin.hn.http.Constant;
import com.example.admin.hn.model.HomeItem;
import com.example.admin.hn.model.HomeTypeInfo;
import com.example.admin.hn.ui.fragment.shop.bean.ShopOrderInfo;
import com.example.admin.hn.ui.shop.GoodsListActivity;
import com.example.admin.hn.ui.shop.ShopTypeListActivity;
import com.example.admin.hn.ui.shop.StepActivity;
import com.example.admin.hn.ui.shop.SubmitCommentActivity;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolString;
import com.example.admin.hn.utils.ToolViewUtils;
import com.google.gson.reflect.TypeToken;

import java.util.List;

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

    private void bindType1(HolderType1 holder, int position) {
        final ShopOrderInfo info = list.get(position);
//        List<ShopOrderInfo.OrderItems> orderItems = info.orderItems;
//        ShopOrderInfo.OrderItems item = orderItems.get(0);
//        ToolViewUtils.glideImageList(item.,holder.goods_img,R.drawable.load_fail);
//        holder.goods_title.setText(item.goodsName + "");
//        holder.goods_amount.setText(item.amount + "");
        if (Constant.SHOP_ORDER_STATUS_NEW.equals(info.status)) {
            holder.lock_logistics.setVisibility(View.GONE);
            holder.pay.setVisibility(View.VISIBLE);
            holder.pay.setText("去支付");
            holder.tv_status.setText("等待付款");
        }else if (Constant.SHOP_ORDER_STATUS_PREPARE.equals(info.status)) {
            holder.lock_logistics.setVisibility(View.VISIBLE);
            holder.pay.setVisibility(View.GONE);
            holder.tv_status.setText("等待发货");
        }else if (Constant.SHOP_ORDER_STATUS_SEND.equals(info.status)) {
            holder.lock_logistics.setVisibility(View.VISIBLE);
            holder.pay.setVisibility(View.VISIBLE);
            holder.pay.setText("确认收货");
            holder.tv_status.setText("等待收货");
        }else if (Constant.SHOP_ORDER_STATUS_NOEVAL.equals(info.status)) {
            holder.lock_logistics.setVisibility(View.GONE);
            holder.pay.setVisibility(View.VISIBLE);
            holder.pay.setText("评价");
            holder.tv_status.setText("等待评价");
         }else if(Constant.SHOP_ORDER_STATUS_FINISH.equals(info.status)){
            holder.lock_logistics.setVisibility(View.GONE);
            holder.pay.setVisibility(View.VISIBLE);
            holder.pay.setText("再次购买");
            holder.tv_status.setText("已完成");
         }
        holder.pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constant.SHOP_ORDER_STATUS_NOEVAL.equals(info.status)) {
                    SubmitCommentActivity.startActivity(mContext,info);
                }
            }
        });
        holder.lock_logistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StepActivity.startActivity(mContext);
            }
        });
    }

    private void bindType2(HolderType2 holder, int position) {
        ShopOrderInfo info = list.get(position);
        holder.item_recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        ItemRecycleAdapter itemRecycleAdapter = new ItemRecycleAdapter(TYPE_TYPE1, info.orderItems);
        holder.item_recyclerView.setAdapter(itemRecycleAdapter);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HolderType1 extends RecyclerView.ViewHolder {
        private ImageView goods_img;
        private TextView tv_status,all_goods_money, goods_title, single_goods_money, goods_amount, shipping_price, all_goods_amount;
        private Button lock_logistics, pay;
        private LinearLayout btn_linear;

        public HolderType1(View itemView) {
            super(itemView);
            goods_img = (ImageView) itemView.findViewById(R.id.goods_img);
            all_goods_money = (TextView) itemView.findViewById(R.id.all_goods_money);
            goods_title = (TextView) itemView.findViewById(R.id.goods_title);
            single_goods_money = (TextView) itemView.findViewById(R.id.single_goods_money);
            goods_amount = (TextView) itemView.findViewById(R.id.goods_amount);
            shipping_price = (TextView) itemView.findViewById(R.id.shipping_price);
            all_goods_amount = (TextView) itemView.findViewById(R.id.all_goods_amount);
            lock_logistics = (Button) itemView.findViewById(R.id.lock_logistics);
            tv_status = (TextView) itemView.findViewById(R.id.tv_status);
            pay = (Button) itemView.findViewById(R.id.pay);
            btn_linear = (LinearLayout) itemView.findViewById(R.id.btn_linear);
        }
    }

    public class HolderType2 extends RecyclerView.ViewHolder {
        private RecyclerView item_recyclerView;

        public HolderType2(View itemView) {
            super(itemView);
            item_recyclerView = (RecyclerView) itemView.findViewById(R.id.item_recyclerView);
            item_recyclerView.setNestedScrollingEnabled(false);
            item_recyclerView.setFocusable(false);
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
//            ToolViewUtils.glideImageList(info.imageurl,holder.type_icon,R.drawable.load_fail);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
