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
import com.example.admin.hn.base.HNApplication;
import com.example.admin.hn.http.OkHttpUtil;
import com.example.admin.hn.model.OrderInfo;
import com.example.admin.hn.model.ShoppingCartInfo;
import com.example.admin.hn.ui.adapter.ShopCartAdapter;
import com.example.admin.hn.ui.fragment.shop.bean.ShopCartInfo;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolRefreshView;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 购物车
 *
 * @author Administrator
 */
public class ShopCartActivity extends BaseActivity implements OnRefreshListener,OnLoadmoreListener{
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

    private ArrayList<ShopCartInfo> list = new ArrayList<>();

    private ShopCartAdapter adapter;

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
                    adapter.setSelectAll(true,false);
                }else {//结算状态
                    img_del_all.setSelected(false);//在结算时 把编辑默认设置为选中状态
                    text_tile_right.setText("编辑");
                    rl_pay.setVisibility(View.VISIBLE);
                    rl_del.setVisibility(View.GONE);
                    adapter.notify(false);
                }
                break;
            case R.id.img_pay_all:
                adapter.setSelectAll(false,!img_pay_all.isSelected());
                img_pay_all.setSelected(!img_pay_all.isSelected());
                break;
            case R.id.img_del_all:
                adapter.setSelectAll(true,!img_del_all.isSelected());
                img_del_all.setSelected(!img_del_all.isSelected());
                break;
            case R.id.go_pay:
                FirmOrderActivity.startActivity(context, new ArrayList<ShoppingCartInfo>());
                break;
            case R.id.btn_delete:
                ToolAlert.dialog(context, "", "是否删除选中的商品", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
        ToolRefreshView.setRefreshLayout(context,refreshLayout,this,this);
        recycleView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ShopCartAdapter(this, R.layout.item_shopping_cart, list);
        recycleView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ShopCartAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(boolean isEdit,boolean isAll) {
                if (isEdit) {
                    img_del_all.setSelected(isAll);
                }else {
                    img_pay_all.setSelected(isAll);
                }
            }
        });
    }


    @Override
    public void initData() {
        for (int i = 0; i < 10; i++) {
            list.add(new ShopCartInfo());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {

    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {

    }
}
