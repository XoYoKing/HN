package com.example.admin.hn.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.hn.MainActivity;
import com.example.admin.hn.R;
import com.example.admin.hn.base.BaseFragment;
import com.example.admin.hn.base.HNApplication;
import com.example.admin.hn.http.Constant;
import com.example.admin.hn.ui.account.PopActivity;
import com.example.admin.hn.ui.adapter.AllChildTabAdapter;
import com.example.admin.hn.ui.fragment.seaShart.AuditingManagerFragment;
import com.example.admin.hn.ui.fragment.seaShart.MaterialUseManagerFragment;
import com.example.admin.hn.ui.fragment.seaShart.ReceiptFragment;
import com.example.admin.hn.ui.shop.OrderManagerActivity;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.widget.AlertDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author duantao
 * @date on 2017/7/26 16:04
 * @describe 纸质海图
 */
public class FourFragment2 extends BaseFragment implements ViewPager.OnPageChangeListener{

	@Bind(R.id.text_title)
	TextView textTitle;
	@Bind(R.id.viewPager)
	ViewPager viewPager;
	@Bind(R.id.tabLayout)
	TabLayout tabLayout;
	@Bind(R.id.text_tile_right)
	TextView right;
	@Bind(R.id.text_tile_del)
	TextView text_tile_del;

	private View view;
	private LocalBroadcastManager localBroadcastManager;
	private BroadcastReceiver br;
	private int childCurrentItem;//子 fragment 所在页面
	private int currentItem;//当前 fragment 所在页面
	private MaterialUseManagerFragment useManagerFragment;
	private AuditingManagerFragment auditingFragment;

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
		initBroadcastReceiver();
	}

	private void addChildFragment() {
		AllChildTabAdapter adapter = new AllChildTabAdapter(getChildFragmentManager(), activity, viewPager);
		if (true) {
			adapter.addTab("订单领用", MaterialUseManagerFragment.class);
			adapter.addTab("审核管理", AuditingManagerFragment.class);
		}else {
			adapter.addTab("审核管理", AuditingManagerFragment.class);
		}
		adapter.addTab("回执", ReceiptFragment.class);
		viewPager.setOffscreenPageLimit(3);
		tabLayout.setupWithViewPager(viewPager);
		viewPager.setCurrentItem(0);
		viewPager.addOnPageChangeListener(this);

		Fragment item = adapter.getItem(0);
		if (item instanceof MaterialUseManagerFragment) {
			useManagerFragment = (MaterialUseManagerFragment) item;
		} else if (item instanceof AuditingManagerFragment) {
			auditingFragment = (AuditingManagerFragment) item;
		}
	}

	@Override
	public void initTitleBar() {
		textTitle.setText("船舶资料管理");
		text_tile_del.setVisibility(View.VISIBLE);
		text_tile_del.setText("搜索");
		right.setText("确认");
	}


	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
	}

	@OnClick({R.id.text_tile_del,R.id.text_tile_right})
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.text_tile_del:
				if (currentItem == 0) {
					PopActivity.startActivity(activity, childCurrentItem,R.layout.popup_not_material__layout, Constant.POP_NOT_MATERIAL);
				} else if (currentItem == 1) {
					PopActivity.startActivity(activity,childCurrentItem,R.layout.popup_order_manager_layout, Constant.POP_SHIP_AUDITING);
				}
				break;
			case R.id.text_tile_right:
				if (childCurrentItem == 0 ||childCurrentItem == 1) {
					final AlertDialog dialog = new AlertDialog(activity);
					dialog.showDialog("资料提交", "是否确定提交资料到此船舶"
							+ "\n船舶名称："+MainActivity.list.get(0).shipname
							+ "\n船舶编号："+MainActivity.list.get(0).shipid, new AlertDialog.DialogOnClickListener() {
						@Override
						public void onPositiveClick() {
							Intent intent = new Intent();
							intent.putExtra("isNew", childCurrentItem);
							intent.setAction(Constant.ACTION_MATERIAL_NOT_MANAGER_FRAGMENT);
							LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
							dialog.dismiss();
						}

						@Override
						public void onNegativeClick() {
							dialog.dismiss();
						}
					},true);

				}else if (childCurrentItem == 2){
					final AlertDialog dialog = new AlertDialog(activity);
					dialog.showDialog("资料提交", "是否确定提交资料到此船舶"
							+ "\n船舶名称："+MainActivity.list.get(0).shipname
							+ "\n船舶编号："+MainActivity.list.get(0).shipid, new AlertDialog.DialogOnClickListener() {
						@Override
						public void onPositiveClick() {
							Intent intent = new Intent(Constant.ACTION_MATERIAL_MANAGER_FRAGMENT);
							LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
							dialog.dismiss();
						}

						@Override
						public void onNegativeClick() {
							dialog.dismiss();
						}
					},true);
				}
				break;
		}
	}

	private void initBroadcastReceiver(){
		localBroadcastManager = LocalBroadcastManager.getInstance(activity);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Constant.ACTION_FOUR_FRAGMENT2);
		br = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent != null) {
					childCurrentItem = intent.getIntExtra("position", 0);
					updateUI();
				}
			}
		};
		localBroadcastManager.registerReceiver(br, intentFilter);
	}

	private void updateUI() {
		switch (currentItem) {
			case 0://订单领用
				if (childCurrentItem == 0 || childCurrentItem==1) {
					text_tile_del.setVisibility(View.VISIBLE);
					text_tile_del.setText("搜索");
					right.setText("确认");
				} else if (childCurrentItem == 2) {
					text_tile_del.setVisibility(View.GONE);
					right.setText("提交");
				} else if (childCurrentItem == 3) {
					text_tile_del.setVisibility(View.GONE);
					right.setText("");
				} else if (childCurrentItem == 4) {
					text_tile_del.setVisibility(View.GONE);
					right.setText("");
				}else {
					text_tile_del.setVisibility(View.VISIBLE);
					text_tile_del.setText("搜索");
					right.setText("确认");
				}
			break;
			case 1://审核管理
				if (childCurrentItem == 0) {
					text_tile_del.setVisibility(View.VISIBLE);
					text_tile_del.setText("搜索");
					right.setText("");
				} else if (childCurrentItem == 1) {
					text_tile_del.setVisibility(View.VISIBLE);
					text_tile_del.setText("搜索");
					right.setText("");
				}else {
					text_tile_del.setVisibility(View.VISIBLE);
					text_tile_del.setText("搜索");
					right.setText("");
				}
				break;
			case 2://回执
				if (childCurrentItem == 0) {
					text_tile_del.setVisibility(View.GONE);
					right.setText("");
				}else {
					text_tile_del.setVisibility(View.GONE);
					right.setText("");
				}
				break;
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		localBroadcastManager.unregisterReceiver(br);
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
		currentItem = position;
		updateUI();
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}
}
