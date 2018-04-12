package com.example.admin.hn.ui.fragment.shop;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.admin.hn.MainActivity;
import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseFragment;
import com.example.admin.hn.base.HNApplication;
import com.example.admin.hn.http.OkHttpUtil;
import com.example.admin.hn.model.OrderInfo;
import com.example.admin.hn.ui.adapter.ShopOrderManagerAdapter;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
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
 * @describe 商城订单管理
 */
public class ShopOrderManagerFragment extends BaseFragment implements OnLoadmoreListener,OnRefreshListener{

	private static final String TAG = "ShopOrderManagerFragment";
	@Bind(R.id.recycleView)
	RecyclerView recycleView;
	@Bind(R.id.network_disabled)
	RelativeLayout network;
	@Bind(R.id.network_img)
	ImageView network_img;
	@Bind(R.id.noData_img)
	ImageView noData_img;
	private String str = "";

	private ArrayList<OrderInfo.Order> list = new ArrayList<>();
	private ShopOrderManagerAdapter adapter;
	private View view;
	private int page = 1;
	private int screen = 1;
	private String url_order = Api.BASE_URL + Api.ORDER;
	private RefreshLayout refreshLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_hnshop, container, false);
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
		ToolRefreshView.setRefreshLayout(activity, refreshLayout, this, this);
		recycleView.setLayoutManager(new LinearLayoutManager(activity));
		adapter = new ShopOrderManagerAdapter(list);
		recycleView.setAdapter(adapter);
	}



	@Override
	public void initData() {
		// 设置搜索文本监听
		data(1, str, 0);
	}


	@Override
	public void initTitleBar() {
	}

	@OnClick({R.id.network_img})
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.network_img:
				network_img.setVisibility(View.GONE);
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
	public void onLoadmore(RefreshLayout refreshlayout) {

	}

	@Override
	public void onRefresh(RefreshLayout refreshlayout) {

	}
}
