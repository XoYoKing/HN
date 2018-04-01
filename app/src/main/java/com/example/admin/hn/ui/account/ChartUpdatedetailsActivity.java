package com.example.admin.hn.ui.account;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.http.OkHttpUtil;
import com.example.admin.hn.widget.ProgersssDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChartUpdatedetailsActivity extends BaseActivity {

    private static final String TAG = "ChartUpdatedetailsActivity";

    @Bind(R.id.text_title_back)
    TextView mTextTitleBack;
    @Bind(R.id.text_title)
    TextView mTextTitle;
    @Bind(R.id.tv_ship_name)
    TextView tv_ship_name;
    @Bind(R.id.tv_ship_number)
    TextView tv_ship_number;
    @Bind(R.id.tv_ship_type)
    TextView tv_ship_type;
    @Bind(R.id.tv_updatetime)
    TextView tv_updatetime;
    @Bind(R.id.tv_size)
    TextView tv_size;
    @Bind(R.id.tv_download)
    TextView tv_download;
    @Bind(R.id.tv_shipstatus)
    TextView tv_shipstatus;

    private static String download;
    private static String size;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_updatedetails);
        ButterKnife.bind(this);
        initTitleBar();
        initView();
    }

    @Override
    public void initTitleBar() {
        super.initTitleBar();
        mTextTitleBack.setBackgroundResource(R.drawable.btn_back);
        mTextTitle.setText(R.string.title_chart_details);
        tv_download.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

    }


    @Override
    public void initView() {
        Intent intent = getIntent();
        download = intent.getStringExtra("download");
        tv_ship_name.setText(intent.getStringExtra("shipname"));
        tv_updatetime.setText(intent.getStringExtra("updatetime"));
        tv_ship_number.setText(intent.getStringExtra("shipnumber"));
        tv_ship_type.setText(intent.getStringExtra("ordertime"));
        size = intent.getStringExtra("size");
        tv_size.setText(size+" kb");
        tv_shipstatus.setText(intent.getStringExtra("shipstatus"));
    }
    @OnClick({R.id.text_title_back, R.id.tv_download})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_title_back:
                finish();
                break;
            case R.id.tv_download:
                progersssDialog = new ProgersssDialog(this);
                OkHttpUtil.dowloadchart(this, Api.BASE_URL + download, progersssDialog,size);
                break;
        }
    }



}
