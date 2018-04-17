package com.example.admin.hn.ui.shop;

import android.content.Context;
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
public class ShopCartActivity extends BaseActivity {
    @Bind(R.id.text_title_back)
    TextView textTitleBack;
    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.text_tile_del)
    TextView text_tile_del;
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


    private ArrayList<OrderInfo.Order> list = new ArrayList<>();
    private String str = "";
    private int status = 1;
    private int page = 1;
    private int screen = 1;
    private String url_order = Api.BASE_URL + Api.ORDER;
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
        text_tile_del.setVisibility(View.VISIBLE);
        text_tile_del.setText("编辑");
    }
    /**
     *
     */
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ShopCartActivity.class);
        context.startActivity(intent);
    }

    @OnClick({R.id.text_title_back,R.id.text_tile_del,R.id.img_pay_all,R.id.go_pay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_title_back:
                finish();
            break;
            case R.id.text_tile_del:
                ToolAlert.showToast(context, "编辑", false);
                break;
            case R.id.img_pay_all:
                img_pay_all.setSelected(!img_pay_all.isSelected());
                break;
            case R.id.go_pay:
                FirmOrderActivity.startActivity(context, new ArrayList<ShoppingCartInfo>());
                break;
        }
    }

    @Override
    public void initData() {
        data(1, str, 0);
    }

    @Override
    public void initView() {
        //下拉刷新
        refreshLayout.setDisableContentWhenLoading(true);
        refreshLayout.setDisableContentWhenRefresh(true);
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        //设置 Header 为 Material风格
        refreshLayout.setRefreshHeader(new MaterialHeader(this).setShowBezierWave(true));
        //设置 Footer 为 球脉冲
        refreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
        //监听
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 1;
                data(1, str, 0);
                refreshlayout.finishRefresh(1000);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page = page + 1;
                data(status, str, 1);
                adapter.notifyDataSetChanged();
                refreshlayout.finishLoadmore(1000);
            }
        });

        recycleView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ShopCartAdapter(this, R.layout.item_shopping_cart, list);
        recycleView.setAdapter(adapter);
    }

    public void data(final int status, String down, final int Loadmore) {
        //status1(查询该用户全部订单) 2(根据船舶名称)3(订单号) 4(船舶编号)
        //screen1 ：审核   2:已完成  3：失败
        Map map = new HashMap();
        map.put("ordernumber", down);
        map.put("shipnumber", down);
        map.put("userid", HNApplication.mApp.getUserId());
        map.put("shipname", down);
        map.put("status", status);
        if (Loadmore == 0) {
            map.put("page", "1");
        } else {
            map.put("page", page);
        }
        map.put("screen", screen);

        String jsonStr = GsonUtils.mapToJson(map);
        try {
            OkHttpUtil.postJsonData2Server(this,
                    url_order,
                    jsonStr,
                    new OkHttpUtil.MyCallBack() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            ToolRefreshView.hintView(adapter, true, network, noData_img, network_img);
                        }

                        @Override
                        public void onResponse(String json) {
                            OrderInfo orderInfo = GsonUtils.jsonToBean(
                                    json, OrderInfo.class
                            );
                            if (orderInfo.getStatus().equals("error")) {
                                ToolAlert.showToast(context, orderInfo.getMessage(), false);
                                if (page==1) {
                                    ToolAlert.showToast(context, orderInfo.getMessage(), false);
                                    list.clear();
                                    adapter.notifyDataSetChanged();
                                }else {
                                    ToolAlert.showToast(context, "已全部加载完成", false);
                                }
                            } else {
                                if (Loadmore == 0) {
                                    list.clear();
                                }
                                if (orderInfo.getDocuments().size() == 0) {
                                    list.clear();
                                }else {
                                    for (int i = 0; i < orderInfo.getDocuments().size(); i++) {
                                        list.add(new OrderInfo.Order(orderInfo.getDocuments().get(i).getOrdernumber(), orderInfo.getDocuments().get(i).getOrdertime(), orderInfo.getDocuments().get(i).getStatus(), orderInfo.getDocuments().get(i).getShipname()));
                                    }
                                    HNApplication.mApp.setShipName(list.get(0).getShipname());
                                }
                                adapter.notifyDataSetChanged();
                            }
                            ToolRefreshView.hintView(adapter, false, network, noData_img, network_img);
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
