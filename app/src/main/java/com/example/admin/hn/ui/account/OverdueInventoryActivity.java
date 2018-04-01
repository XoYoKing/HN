package com.example.admin.hn.ui.account;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author duantao
 * @date on 2017/8/7 13:52
 * @describe 已过期库存详细页面
 */
public class OverdueInventoryActivity extends BaseActivity {
    @Bind(R.id.text_title_back)
    TextView mTextTitleBack;
    @Bind(R.id.text_title)
    TextView mTextTitle;

    @Bind(R.id.btn_confirm)
    Button mBtconfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overdue_inventory);
        ButterKnife.bind(this);
        initView();
        initTitleBar();
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
    }


    @Override
    public void initTitleBar() {
        super.initTitleBar();
        mTextTitleBack.setBackgroundResource(R.drawable.btn_back);
        mTextTitle.setText(R.string.title_inventory);
    }


}
