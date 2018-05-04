package com.example.admin.hn.ui.shop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseActivity;
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
 * 选择市
 */
public class CityActivity extends BaseActivity{

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
    private String source;
    private CityInfo provinceInfo;
    private String url = Api.SHOP_BASE_URL + Api.GET_AREA_LIST;
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
     * 跳转到 CityActivity
     *
     * @param province  省
     */
    public static void startActivity(Activity province, CityInfo cityInfo, String source) {
        provinceContext = province;
        Intent intent = new Intent(province, CityActivity.class);
        intent.putExtra("source", source);
        intent.putExtra("provinceInfo", cityInfo);
        province.startActivity(intent);
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
        textTitle.setText("选择城市");
        textTitleBack.setBackgroundResource(R.drawable.btn_back);
        source = getIntent().getStringExtra("source");
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
                CityInfo cityInfo = list.get(position);
                AreaActivity.startActivity(provinceContext,CityActivity.this, cityInfo, source);
            }
        });
    }

    private void sendHttp(){
        params.put("areaParentId", provinceInfo.id + "");
        http.get(url, params, progressTitle,new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.e("城市列表", json);
                if (GsonUtils.isShopSuccess(json)) {
                    progressTitle = null;
                    TypeToken typeToken = new TypeToken<List<CityInfo>>() {
                    };
                    List<CityInfo> cityInfos = (List<CityInfo>) GsonUtils.jsonToList2(json, typeToken, "content");                    if (ToolString.isEmptyList(cityInfos)) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
