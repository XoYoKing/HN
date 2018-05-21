package com.example.admin.hn.ui.shop;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.ui.fragment.shop.bean.PayOrderInfo;
import com.example.admin.hn.utils.AbMathUtil;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.widget.AlertDialog;


import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by hjy on 2016/11/5.
 * 支付
 */
public class PayActivity extends BaseActivity {

    @Bind(R.id.text_title_back)
    TextView textTitleBack;
    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.tv_orderAmount)
    TextView tv_orderAmount;
    @Bind(R.id.cb_weixin)
    ImageView cb_weixin;
    @Bind(R.id.cb_zhifub)
    ImageView cb_zhifub;
    private PayOrderInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);
        initTitleBar();
        initView();
        initData();
    }

    /**
     * @param context
     */
    public static void startActivity(Context context, PayOrderInfo info) {
        Intent intent = new Intent(context, PayActivity.class);
        intent.putExtra("info", info);
        context.startActivity(intent);
    }


    @Override
    public void initTitleBar() {
        textTitle.setText("支付订单");
        textTitleBack.setBackgroundResource(R.drawable.btn_back);
    }

    @Override
    public void initView() {
        info = (PayOrderInfo) getIntent().getSerializableExtra("info");
        cb_weixin.setSelected(true);
    }


    @Override
    public void initData() {
        tv_orderAmount.setText(AbMathUtil.roundStr(info.orderAmount, 2));
    }

    @OnClick({R.id.text_title_back, R.id.go_pay, R.id.ll_weixin, R.id.ll_zhifub})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_title_back:
                close();
                break;
            case R.id.go_pay:
                ToolAlert.showToast(context, "支付成功", false);
                break;
            case R.id.ll_weixin:
                if (!cb_weixin.isSelected()){
                    cb_weixin.setSelected(true);
                    cb_zhifub.setSelected(false);
                }
                break;
            case R.id.ll_zhifub:
                if (!cb_zhifub.isSelected()){
                    cb_zhifub.setSelected(true);
                    cb_weixin.setSelected(false);
                }
                break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            close();
            return true;
        } else
            return super.onKeyDown(keyCode, event);
    }

    private void close() {

        final AlertDialog dialog = new AlertDialog(context);
        dialog.showDialog("确定离开支付？", "您的订单将在24小时之内未支付将被取消，请尽快完成支付", new AlertDialog.DialogOnClickListener() {
            @Override
            public void onPositiveClick() {
                OrderManagerActivity.startActivity(context, 0);
                dialog.dismiss();
                finish();
            }

            @Override
            public void onNegativeClick() {
                dialog.dismiss();
            }
        },true);
    }
}
