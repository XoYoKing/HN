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
import com.orhanobut.logger.Logger;

import butterknife.ButterKnife;

/**
 * @author duantao
 * @date on 2017/7/26 16:04
 * @describe 订单库存管理
 */
public class OrderInventoryManagerFragment extends BaseFragment {
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

	}

	private void addChildFragment() {
		AllChildTabAdapter adapter = new AllChildTabAdapter(getChildFragmentManager(), activity, viewPager);
		adapter.addTab("未过期", "2",InventoryFragment.class);
		adapter.addTab("已过期","3", InventoryFragment.class);
		tabLayout.setupWithViewPager(viewPager);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		Logger.i("isVisibleToUser", isVisibleToUser + "");
		if (isVisibleToUser && isFirstHttp) {
			isFirstHttp = false;
			addChildFragment();
		}
	}

	@Override
	public void initTitleBar() {

	}
}
