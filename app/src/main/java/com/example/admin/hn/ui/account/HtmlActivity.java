package com.example.admin.hn.ui.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;


import com.example.admin.hn.R;
import com.example.admin.hn.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HtmlActivity extends BaseActivity {


    private String url = null;
    @Bind(R.id.text_title_back)
    TextView mTextTitleBack;
    @Bind(R.id.text_title)
    TextView mTextTitle;
    @Bind(R.id.web)
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html);
        ButterKnife.bind(this);
        initTitleBar();
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        WebSettings ws = mWebView.getSettings();
        //支持javascript
        mWebView.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        ws.setSupportZoom(true);
        ws.setDefaultZoom(WebSettings.ZoomDensity.FAR);
//        ws.setBuiltInZoomControls(true);//设置出现缩放工具
        //自适应屏幕
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.setWebViewClient(
                new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }

                }
        );
        if (url != null) {
            mWebView.loadUrl(url);
        } else

        {
            finish();
        }

    }

    @Override
    public void initTitleBar() {
        super.initTitleBar();
        mTextTitleBack.setBackgroundResource(R.drawable.btn_back);
        mTextTitle.setText("海图");
    }

    @OnClick(R.id.text_title_back)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_title_back:
                finish();
                break;
        }
    }



    /**
     * 跳转到WebViewActivity
     *
     * @param context 指定跳转的activity
     * @param url     url
     */
    public static void startActivity(Activity context, String url) {
        Intent intent = new Intent(context, HtmlActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }


}
