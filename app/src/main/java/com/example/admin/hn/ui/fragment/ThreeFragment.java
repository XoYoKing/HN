package com.example.admin.hn.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.admin.hn.R;
import com.example.admin.hn.base.BaseFragment;
import com.example.admin.hn.ui.adapter.AllChildTabAdapter;
import com.example.admin.hn.ui.fragment.read.ReadChartFragment;
import com.example.admin.hn.ui.fragment.read.ReadDrawFragment;
import com.example.admin.hn.ui.fragment.read.ReadMagazineFragment;

import butterknife.Bind;
import butterknife.ButterKnife;



/**
 * @author duantao
 * @date on 2017/7/26 16:04
 * @describe 文库
 */
public class ThreeFragment extends BaseFragment {

	@Bind(R.id.text_title)
	TextView textTitle;
	@Bind(R.id.viewPager)
	ViewPager viewPager;
	@Bind(R.id.tabLayout)
	TabLayout tabLayout;
	private View view;


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
		adapter.addTab("电子海图资料", ReadChartFragment.class);
		adapter.addTab("图纸管理", ReadDrawFragment.class);
		adapter.addTab("航海知识库", ReadMagazineFragment.class);
		viewPager.setOffscreenPageLimit(2);
		tabLayout.setupWithViewPager(viewPager);
	}

	@Override
	public void initTitleBar() {
		textTitle.setText("文库");
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
	}
}
