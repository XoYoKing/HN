package com.example.admin.hn.ui.fragment.shop.bean;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.base.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author duantao
 * @date on 2017/7/26 16:04
 * @describe 商品详情界面
 */
public class GoodsDetailFragment extends BaseFragment {
	@Bind(R.id.web)
	WebView mWebView;
	private View view;
	private String url;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_goods_detail, container, false);
		ButterKnife.bind(this, view);
		initTitleBar();
		initView();
		initData();
		return view;
	}

	@Override
	public void initView() {
		Bundle bundle = getArguments();
		if (bundle != null) {
			url = bundle.getString("type");
		}
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
		mWebView.loadUrl(url);
	}

	@Override
	public void initData() {

	}


	@Override
	public void initTitleBar() {

	}
}
