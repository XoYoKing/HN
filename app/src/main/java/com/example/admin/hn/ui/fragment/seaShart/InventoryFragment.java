package com.example.admin.hn.ui.fragment.seaShart;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.hn.R;
import com.example.admin.hn.base.BaseFragment;
import com.example.admin.hn.ui.adapter.AllChildTabAdapter;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * @author duantao
 * @date on 2017/7/26 16:04
 * @describe 库存管理
 */
public class InventoryFragment extends BaseFragment {
	private View view;
	private TabLayout tabLayout;
	private ViewPager viewPager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_inventory, container, false);
		ButterKnife.bind(this, view);
		initTitleBar();
		initView();
		initData();
		return view;
	}

	@Override
	public void initView() {
		tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
		viewPager = (ViewPager) view.findViewById(R.id.viewPager);
	}


	@Override
	public void initData() {
		addChildFragment();
	}

	private void addChildFragment() {
		AllChildTabAdapter adapter = new AllChildTabAdapter(getChildFragmentManager(), activity, viewPager);
		adapter.addTab("未过期", InventoryListFragment.class);
		adapter.addTab("已过期", InventoryListFragment.class);
		tabLayout.setupWithViewPager(viewPager);
	}

	@Override
	public void initTitleBar() {

	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
	}


}
