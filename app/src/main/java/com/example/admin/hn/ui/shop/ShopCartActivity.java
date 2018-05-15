package com.example.admin.hn.ui.shop;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseActivity;

import com.example.admin.hn.model.AddressInfo;
import com.example.admin.hn.ui.adapter.ShopCartAdapter;
import com.example.admin.hn.ui.fragment.shop.bean.ShopCartInfo;
import com.example.admin.hn.utils.AbMathUtil;

import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolRefreshView;
import com.example.admin.hn.utils.ToolShopCartUtil;
import com.example.admin.hn.utils.ToolString;

import com.example.admin.hn.volley.RequestListener;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.litepal.crud.DataSupport;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 购物车
 *
 * @author Administrator
 */
public class ShopCartActivity extends BaseActivity implements OnRefreshListener{
    @Bind(R.id.text_title_back)
    TextView textTitleBack;
    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.text_tile_right)
    TextView text_tile_right;
    @Bind(R.id.recycleView)
    RecyclerView recycleView;
    @Bind(R.id.network_disabled)
    RelativeLayout network;
    @Bind(R.id.network_img)
    ImageView network_img;
    @Bind(R.id.noData_img)
    ImageView noData_img;
    @Bind(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @Bind(R.id.img_pay_all)
    ImageView img_pay_all;
    @Bind(R.id.img_del_all)
    ImageView img_del_all;
    @Bind(R.id.rl_pay)
    RelativeLayout rl_pay;
    @Bind(R.id.rl_del)
    RelativeLayout rl_del;
    @Bind(R.id.tv_all_price)
    TextView tv_all_price;


    private ArrayList<ShopCartInfo> list = new ArrayList<>();
    private String url = Api.SHOP_BASE_URL + Api.GET_ADDRESS_LIST;
    private ShopCartAdapter adapter;
    private AddressInfo defaultInfo;//默认地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        ButterKnife.bind(this);
        initTitleBar();
        initView();
        initData();
    }

    @Override
    public void initTitleBar() {
        textTitle.setText("购物车");
        textTitleBack.setBackgroundResource(R.drawable.btn_back);
        text_tile_right.setText("编辑");
    }
    /**
     *
     */
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ShopCartActivity.class);
        context.startActivity(intent);
    }

    @OnClick({R.id.text_title_back,R.id.text_tile_right,R.id.img_pay_all,R.id.img_del_all,R.id.go_pay,R.id.btn_delete})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_title_back:
                finish();
            break;
            case R.id.text_tile_right:
                if (rl_pay.getVisibility() == View.VISIBLE) {//编辑状态
                    text_tile_right.setText("完成");
                    rl_pay.setVisibility(View.GONE);
                    rl_del.setVisibility(View.VISIBLE);
                    adapter.setSelectAll(true,false,true);
                }else {//结算状态
                    img_del_all.setSelected(false);//在结算时 把编辑默认设置为选中状态
                    text_tile_right.setText("编辑");
                    rl_pay.setVisibility(View.VISIBLE);
                    rl_del.setVisibility(View.GONE);
                    adapter.notify(false);
                }
                break;
            case R.id.img_pay_all:
                double sum = adapter.setSelectAll(false, !img_pay_all.isSelected(), false);
                tv_all_price.setText(AbMathUtil.roundStr(sum, 2) + "");
                img_pay_all.setSelected(!img_pay_all.isSelected());
                break;
            case R.id.img_del_all:
                adapter.setSelectAll(true,!img_del_all.isSelected(),false);
                img_del_all.setSelected(!img_del_all.isSelected());
                break;
            case R.id.go_pay:
                List<ShopCartInfo> selectInfo = adapter.getSelectInfo(false);
                if (ToolString.isEmptyList(selectInfo)) {
                    FirmOrderActivity.startActivity(context, selectInfo,defaultInfo);
                }else {
                    ToolAlert.showToast(context, "请选择需要结算的商品");
                }
                break;
            case R.id.btn_delete:
                ToolAlert.dialog(context, "", "是否删除选中的商品", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<ShopCartInfo> selectInfo = adapter.getSelectInfo(true);
                        ToolShopCartUtil.deleteShopCartInfo(context,selectInfo);
                        sendHttp();
                        dialog.dismiss();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                break;
        }
    }

    @Override
    public void initView() {
        //下拉刷新
        ToolRefreshView.setRefreshLayout(context,refreshLayout,this);
        recycleView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ShopCartAdapter(this, R.layout.item_shopping_cart, list);
        recycleView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ShopCartAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(boolean isEdit,boolean isAll,double sumPrice) {
                if (isEdit) {
                    img_del_all.setSelected(isAll);
                }else {
                    tv_all_price.setText(AbMathUtil.roundStr(sumPrice, 2) + "");
                    img_pay_all.setSelected(isAll);
                }
            }
        });
    }

    @Override
    public void initData() {
        sendHttp();
        getAddress();
    }

    private void sendHttp() {
        try {
            List<ShopCartInfo> infoList = DataSupport.select("*")
                    .order("id desc")
                    .find(ShopCartInfo.class);
            list.clear();
            if (ToolString.isEmptyList(infoList)) {
                list.addAll(infoList);
                ToolRefreshView.hintView(adapter, refreshLayout, false, network, noData_img, network_img);
            }else {
                //如果购物车为空，把选中状态都设置为false
                img_del_all.setSelected(false);
                img_pay_all.setSelected(false);
                ToolRefreshView.hintView(adapter, refreshLayout, false, network, noData_img, network_img);
            }
            adapter.getSumPrice(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAddress(){
        http.get(url, params, null, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.e("收货地址列表", json);
                if (GsonUtils.isShopSuccess(json)) {
                    TypeToken typeToken = new TypeToken<List<AddressInfo>>() {};
                    List<AddressInfo> data = (List<AddressInfo>) GsonUtils.jsonToList2(json, typeToken, "content");
                    if (ToolString.isEmptyList(data)) {
                        for (int i = 0; i < data.size(); i++) {
                            AddressInfo info = data.get(i);
                            if (info.isDefaul == 1) {
                                defaultInfo = info;
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void requestError(String message) {

            }
        });
    }


    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        isRefresh = true;
        sendHttp();
        getAddress();
    }
}
