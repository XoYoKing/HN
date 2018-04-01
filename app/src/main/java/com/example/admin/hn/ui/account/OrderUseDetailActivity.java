package com.example.admin.hn.ui.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 订单领用详情
 *
 * @author Administrator
 */
public class OrderUseDetailActivity extends BaseActivity {
    @Bind(R.id.text_title_back)
    TextView textTitleBack;
    @Bind(R.id.text_title)
    TextView textTitle;
    private int id;
    private String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_use_details);
        ButterKnife.bind(this);
        initTitleBar();
        initData();
    }

    @Override
    public void initTitleBar() {
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        title = intent.getStringExtra("title");
        textTitle.setText("订单领用详情");
        textTitleBack.setBackgroundResource(R.drawable.btn_back);
    }
    /**
     *
     */
    public static void startActivity(Context context, int id, String title) {
        Intent intent = new Intent(context, OrderUseDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    @OnClick(R.id.text_title_back)
    public void onClick(View v) {
        if (v.getId() == R.id.text_title_back) {
            finish();
        }
    }

    @Override
    public void initData() {
        super.initData();
    }
}
