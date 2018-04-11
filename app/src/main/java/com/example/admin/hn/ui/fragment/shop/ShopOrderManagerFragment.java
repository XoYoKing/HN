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
public class ShopOrderManagerFragment extends BaseFragment {

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
	private int status = 1;
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
		refreshLayout.setDisableContentWhenLoading(true);
		refreshLayout.setDisableContentWhenRefresh(true);
		refreshLayout.setEnableScrollContentWhenLoaded(true);
		//设置 Header 为 Material风格
		refreshLayout.setRefreshHeader(new MaterialHeader(getActivity()).setShowBezierWave(true));
		//设置 Footer 为 球脉冲
//		refreshLayout.setRefreshFooter(new BallPulseFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale));
		//监听
		refreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(RefreshLayout refreshlayout) {
				page = 1;
				data(1, str, 0);
				refreshlayout.finishRefresh(1000);
			}
		});
		refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
			@Override
			public void onLoadmore(RefreshLayout refreshlayout) {
				page = page + 1;
				data(status, str, 1);
				adapter.notifyDataSetChanged();
				refreshlayout.finishLoadmore(1000);
			}
		});

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
		//status1(查询该用户全部订单) 2(根据船舶名称)3(订单号) 4(船舶编号)
		//screen1 ：审核   2:已完成  3：失败
		Map map = new HashMap();
		map.put("ordernumber", down);
		map.put("shipnumber", down);
		map.put("userid", MainActivity.USER_ID);
		map.put("shipname", down);
		map.put("status", status);
		if (Loadmore == 0) {
			map.put("page", "1");
		} else {
			map.put("page", page);
		}
		map.put("screen", screen);

		String jsonStr = GsonUtils.mapToJson(map);
		Logger.i(TAG, jsonStr);
		try {
			OkHttpUtil.postJsonData2Server(getActivity(),
					url_order,
					jsonStr,
					new OkHttpUtil.MyCallBack() {
						@Override
						public void onFailure(Request request, IOException e) {
							ToolAlert.showToast(getActivity(), "服务器异常,请稍后再试", false);
							ToolRefreshView.hintView(adapter, true, network, noData_img, network_img);
						}

						@Override
						public void onResponse(String json) {
							Logger.i(TAG, json);
							OrderInfo orderInfo = GsonUtils.jsonToBean(
									json, OrderInfo.class
							);
							if (orderInfo.getStatus().equals("error")) {
								ToolAlert.showToast(getActivity(), orderInfo.getMessage(), false);
								if (page==1) {
									ToolAlert.showToast(getActivity(), orderInfo.getMessage(), false);
									list.clear();
									adapter.notifyDataSetChanged();
								}else {
									ToolAlert.showToast(getActivity(), "已全部加载完成", false);

								}
							} else {
								if (Loadmore == 0) {
									list.clear();
								}
								if (orderInfo.getDocuments().size() == 0) {
									list.clear();
								}else {
									for (int i = 0; i < orderInfo.getDocuments().size(); i++) {
										list.add(new OrderInfo.Order(orderInfo.getDocuments().get(i).getOrdernumber(), orderInfo.getDocuments().get(i).getOrdertime(), orderInfo.getDocuments().get(i).getStatus(), orderInfo.getDocuments().get(i).getShipname()));
									}
									MainActivity.ship = list.get(0).getShipname();
								}
								adapter.notifyDataSetChanged();
							}
							ToolRefreshView.hintView(adapter, false, network, noData_img, network_img);
						}
					});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
