package com.example.admin.hn.ui.account;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;

import com.example.admin.hn.utils.ToolString;
import com.example.admin.hn.volley.RequestListener;
import com.orhanobut.logger.Logger;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 订单退回
 *
 * @author Administrator
 */
public class ReturnActivity extends BaseActivity {
    @Bind(R.id.text_title_back)
    TextView textTitleBack;
    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.tv_numberNo)
    TextView tv_numberNo;
    @Bind(R.id.et_content)
    EditText et_content;
    @Bind(R.id.bt_return)
    Button bt_return;

    private String url = Api.BASE_URL + Api.BACK_APPLY;
    private String applyno;
    private String remark;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);
        ButterKnife.bind(this);
        initTitleBar();
        initData();
    }

    @Override
    public void initTitleBar() {
        Intent intent = getIntent();
        applyno = intent.getStringExtra("applyno");
        textTitle.setText("退回");
        textTitleBack.setBackgroundResource(R.drawable.btn_back);
        tv_numberNo.setText(applyno + "");
    }

    /**
     *
     */
    public static void startActivity(Context context, String applyno) {
        Intent intent = new Intent(context, ReturnActivity.class);
        intent.putExtra("applyno", applyno);
        ((Activity) context).startActivityForResult(intent, 1000);
    }

    @OnClick({R.id.text_title_back, R.id.bt_return})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_title_back:
                close();
                break;
            case R.id.bt_return:
                remark = et_content.getText().toString();
                if (ToolString.isEmpty(remark)) {
                    returnApplyNo();
                }else {
                    ToolAlert.showToast(context, "请输入退回备注");
                }
                break;
        }
    }

    @Override
    public void initData() {
        super.initData();
    }

    private void returnApplyNo() {
        params.put("applyNo", applyno + "");
        params.put("remark", remark);
        http.postJson(url, params, "正在退回...",new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.e("退回", json);
                if (GsonUtils.isSuccess(json)) {
                    close();
                } else {
                    ToolAlert.showToast(context, GsonUtils.getError(json));
                }
            }

            @Override
            public void requestError(String message) {
                ToolAlert.showToast(context, message);
            }
        });
    }


    private void close() {
        setResult(1000);
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            close();
        }
        return super.onKeyDown(keyCode, event);
    }

}
