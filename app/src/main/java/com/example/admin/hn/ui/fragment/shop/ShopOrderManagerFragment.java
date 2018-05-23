package com.example.admin.hn.ui.fragment.shop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseFragment;
import com.example.admin.hn.ui.adapter.ShopOrderListAdapter;
import com.example.admin.hn.ui.adapter.ShopOrderManagerAdapter;
import com.example.admin.hn.ui.fragment.shop.bean.ShopOrderInfo;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.SpaceItemDecoration;
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

	private ArrayList<ShopOrderInfo> list = new ArrayList<>();
	private ShopOrderListAdapter adapter;
	private View view;
	private String url = Api.SHOP_BASE_URL + Api.GET_LIST_ORDER;
	private RefreshLayout refreshLayout;
	private String type;
	private int page;
	private int rows = 10;
	private int totalPage;

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
		recycleView.addItemDecoration(new SpaceItemDecoration(0,30,0,0));
		adapter = new ShopOrderListAdapter(activity,list);
		recycleView.setAdapter(adapter);
	}



	@Override
	public void initData() {
		Bundle bundle = getArguments();
		type = bundle.getString("type");
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (getUserVisibleHint() && isFirstHttp && http != null) {
			isFirstHttp = false;
			sendHttp();
		}
	}


	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser && http != null) {
			isFirstHttp = false;
			sendHttp();
		}
	}

	@Override
	public void initTitleBar() {

	}

	@OnClick({R.id.network_img})
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.network_img:
				network_img.setVisibility(View.GONE);
				sendHttp();
				break;
		}
	}

	private void sendHttp(){
		params.put("page", page+"");
		params.put("rows", rows+"");
		params.put("status", type+"");
		http.get(url, params, progressTitle, new RequestListener() {
			@Override
			public void requestSuccess(String json) {
				Logger.e("订单管理", json);
				if (GsonUtils.isShopSuccess(json)) {
					progressTitle = null;
					TypeToken typeToken=new TypeToken<List<ShopOrderInfo>>(){};
					List<ShopOrderInfo> data = (List<ShopOrderInfo>) GsonUtils.jsonToList(json, typeToken, "data");
					if (ToolString.isEmptyList(data)) {
						if (isRefresh) {
							list.clear();
						}
						list.addAll(data);
					}
				}else {
					ToolAlert.showToast(activity,GsonUtils.getError(json));
				}
				ToolRefreshView.hintView(adapter,refreshLayout, false, network, noData_img, network_img);
			}

			@Override
			public void requestError(String message) {
				ToolAlert.showToast(activity, message);
				ToolRefreshView.hintView(adapter, refreshLayout,true, network, noData_img, network_img);
			}
		});
	}

	@Override
	public void onLoadmore(RefreshLayout refreshlayout) {
		isRefresh = false;
		page=page+1;
		if (ToolRefreshView.isLoadMore(refreshlayout,page, totalPage)) {
			sendHttp();
		}
	}

	@Override
	public void onRefresh(RefreshLayout refreshlayout) {
		isRefresh = true;
		page = 0;
		sendHttp();
	}
}
