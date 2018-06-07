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
import com.example.admin.hn.model.HomeTypeInfo;
import com.example.admin.hn.ui.article.ArticleDetailsActivity;
import com.example.admin.hn.ui.shop.GoodsDetailActivity;
import com.example.admin.hn.ui.shop.GoodsListActivity;
import com.example.admin.hn.ui.shop.ShopTypeListActivity;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolString;
import com.example.admin.hn.utils.ToolViewUtils;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * 商品二级分类
 */

public class ShopTypeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_TYPE1 = 1;//
    public static final int TYPE_TYPE2 = 2;//
    public static final int TYPE_TYPE3 = 3;//
    private Context mContext;
    private List<HomeTypeInfo.Children> list;

    public ShopTypeListAdapter(Context mContext, List<HomeTypeInfo.Children> list) {
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
        HomeTypeInfo.Children info = list.get(position);
        holder.item_recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        TypeToken typeToken = new TypeToken<List<HomeItem>>() {
        };
        List<HomeItem> data = (List<HomeItem>) GsonUtils.jsonToList(info.sitMenu.menuData, typeToken, "data");
        if (ToolString.isEmptyList(data)) {
            holder.mTvLabel.setVisibility(View.VISIBLE);
            holder.mTvLabel.setText(info.sitMenu.menuNames+"");
            ItemRecycleAdapter itemRecycleAdapter = new ItemRecycleAdapter(TYPE_TYPE1, data);
            holder.item_recyclerView.setAdapter(itemRecycleAdapter);
            holder.tv_item_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShopTypeListActivity.startActivity(mContext);
                }
            });
        }else {
            holder.mTvLabel.setVisibility(View.GONE);
        }
        if (onDataListener != null) {
            onDataListener.onDataListener(true,position);
        }
    }

    private OnDataListener onDataListener;

    public void setOnDataListener(OnDataListener onDataListener) {
        this.onDataListener = onDataListener;
    }

    public interface OnDataListener{
        void onDataListener(boolean isNull,int position);
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
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

        class HolderType1 extends RecyclerView.ViewHolder {

            public TextView tv_name;
            public ImageView type_icon;
            View itemView;

            HolderType1(View itemView) {
                super(itemView);
                this.itemView = itemView;
                tv_name= (TextView) itemView.findViewById(R.id.type_name);
                type_icon= (ImageView) itemView.findViewById(R.id.type_icon);
            }
        }

        class HolderType2 extends RecyclerView.ViewHolder {
            View itemView;

            HolderType2(View itemView) {
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
            final HomeItem info = (HomeItem) data.get(position);
            holder.tv_name.setText(info.name + "");
            ToolViewUtils.glideImageList(info.image,holder.type_icon,R.drawable.load_fail);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("category".equals(info.operate)|| "5".equals(info.operate)) {
                        GoodsListActivity.startActivity(mContext,info);
                    } else if ("goods".equals(info.operate)|| "3".equals(info.operate)) {
                        GoodsDetailActivity.startActivity(mContext, info.value);
                    }
                }
            });
        }

        private void bindType2(HolderType2 holder, final int position) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
