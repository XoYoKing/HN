package com.example.admin.hn.ui.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.utils.ToolAlert;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by hjy on 2016/11/5.
 * 创建新地址
 */
public class CreateAddressActivity extends BaseActivity {

    @Bind(R.id.text_title_back)
    TextView textTitleBack;
    @Bind(R.id.text_tile_del)
    TextView text_tile_del;
    @Bind(R.id.text_title)
    TextView textTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address);
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
        Intent intent = new Intent(context, CreateAddressActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void initTitleBar() {
        textTitle.setText("添加新地址");
        textTitleBack.setBackgroundResource(R.drawable.btn_back);
        text_tile_del.setText("完成");
        text_tile_del.setVisibility(View.VISIBLE);

    }

    @Override
    public void initView() {

    }


    @Override
    public void initData() {

    }

    @OnClick({R.id.text_title_back, R.id.text_tile_del})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.text_title_back:
                finish();
                break;
            case R.id.text_tile_del:
                ToolAlert.showToast(context, "完成", false);
                finish();
                break;
        }
    }

}
