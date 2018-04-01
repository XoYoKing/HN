package com.example.admin.hn.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;


import com.example.admin.hn.R;

import com.example.admin.hn.base.BaseFragment;
import com.example.admin.hn.ui.adapter.AllChildTabAdapter;
import com.example.admin.hn.ui.fragment.shop.HnShopFragment;
import com.example.admin.hn.ui.fragment.shop.ShopFragment;


import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * @author duantao
 * @date on 2017/7/26 16:04
 * @describe 商城
 */
public class TowFragment extends BaseFragment {

	@Bind(R.id.text_title)
	TextView textTitle;
	private View view;
	@Bind(R.id.viewPager)
	ViewPager viewPager;
	@Bind(R.id.tabLayout)
	TabLayout tabLayout;
	@Bind(R.id.text_tile_del)
	TextView text_tile_del;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_tablaout, container, false);
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
		adapter.addTab("海宁海图", HnShopFragment.class);
		adapter.addTab("纸质海图", ShopFragment.class);
		tabLayout.setupWithViewPager(viewPager);
	}

	@Override
	public void initTitleBar() {
		textTitle.setText("商城");
		text_tile_del.setVisibility(View.VISIBLE);
		text_tile_del.setText("查询");
		text_tile_del.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
	}

}
