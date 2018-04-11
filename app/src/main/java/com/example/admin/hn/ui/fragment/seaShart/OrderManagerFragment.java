package com.example.admin.hn.ui.fragment.seaShart;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.hn.R;
import com.example.admin.hn.base.BaseFragment;
import com.example.admin.hn.ui.adapter.AllChildTabAdapter;

import butterknife.ButterKnife;


/**
 * @author duantao
 * @date on 2017/7/26 16:04
 * @describe 订单管理
 */
public class OrderManagerFragment extends BaseFragment {
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
		adapter.addTab("全部","1", OrderManagerStatusFragment.class);
		adapter.addTab("退回","2", OrderManagerStatusFragment.class);
		adapter.addTab("通过","3", OrderManagerStatusFragment.class);
		adapter.addTab("待审核","4", OrderManagerStatusFragment.class);
		viewPager.setOffscreenPageLimit(4);
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
