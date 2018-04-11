package com.example.admin.hn.ui.fragment.read;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.example.admin.hn.MainActivity;
import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseFragment;
import com.example.admin.hn.http.OkHttpUtil;
import com.example.admin.hn.model.OrderInfo;
import com.example.admin.hn.ui.adapter.SeaMapAdapter;
import com.example.admin.hn.ui.article.ArticleDetailsActivity;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolAppUtils;
import com.example.admin.hn.utils.ToolRefreshView;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author duantao
 * @date on 2017/7/26 16:04
 * @describe 海图资料目录
 */
public class ReadChartFragment extends BaseFragment implements OnRefreshListener,OnLoadmoreListener {

    private static final String TAG = "ReadDrawFragment";
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.network_disabled)
    RelativeLayout network;
    @Bind(R.id.network_img)
    ImageView network_img;
    @Bind(R.id.noData_img)
    ImageView noData_img;

    private String str = "";

    private ArrayList<OrderInfo.Order> list = new ArrayList<>();
    private SeaMapAdapter adapter;
    private View view;
    private int status = 1;
    private int page = 1;
    private int screen = 1;
    private String url_order = Api.BASE_URL + Api.ORDER;
    private ArrayList<String> data_list;
    private ArrayAdapter<String> arr_adapter;
    private String value;//被选中的类别
    private RefreshLayout refreshLayout;
    private View screen_view;
    private PopupWindow window;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list_layout, container, false);
        ButterKnife.bind(this, view);
        initTitleBar();
        initView();
        initData();
        return view;
    }

    @Override
    public void initView() {
        //下拉刷新
        refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        ToolRefreshView.setRefreshLayout(activity, refreshLayout,this,this);

        adapter = new SeaMapAdapter(getActivity(), R.layout.item_sea_map_layout, list);
        listView.setAdapter(adapter);

    }

    @Override
    public void initData() {
        // 设置搜索文本监听

        data(1, "", 0);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArticleDetailsActivity.startActivity(activity, 0, list.get(position).getShipname());
            }
        });
        listView.setTextFilterEnabled(true);
    }


    @Override
    public void initTitleBar() {
    }

    @OnClick({ R.id.network_img})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.network_img:
                network_img.setVisibility(View.GONE);
                page = 1;
                data(1, str, 0);
                refreshLayout.finishRefresh(1000);
                break;
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            page = 1;
            data(1, str, 0);
        }
    }


    public void data(final int status, String down, final int Loadmore) {
        for (int i = 0; i < 10; i++) {
            list.add(new OrderInfo.Order());
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 1;
        data(1, str, 0);
        refreshlayout.finishRefresh(1000);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        page = page + 1;
        data(status, str, 1);
        adapter.notifyDataSetChanged();
        refreshlayout.finishLoadmore(1000);
    }
}
