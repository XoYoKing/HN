package com.example.admin.hn.ui.fragment.shop;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.admin.hn.R;

import com.example.admin.hn.base.BaseFragment;

import com.example.admin.hn.model.BannerInfo;
import com.example.admin.hn.model.GoodsInfo;

import com.example.admin.hn.model.HomeItem;
import com.example.admin.hn.model.SpecGoodsPriceInfo;
import com.example.admin.hn.ui.account.HtmlActivity;
import com.example.admin.hn.ui.adapter.GoodsSpecTypeAdapter;
import com.example.admin.hn.utils.GlideImageLoader;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAppUtils;
import com.example.admin.hn.utils.ToolString;
import com.example.admin.hn.utils.ToolViewUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author duantao
 * @date on 2017/7/26 16:04
 * @describe 商品界面
 */
public class GoodsFragment extends BaseFragment {

	@Bind(R.id.banner)
	Banner bannerGuideContent;
	@Bind(R.id.goods_name)
	TextView goods_name;
	@Bind(R.id.goods_name_tip)
	TextView goods_name_tip;
	@Bind(R.id.tv_price)
	TextView tv_price;
	@Bind(R.id.tv_spec)
	TextView tv_spec;
	@Bind(R.id.img)
	ImageView img;

	private View view;
	private PopupWindow window;
	private View popView;
	private ListView type_lv;
	private ImageView space_close_img;
	private ImageView goods_spec_icon;
//	private TextView tv_goods_spec;
	private TextView tv_goods_price;
	private TextView tv_goods_inventory;
	private EditText et_number;
	private GoodsSpecTypeAdapter adapter;
	private List<BannerInfo> mActivityListBean = new ArrayList<>();
	private GoodsInfo goodsInfo;
	private int number;
	private String[] split;


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
		Bundle bundle = getArguments();
		try {
			goodsInfo = (GoodsInfo) bundle.getSerializable("obj");
			if (goodsInfo != null) {
                goods_name.setText(goodsInfo.goods.goodsFullSpecs + "");
                goods_name_tip.setText(goodsInfo.spu.usp + "");
				tv_price.setText("￥"+goodsInfo.goods.goodsPrice + "");
				tv_spec.setText(goodsInfo.goods.goodsSpec + "");
				ToolViewUtils.glideImageList(goodsInfo.goods.imageUrl, img, R.drawable.load_fail);
				String specItemsIds = goodsInfo.goods.specItemsIds.substring(0, goodsInfo.goods.specItemsIds.length());
				split = specItemsIds.split(",");
			}
//			List<BannerInfo> bannerInfos = new ArrayList<>();
//			BannerInfo bannerInfo = new BannerInfo();
//			bannerInfo.imgUrl = goodsInfo.goods.imageUrl;
//			bannerInfos.add(bannerInfo);
//			initBanner(bannerInfos);
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	/**
	 * 初始化规格参数弹窗界面
	 */
	private void initPopView() {
		type_lv = (ListView) popView.findViewById(R.id.type_lv);
		space_close_img = (ImageView) popView.findViewById(R.id.space_close_img);
		goods_spec_icon = (ImageView) popView.findViewById(R.id.goods_spec_icon);
		tv_goods_price = (TextView) popView.findViewById(R.id.tv_goods_price);
		View add = popView.findViewById(R.id.add);
		View remove = popView.findViewById(R.id.remove);
		tv_goods_inventory = (TextView) popView.findViewById(R.id.tv_goods_inventory);
		et_number = (EditText) popView.findViewById(R.id.et_number);
		tv_goods_price.setText("￥"+goodsInfo.goods.goodsPrice + "");
		tv_goods_inventory.setText("库存：" + goodsInfo.goods.qty + "");

		ToolViewUtils.glideImageList(goodsInfo.goods.imageUrl, goods_spec_icon, R.drawable.load_fail);
		space_close_img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				window.dismiss();
			}
		});
		setSelectItem();
		adapter = new GoodsSpecTypeAdapter(activity, R.layout.goods_spec_type_item, goodsInfo.spec);
		type_lv.setAdapter(adapter);
		adapter.setSelectSpecListener(new GoodsSpecTypeAdapter.OnSelectSpecListener() {
			@Override
			public void onSelectSpecListener(SpecGoodsPriceInfo specGoodsPriceInfo) {

			}
		});

		et_number.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				String s1 = s.toString();
				if (ToolString.isEmpty(s1)) {
					number = Integer.parseInt(s1);
					if (number > goodsInfo.goods.qty) {
						//购买数量大于库存
						number = goodsInfo.goods.qty;
						et_number.setText(number+"");
						ToolViewUtils.setSelection(et_number);
					}
				}
			}
		});

		add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (number +1 <= goodsInfo.goods.qty) {
					et_number.setText((number + 1) + "");
					ToolViewUtils.setSelection(et_number);
				}else {

				}
			}
		});

		remove.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (number - 1 >= 1) {
					et_number.setText((number - 1) + "");
					ToolViewUtils.setSelection(et_number);
				} else {

				}
			}
		});

//		setCheckSpecPriceInfo(checkPriceInfo);
	}

	//初始化 被选中的状态
	private void setSelectItem() {
		try {
			for (int i = 0; i < split.length; i++) {
                GoodsInfo.SpecInfo spec = goodsInfo.spec.get(i);
                List<GoodsInfo.SpecInfo.SpecItem> specItem = spec.specItem;
                String specItemId = split[i];//获取选中的ID
                for (int j = 0; j <specItem.size(); j++) {
                    //查找被选中的ID的下标
                    GoodsInfo.SpecInfo.SpecItem item = specItem.get(j);
                    if (specItemId.equals(item.id)) {
                        //找到被选中的下标  设置为选中
                        item.isSelect = true;
                        break;
                    }
                }
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置banner
	 *
	 * @param slideList
	 */
	private void initBanner(List<BannerInfo> slideList) {
		if (slideList!=null && slideList.size()>0) {
			if (mActivityListBean.size() > 0) {
				mActivityListBean.clear();
			}
			mActivityListBean.addAll(slideList);
			bannerGuideContent.setVisibility(View.VISIBLE);
			//设置banner样式
			bannerGuideContent.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);//设置不显示指示器   BannerConfig.CIRCLE_INDICATOR//圆点指示器
			//设置图片加载器
			bannerGuideContent.setImageLoader(new GlideImageLoader(R.drawable.load_fail));
			//设置图片集合
			bannerGuideContent.setImages(mActivityListBean);
			//设置banner动画效果
			bannerGuideContent.setBannerAnimation(Transformer.DepthPage);
			//设置自动轮播，默认为true
			bannerGuideContent.isAutoPlay(true);
			//设置轮播时间
			bannerGuideContent.setDelayTime(5000);
			//设置指示器位置（当banner模式中有指示器时）
			bannerGuideContent.setIndicatorGravity(BannerConfig.CENTER);//修改小圆点位置
			bannerGuideContent.setOnBannerListener(new OnBannerListener() {
				@Override
				public void OnBannerClick(int position) {
					BannerInfo bannerBean = mActivityListBean.get(position);
					if (ToolString.isNoBlankAndNoNull(bannerBean.link)) {
						HtmlActivity.startActivity(activity, bannerBean.link);
					}
				}
			});
			//banner设置方法全部调用完毕时最后调用
			bannerGuideContent.start();
		}
	}
}
