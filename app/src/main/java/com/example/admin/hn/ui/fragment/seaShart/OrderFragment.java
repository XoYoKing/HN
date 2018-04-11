package com.example.admin.hn.ui.fragment.seaShart;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.example.admin.hn.MainActivity;
import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseFragment;
import com.example.admin.hn.base.HNApplication;
import com.example.admin.hn.http.OkHttpUtil;
import com.example.admin.hn.model.OrderInfo;
import com.example.admin.hn.ui.account.MessageCenterActivity;
import com.example.admin.hn.ui.account.OrderActivity;
import com.example.admin.hn.ui.adapter.GroupAdapter;
import com.example.admin.hn.ui.adapter.OrderAdapter;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.okhttp.Request;
import com.zyyoona7.lib.EasyPopup;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author duantao
 * @date on 2017/7/26 16:04
 * @describe 订单管理
 */
public class OrderFragment extends BaseFragment {

	private static final String TAG = "OrderFragment";
	@Bind(R.id.tv_top_title)
	TextView title;
	@Bind(R.id.text_tile_right)
	FrameLayout right;
	@Bind(R.id.iv_two)
	ImageView iv_two;
	//    @Bind(R.id.btn)
//    TitleButton mTBtn;
	@Bind(R.id.lv_order)
	ListView listView;
	@Bind(R.id.btn_left)
	Button mLeft;
	@Bind(R.id.btn_right)
	Button mRight;
	@Bind(R.id.startdate)
	TextView startdate;
	@Bind(R.id.enddate)
	TextView enddate;
	//    @Bind(R.id.etSearch)
//    EditText etSearch;
//    @Bind(R.id.ivDeleteText)
//    ImageView ivDeleteText;
	@Bind(R.id.tv_choice)
	TextView tv_choice;
	@Bind(R.id.ll_hide)
	LinearLayout hide;
	@Bind(R.id.iv_search)
	ImageView search;
	@Bind(R.id.searchView)
	SearchView mSearchView;
	@Bind(R.id.fl_search)
	FrameLayout fl_search;

	private GroupAdapter groupAdapter;
	private PopupWindow mPopupWindow;
	private View contentView;
	private ArrayList<String> group;
	private ListView listview;
	private String str = "";

	private ArrayList<OrderInfo.Order> list = new ArrayList<>();
	private OrderAdapter adapter;
	private View view;
	//是否审核1已审核2未审核
	private String statu = "1";
	private boolean iSclick = false;
	//搜索条件1(查询该用户全部订单) 2(根据船舶名称)3(船舶编号)4(订单号)
	private int status = 1;
	private int page = 1;
	private int screen = 1;
	private String url_order = Api.BASE_URL + Api.ORDER;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.activity_order_fragment, container, false);
		ButterKnife.bind(this, view);
		initTitleBar();
		initView();
		initData();
		return view;
	}

	private EasyPopup mCirclePop;
	private TextView pp_order;
	private TextView pp_ship;

	@Override
	public void initView() {
		//popupwindow
//        mCirclePop = new EasyPopup(getActivity())
//                .setContentView(R.layout.layout_circle_comment)
//                .setAnimationStyle(R.style.CirclePopAnim)
//                        //是否允许点击PopupWindow之外的地方消失
//                .setFocusAndOutsideEnable(true)
//                        //允许背景变暗
//                .setBackgroundDimEnable(true)
//                        //变暗的透明度(0-1)，0为完全透明
//                .setDimValue(0.4f)
//                .createPopup();
//        pp_ship = mCirclePop.getView(R.id.pp_ship);
//        pp_order = mCirclePop.getView(R.id.pp_order);
//        pp_order.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mCirclePop.dismiss();
//                tv_choice.setText("船舶编号");
//                status = 4;
//
//            }
//        });
//        pp_ship.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mCirclePop.dismiss();
//                tv_choice.setText("船舶名称");
//                status = 2;
//
//            }
//        });
		tv_choice.setText("船舶名称：");
		hide.setVisibility(View.GONE);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new Date());
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		startdate.setText(sdf.format(c.getTime()));

		enddate.setText(date);
		//下拉刷新
		final RefreshLayout refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
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
		adapter = new OrderAdapter(getActivity(), R.layout.order_adapter,list);
		listView.setAdapter(adapter);
		//完成与未完成button监听
