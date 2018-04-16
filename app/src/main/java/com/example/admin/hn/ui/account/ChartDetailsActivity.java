package com.example.admin.hn.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author duantao
 * @date on 2017/8/3 9:37
 * @describe 海图详情
 */
public class ChartDetailsActivity extends BaseActivity {

    private static final String TAG = "ChartDetailsActivity";

    @Bind(R.id.text_title_back)
    TextView mTextTitleBack;
    @Bind(R.id.text_title)
    TextView mTextTitle;
    @Bind(R.id.tv_order_rate)
    TextView tv_order_rate;
    @Bind(R.id.tv_order_rate1)
    TextView tv_order_rate1;
    @Bind(R.id.tv_order_time)
    TextView tv_order_time;
    @Bind(R.id.tv_order_number)
    TextView tv_order_number;
    @Bind(R.id.btn_confirm)
    Button mBtconfirm;


    private String ordernumber;
    private String productnumber;
    private String shipname;
    private String updatetime;
    private String url_chart = Api.BASE_URL + Api.CHART;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_details);
        ButterKnife.bind(this);
        initTitleBar();
        initView();

    }

    @OnClick({R.id.text_title_back, R.id.btn_confirm})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_title_back:
                finish();
                break;
            case R.id.btn_confirm:
                finish();
                break;
        }
    }


    @Override
    public void initView() {
        super.initView();
        Intent intent = getIntent();
        ordernumber = intent.getStringExtra("ordernumber");
        shipname= intent.getStringExtra("shipname");
        updatetime= intent.getStringExtra("updatetime");
        productnumber= intent.getStringExtra("productnumber");
        tv_order_rate.setText(shipname);
        tv_order_rate1.setText(updatetime);
        tv_order_time.setText(productnumber);
        tv_order_number.setText(ordernumber);
    }


    @Override
    public void initTitleBar() {
        super.initTitleBar();
        mTextTitleBack.setBackgroundResource(R.drawable.btn_back);
        mTextTitle.setText(R.string.title_chart_details);
    }

}

