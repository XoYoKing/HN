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
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseFragment;
import com.example.admin.hn.model.OrderInfo;
import com.example.admin.hn.ui.adapter.CommentAdapter;
import com.example.admin.hn.ui.adapter.HnGoodsAdapter;
import com.example.admin.hn.ui.fragment.shop.bean.CommentInfo;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolRefreshView;
import com.example.admin.hn.utils.ToolString;
import com.example.admin.hn.volley.RequestListener;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

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
	private ArrayList<CommentInfo> list = new ArrayList<>();
	private String url = Api.SHOP_BASE_URL + Api.GET_LIST_COMMENT;
	private String spuId;
	private int page;
	private int totalPage;

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
	public void initTitleBar() {

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
		Bundle bundle = getArguments();
		spuId = bundle.getString("type");
		sendHttp();
	}

	private void sendHttp() {
		params.put("spuId", spuId);
		params.put("page", page+"");
		http.get(url, params, progressTitle, new RequestListener() {
			@Override
			public void requestSuccess(String json) {
				Logger.e("评论列表", json);
				if (GsonUtils.isShopSuccess(json)) {
					TypeToken typeToken=new TypeToken<List<CommentInfo>>(){};
					List<CommentInfo> data = (List<CommentInfo>) GsonUtils.jsonToList2(json, typeToken, "content");
					totalPage = GsonUtils.getTotalPage(json);
					if (ToolString.isEmptyList(data)) {
						if (isRefresh) {
							list.clear();
						}
						list.addAll(data);
					}
				}else {
					ToolAlert.showToast(activity,GsonUtils.getError(json));
				}
				ToolRefreshView.hintView(adapter,refreshLayout, false,  network, noData_img, network_img);
			}

			@Override
			public void requestError(String message) {
				ToolAlert.showToast(activity,message);
				ToolRefreshView.hintView(adapter,refreshLayout, true,  network, noData_img, network_img);
			}
		});
	}

	@Override
	public void onLoadmore(RefreshLayout refreshlayout) {
		page = page + 1;
		isRefresh = false;
		if (ToolRefreshView.isLoadMore(refreshlayout,page, totalPage)) {
			sendHttp();
		}
	}

	@Override
	public void onRefresh(RefreshLayout refreshlayout) {
		page = 0;
		isRefresh = true;
		sendHttp();
	}
}
