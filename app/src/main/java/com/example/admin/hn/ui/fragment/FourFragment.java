package com.example.admin.hn.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
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
import com.example.admin.hn.ui.account.MessageCenterActivity;
import com.example.admin.hn.ui.adapter.AllChildTabAdapter;
import com.example.admin.hn.ui.fragment.seaShart.InventoryFragment;
import com.example.admin.hn.ui.fragment.seaShart.Order2Fragment;
import com.example.admin.hn.ui.fragment.seaShart.OrderListFragment;
import com.example.admin.hn.ui.fragment.seaShart.OrderUseFragment;
import com.example.admin.hn.ui.fragment.seaShart.PurchaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author duantao
 * @date on 2017/7/26 16:04
 * @describe 海图管理
 */
public class FourFragment extends BaseFragment {

	@Bind(R.id.tv_top_title)
	TextView textTitle;
	@Bind(R.id.text_tile_right)
	FrameLayout right;
	@Bind(R.id.iv_two)
	ImageView iv_two;
	@Bind(R.id.viewPager)
	ViewPager viewPager;
	@Bind(R.id.tabLayout)
	TabLayout tabLayout;
	private View view;

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
	}

	private void addChildFragment() {

		AllChildTabAdapter adapter = new AllChildTabAdapter(getChildFragmentManager(), activity, viewPager);
		adapter.addTab("订单管理", OrderListFragment.class);
		adapter.addTab("采购管理", PurchaseFragment.class);
		adapter.addTab("订单领用", OrderUseFragment.class);
		adapter.addTab("库存管理", InventoryFragment.class);
		viewPager.setOffscreenPageLimit(3);
		tabLayout.setupWithViewPager(viewPager);
		viewPager.setCurrentItem(0);
	}

	@Override
	public void initTitleBar() {
		textTitle.setText("海图");
	}


	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (!hidden) {
			if (MainActivity.number == 0) {
				iv_two.setVisibility(View.GONE);
			} else {
				iv_two.setVisibility(View.VISIBLE);
			}
		}
	}

	@OnClick({R.id.text_tile_right})
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.text_tile_right:
				iv_two.setVisibility(View.GONE);
				Intent intent = new Intent(getActivity(), MessageCenterActivity.class);
				startActivity(intent);
				break;
		}
	}

}
