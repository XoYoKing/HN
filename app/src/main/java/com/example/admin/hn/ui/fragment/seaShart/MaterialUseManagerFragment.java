package com.example.admin.hn.ui.fragment.seaShart;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.hn.R;
import com.example.admin.hn.base.BaseFragment;
import com.example.admin.hn.ui.adapter.AllChildTabAdapter;
import com.example.admin.hn.ui.fragment.FourFragment2;
import com.example.admin.hn.utils.ToolAlert;

import butterknife.ButterKnife;


/**
 * @author duantao
 * @date on 2017/7/26 16:04
 * @describe 资料领用管理
 */
public class MaterialUseManagerFragment extends BaseFragment implements ViewPager.OnPageChangeListener{
	private View view;
	private TabLayout tabLayout;
	private ViewPager viewPager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_tab_layout, container, false);
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
		adapter.addTab("待选","1", MaterialNotManagerFragment.class);
		adapter.addTab("已选","2", MaterialManagerFragment.class);
		adapter.addTab("申请单","3", ShipApplyingFragment.class);
		adapter.addTab("领用单","4", ApplyedFragment.class);
		tabLayout.setupWithViewPager(viewPager);
		viewPager.addOnPageChangeListener(this);
	}

	@Override
	public void initTitleBar() {

	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
		Intent intent = new Intent("FourFragment2");
		intent.putExtra("position", position);
		LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}


}
