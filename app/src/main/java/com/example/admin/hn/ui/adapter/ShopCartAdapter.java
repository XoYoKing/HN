package com.example.admin.hn.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.model.OrderNotUseInfo;
import com.example.admin.hn.ui.fragment.shop.bean.ShopCartInfo;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolString;
import com.example.admin.hn.utils.ToolViewUtils;
import com.example.admin.hn.widget.ExtendedEditText;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 购物车
 */

public class ShopCartAdapter extends CommonAdapter<ShopCartInfo> {
    private boolean isEdit;//是否是编辑状态 默认为false
    private boolean isAll;//是否是全部选中

    public ShopCartAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final ShopCartInfo info, final int position) {
        final ImageView img_select = holder.getView(R.id.img_select);
        final ImageView add = holder.getView(R.id.add);
        final ImageView remove = holder.getView(R.id.remove);
        final TextView tv_number = holder.getView(R.id.tv_number);
        tv_number.setText(info.buyNumber + "");
        if (info.buyNumber < info.qty && info.qty > 1) {
            add.setSelected(true);
        } else {
            add.setSelected(false);
        }
        if (info.buyNumber > 1 && info.qty > 1) {
            remove.setSelected(true);
        } else {
            remove.setSelected(false);
        }

        if (isEdit) {
            img_select.setSelected(info.isEditSelect);
        } else {
            img_select.setSelected(info.isSelect);
        }
        holder.setOnClickListener(R.id.img_select, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateView(info, img_select);
            }
        });

        holder.setOnClickListener(R.id.add, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add(add,remove,tv_number, info);
            }
        });

        holder.setOnClickListener(R.id.remove, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(add,remove,tv_number, info);
            }
        });
    }

    private void updateAddOrRemove(ImageView add, ImageView remove, TextView tv_number, int number, ShopCartInfo goodsInfo) {
        if (goodsInfo.qty == 0) {
            add.setSelected(false);
            remove.setSelected(false);
        } else {
            if (number == goodsInfo.qty) {
                add.setSelected(false);
            } else {
                add.setSelected(true);
            }
            if (number == 1) {
                remove.setSelected(false);
            } else {
                remove.setSelected(true);
            }
        }
    }

    /**
     * 增加数量
     */
    private void add(ImageView add, ImageView remove,TextView tv_number, ShopCartInfo info) {
        if (info.buyNumber>=info.qty) {
            //购买数量大于等于库存的時候不能继续增加
        } else {
            info.buyNumber = info.buyNumber + 1;
            tv_number.setText(info.buyNumber + "");
        }
        updateAddOrRemove(add, remove, tv_number, info.buyNumber, info);
    }

    /**
     * 减少数量
     */
    private void remove(ImageView add, ImageView remove,TextView tv_number, ShopCartInfo info) {
        if (info.buyNumber == 1) {
            //购买数量为1的时候 不能继续减少
        } else {
            info.buyNumber = info.buyNumber - 1;
            tv_number.setText(info.buyNumber + "");
        }
        updateAddOrRemove(add, remove, tv_number, info.buyNumber, info);
    }

    /**
     * 更新布局
     *
     * @param info
     * @param img_select
     */
    private void updateView(ShopCartInfo info, ImageView img_select) {
        if (isEdit) {
            if (info.isEditSelect) {
                info.isEditSelect = false;
            } else {
                info.isEditSelect = true;
            }
            img_select.setSelected(info.isEditSelect);
        } else {
            if (info.isSelect) {
                info.isSelect = false;
            } else {
                info.isSelect = true;
            }
            img_select.setSelected(info.isSelect);
        }
        if (onItemClickListener != null) {
            for (int i = 0; i < mDatas.size(); i++) {
                //遍历集合是否被全部选中
                if (isEdit) {
                    if (mDatas.get(i).isEditSelect) {
                        isAll = true;
                    } else {
                        //只要有一个没有被选中 跳出循环
                        isAll = false;
                        break;
                    }
                } else {
                    if (mDatas.get(i).isSelect) {
                        isAll = true;
                    } else {
                        //只要有一个没有被选中 跳出循环
                        isAll = false;
                        break;
                    }
                }
            }
            onItemClickListener.onItemClickListener(isEdit, isAll);
        }
    }

    /**
     * 设置全部选中 或者取消
     */
    public void setSelectAll(boolean isEdit, boolean isSelect) {
        if (isSelect) {
            isAll = true;
        } else {
            isAll = false;
        }
        this.isEdit = isEdit;
        for (int i = 0; i < mDatas.size(); i++) {
            ShopCartInfo info = mDatas.get(i);
            if (isEdit) {
                info.isEditSelect = isSelect;
            } else {
                info.isSelect = isSelect;
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 刷新列表
     */
    public void notify(boolean isEdit) {
        this.isEdit = isEdit;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClickListener(boolean isEdit, boolean isAll);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}