package com.example.admin.hn.ui.shop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.http.Constant;
import com.example.admin.hn.model.CityInfo;
import com.example.admin.hn.ui.adapter.CityAdapter;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolRefreshView;
import com.example.admin.hn.utils.ToolString;
import com.example.admin.hn.volley.RequestListener;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * 选择地区
 */
public class AreaActivity extends BaseActivity {

    @Bind(R.id.text_title_back)
    TextView textTitleBack;
    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.network_disabled)
    RelativeLayout network;
    @Bind(R.id.network_img)
    ImageView network_img;
    @Bind(R.id.noData_img)
    ImageView noData_img;
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    private List<CityInfo> list = new ArrayList<>();
    private CityAdapter mAdapter;
    private static Activity provinceContext;
    private static Activity cityContext;
    private String source;
    private CityInfo cityInfo;

    private String url = Api.SHOP_BASE_URL + Api.GET_AREA_LIST;
    private CityInfo provinceInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_layout);
        ButterKnife.bind(this);
        initTitleBar();
        initView();
        initData();
    }

    /**
     *
     * @param province  省
     * @param city      市
     * @param cityInfo
     * @param source
     */
    public static void startActivity(Activity province,Activity city, CityInfo cityInfo, String source) {
        provinceContext = province;
        cityContext = city;
        Intent intent = city.getIntent();
        intent.setClass(city, AreaActivity.class);
        intent.putExtra("source", source);
        intent.putExtra("cityInfo", cityInfo);
        city.startActivity(intent);
    }

    @OnClick({R.id.text_title_back,R.id.noData_img})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_title_back:
                finish();
                break;
            case R.id.noData_img:
                noData_img.setVisibility(View.GONE);
                sendHttp();
                break;
        }
    }


    @Override
    public void initTitleBar() {
        textTitle.setText("选择地区");
        textTitleBack.setBackgroundResource(R.drawable.btn_back);
        source = getIntent().getStringExtra("source");
        cityInfo = (CityInfo) getIntent().getSerializableExtra("cityInfo");
        provinceInfo = (CityInfo) getIntent().getSerializableExtra("provinceInfo");
    }


    @Override
    public void initData() {
        sendHttp();
    }
    @Override
    public void initView() {
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setEnableRefresh(false);
        mAdapter = new CityAdapter(this, R.layout.item_select_city, list);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setAreaValue(position);
            }
        });
    }

    private void setAreaValue(int position) {
        CityInfo areaInfo = list.get(position);
        Intent intent = new Intent(Constant.ACTION_CREATE_ADDRESS_ACTIVITY);
        intent.putExtra("provinceInfo", provinceInfo);
        intent.putExtra("cityInfo", cityInfo);
        intent.putExtra("areaInfo", areaInfo);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        if (cityContext != null) {
            cityContext.finish();//关闭省
        }
        if (provinceContext!=null) {
            provinceContext.finish();//关闭市
        }
        finish();//关闭地区
        cityContext = null;
        provinceContext = null;
    }

    private void sendHttp(){
        params.put("areaParentId", cityInfo.id + "");
        http.get(url, params,progressTitle, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.e("城市列表", json);
                if (GsonUtils.isShopSuccess(json)) {
                    progressTitle = null;
                    TypeToken typeToken = new TypeToken<List<CityInfo>>() {
                    };
                    List<CityInfo> cityInfos = (List<CityInfo>) GsonUtils.jsonToList2(json, typeToken, "content");
                    if (ToolString.isEmptyList(cityInfos)) {
                        if (list.size() > 0) {
                            list.clear();
                        }
                        list.addAll(cityInfos);
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    ToolAlert.showToast(context, GsonUtils.getError(json));
                }
                ToolRefreshView.hintView(mAdapter, false,network, noData_img, network_img);
            }

            @Override
            public void requestError(String message) {
                ToolAlert.showToast(context,message);
                ToolRefreshView.hintView(mAdapter, true,network, noData_img, network_img);
            }
        });
    }

}
