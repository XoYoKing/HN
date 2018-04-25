package com.example.admin.hn.ui.fragment.shop;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseFragment;
import com.example.admin.hn.base.HNApplication;
import com.example.admin.hn.http.OkHttpUtil;
import com.example.admin.hn.model.OrderInfo;
import com.example.admin.hn.model.ShoppingCartInfo;
import com.example.admin.hn.model.SpecGoodsPriceInfo;
import com.example.admin.hn.model.SpecTypeInfo;
import com.example.admin.hn.ui.adapter.GoodsSpecTypeAdapter;
import com.example.admin.hn.ui.adapter.HnGoodsAdapter;
import com.example.admin.hn.ui.shop.FirmOrderActivity;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolAppUtils;
import com.example.admin.hn.utils.ToolRefreshView;
import com.example.admin.hn.widget.AlertDialog;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author duantao
 * @date on 2017/7/26 16:04
 * @describe 商品界面
 */
public class GoodsFragment extends BaseFragment {

	private View view;
	private PopupWindow window;
	private View popView;
	private ListView type_lv;
	private ImageView space_close_img;
	private ImageView goods_spec_icon;
	private TextView tv_goods_price;
	private TextView tv_goods_number;
	private TextView tv_goods_spec;
	private GoodsSpecTypeAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_goods_goods, container, false);
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

	}


	@Override
	public void initTitleBar() {
	}

	@OnClick({R.id.ll_spec})
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ll_spec:
				showPopWindow(v);
				break;
		}
	}

	/**
	 * 显示规格参数弹窗界面
	 */
	private void showPopWindow(View v) {
		// TODO Auto-generated method stub
		popView = LayoutInflater.from(activity).inflate(R.layout.select_spec_layout, null);

		initPopView();
		// 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
		window = new PopupWindow(popView,
				WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		window.setFocusable(true);

		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x00808080);
		window.setBackgroundDrawable(dw);

		// 设置popWindow的显示和消失动画
		window.setAnimationStyle(R.style.my_popshow_anim_style);
		//设置在底部显示
		window.showAtLocation(v, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		//popWindow消失监听方法
		window.setOnDismissListener(new PopupWindow.OnDismissListener() {

			@Override
			public void onDismiss() {
				ToolAppUtils.backgroundAlpha(activity,1f);
			}
		});
		ToolAppUtils.backgroundAlpha(activity,0.5f);
	}
	private List<SpecTypeInfo> spec_data = new ArrayList<>();
	/**
	 * 初始化规格参数弹窗界面
	 */
	private void initPopView() {
		type_lv = (ListView) popView.findViewById(R.id.type_lv);
		space_close_img = (ImageView) popView.findViewById(R.id.space_close_img);
		goods_spec_icon = (ImageView) popView.findViewById(R.id.goods_spec_icon);
		tv_goods_price = (TextView) popView.findViewById(R.id.tv_goods_price);
		tv_goods_number = (TextView) popView.findViewById(R.id.tv_goods_number);
		tv_goods_spec = (TextView) popView.findViewById(R.id.tv_goods_spec);

		space_close_img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		adapter = new GoodsSpecTypeAdapter(activity, R.layout.goods_spec_type_item, spec_data);
		type_lv.setAdapter(adapter);
		adapter.setSelectSpecListener(new GoodsSpecTypeAdapter.OnSelectSpecListener() {
			@Override
			public void onSelectSpecListener(SpecGoodsPriceInfo specGoodsPriceInfo) {

			}
		});

//		setCheckSpecPriceInfo(checkPriceInfo);
	}

}