//        mTBtn.setOnLeftClickListener(new TitleButton.OnLeftClickListener() {
//
//            @Override
//            public void onLeftClickListener() {
////                ToolAlert.showToast(getActivity(), "已审核", false);
//                statu = "1";
//                data(status,0);
//                adapter.notifyDataSetChanged();
//            }
//        });
//        mTBtn.setOnRightClickListener(new TitleButton.OnRightClickListener() {
//
//            @Override
//            public void onRightClickListener() {
////                ToolAlert.showToast(getActivity(), "未审核", false);
//                statu = "2";
//                data(status,0);
//                adapter.notifyDataSetChanged();
//            }
//        });
	}


	@Override
	public void initData() {
		// 设置搜索文本监听
		mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			// 当点击搜索按钮时触发该方法
			@Override
			public boolean onQueryTextSubmit(String query) {
//                ToolAlert.showToast(getActivity(), "搜索" + query, false);
				status = 2;
				data(status, query, 0);
				hide.setVisibility(View.GONE);
				return false;
			}

			// 当搜索内容改变时触发该方法
			@Override
			public boolean onQueryTextChange(String newText) {
				if (!TextUtils.isEmpty(newText)) {
//                    listView.setFilterText(newText);
					str = newText;
				} else {
					listView.clearTextFilter();
				}
				return false;
			}
		});
		data(1, "", 0);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent_order;
//                if (statu.equals("1")) {
				intent_order = new Intent(getActivity(), OrderActivity.class);
//                } else {
//                    intent_order = new Intent(getActivity(), NotsubmittedOrderActivity.class);
//                }

				intent_order.putExtra("shipname", list.get(position).getShipname());
				intent_order.putExtra("number", list.get(position).getOrdernumber());
				startActivity(intent_order);
			}
		});
		listView.setTextFilterEnabled(true);
	}


	@Override
	public void initTitleBar() {
		if (HNApplication.mApp.getMsgNumber() == 0) {
			iv_two.setVisibility(View.GONE);
		}
		title.setText("海图");
		mLeft.setText("已审批");
		mRight.setText("未审批");

//        etSearch.addTextChangedListener(new TextWatcher() {
//
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // TODO Auto-generated method stub
//
//            }
//
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//                // TODO Auto-generated method stub
//
//            }
//
//            public void afterTextChanged(Editable s) {
//                if (s.length() == 0) {
//                    ivDeleteText.setVisibility(View.GONE);
//                } else {
//                    ivDeleteText.setVisibility(View.VISIBLE);
//                }
//            }
//        });


	}


	@OnClick({R.id.text_tile_right, R.id.startdate, R.id.enddate, R.id.tv_choice, R.id.iv_search, R.id.fl_search})
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.text_tile_right:
				iv_two.setVisibility(View.GONE);
				Intent intent = new Intent(getActivity(), MessageCenterActivity.class);
				startActivity(intent);
				break;
			case R.id.startdate:
				//时间选择器
				TimePickerView pvTime = new TimePickerView.Builder(getActivity(), new TimePickerView.OnTimeSelectListener() {
					@Override
					public void onTimeSelect(Date date, View v) {//选中事件回调
						startdate.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
						status = 2;
						data(status, "", 0);
						Logger.i(TAG, date + "时间");
					}
				}).isCyclic(true).setBackgroundId(0x00FFFFFF).setContentSize(21).setType(new boolean[]{true, true, true, false, false, false}).build();
				pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
				pvTime.show();

				break;
			case R.id.enddate:
				//时间选择器
				TimePickerView pTime = new TimePickerView.Builder(getActivity(), new TimePickerView.OnTimeSelectListener() {
					@Override
					public void onTimeSelect(Date date, View v) {//选中事件回调
						enddate.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
						status = 2;
						data(status, "", 0);
						Logger.i(TAG, date + "时间");
					}
				}).isCyclic(true).setBackgroundId(0x00FFFFFF).setContentSize(21).setType(new boolean[]{true, true, true, false, false, false}).build();
				pTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
				pTime.show();

				break;
