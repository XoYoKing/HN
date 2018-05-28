package com.example.admin.hn.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseFragment;
import com.example.admin.hn.http.Constant;
import com.example.admin.hn.model.LibraryTypeInfo;
import com.example.admin.hn.ui.account.PopActivity;
import com.example.admin.hn.ui.adapter.AllChildTabAdapter;
import com.example.admin.hn.ui.fragment.read.ReadChartFragment;
import com.example.admin.hn.ui.fragment.read.ReadDrawFragment;
import com.example.admin.hn.ui.fragment.read.ReadMagazineFragment;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolRefreshView;
import com.example.admin.hn.utils.ToolString;
import com.example.admin.hn.volley.RequestListener;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;

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
	@Bind(R.id.network_disabled)
	RelativeLayout network;
	@Bind(R.id.network_img)
	ImageView network_img;
	@Bind(R.id.noData_img)
	ImageView noData_img;
	private String url = Api.SHOP_BASE_URL + Api.GET_CATEGORY_LIST;
	private View view;
	private List<LibraryTypeInfo> list;
	private AllChildTabAdapter adapter;

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
		tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
		adapter = new AllChildTabAdapter(getChildFragmentManager(), activity, viewPager);
		sendHttp();
	}

	private void sendHttp() {
		http.get(url, params, progressTitle, new RequestListener() {
			@Override
			public void requestSuccess(String json) {
				Logger.e("文库分类", json);
				if (GsonUtils.isShopSuccess(json)) {
					TypeToken typeToken=new TypeToken<List<LibraryTypeInfo>>(){};
					list = (List<LibraryTypeInfo>) GsonUtils.jsonToList2(json, typeToken, "content");
					if (ToolString.isEmptyList(list)) {
						addChildFragment();
					}
				}else {
					ToolAlert.showToast(activity, GsonUtils.getError(json));
				}
				ToolRefreshView.hintView(adapter,false,network,noData_img,network_img);
			}

			@Override
			public void requestError(String message) {
				ToolAlert.showToast(activity, message);
				ToolRefreshView.hintView(adapter,true,network,noData_img,network_img);
			}
		});
	}


	@Override
	public void initData() {

	}

	private void addChildFragment() {
		//设置tabLayout 为滑动
		for (int i = 0; i < list.size(); i++) {
			LibraryTypeInfo typeInfo = list.get(i);
			adapter.addTab(typeInfo.categoryName + "", typeInfo, i + "", ReadChartFragment.class);
		}
		viewPager.setOffscreenPageLimit(list.size());
		tabLayout.setupWithViewPager(viewPager);
	}

	@Override
	public void initTitleBar() {
		textTitle.setText("航海知识库");
		text_tile_del.setVisibility(View.VISIBLE);
		text_tile_del.setText("搜索");

	}


	@OnClick({R.id.text_tile_del,R.id.network_img})
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.text_tile_del:
				int currentItem = viewPager.getCurrentItem();
				PopActivity.startActivity(activity, currentItem, R.layout.popup_char_layout, Constant.POP_LIB_TYPE);
				break;
			case R.id.network_img:
				sendHttp();
				network_img.setVisibility(View.GONE);
				break;
		}
	}

}
