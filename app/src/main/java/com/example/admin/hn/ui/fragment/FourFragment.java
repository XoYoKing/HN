package com.example.admin.hn.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.hn.MainActivity;
import com.example.admin.hn.R;
import com.example.admin.hn.base.BaseFragment;
import com.example.admin.hn.base.HNApplication;
import com.example.admin.hn.http.Constant;
import com.example.admin.hn.ui.account.MessageCenterActivity;
import com.example.admin.hn.ui.account.PopActivity;
import com.example.admin.hn.ui.adapter.AllChildTabAdapter;
import com.example.admin.hn.ui.fragment.seaShart.OrderInventoryManagerFragment;
import com.example.admin.hn.ui.fragment.seaShart.OrderManagerFragment;
import com.example.admin.hn.ui.fragment.seaShart.OrderPurchaseManagerFragment;
import com.example.admin.hn.ui.fragment.seaShart.OrderUseManagerFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author duantao
 * @date on 2017/7/26 16:04
 * @describe 电子海图
 */
public class FourFragment extends BaseFragment {

	@Bind(R.id.tv_top_title)
	TextView textTitle;
	@Bind(R.id.tv_search)
	TextView tv_search;

	@Bind(R.id.text_tile_right)
	FrameLayout right;
	@Bind(R.id.iv_two)
	ImageView iv_two;
	@Bind(R.id.viewPager)
	ViewPager viewPager;
	@Bind(R.id.tabLayout)
	TabLayout tabLayout;

	private View view;
	private LocalBroadcastManager localBroadcastManager;
	private BroadcastReceiver br;
	private int childCurrentItem;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_four_tablaout, container, false);
		ButterKnife.bind(this, view);
		initTitleBar();
		initView();
		initData();
		return view;
	}

	@Override
	public void initView() {
	}


	@Override
	public void initData() {
		addChildFragment();
		initBroadcastReceiver();
	}

	private void addChildFragment() {

		AllChildTabAdapter adapter = new AllChildTabAdapter(getChildFragmentManager(), activity, viewPager);
		adapter.addTab("订单管理", OrderManagerFragment.class);
//		adapter.addTab("采购管理", OrderPurchaseManagerFragment.class);
//		adapter.addTab("订单领用", OrderUseManagerFragment.class);
		adapter.addTab("库存管理", OrderInventoryManagerFragment.class);
		tabLayout.setupWithViewPager(viewPager);
		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				if (position == 0) {
					tv_search.setVisibility(View.VISIBLE);
				}else {
					tv_search.setVisibility(View.INVISIBLE);
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
	}

	@Override
	public void initTitleBar() {
		textTitle.setText("电子海图");
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
		}
	}

	@OnClick({R.id.text_tile_right, R.id.tv_search})
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.text_tile_right:
				iv_two.setVisibility(View.GONE);
				Intent intent = new Intent(getActivity(), MessageCenterActivity.class);
				startActivity(intent);
				break;
			case R.id.tv_search:
				int currentItem = viewPager.getCurrentItem();
				if (currentItem == 0) {
					PopActivity.startActivity(activity,childCurrentItem,R.layout.popup_order_manager_layout, Constant.POP_ORDER_MANAGER);
				} else if (currentItem == 1) {
					PopActivity.startActivity(activity,R.layout.popup_order_inventory_layout, Constant.POP_ORDER_INVENTORY);
				}
				break;
		}
	}

	private void initBroadcastReceiver(){
		localBroadcastManager = LocalBroadcastManager.getInstance(activity);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Constant.ACTION_FOUR_FRAGMENT);
		br = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent != null) {
					childCurrentItem = intent.getIntExtra("position", 0);
				}
			}
		};
		localBroadcastManager.registerReceiver(br, intentFilter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		localBroadcastManager.unregisterReceiver(br);
	}
}
