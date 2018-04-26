package com.example.admin.hn.ui.fragment.shop;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.admin.hn.R;
import com.example.admin.hn.base.BaseFragment;
import com.example.admin.hn.model.OrderInfo;
import com.example.admin.hn.ui.adapter.CommentAdapter;
import com.example.admin.hn.ui.adapter.HnGoodsAdapter;
import com.example.admin.hn.utils.ToolRefreshView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author duantao
 * @date on 2017/7/26 16:04
 * @describe 评论界面
 */
public class CommentFragment extends BaseFragment implements OnLoadmoreListener,OnRefreshListener{

	@Bind(R.id.recycleView)
	RecyclerView recycleView;
	@Bind(R.id.refreshLayout)
	RefreshLayout refreshLayout;
	@Bind(R.id.network_disabled)
	RelativeLayout network;
	@Bind(R.id.network_img)
	ImageView network_img;
	@Bind(R.id.noData_img)
	ImageView noData_img;
	private View view;
	private CommentAdapter adapter;
	private ArrayList<OrderInfo.Order> list = new ArrayList<>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_recycle_layout, container, false);
		ButterKnife.bind(this, view);
		initTitleBar();
		initView();
		initData();
		return view;
	}

	@Override
	public void initView() {
		ToolRefreshView.setRefreshLayout(activity, refreshLayout, this, this);
		recycleView.setLayoutManager(new LinearLayoutManager(activity));
		adapter = new CommentAdapter(getActivity(),R.layout.item_comment_layout, list);
		recycleView.setAdapter(adapter);
	}

	@Override
	public void initData() {
		for (int i = 0; i < 10; i++) {
			list.add(new OrderInfo.Order());
		}
		adapter.notifyDataSetChanged();
	}


	@Override
	public void initTitleBar() {

	}

	@Override
	public void onLoadmore(RefreshLayout refreshlayout) {

	}

	@Override
	public void onRefresh(RefreshLayout refreshlayout) {

	}
}
