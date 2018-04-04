package com.example.admin.hn.ui.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.model.ShoppingCartInfo;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.widget.AlertDialog;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by hjy on 2016/11/5.
 * 商品详情
 */
public class GoodsDetailActivity extends BaseActivity {

    @Bind(R.id.text_title_back)
    TextView textTitleBack;
    @Bind(R.id.text_title)
    TextView textTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        ButterKnife.bind(this);
        initTitleBar();
        initView();
        initData();
    }

    /**
     *
     * @param context
     */
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, GoodsDetailActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void initTitleBar() {
        textTitle.setText("商品详情");
        textTitleBack.setBackgroundResource(R.drawable.btn_back);
    }

    @Override
    public void initView() {

    }


    @Override
    public void initData() {

    }

    @OnClick({R.id.text_title_back,R.id.add_shopping_cart,R.id.confirm_bid})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_title_back:
                finish();
                break;
            case R.id.add_shopping_cart:
                addShoppingCar();
                break;
            case R.id.confirm_bid:
                FirmOrderActivity.startActivity(context, new ArrayList<ShoppingCartInfo>());
                break;
        }
    }


    private void addShoppingCar() {
        final AlertDialog dialog = new AlertDialog(this);
        dialog.setBtCancel("继续购物");
        dialog.setBtConfirm("去结算");
        dialog.showDialog("添加购物车成功", "共" + 1 + "件", new AlertDialog.DialogOnClickListener() {
            @Override
            public void onPositiveClick() {
                dialog.dismiss();
            }

            @Override
            public void onNegativeClick() {
                dialog.dismiss();
            }
        });
    }

}
