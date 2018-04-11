package com.example.admin.hn.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.example.admin.hn.R;
import com.example.admin.hn.base.BaseFragment;
import com.example.admin.hn.model.ScreenTypeInfo;
import com.example.admin.hn.ui.account.PopActivity;
import com.example.admin.hn.ui.adapter.AllChildTabAdapter;
import com.example.admin.hn.ui.adapter.ScreenTypeAdapter;
import com.example.admin.hn.ui.fragment.read.ReadChartFragment;
import com.example.admin.hn.ui.fragment.read.ReadDrawFragment;
import com.example.admin.hn.ui.fragment.read.ReadMagazineFragment;
import com.example.admin.hn.ui.shop.OrderManagerActivity;
import com.example.admin.hn.ui.shop.ShopCartActivity;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolAppUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author duantao
 * @date on 2017/7/26 16:04
 * @describe 文库
 */
public class ThreeFragment extends BaseFragment {

	@Bind(R.id.text_title)
	TextView textTitle;
	@Bind(R.id.text_tile_del)
	TextView text_tile_del;
	@Bind(R.id.viewPager)
	ViewPager viewPager;
	@Bind(R.id.tabLayout)
	TabLayout tabLayout;
	@Bind(R.id.linear_show)
	View linear_show;//

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
		adapter.addTab("航海通告", ReadChartFragment.class);
		adapter.addTab("杂志", ReadDrawFragment.class);
		adapter.addTab("出版物", ReadMagazineFragment.class);
		viewPager.setOffscreenPageLimit(2);
		tabLayout.setupWithViewPager(viewPager);
	}

	@Override
	public void initTitleBar() {
		textTitle.setText("航海知识库");
		text_tile_del.setVisibility(View.VISIBLE);
		text_tile_del.setText("搜索");

	}


	@OnClick({R.id.text_tile_del})
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.text_tile_del:
				int currentItem = viewPager.getCurrentItem();
				if (currentItem == 0) {
					PopActivity.startActivity(activity, R.layout.popup_char_layout, 100);
				} else if (currentItem == 1) {
					PopActivity.startActivity(activity,R.layout.popup_draw_layout, 200);
				} else if (currentItem == 2) {
					PopActivity.startActivity(activity,R.layout.popup_magazine_layout, 300);
				}
				break;
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
	}
}
