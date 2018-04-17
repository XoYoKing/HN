package com.example.admin.hn.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.base.HNApplication;
import com.example.admin.hn.model.ChartInfo;
import com.example.admin.hn.ui.adapter.ChartAdapter;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.volley.RequestListener;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zyyoona7.lib.EasyPopup;
import com.zyyoona7.lib.HorizontalGravity;
import com.zyyoona7.lib.VerticalGravity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author duantao
 * @date on 2017/8/2 16:08
 * @describe 海图管理
 */
public class ChartActivity extends BaseActivity {

    private static final String TAG = "ChartActivity";

    @Bind(R.id.text_title_back)
    TextView mTextTitleBack;
    @Bind(R.id.text_title)
    TextView mTextTitle;
    @Bind(R.id.lv_chart)
    ListView listView;
    @Bind(R.id.text_tile_right)
    TextView mTextright;
    @Bind(R.id.ll_hide)
    LinearLayout hide;
    @Bind(R.id.tv_choice)
    TextView tv_choice;
//    @Bind(R.id.tvSearch)
//    TextView tvSearch;
//    @Bind(R.id.etSearch)
//    EditText etSearch;

    private ArrayList<ChartInfo.Chart> list = new ArrayList<>();
    private ChartAdapter adapter;
    private boolean iSclick = false;
    private int page = 1;
    private String url_chart = Api.BASE_URL + Api.CHART;
    private String pop = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        ButterKnife.bind(this);
        initTitleBar();
        initView();
        initData();
    }


    private EasyPopup mCirclePop;
    private TextView pp_order;
    private TextView pp_ship;

    @Override
    public void initView() {
        super.initView();
        //popupwindow
        mCirclePop = new EasyPopup(ChartActivity.this)
                .setContentView(R.layout.layout_chart_comment)
                .setAnimationStyle(R.style.CirclePopAnim)
                        //是否允许点击PopupWindow之外的地方消失
                .setFocusAndOutsideEnable(true)
                        //允许背景变暗
                .setBackgroundDimEnable(true)
                        //变暗的透明度(0-1)，0为完全透明
                .setDimValue(0.4f)
                .createPopup();
        pp_order = mCirclePop.getView(R.id.pp_order);
        pp_ship = mCirclePop.getView(R.id.pp_ship);
        pp_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCirclePop.dismiss();
                tv_choice.setText("订单编号");
                pop = "3";
            }
        });
        pp_ship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCirclePop.dismiss();
                tv_choice.setText("船舶名称");
                pop = "2";
            }
        });
        //下拉刷新
        final RefreshLayout refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setDisableContentWhenLoading(true);
        refreshLayout.setDisableContentWhenRefresh(true);
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        //设置 Header 为 Material风格
        refreshLayout.setRefreshHeader(new MaterialHeader(this).setShowBezierWave(true));
        //设置 Footer 为 球脉冲
        refreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
        //监听
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                data("1");
                refreshlayout.finishRefresh(1000);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {

                refreshlayout.finishLoadmore(1000);
            }
        });
        adapter = new ChartAdapter(this, R.layout.chart_adapter, list);
        listView.setAdapter(adapter);
    }


    @Override
    public void initTitleBar() {
        super.initTitleBar();
        mTextTitleBack.setBackgroundResource(R.drawable.btn_back);
        mTextright.setBackgroundResource(R.drawable.search);
        mTextTitle.setText(R.string.title_chart);
        hide.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        data("1");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent_order = new Intent(ChartActivity.this, ChartDetailsActivity.class);
                intent_order.putExtra("ordernumber", list.get(position).getOrdernumber());
                intent_order.putExtra("shipname", list.get(position).getShipname());
                intent_order.putExtra("productnumber", list.get(position).getProductNumber());
                intent_order.putExtra("updatetime", list.get(position).getUpdatetime());
                startActivity(intent_order);
            }
        });
    }


    @OnClick({R.id.text_title_back, R.id.text_tile_right, R.id.tv_choice})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_title_back:
                finish();
                break;
            case R.id.text_tile_right:
                    if (iSclick == false) {
                        pop = "3";
                        hide.setVisibility(View.VISIBLE);
                        iSclick = true;
                    } else {
                        hide.setVisibility(View.GONE);
                        iSclick = false;
                    }
                break;
            case R.id.tv_choice:
                mCirclePop.showAtAnchorView(tv_choice, VerticalGravity.BELOW, HorizontalGravity.CENTER, 0, 0);
                break;
//            case R.id.tvSearch:
//                if (!etSearch.getText().toString().equals("")) {
//                    data(pop);
//                    hide.setVisibility(View.GONE);
//                    iSclick = false;
//                }else{
//                    ToolAlert.showToast(ChartActivity.this, "请输入搜索内容", false);
//                }

//                break;
        }
    }


    public void data(final String string) {
        //status 1(查询该用户海图) 2(根据海图名称)3(订单号)
        Map map = new HashMap();
//        map.put("ordernumber", etSearch.getText().toString());
//        map.put("ordername", etSearch.getText().toString());
        map.put("userid", HNApplication.mApp.getUserId());
        map.put("status", string);
        map.put("page", page);
        http.postJson(url_chart, map, progressTitle, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.i(TAG, json);
                if (GsonUtils.isSuccess(json)) {
                    ChartInfo chartInfo = GsonUtils.jsonToBean(
                            json, ChartInfo.class
                    );
                    list.clear();
                    for (int i = 0; i < chartInfo.getDocuments().size(); i++) {
                        list.add(new ChartInfo.Chart(chartInfo.getDocuments().get(i).getOrdernumber(), chartInfo.getDocuments().get(i).getShipname(), chartInfo.getDocuments().get(i).getUpdatetime(), chartInfo.getDocuments().get(i).getProductNumber()));
                    }
                    adapter.notifyDataSetChanged();
                }else {
                    ToolAlert.showToast(ChartActivity.this,GsonUtils.getError(json));
                }
            }

            @Override
            public void requestError(String message) {
                ToolAlert.showToast(ChartActivity.this,message);
            }
        });
    }
}
