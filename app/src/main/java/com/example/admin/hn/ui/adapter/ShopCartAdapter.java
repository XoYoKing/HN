package com.example.admin.hn.ui.adapter;

import android.content.ContentValues;
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
import com.example.admin.hn.utils.AbMathUtil;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolString;
import com.example.admin.hn.utils.ToolViewUtils;
import com.example.admin.hn.widget.ExtendedEditText;
import com.orhanobut.logger.Logger;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车
 */

public class ShopCartAdapter extends CommonAdapter<ShopCartInfo> {
    private boolean isEdit;//是否是编辑状态 默认为false
    private boolean isAll;//是否是全部选中
    private double sumPrice;//计算选中的商品价格总和

    public ShopCartAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final ShopCartInfo info, final int position) {
        final ImageView img_select = holder.getView(R.id.img_select);
        final ImageView goods_img = holder.getView(R.id.goods_img);
        final ImageView add = holder.getView(R.id.add);
        final ImageView remove = holder.getView(R.id.remove);
        final TextView tv_number = holder.getView(R.id.tv_number);
        ToolViewUtils.glideImageList(info.getImageUrl(), goods_img, R.drawable.load_fail);
        holder.setText(R.id.goods_title, info.getGoodsFullName()+ "");
        holder.setText(R.id.tv_usp, info.getUsp() + "");
        holder.setText(R.id.tv_goods_price,"￥"+ AbMathUtil.roundStr(info.getGoodsPrice(), 2) + "");
        tv_number.setText(info.getBuyNumber() + "");
        if (info.getBuyNumber() < info.getQty() && info.getQty() > 1) {
            add.setSelected(true);
        } else {
            add.setSelected(false);
        }
        if (info.getBuyNumber() > 1 && info.getQty() > 1) {
            remove.setSelected(true);
        } else {
            remove.setSelected(false);
        }
        if (isEdit) {
            img_select.setSelected(info.isEditSelect());
        } else {
            img_select.setSelected(info.isSelect());
        }
        holder.setOnClickListener(R.id.img_select, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDtat(info, img_select);
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

    private void updateAddOrRemove(ImageView add, ImageView remove,  int number, ShopCartInfo goodsInfo) {
        if (goodsInfo.getQty() == 0) {
            add.setSelected(false);
            remove.setSelected(false);
        } else {
            if (number == goodsInfo.getQty()) {
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
        if (onItemClickListener != null) {
            onItemClickListener.onItemClickListener(isEdit, isAll,sumPrice);
        }
    }

    /**
     * 增加数量
     */
    private void add(ImageView add, ImageView remove,TextView tv_number, ShopCartInfo info) {
        if (info.getBuyNumber()>=info.getQty()) {
            //购买数量大于等于库存的時候不能继续增加
        } else {
            if (info.isSelect()) {
                //如果此商品是被选中状态增加一个当前商品的价格
                sumPrice += info.getGoodsPrice();
            }
            info.setBuyNumber(info.getBuyNumber() + 1);
            tv_number.setText(info.getBuyNumber() + "");
            info.update(info.getId());//更新数据库
        }
        updateAddOrRemove(add, remove, info.getBuyNumber(), info);
    }

    /**
     * 减少数量
     */
    private void remove(ImageView add, ImageView remove,TextView tv_number, ShopCartInfo info) {
        if (info.getBuyNumber() == 1) {
            //购买数量为1的时候 不能继续减少
        } else {
            if (info.isSelect()) {
                //如果此商品是被选中状态减少一个当前商品的价格
                sumPrice -= info.getGoodsPrice();
            }
            info.setBuyNumber(info.getBuyNumber() - 1);
            tv_number.setText(info.getBuyNumber() + "");
            info.update(info.getId());//更新数据库
        }
        updateAddOrRemove(add, remove, info.getBuyNumber(), info);
    }

    /**
     * 更新布局
     *
     * @param info
     * @param img_select
     */
    private void updateDtat(ShopCartInfo info, ImageView img_select) {
        if (isEdit) {
            if (info.isEditSelect()) {
                info.setEditSelect(false);
            } else {
                info.setEditSelect(true);
            }
            img_select.setSelected(info.isEditSelect());
        } else {
            if (info.isSelect()) {
                info.setSelect(false);
            } else {
                info.setSelect(true);
            }
            img_select.setSelected(info.isSelect());
            //修改本地数据库选中状态
            ContentValues values = new ContentValues();
            values.put("select", info.isSelect());
            DataSupport.update(ShopCartInfo.class, values, info.getId());
        }
        if (onItemClickListener != null) {
            //在循环之前 清除总价
            sumPrice = 0;
            //在遍历之前认为是全部选中
            isAll = true;
            for (int i = 0; i < mDatas.size(); i++) {
                //遍历集合是否被全部选中
                ShopCartInfo cartInfo = mDatas.get(i);
                if (isEdit) {//在编辑状态下
                    if (!cartInfo.isEditSelect()) {
                        //只要有一个没有被选中 跳出循环
                        isAll = false;
                        break;
                    }
                } else {//在结算状态下
                    if (cartInfo.isSelect()) {
                        //计算当前商品的总价 价格*数量
                        double goodsPrice = cartInfo.getGoodsPrice() * cartInfo.getBuyNumber();
                        sumPrice += goodsPrice;
                    } else {
                        isAll = false;
                    }
                }
            }
            onItemClickListener.onItemClickListener(isEdit, isAll,sumPrice);
        }
    }

    /**
     * 设置全部选中 或者取消
     * @param isEdit     是否是在編輯狀態
     * @param isSelect 是否全部选中 或者取消
     * @param isClickListener  是否需要回调
     * @return
     */
    public double setSelectAll(boolean isEdit, boolean isSelect,boolean isClickListener) {
        //在设置全部选中或者取消清除总价
        sumPrice = 0;
        if (isSelect) {
            isAll = true;
        } else {
            isAll = false;
        }
        this.isEdit = isEdit;
        for (int i = 0; i < mDatas.size(); i++) {
            ShopCartInfo info = mDatas.get(i);
            if (isEdit) {
                info.setEditSelect(isSelect);
            } else {
                info.setSelect(isSelect);
                //修改本地数据库选中状态
                ContentValues values = new ContentValues();
                values.put("select", info.isSelect());
                DataSupport.update(ShopCartInfo.class, values, info.getId());
                if (isSelect) {
                    //如果是全选就计算所有的商品价格的总和
                    double goodsPrice = info.getGoodsPrice() * info.getBuyNumber();
                    sumPrice += goodsPrice;
                }
            }
        }
        if (onItemClickListener != null && isClickListener) {
            onItemClickListener.onItemClickListener(isEdit, isAll, sumPrice);
        }
        notifyDataSetChanged();
        return sumPrice;
    }

    /**
     * 获取被选中的商品总价格
     * @return
     */
    public double getSumPrice(){
        sumPrice = 0;
        if (ToolString.isEmptyList(mDatas)) {
            isAll = true;
        }else {
            isAll = false;
        }
        for (int i = 0; i < mDatas.size(); i++) {
            ShopCartInfo info = mDatas.get(i);
            if (info.isSelect()) {
                double goodsPrice = info.getGoodsPrice() * info.getBuyNumber();
                sumPrice += goodsPrice;
            }else {
                isAll = false;
            }
        }
        if (onItemClickListener != null) {
            onItemClickListener.onItemClickListener(isEdit, isAll, sumPrice);
        }
        return sumPrice;
    }


    /**
     * 获取被选中的商品集合
     * @param isEdit  区分是编辑状态还是 结算状态
     * @return
     */
    public List<ShopCartInfo> getSelectInfo(boolean isEdit){
        List<ShopCartInfo> list = new ArrayList<>();
        for (int i = 0; i < mDatas.size(); i++) {
            ShopCartInfo info = mDatas.get(i);
            if (isEdit) {
                if (info.isEditSelect()) {
                    list.add(info);
                }
            } else {
                if (info.isSelect()) {
                    list.add(info);
                }
            }
        }
        return list;
    }

    /**
     * 刷新列表
     */
    public void notify(boolean isEdit) {
        this.isEdit = isEdit;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClickListener(boolean isEdit, boolean isAll,double sumPrice);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}