//            case R.id.ivDeleteText:
//                etSearch.setText("");
//                break;

			case R.id.tv_choice:
//                mCirclePop.showAtAnchorView(tv_choice, VerticalGravity.BELOW, HorizontalGravity.CENTER, 0, 0);
				break;

			case R.id.iv_search:
				showPopwindow(v);
				break;
			case R.id.fl_search:
				if (iSclick == false) {
					hide.setVisibility(View.VISIBLE);
					mSearchView.clearFocus();
					iSclick = true;
				} else {
					status = 1;
					hide.setVisibility(View.GONE);
					iSclick = false;
				}
				break;
//            case R.id.tvSearch:
//                if (!etSearch.getText().toString().equals("")) {
//                    data(status,0);
//                    hide.setVisibility(View.GONE);
//                    iSclick = false;
//                    status = 1;
//                }else {
//                    ToolAlert.showToast(getActivity(), "请输入搜索内容", false);
//
//                }

		}
	}


	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (!hidden) {
			if (HNApplication.mApp.getMsgNumber() == 0) {
				iv_two.setVisibility(View.GONE);
			} else {
				iv_two.setVisibility(View.VISIBLE);
			}
			page = 1;
			data(1, str, 0);
		}
	}


	public void data(final int status, String down, final int Loadmore) {
		//status1(查询该用户全部订单) 2(根据船舶名称)3(订单号) 4(船舶编号)
		//screen1 ：审核   2:已完成  3：失败
		Map map = new HashMap();
		map.put("ordernumber", down);
		map.put("starttime", startdate.getText().toString());
		map.put("endtime", enddate.getText().toString());
		map.put("shipnumber", down);
		map.put("userid", HNApplication.mApp.getUserId());
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
									HNApplication.mApp.setShipName(list.get(0).getShipname());
								}
								adapter.notifyDataSetChanged();
							}

						}
					});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//    public ArrayList<OrderInfo.Order> getlist(String statu,ArrayList<OrderInfo.Order> list){
//        ArrayList<OrderInfo.Order> lists = new ArrayList<>();
//        if (!statu.equals("1")){
//            for (int i=0;i<list.size();i++){
//                if (list.get(i).getStatus().equals("待审核")){
//                    lists.add(list.get(i));
//                }
//            }
//        }else {
//            for (int i=0;i<list.size();i++){
//                if (!list.get(i).getStatus().equals("待审核")){
//                    lists.add(list.get(i));
//                }
//            }
//        }
//
//        return lists;
//    }


	private void showPopwindow(View parent) {
		if (mPopupWindow == null) {
			LayoutInflater mLayoutInflater = LayoutInflater.from(getActivity());
			contentView = mLayoutInflater.inflate(R.layout.group_list, null);
			listview = (ListView) contentView.findViewById(R.id.lv_group);
			// 加载数据
			group = new ArrayList<>();
			group.add("全部");
			group.add("退回");
			group.add("通过");
			group.add("待审核");
//            group.add("被退回");
			groupAdapter = new GroupAdapter(getActivity(),R.layout.group_item, group);
			listview.setAdapter(groupAdapter);

			mPopupWindow = new PopupWindow(contentView, getActivity().getWindowManager()
					.getDefaultDisplay().getWidth() / 3, getActivity().getWindowManager()
					.getDefaultDisplay().getHeight() / 3);
		}
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setFocusable(true);

		// 显示的位置为:屏幕的宽度的1/16
		int xPos = getActivity().getWindowManager().getDefaultDisplay().getWidth() / 16;

		mPopupWindow.showAsDropDown(parent, xPos, 0);

		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (mPopupWindow != null) {
					mPopupWindow.dismiss();
				}

//        Toast.makeText(MyBillActivity.this, "点击了" + position + "项", Toast.LENGTH_SHORT).show();
				switch (position) {
					case 0:
						screen = 1;
						data(status, str, 0);
						break;
					case 1:
						screen = 2;
						data(status, str, 0);
						break;
					case 2:
						screen = 3;
						data(status, str, 0);
						break;
					case 3:
						screen = 4;
						data(status, str, 0);
						break;
				}
			}
		});

	}


}
