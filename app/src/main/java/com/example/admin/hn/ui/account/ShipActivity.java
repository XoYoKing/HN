package com.example.admin.hn.ui.account;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.hn.MainActivity;
import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.base.HNApplication;
import com.example.admin.hn.http.OkHttpUtil;
import com.example.admin.hn.model.ShipInfo;
import com.example.admin.hn.ui.adapter.ShipAdapter;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.widget.TitleButton;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;
import com.zhy.adapter.abslistview.ViewHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 船舶选择
 */
public class ShipActivity extends BaseActivity {

	private static String TAG = "ShipActivity";

	@Bind(R.id.text_title_back)
	TextView mTextTitleBack;
	@Bind(R.id.text_title)
	TextView mTextTitle;
	@Bind(R.id.listveiw)
	ListView listveiw;
	@Bind(R.id.btn)
	TitleButton mTBtn;
	@Bind(R.id.text_tile_right)
	TextView right;
	@Bind(R.id.text_tile_del)
	TextView text_tile_del;

	private boolean isdel=false;
	private ShipAdapter adapter;
	private List<ShipInfo.ship> listship = new ArrayList<>();
	private List<HashMap<String, Object>> list = new ArrayList<>();
	private List<ShipInfo.ship> listStr = new ArrayList<>();
	private List<String> lists = new ArrayList<>();
	private List<String> listss = new ArrayList<>();
	private String url_ship = Api.BASE_URL + Api.SHIPINQUIRY;
	private String url_shipselection = Api.BASE_URL + Api.SHIPSELECTION;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ship);
		ButterKnife.bind(this);
		initTitleBar();
		initData();
		data();
	}


	@Override
	public void initTitleBar() {
		super.initTitleBar();
		mTextTitleBack.setBackgroundResource(R.drawable.btn_back);
		mTextTitle.setText(R.string.title_my_ship);
		right.setText("提交");
		text_tile_del.setVisibility(View.VISIBLE);
		text_tile_del.setText("全选");
	}

	@Override
	public void initData() {
		super.initData();
		mTBtn.setOnLeftClickListener(new TitleButton.OnLeftClickListener() {
			@Override
			public void onLeftClickListener() {
				list.clear();
				data();
				right.setVisibility(View.VISIBLE);
				text_tile_del.setVisibility(View.VISIBLE);
			}
		});
		mTBtn.setOnRightClickListener(new TitleButton.OnRightClickListener() {

			@Override
			public void onRightClickListener() {
				list.clear();
				for (int i = 0; i < MainActivity.list.size(); i++) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("name", MainActivity.list.get(i).getShipname());
					map.put("boolean", true);//初始化为未选
					map.put("number", MainActivity.list.get(i).getShipnumber());
					list.add(map);
				}//初始化数据
				right.setVisibility(View.GONE);
				text_tile_del.setVisibility(View.GONE);
				adapter.notifyDataSetChanged();
			}
		});

		listveiw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ViewHolder viewCache = (ViewHolder) view.getTag();
				CheckBox checkBox = viewCache.getView(R.id.cb_status);
				checkBox.toggle();
				list.get(position).put("boolean", checkBox.isChecked());
				adapter.notifyDataSetChanged();
				if (checkBox.isChecked()) {//被选中状态
					lists.add(list.get(position).get("name").toString());
					listss.add(list.get(position).get("number").toString());
				} else//从选中状态转化为未选中
				{
					listss.remove(list.get(position).get("number").toString());
					lists.remove(list.get(position).get("name").toString());
				}
			}
		});
		adapter = new ShipAdapter(ShipActivity.this, R.layout.ship_adapter, list);
		listveiw.setAdapter(adapter);
	}

	@OnClick({R.id.text_title_back, R.id.text_tile_right, R.id.text_tile_del})
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.text_title_back:
				finish();
				break;
			case R.id.text_tile_right:
				for (int i = 0; i < listss.size(); i++) {
					listStr.add(new ShipInfo.ship(listss.get(i), lists.get(i)));
				}
				if (listStr.size() == 0) {
					ToolAlert.showToast(ShipActivity.this, "请选择船舶后再提交", false);
				} else {
					ShipInfo shipInfo = new ShipInfo(HNApplication.mApp.getUserId(), listStr);
					String jsonObject = GsonUtils.beanToJson(shipInfo);
					shipSelection(jsonObject);
				}
				break;
			case R.id.text_tile_del:
				if (isdel==false) {
					text_tile_del.setText("取消");
					for (int i = 0; i < list.size(); i++) {
						list.get(i).put("boolean", true);
						adapter.notifyDataSetChanged();
						lists.add(list.get(i).get("name").toString());
						listss.add(list.get(i).get("number").toString());
					}//初始化数据
					isdel=true;
				}else {
					text_tile_del.setText("全选");
					for (int i = 0; i < list.size(); i++) {
						list.get(i).put("boolean", false);
						adapter.notifyDataSetChanged();
						listss.remove(list.get(i).get("number").toString());
						lists.remove(list.get(i).get("name").toString());
					}//初始化数据
					isdel=false;
				}

				break;
		}
	}

	private void data() {
		Map map = new HashMap();
		map.put("Userid", HNApplication.mApp.getUserId());
		String jsonStr = GsonUtils.mapToJson(map);
		Logger.i(TAG, jsonStr);
		try {
			OkHttpUtil.postJsonData2Server(ShipActivity.this,
					url_ship,
					jsonStr,
					progressTitle,
					new OkHttpUtil.MyCallBack() {
						@Override
						public void onFailure(Request request, IOException e) {
							ToolAlert.showToast(ShipActivity.this, "服务器异常,请稍后再试", false);
						}

						@Override
						public void onResponse(String json) {
							Logger.i(TAG, json);
							ShipInfo shipInfo = GsonUtils.jsonToBean(
									json, ShipInfo.class
							);
							if (shipInfo.getStatus().equals("error")) {
								ToolAlert.showToast(ShipActivity.this, shipInfo.getMessage(), false);
							} else {
								for (int i = 0; i < shipInfo.getDocuments().size(); i++) {
									HashMap<String, Object> map = new HashMap<String, Object>();
									map.put("name", shipInfo.getDocuments().get(i).getShipname());
									map.put("boolean", false);//初始化为未选
									map.put("number", shipInfo.getDocuments().get(i).getShipnumber());
									list.add(map);
								}//初始化数据
								adapter.notifyDataSetChanged();
							}
						}
					}

			);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void shipSelection(String str) {
		Logger.i(TAG, str);
		try {
			OkHttpUtil.postJsonData2Server(ShipActivity.this, url_shipselection, str,"正在提交...",
					new OkHttpUtil.MyCallBack() {
						@Override
						public void onFailure(Request request, IOException e) {
							ToolAlert.showToast(ShipActivity.this, "服务器异常,请稍后再试", false);
						}

						@Override
						public void onResponse(String json) {
							Logger.i(TAG, json);
							ShipInfo shipInfo = GsonUtils.jsonToBean(
									json, ShipInfo.class
							);
							if (shipInfo.getStatus().equals("error")) {
								ToolAlert.showToast(ShipActivity.this, shipInfo.getMessage(), false);
							} else {
								ToolAlert.showToast(ShipActivity.this, "提交成功", false);
								MainActivity.list = new ArrayList<>(listStr);
								finish();
							}
						}
					}
			);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
