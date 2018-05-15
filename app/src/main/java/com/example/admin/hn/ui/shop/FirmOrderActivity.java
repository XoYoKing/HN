package com.example.admin.hn.ui.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.model.AddressInfo;
import com.example.admin.hn.ui.adapter.FirmOrderAdapter;
import com.example.admin.hn.ui.fragment.shop.bean.ShopCartInfo;
import com.example.admin.hn.ui.fragment.shop.bean.SubmitGoodsInfo;
import com.example.admin.hn.utils.AbDateUtil;
import com.example.admin.hn.utils.AbMathUtil;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.volley.RequestListener;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by hjy on 2016/11/5.
 * 确认订单
 */
public class FirmOrderActivity extends BaseActivity {

    @Bind(R.id.text_title_back)
    TextView textTitleBack;
    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.recycleView)
    RecyclerView recycleView;
    @Bind(R.id.goods_receipt_name)
    TextView goods_receipt_name;
    @Bind(R.id.goods_receipt_phone)
    TextView goods_receipt_phone;
    @Bind(R.id.tv_address)
    TextView tv_address;
    @Bind(R.id.tv_sum_money)
    TextView tv_sum_money;
    @Bind(R.id.tv_goods_money)
    TextView tv_goods_money;
    @Bind(R.id.tv_goodsFreight)
    TextView tv_goodsFreight;


    private static List<ShopCartInfo> list;
    private  List<SubmitGoodsInfo> sub_list=new ArrayList<>();
    private FirmOrderAdapter adapter;
    private AddressInfo addressInfo;
    private String url = Api.SHOP_BASE_URL + Api.GET_ORDER_ADD;
    private double sum_price;//总价格
    private double goods_price;//总价格
    private double sum_freight;//运费

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firm_order);
        ButterKnife.bind(this);
        initTitleBar();
        initView();
        initData();
    }

    /**
     * @param context
     */
    public static void startActivity(Context context, List<ShopCartInfo> orderInfo, AddressInfo addressInfo) {
        list = orderInfo;
        Intent intent = new Intent(context, FirmOrderActivity.class);
        intent.putExtra("addressInfo", addressInfo);
        context.startActivity(intent);
    }


    @Override
    public void initTitleBar() {
        textTitle.setText("确认订单");
        textTitleBack.setBackgroundResource(R.drawable.btn_back);
        Intent intent = getIntent();
        addressInfo = (AddressInfo) intent.getSerializableExtra("addressInfo");
    }

    @Override
    public void initView() {
        if (addressInfo != null) {
            setAddress(addressInfo);
        }
        for (int i = 0; i < list.size(); i++) {
            ShopCartInfo cartInfo = list.get(i);
            goods_price += cartInfo.getBuyNumber() * cartInfo.getGoodsPrice();
            sum_freight += cartInfo.getGoodsFreight();
            //初始化需要提交的订单列表
            SubmitGoodsInfo submit = new SubmitGoodsInfo();
            submit.goodsName = cartInfo.getGoodsName();
            submit.goodsId = cartInfo.getGoodsId();
            submit.spuId = cartInfo.getSpuId();
            submit.goodsSpec = cartInfo.getGoodsSpec();
            submit.goodsFullSpecs = cartInfo.getGoodsFullSpecs();
            submit.goodsPrice = cartInfo.getGoodsPrice();
            submit.amount = cartInfo.getBuyNumber() * cartInfo.getGoodsPrice();
            submit.qty = cartInfo.getBuyNumber();
            sub_list.add(submit);
        }
        sum_price = goods_price + sum_freight;
        tv_goods_money.setText("￥" + AbMathUtil.roundStr(goods_price, 2));
        tv_goodsFreight.setText("￥" + AbMathUtil.roundStr(sum_freight, 2));
        tv_sum_money.setText(AbMathUtil.roundStr(sum_price, 2));
    }


    @Override
    public void initData() {
        adapter = new FirmOrderAdapter(this, R.layout.item_firm_order_layout, list);
        recycleView.setLayoutManager(new LinearLayoutManager(context));
        recycleView.setAdapter(adapter);
    }


    private void setAddress(AddressInfo info) {
        if (info != null) {
            addressInfo = info;
            goods_receipt_name.setText(info.receiverName + "");
            goods_receipt_phone.setText(info.phone + "");
            tv_address.setText(info.areaName + " " + info.receiverAddr);
        }
    }

    @OnClick({R.id.text_title_back, R.id.go_pay, R.id.address_linear})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_title_back:
                finish();
                break;
            case R.id.go_pay:
                payOrder();
                break;
            case R.id.address_linear:
                SelectAddressActivity.startActivity(this, addressInfo);
                break;
        }
    }

    private void payOrder() {
        if (addressInfo == null) {
            ToolAlert.showToast(context, "请选择收货地址");
            return;
        }

        createOrder();
    }

    /**
     * 创建 商品订单
     * 必传参数
     * 会员ID:memberId
     * 会员名称:memberName
     * 订单来源:orderSource 0为pc端，1为移动端
     * 订单状态:status 0为待付款 1为待发货 2为待收货 3为待评价 4订单完成
     * 订单金额:orderSource
     * 订单生成时间:cancelDate
     * 订单是否取消 isCancel 0为未取消，1为取消
     *
     * 可选参数
     * 收货人地址:receiverAddr
     * 收货人姓名:receiverName
     * 收货人电话:receiverPhone
     * 收货人备注:receiverNote
     *
     */
    private void createOrder() {
        params.put("memberName", "memberName");
        params.put("orderSource", "1");
        params.put("orderAmount", sum_price+"");
        params.put("receiverAddr", addressInfo.receiverAddr);
        params.put("receiverName", addressInfo.receiverName);
        params.put("receiverPhone", addressInfo.phone);
        params.put("sub_list", GsonUtils.toListJson(sub_list));
        http.post(url, params, "确认订单...", new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.e("确认订单", json);
            }

            @Override
            public void requestError(String message) {
                ToolAlert.showToast(context, message);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {//获取 选择的地址
            AddressInfo info = (AddressInfo) data.getSerializableExtra("info");
            setAddress(info);
        }
    }
}
