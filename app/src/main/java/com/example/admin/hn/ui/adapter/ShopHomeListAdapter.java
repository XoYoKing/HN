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
import com.example.admin.hn.ui.article.ArticleDetailsActivity;
import com.example.admin.hn.ui.shop.GoodsDetailActivity;
import com.example.admin.hn.ui.shop.ShopTypeListActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/27 0027.
 */

public class ShopHomeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_TYPE1 = 1;//今日推荐
    public static final int TYPE_TYPE2 = 2;//按银行卡选择
    public static final int TYPE_TYPE3 = 3;//按用途选择
    public static final int TYPE_TYPE4 = 4;//按主题选择

    private Context mContext;
    private List<HomeInfo> list;

    public ShopHomeListAdapter(Context mContext, List<HomeInfo> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (viewType == TYPE_TYPE1) {
//            return new HolderType(LayoutInflater.from(mContext).inflate(R.layout.item_credit_card2, parent, false));
//        } else {
//            return new HolderType(LayoutInflater.from(mContext).inflate(R.layout.item_credit_card, parent, false));
//        }
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
        if (itemViewType == TYPE_TYPE1) {
            holder.mTvLabel.setText("今日推荐");
            holder.item_recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        } else if (itemViewType == TYPE_TYPE2) {
            holder.mTvLabel.setText("海宁推荐");
            holder.item_recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        } else if (itemViewType == TYPE_TYPE3) {
            holder.mTvLabel.setText("热点新闻");
            holder.item_recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
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
                    return new HolderType1(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_shop_item2, parent, false));
                case TYPE_TYPE2:
                    return new HolderType2(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_shop_item1, parent, false));
                case TYPE_TYPE3:
                    return new HolderType3(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_shop_item3, parent, false));
                default:
                    return null;
            }

        }

        /**
         * 今日推荐
         */
        class HolderType1 extends RecyclerView.ViewHolder {
            public ImageView iv_img;
            public TextView tv_name;
            public TextView tv_des;
            View itemView;

            HolderType1(View itemView) {
                super(itemView);
                this.itemView = itemView;

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
            } else if (holder instanceof HolderType3) {
                bindType3((HolderType3) holder, position);
            }
        }

        private void bindType1(HolderType1 holder, int position) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GoodsDetailActivity.startActivity(mContext);
                }
            });
        }

        private void bindType2(HolderType2 holder, final int position) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GoodsDetailActivity.startActivity(mContext);
                }
            });
        }

        private void bindType3(HolderType3 holder, int position) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArticleDetailsActivity.startActivity(mContext,0,"文章详情");
                }
            });
        }

    }
}
