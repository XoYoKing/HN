package com.example.admin.hn.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.admin.hn.MainActivity;
import com.example.admin.hn.R;
import com.example.admin.hn.base.BaseFragment;
import com.example.admin.hn.base.HNApplication;
import com.example.admin.hn.http.Constant;
import com.example.admin.hn.utils.ToolAlert;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author duantao
 * @date on 2017/7/26 16:04
 * @describe 地图
 */
public class MapFragment extends BaseFragment {

    @Bind(R.id.tv_top_title)
    TextView tv_top_title;
    @Bind(R.id.iv_two)
    ImageView iv_two;
    @Bind(R.id.iv_one)
    ImageView iv_one;
    @Bind(R.id.iv_search)
    ImageView iv_search;
    @Bind(R.id.web)
    WebView mWebView;
    @Bind(R.id.sp)
    Spinner sp;

    private String url = null;
    View view;
    private int WRITE_COARSE_LOCATION_REQUEST_CODE = 1;
    private Bundle savedInstanceState;
    private List<String> sp_list=new ArrayList<>();
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver br;
    private ArrayAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);
        this.savedInstanceState = savedInstanceState;
        init();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void init() {
        tv_top_title.setText("船位");
        iv_two.setVisibility(View.GONE);
        iv_one.setVisibility(View.GONE);
        iv_search.setVisibility(View.GONE);
        initBroadcastReceiver();
        sendHttp();
    }

    private void sendHttp(){
        setSpData();
    }

    /**
     * 初始化 审核状态选择器
     */
    private void setSpData() {
        //数据
        for (int i = 0; i < MainActivity.list.size(); i++) {
            sp_list.add(MainActivity.list.get(i).shipname);
        }
        //适配器
        adapter = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, sp_list);
        //加载适配器
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               String shipname = sp_list.get(position);
               ship(shipname);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (MainActivity.list.size() > 0) {
            ship(MainActivity.list.get(0).shipname);
        }
    }

    private void setUrl(String shipName){
        url = "http://api.shipxy.com/apicall/location?k=a7222a0180264aa99245ff2b53595a31&kw=" +shipName+ "&tip=1&track=1";
    }

    /**
     * 加载船舶信息
     * @param shipName
     */
    private void ship(String shipName){
        if (isFirstHttp) {
            isFirstHttp = false;
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
        }
        setUrl(shipName);
        if (url != null) {
            mWebView.loadUrl(url);
        }
    }
    private void initBroadcastReceiver(){
        localBroadcastManager = LocalBroadcastManager.getInstance(activity);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_MAP_FRAGMENT);
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    if (sp_list.size() > 0) {
                        sp_list.clear();
                    }
                    for (int i = 0; i < MainActivity.list.size(); i++) {
                        sp_list.add(MainActivity.list.get(i).shipname);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        };
        localBroadcastManager.registerReceiver(br, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(br);
    }
}
