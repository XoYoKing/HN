package com.example.admin.hn.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseActivity;
import com.orhanobut.logger.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AgreementActivity extends BaseActivity {

    private static final String TAG = "AgreementActivity";

    @Bind(R.id.text_title)
    TextView mTextTitle;
    @Bind(R.id.text_title_back)
    TextView mTextTitleBack;
    @Bind(R.id.webView)
    WebView mWebView;

    private String url = Api.BASE_URL + Api.AGREEMENT;
    private int agreementtype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);

        ButterKnife.bind(this);

        initTitleBar();
        Intent intent = getIntent();
        agreementtype = intent.getIntExtra("agreementtype", 0);

        Logger.i(TAG, "agreementtype=" + agreementtype);
        initData();
    }

    @OnClick(R.id.text_title_back)
    public void onClick(View v) {
        if (v.getId() == R.id.text_title_back) {
            finish();
        }
    }


    @Override
    public void initTitleBar() {
        super.initTitleBar();
        mTextTitle.setText(R.string.title_purchase_agreement);
        mTextTitleBack.setBackgroundResource(R.drawable.btn_back);
    }


    @Override
    public void initData() {
        super.initData();

        //mWebView = (WebView) findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.loadUrl(url);
    }
    }
