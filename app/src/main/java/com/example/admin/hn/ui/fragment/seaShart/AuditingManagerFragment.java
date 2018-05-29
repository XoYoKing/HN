package com.example.admin.hn.ui.fragment.seaShart;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.hn.R;
import com.example.admin.hn.base.BaseFragment;
import com.example.admin.hn.http.Constant;
import com.example.admin.hn.ui.adapter.AllChildTabAdapter;

import butterknife.ButterKnife;


/**
 * @author duantao
 * @date on 2017/7/26 16:04
 * @describe 资料审核管理
 */
public class AuditingManagerFragment extends BaseFragment implements ViewPager.OnPageChangeListener{
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

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (getUserVisibleHint() && isFirstHttp && http != null) {
			isFirstHttp = false;
			addChildFragment();
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser && isFirstHttp&& http != null) {
			isFirstHttp = false;
			addChildFragment();
		}
	}


	private void addChildFragment() {
		AllChildTabAdapter adapter = new AllChildTabAdapter(getChildFragmentManager(), activity, viewPager);
		adapter.addTab("申请单","0", BankApplyingFragment.class);
		adapter.addTab("领用单","1", ApplyedFragment.class);
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
		Intent intent = new Intent(Constant.ACTION_FOUR_FRAGMENT2);
		intent.putExtra("position", position);
		LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}
}
