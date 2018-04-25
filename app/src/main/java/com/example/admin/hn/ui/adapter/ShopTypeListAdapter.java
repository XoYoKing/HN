package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.model.HomeInfo;
import com.example.admin.hn.model.HomeItem;
import com.example.admin.hn.ui.article.ArticleDetailsActivity;
import com.example.admin.hn.ui.shop.GoodsDetailActivity;
import com.example.admin.hn.ui.shop.GoodsListActivity;
import com.example.admin.hn.ui.shop.ShopTypeListActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/27 0027.
 */

public class ShopTypeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_TYPE1 = 1;//今日推荐
    public static final int TYPE_TYPE2 = 2;//按银行卡选择
    public static final int TYPE_TYPE3 = 3;//按银行卡选择
    private Context mContext;
    private List<HomeInfo> list;

    public ShopTypeListAdapter(Context mContext, List<HomeInfo> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HolderType(LayoutInflater.from(mContext).inflate(R.layout.item_home_shop_recycle, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HolderType) {
            bindType((HolderType) holder, position);
        }
    }

    private void bindType(HolderType holder, int position) {
        HomeInfo info = list.get(position);
        int itemViewType = info.type;
        holder.mTvLabel.setText(info.name+"");
        if (itemViewType == TYPE_TYPE1) {
//            holder.item_recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            holder.item_recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        } else if (itemViewType == TYPE_TYPE2) {
            holder.item_recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        } else if (itemViewType == TYPE_TYPE3) {
            holder.item_recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        }
        ItemRecycleAdapter itemRecycleAdapter = new ItemRecycleAdapter(itemViewType, info.data);
        holder.item_recyclerView.setAdapter(itemRecycleAdapter);
        holder.tv_item_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopTypeListActivity.startActivity(mContext);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HolderType extends RecyclerView.ViewHolder {
        private TextView mTvLabel, tv_item_more;
        private RecyclerView item_recyclerView;

        public HolderType(View itemView) {
            super(itemView);
            item_recyclerView = (RecyclerView) itemView.findViewById(R.id.item_recyclerView);
            mTvLabel = (TextView) itemView.findViewById(R.id.tv_label);
            tv_item_more = (TextView) itemView.findViewById(R.id.tv_more);
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
                    return new HolderType1(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_type_layout, parent, false));
                case TYPE_TYPE2:
                    return new HolderType2(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_type_layout, parent, false));
                  default:
                    return null;
            }

        }

        /**
         * 今日推荐
         */
        class HolderType1 extends RecyclerView.ViewHolder {

            public TextView tv_name;
            View itemView;

            HolderType1(View itemView) {
                super(itemView);
                this.itemView = itemView;
                tv_name= (TextView) itemView.findViewById(R.id.type_name);
            }
        }

        /**
         * 按银行卡选择
         */
        class HolderType2 extends RecyclerView.ViewHolder {
            ImageView ivImg;
            TextView tvName;
            TextView tvDes;
            View itemView;

            HolderType2(View itemView) {
                super(itemView);
                this.itemView = itemView;
            }
        }

        /**
         * 按用途选择
         */
        class HolderType3 extends RecyclerView.ViewHolder {
            ImageView ivImg;
            TextView tvName;
            View itemView;

            HolderType3(View itemView) {
                super(itemView);
                this.itemView = itemView;

            }
        }

        /**
         * 按主题选择
         */
        class HolderType4 extends RecyclerView.ViewHolder {
            ImageView ivImg;
            TextView tvName;
            TextView tvDes;
            View itemView;

            HolderType4(View itemView) {
                super(itemView);
                this.itemView = itemView;

            }
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
            HomeItem info = (HomeItem) data.get(position);
//            holder.tv_name.setText(info.goodsName);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (true) {
                        GoodsListActivity.startActivity(mContext);
                    }else {
                        GoodsDetailActivity.startActivity(mContext);
                    }
                }
            });
        }

        private void bindType2(HolderType2 holder, final int position) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GoodsListActivity.startActivity(mContext);
                }
            });
        }
    }
}
