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
 *  图纸资料详情
 *
 * @author Administrator
 */
public class ReadDrawDetailActivity extends BaseActivity {
    @Bind(R.id.text_title_back)
    TextView textTitleBack;
    @Bind(R.id.text_title)
    TextView textTitle;
    private int id;
    private String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_draw_details);
        ButterKnife.bind(this);
        initTitleBar();
        initData();
    }

    @Override
    public void initTitleBar() {
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        title = intent.getStringExtra("title");
        textTitle.setText("图纸资料详情");
        textTitleBack.setBackgroundResource(R.drawable.btn_back);
    }
    /**
     *
     */
    public static void startActivity(Context context, int id, String title) {
        Intent intent = new Intent(context, ReadDrawDetailActivity.class);
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
