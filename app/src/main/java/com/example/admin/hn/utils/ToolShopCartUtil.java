package com.example.admin.hn.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.example.admin.hn.http.Constant;
import com.example.admin.hn.model.GoodsInfo;
import com.example.admin.hn.ui.fragment.shop.bean.ShopCartInfo;
import com.orhanobut.logger.Logger;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by WIN10 on 2018/5/10.
 * 购物车增删改查
 */

public class ToolShopCartUtil {

    /**
     * 添加到购物车
     */
    public static void addShopCartInfo(Context context,GoodsInfo goodsInfo,int number) {
        try {
            List<ShopCartInfo> all = DataSupport.findAll(ShopCartInfo.class);
            if (ToolString.isEmptyList(all)) {
                for (int i = 0; i < all.size(); i++) {
                    ShopCartInfo shop = all.get(i);
                    List<String> specItemsIds = shop.getCurrGoodsSpecItemsIds();
                    List<String> itemsIds = goodsInfo.currGoodsSpecItemsIds;
                    String json1 = GsonUtils.toListJson(specItemsIds);
                    String json2 = GsonUtils.toListJson(itemsIds);
                    boolean is;
                    if (json1.equals(json2)) {
                        is = true;
                    }else {
                        is = false;
                    }
                    if (shop.getGoodsId().equals(goodsInfo.goods.id) && is) {
                        //如果找到同一个商品同一种规格就修改购买的数量
                        shop.setBuyNumber(shop.getBuyNumber() + 1);
                        shop.update(shop.getId());
                        ToolAlert.showToast(context,"已添加到购物车");
                        break;
                    }
                    if (i == all.size() - 1) {
                        save(context, goodsInfo, number);
                    }
                }
            }else {
                save(context, goodsInfo, number);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void save(Context context, GoodsInfo goodsInfo, int number) {
        //如果到最后都没找到相同的商品就添加新的列
        ShopCartInfo shopCartInfo = new ShopCartInfo();
        shopCartInfo.setBuyNumber(number);
        shopCartInfo.setSelect(true);//添加的商品默认为选中状态
        shopCartInfo.setEditSelect(false);
        shopCartInfo.setGoodsId(goodsInfo.goods.id);
        shopCartInfo.setGoodsName(goodsInfo.spu.goodsName);
        shopCartInfo.setGoodsFullSpecs(goodsInfo.goods.goodsFullSpecs);
        shopCartInfo.setGoodsFullName(goodsInfo.goods.goodsFullName);
        shopCartInfo.setGoodsPrice(goodsInfo.goods.goodsPrice);
        shopCartInfo.setQty(goodsInfo.goods.qty);
        shopCartInfo.setImageUrl(goodsInfo.goods.imageUrl);
        shopCartInfo.setSpuId(goodsInfo.goods.spuId);
        shopCartInfo.setUsp(goodsInfo.spu.usp);
        shopCartInfo.setCurrGoodsSpecItemsIds(goodsInfo.currGoodsSpecItemsIds);
        boolean save = shopCartInfo.save();
        if (save) {
            int count = DataSupport.count(ShopCartInfo.class);
            ToolAlert.showToast(context,"已添加到购物车");
            Intent intent = new Intent(Constant.ACTION_GOODS_DETAIL_ACTIVITY);
            intent.putExtra("status", 2);
            intent.putExtra("count", count);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        } else {
            ToolAlert.showToast(context, "添加购物车失败");
        }
    }

    public static void deleteShopCartInfo(Context context, List<ShopCartInfo> list) {
        try {
            for (int i = 0; i < list.size(); i++) {
                ShopCartInfo cartInfo = list.get(i);
                cartInfo.delete();
            }
            int count = DataSupport.count(ShopCartInfo.class);
            Intent intent = new Intent(Constant.ACTION_GOODS_DETAIL_ACTIVITY);
            intent.putExtra("status", 2);
            intent.putExtra("count", count);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
