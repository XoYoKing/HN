package com.example.admin.hn.ui.fragment.read;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.example.admin.hn.MainActivity;
import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseFragment;
import com.example.admin.hn.http.OkHttpUtil;
import com.example.admin.hn.model.OrderInfo;
import com.example.admin.hn.ui.account.ReadDrawDetailActivity;
import com.example.admin.hn.ui.adapter.DrawAdapter;
import com.example.admin.hn.ui.article.ArticleDetailsActivity;
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
 * @describe 图纸管理
 */
public class ReadDrawFragment extends BaseFragment {

	private static final String TAG = "ReadDrawFragment";
	@Bind(R.id.listView)
	ListView listView;
	@Bind(R.id.sp)
	Spinner sp;
	@Bind(R.id.sp1)
	Spinner sp1;
	@Bind(R.id.ll_hide)
	LinearLayout hide;

	@Bind(R.id.network_disabled)
	RelativeLayout network;
	@Bind(R.id.network_img)
	ImageView network_img;
	@Bind(R.id.noData_img)
	ImageView noData_img;

	private String str = "";

	private ArrayList<OrderInfo.Order> list = new ArrayList<>();
	private DrawAdapter adapter;
	private View view;
	private int status = 1;
	private int page = 1;
	private int screen = 1;
	private String url_order = Api.BASE_URL + Api.ORDER;
	private ArrayList<String> data_list;
	private ArrayAdapter<String> arr_adapter;
	private ArrayList<String> data_list1;
	private ArrayAdapter<String> arr_adapter1;
	private String value;//被选中的类别
	private String value1;//被选中的类别
	private RefreshLayout refreshLayout;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_draw, container, false);
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
		refreshLayout.setRefreshFooter(new BallPulseFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale));
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
		adapter = new DrawAdapter(getActivity(), list);
		listView.setAdapter(adapter);
		//默认隐藏搜索条件
		hide.setVisibility(View.GONE);
		setSpData();
		setSpData1();
	}

	private void setSpData() {
		//数据
		data_list = new ArrayList<>();
		data_list.add("请选择");
		data_list.add("11111111");
		data_list.add("22222222");
		data_list.add("33333333");
		//适配器
		arr_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, data_list);
		//设置样式
		arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//加载适配器
		sp.setAdapter(arr_adapter);
		sp.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				value = data_list.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	private void setSpData1() {
		//数据
		data_list1 = new ArrayList<>();
		data_list1.add("请选择");
		data_list1.add("11111111");
		data_list1.add("22222222");
		data_list1.add("33333333");
		//适配器
		arr_adapter1 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, data_list);
		//设置样式
		arr_adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//加载适配器
		sp1.setAdapter(arr_adapter1);
		sp1.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				value1 = data_list1.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}


	@Override
	public void initData() {
		// 设置搜索文本监听

		data(1, "", 0);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ReadDrawDetailActivity.startActivity(activity,0,list.get(position).getShipname());
			}
		});
		listView.setTextFilterEnabled(true);
	}


	@Override
	public void initTitleBar() {
	}

	@OnClick({R.id.bt_reset,R.id.bt_query,R.id.fl_search,R.id.network_img})
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.bt_reset:
				ToolAlert.showToast(getContext(), "重置", false);
				break;
			case R.id.bt_query:
				ToolAlert.showToast(getContext(), "搜索", false);
				break;
			case R.id.fl_search:
				if (hide.getVisibility()==View.GONE) {
					hide.setVisibility(View.VISIBLE);
				} else {
					hide.setVisibility(View.GONE);
				}
				break;
			case R.id.network_img :
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
