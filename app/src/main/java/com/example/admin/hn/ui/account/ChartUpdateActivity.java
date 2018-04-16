package com.example.admin.hn.ui.account;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.admin.hn.MainActivity;
import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.base.HNApplication;
import com.example.admin.hn.http.OkHttpUtil;
import com.example.admin.hn.model.UpdateInfo;
import com.example.admin.hn.ui.adapter.GroupAdapter;
import com.example.admin.hn.ui.adapter.UpdateAdapter;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolRefreshView;
import com.example.admin.hn.volley.RequestListener;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.okhttp.Request;
import com.zyyoona7.lib.EasyPopup;
import com.zyyoona7.lib.HorizontalGravity;
import com.zyyoona7.lib.VerticalGravity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChartUpdateActivity extends BaseActivity implements AdapterView.OnItemClickListener ,OnLoadmoreListener,OnRefreshListener {
	private static final String TAG = "ChartUpdateActivity";

	@Bind(R.id.text_title_back)
	TextView mTextTitleBack;
	@Bind(R.id.text_title)
	TextView mTextTitle;
	@Bind(R.id.text_tile_right)
	TextView mTextright;
	@Bind(R.id.network_disabled)
	RelativeLayout network;
	@Bind(R.id.network_img)
	ImageView network_img;
	@Bind(R.id.noData_img)
	ImageView noData_img;
	@Bind(R.id.tv_choice)
	TextView tv_choice;
	@Bind(R.id.ll_hide)
	LinearLayout hide;
	@Bind(R.id.listView)
	ListView listView;
	@Bind(R.id.searchView)
	SearchView mSearchView;
	@Bind(R.id.sp_environment)
	Spinner environment;
	@Bind(R.id.sp)
	Spinner sp;
	private ArrayList<UpdateInfo.update> list = new ArrayList<>();
	private UpdateAdapter adapter;
	private boolean iSclick = false;
	private String url_chart = Api.BASE_URL + Api.SHIPPACKAGE;
	private int page = 1;
	private int screen = 1;
	private String status = "2";
	private EasyPopup mCirclePop;
	private TextView pp_order;
	private TextView pp_ship;
	private TextView pp_name;
	private List<String> lists,listss;
	private ArrayAdapter<String> arr_adapter,adapters;
	private String dates,datess;
	private RefreshLayout refreshLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chart_update);
		ButterKnife.bind(this);
		initTitleBar();
		initView();
		data("", "1", 1);
	}


	@Override
	public void initTitleBar() {
		super.initTitleBar();
		mTextTitleBack.setBackgroundResource(R.drawable.btn_back);
		mTextTitle.setText(R.string.title_chart_update);
		mTextright.setBackgroundResource(R.drawable.sort);
		hide.setVisibility(View.GONE);
	}

	public static void startActivity(Context context){
		Intent intent = new Intent(context, ChartUpdateActivity.class);
		context.startActivity(intent);
	}


	@Override
	public void initView() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		lists = new ArrayList<>();
		for (int i = 1; i < 54; i++) {
			lists.add(i+"");
		}
		listss= new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			listss.add(year-i+"");
		}
		datess = listss.get(0);
		dates = lists.get(0);
		//适配器
		arr_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listss);
		//设置样式
		arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapters= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lists);
		adapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//加载适配器
		environment.setAdapter(arr_adapter);
		environment.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				datess = listss.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		sp.setAdapter(adapters);
		sp.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				dates = lists.get(position);
				page = 1;
				data("", status, 1);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		tv_choice.setText("船舶名称");
		Drawable nav_up = getResources().getDrawable(R.drawable.seletor_stock_arrow);
		nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
		tv_choice.setCompoundDrawables(null, null, nav_up, null);
		hide.setVisibility(View.GONE);

		// 设置搜索文本监听
		mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			// 当点击搜索按钮时触发该方法
			@Override
			public boolean onQueryTextSubmit(String query) {
//                ToolAlert.showToast(getActivity(), "搜索" + query, false);
				page = 1;
				data(query, status, 1);
				hide.setVisibility(View.GONE);
				return false;
			}

			// 当搜索内容改变时触发该方法
			@Override
			public boolean onQueryTextChange(String newText) {
				if (!TextUtils.isEmpty(newText)) {
//                    listView.setFilterText(newText);
				} else {
					listView.clearTextFilter();
				}
				return false;
			}
		});
		mCirclePop = new EasyPopup(ChartUpdateActivity.this)
				.setContentView(R.layout.layout_chart_update)
				.setAnimationStyle(R.style.CirclePopAnim)
						//是否允许点击PopupWindow之外的地方消失
				.setFocusAndOutsideEnable(true)
						//允许背景变暗
				.setBackgroundDimEnable(true)
						//变暗的透明度(0-1)，0为完全透明
				.setDimValue(0.4f)
				.createPopup();
		pp_ship = mCirclePop.getView(R.id.pp_ship);
		pp_name = mCirclePop.getView(R.id.ship_name);
		pp_ship.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mCirclePop.dismiss();
				tv_choice.setText("船舶名称");
				status = "2";
			}
		});
		pp_name.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mCirclePop.dismiss();
				tv_choice.setText("船舶编号");
				status = "3";
			}
		});
		//下拉刷新
		refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
		ToolRefreshView.setRefreshLayout(context,refreshLayout,this,this);
		adapter = new UpdateAdapter(ChartUpdateActivity.this, R.layout.order_adapter, list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(ChartUpdateActivity.this, ChartUpdatedetailsActivity.class);
				intent.putExtra("shipstatus", list.get(position).getShipstatus());
				intent.putExtra("updatetime", list.get(position).getUpdatetime());
				intent.putExtra("shipnumber", list.get(position).getShipnumber());
				intent.putExtra("download", list.get(position).getDownload());
				intent.putExtra("shipname", list.get(position).getShipname());
				intent.putExtra("ordertime", list.get(position).getOrdertime());
				intent.putExtra("size", list.get(position).getSize());
				startActivity(intent);
			}
		});
	}


	@OnClick({R.id.text_title_back, R.id.fl_search, R.id.tv_choice, R.id.text_tile_right})
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.text_title_back:
				finish();
				break;
			case R.id.fl_search:
				if (iSclick == false) {
					hide.setVisibility(View.VISIBLE);
					mSearchView.clearFocus();
					iSclick = true;
				} else {
					hide.setVisibility(View.GONE);
					iSclick = false;
				}
				break;
			case R.id.tv_choice:
				mCirclePop.showAtAnchorView(tv_choice, VerticalGravity.BELOW, HorizontalGravity.CENTER, 0, 0);
				break;
			case R.id.text_tile_right:
				showPopwindow(v);
				break;

		}
	}


	public void data(String scree, String statu, final int Loadmore) {
		Map map = new HashMap();
		//screen(1全部2已同步3未同步)  status（1全部2船舶名称3船舶编号）
		map.put("screen", screen);
		map.put("page", page);
		map.put("time", datess+dates);
		map.put("shipnumber", scree);
		map.put("userid", HNApplication.mApp.getUserId());
		map.put("shipname", scree);
		map.put("status", statu);
		http.postJson(url_chart, map, progressTitle, new RequestListener() {
			@Override
			public void requestSuccess(String json) {
				Logger.i(TAG, json);
				if (GsonUtils.isSuccess(json)) {
					UpdateInfo updateInfo = GsonUtils.jsonToBean(
							json, UpdateInfo.class
					);
					if (Loadmore == 1) {
						list.clear();
					}
					if (updateInfo.getDocuments().size()==0){
						list.clear();
					}else {
						for (int i = 0; i < updateInfo.getDocuments().size(); i++) {
							list.add(new UpdateInfo.update(updateInfo.getDocuments().get(i).getShipname(), updateInfo.getDocuments().get(i).getShipnumber(), updateInfo.getDocuments().get(i).getOrdertime(), updateInfo.getDocuments().get(i).getUpdatetime(), updateInfo.getDocuments().get(i).getSize(), updateInfo.getDocuments().get(i).getDownload(), updateInfo.getDocuments().get(i).getShipstatus()));
						}
					}
				}else {
					if (page==1) {
						ToolAlert.showToast(context, GsonUtils.getError(json));
						list.clear();
					}else {
						ToolAlert.showToast(ChartUpdateActivity.this, "已全部加载完成", false);
					}
				}
				ToolRefreshView.hintView(adapter,false,network,noData_img,network_img);
			}

			@Override
			public void requestError(String message) {
				ToolAlert.showToast(context, message);
				ToolRefreshView.hintView(adapter,true,network,noData_img,network_img);
			}
		});

	}

	private PopupWindow mPopupWindow;
	private GroupAdapter groupAdapter;
	private View contentView;
	private ArrayList<String> group;
	private ListView listview;

	//筛选栏
	private void showPopwindow(View parent) {
		if (mPopupWindow == null) {
			LayoutInflater mLayoutInflater = LayoutInflater.from(this);
			contentView = mLayoutInflater.inflate(R.layout.group_list, null);
			listview = (ListView) contentView.findViewById(R.id.lv_group);
			// 加载数据
			group = new ArrayList<>();
			group.add("全部");
			group.add("已同步");
			group.add("未同步");
			groupAdapter = new GroupAdapter(this,R.layout.group_item, group);
			listview.setAdapter(groupAdapter);

			mPopupWindow = new PopupWindow(contentView, getWindowManager()
					.getDefaultDisplay().getWidth() / 3, getWindowManager()
					.getDefaultDisplay().getHeight() / 3);
		}
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setFocusable(true);

		// 显示的位置为:屏幕的宽度的1/16
		int xPos = getWindowManager().getDefaultDisplay().getWidth() / 16;

		mPopupWindow.showAsDropDown(parent, xPos, 0);

		listview.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
//        mHomeNameTextView.setText(groups.get(position));

		if (mPopupWindow != null) {
			mPopupWindow.dismiss();
		}

//        Toast.makeText(MyBillActivity.this, "点击了" + position + "项", Toast.LENGTH_SHORT).show();
		switch (position) {
			case 0:
//                Toast.makeText(ChartUpdateActivity.this, "全部", Toast.LENGTH_SHORT).show();
				screen = 1;
				page = 1;
				data("", "1", 1);
				break;
			case 1:
				page = 1;
				screen = 2;
				data("", "1", 1);
//                Toast.makeText(ChartUpdateActivity.this, "成功", Toast.LENGTH_SHORT).show();
				break;

			case 2:
				page = 1;
				screen = 3;
				data("", "1", 1);
//                Toast.makeText(ChartUpdateActivity.this, "失败", Toast.LENGTH_SHORT).show();
				break;
		}
	}

	@Override
	public void onLoadmore(RefreshLayout refreshlayout) {
		page = page + 1;
		data("", "1", 2);
		refreshlayout.finishLoadmore(1000);
	}

	@Override
	public void onRefresh(RefreshLayout refreshlayout) {
		page = 1;
		data("", "1", 1);
		refreshlayout.finishRefresh(1000);
	}
}
