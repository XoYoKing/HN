package com.example.admin.hn.ui.fragment.shop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.admin.hn.R;

import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseFragment;

import com.example.admin.hn.http.Constant;
import com.example.admin.hn.model.AddressInfo;
import com.example.admin.hn.model.BannerInfo;
import com.example.admin.hn.model.GoodsInfo;

import com.example.admin.hn.model.HomeItem;
import com.example.admin.hn.model.SpecGoodsPriceInfo;
import com.example.admin.hn.ui.account.HtmlActivity;
import com.example.admin.hn.ui.adapter.CommentAdapter;
import com.example.admin.hn.ui.adapter.GoodsSpecTypeAdapter;
import com.example.admin.hn.ui.fragment.shop.bean.CommentInfo;
import com.example.admin.hn.ui.fragment.shop.bean.ShopCartInfo;
import com.example.admin.hn.ui.shop.FirmOrderActivity;
import com.example.admin.hn.ui.shop.GoodsDetailActivity;
import com.example.admin.hn.ui.shop.SelectAddressActivity;
import com.example.admin.hn.utils.AbMathUtil;
import com.example.admin.hn.utils.GlideImageLoader;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolAppUtils;
import com.example.admin.hn.utils.ToolRefreshView;
import com.example.admin.hn.utils.ToolShopCartUtil;
import com.example.admin.hn.utils.ToolString;
import com.example.admin.hn.utils.ToolViewUtils;
import com.example.admin.hn.volley.RequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONObject;
import org.litepal.crud.DataSupport;

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
    @Bind(R.id.tv_brandName)
    TextView tv_brandName;
    @Bind(R.id.tv_address)
    TextView tv_address;
    @Bind(R.id.tv_weight)
    TextView tv_weight;
    @Bind(R.id.img)
    ImageView img;
    @Bind(R.id.tv_comment)
    TextView tv_comment;
    @Bind(R.id.bt_comment)
    Button bt_comment;
    @Bind(R.id.recycleView)
    RecyclerView recycleView;

    private View view;
    private PopupWindow window;
    private View popView;
    private RecyclerView type_lv;
    private ImageView space_close_img;
    private ImageView goods_spec_icon;
    private TextView tv_goods_price;
    private EditText et_number;
    private GoodsSpecTypeAdapter adapter;
    private List<BannerInfo> mActivityListBean = new ArrayList<>();
    private GoodsInfo goodsInfo;
    private int number;
    private ImageView add;
    private ImageView remove;
    private AddressInfo selectInfo;//当前选择的地址

    private String url_adr = Api.SHOP_BASE_URL + Api.GET_ADDRESS_LIST;
    private String url_com = Api.SHOP_BASE_URL + Api.GET_LIST_COMMENT;
    private Button space_add_shopping_cart;
    private Button space_confirm_bid;
    private ShopCartInfo shopCartInfo;
    private TextView tv_goods_no;
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver br;
    private ArrayList<CommentInfo> list = new ArrayList<>();
    private CommentAdapter com_adapter;

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
        recycleView.setLayoutManager(new LinearLayoutManager(activity));
        recycleView.setNestedScrollingEnabled(false);
        recycleView.setFocusable(false);
        com_adapter = new CommentAdapter(getActivity(), R.layout.item_comment_layout, list);
        recycleView.setAdapter(com_adapter);
    }

    @Override
    public void initData() {
        setValue(null);//设置商品信息
        if (goodsInfo != null) {
            getAddress();//获取地址信息
            getComment();//获取商品评论信息
        }
    }

    private void setValue(GoodsInfo goods) {
        if (goods == null) {
            try {
                Bundle bundle = getArguments();
                goodsInfo = (GoodsInfo) bundle.getSerializable("obj");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            goodsInfo = goods;
        }
        if (goodsInfo != null) {
            goods_name.setText(goodsInfo.goods.goodsFullName + "");
            goods_name_tip.setText(goodsInfo.spu.usp + "");
            tv_price.setText("￥" + AbMathUtil.roundStr(goodsInfo.goods.goodsPrice, 2) + "");
            tv_spec.setText(goodsInfo.goods.goodsSpec + "");
            tv_brandName.setText(goodsInfo.spu.brandName + "");
            tv_weight.setText(goodsInfo.spu.freightWeight + "kg");
            ToolViewUtils.glideImageList(goodsInfo.goods.imageUrl, img, R.drawable.load_fail);
//			List<BannerInfo> bannerInfos = new ArrayList<>();
//			BannerInfo bannerInfo = new BannerInfo();
//			bannerInfo.imgUrl = goodsInfo.goods.imageUrl;
//			bannerInfos.add(bannerInfo);
//			initBanner(bannerInfos);
            if (tv_goods_price != null) {
                tv_goods_price.setText("￥ " + goodsInfo.goods.goodsPrice + "");
            }
        }else {

        }
    }

    private void getComment() {
        Map map = new HashMap();
        map.put("spuId", goodsInfo.goods.spuId + "");
        map.put("page", "0");
        http.get(url_com, map, null, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.e("评论列表", json);
                if (GsonUtils.isShopSuccess(json)) {
                    TypeToken typeToken = new TypeToken<List<CommentInfo>>() {
                    };
                    List<CommentInfo> data = (List<CommentInfo>) GsonUtils.jsonToList2(json, typeToken, "content");
                    if (ToolString.isEmptyList(data)) {
                        bt_comment.setEnabled(true);
                        for (int i = 0; i < data.size(); i++) {
                            //取前两条数据展示
                            if (i < 2)
                                list.add(data.get(i));
                        }
                        tv_comment.setText("评论(" + data.size() + ")");
                        com_adapter.notifyDataSetChanged();
                    } else {
                        tv_comment.setText("评论(0)");
                        bt_comment.setText("暂无评论！");
                    }
                } else {
                    bt_comment.setText("评论加载失败！");
                }
            }

            @Override
            public void requestError(String message) {
                bt_comment.setText("网络请求失败！");
            }
        });
    }


    private void getAddress() {
        http.get(url_adr, params, null, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.e("收货地址列表", json);
                if (GsonUtils.isShopSuccess(json)) {
                    TypeToken typeToken = new TypeToken<List<AddressInfo>>() {
                    };
                    List<AddressInfo> data = (List<AddressInfo>) GsonUtils.jsonToList2(json, typeToken, "content");
                    if (ToolString.isEmptyList(data)) {
                        for (int i = 0; i < data.size(); i++) {
                            AddressInfo info = data.get(i);
                            if (info.isDefaul == 1) {
                                setAddress(false, info);
                                break;
                            }
                            if (i == data.size() - 1) {
                                //如果没有找到默认地址 取第一个地址
                                setAddress(false, data.get(0));
                            }
                        }
                    } else {
                        setAddress(false, null);
                    }
                } else {
                    setAddress(true, null);
                }
            }

            @Override
            public void requestError(String message) {
                setAddress(true, null);
            }
        });
    }


    //设置默认地址
    private void setAddress(boolean isNet, AddressInfo info) {
        if (info != null) {
            selectInfo = info;
            tv_address.setText(info.areaName + " " + info.receiverAddr);
            Intent intent = new Intent(Constant.ACTION_GOODS_DETAIL_ACTIVITY);
            intent.putExtra("status", 1);
            intent.putExtra("info", info);
            LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
        } else {
            if (isNet) {
                tv_address.setText("获取收货地址失败，快去设置吧!");
            } else {
                tv_address.setText("您还没有设置收货地址，快去设置吧!");
            }
        }
    }

    /**
     * 通过SpuId和规格的specItemsIds查询商品详情接口
     */
    private String url_detail = Api.SHOP_BASE_URL + Api.GET_GOODS_SPEC_DETAIL;

    private void getGoodsDetail(String specItemsIds) {
        Map map = new HashMap();
        map.put("specItemsIds", specItemsIds);
        map.put("spuId", goodsInfo.spu.id);
        http.get(url_detail, map, progressTitle, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.e("商品详情", json);
                if (GsonUtils.isShopSuccess(json)) {
                    goodsInfo = GsonUtils.jsonToBean2(json, GoodsInfo.class);
                    if (goodsInfo != null) {
                        setValue(goodsInfo);
                        Intent intent = new Intent(Constant.ACTION_GOODS_DETAIL_ACTIVITY);
                        intent.putExtra("status", 3);
                        intent.putExtra("goodsInfo", goodsInfo);
                        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
                    }
                } else {
                    ToolAlert.showToast(activity, GsonUtils.getError(json));
                }
            }

            @Override
            public void requestError(String message) {
                ToolAlert.showToast(activity, message);
            }
        });
    }


    @Override
    public void initTitleBar() {
//		initBroadcastReceiver();
    }

    @OnClick({R.id.ll_spec, R.id.tv_address, R.id.bt_comment, R.id.rl_comment})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_spec:
                if (goodsInfo != null) {
                    showPopWindow(v);
                }
                break;
            case R.id.rl_comment:
                //跳转到评论页面
                ((GoodsDetailActivity) activity).setCurrentPage(2);
                break;
            case R.id.bt_comment:
                //跳转到评论页面
                ((GoodsDetailActivity) activity).setCurrentPage(2);
                break;
            case R.id.tv_address:
                SelectAddressActivity.startActivity(activity, selectInfo);
                break;
        }
    }

    private ArrayList<ShopCartInfo> shopCartInfos = new ArrayList<>();

    private void confirmOrder() {
        if (shopCartInfos.size() > 0) {
            shopCartInfos.clear();
        }
        ShopCartInfo shopCartInfo = new ShopCartInfo();
        shopCartInfo.setBuyNumber(1);
        shopCartInfo.setGoodsId(goodsInfo.goods.id);
        shopCartInfo.setGoodsName(goodsInfo.spu.goodsName);
        shopCartInfo.setGoodsSpec(goodsInfo.goods.goodsSpec);
        shopCartInfo.setGoodsFullSpecs(goodsInfo.goods.goodsFullSpecs);
        shopCartInfo.setGoodsFullName(goodsInfo.goods.goodsFullName);
        shopCartInfo.setGoodsPrice(goodsInfo.goods.goodsPrice);
        shopCartInfo.setQty(goodsInfo.goods.qty);
        shopCartInfo.setImageUrl(goodsInfo.goods.imageUrl);
        shopCartInfo.setSpuId(goodsInfo.goods.spuId);
        shopCartInfo.setUsp(goodsInfo.spu.usp);
        shopCartInfo.setCurrGoodsSpecItemsIds(goodsInfo.currGoodsSpecItemsIds);
        shopCartInfos.add(shopCartInfo);
        FirmOrderActivity.startActivity(activity, shopCartInfos, selectInfo,false);
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
                ToolAppUtils.backgroundAlpha(activity, 1f);
            }
        });
        ToolAppUtils.backgroundAlpha(activity, 0.5f);
    }

    /**
     * 初始化规格参数弹窗界面
     */
    private void initPopView() {
        type_lv = (RecyclerView) popView.findViewById(R.id.type_lv);
        space_add_shopping_cart = (Button) popView.findViewById(R.id.space_add_shopping_cart);
        space_confirm_bid = (Button) popView.findViewById(R.id.space_confirm_bid);
        space_close_img = (ImageView) popView.findViewById(R.id.space_close_img);
        goods_spec_icon = (ImageView) popView.findViewById(R.id.goods_spec_icon);
        tv_goods_price = (TextView) popView.findViewById(R.id.tv_goods_price);
        add = (ImageView) popView.findViewById(R.id.add);
        remove = (ImageView) popView.findViewById(R.id.remove);
        tv_goods_no = (TextView) popView.findViewById(R.id.tv_goods_no);
        et_number = (EditText) popView.findViewById(R.id.et_number);
        tv_goods_price.setText("￥ " + goodsInfo.goods.goodsPrice + "");
        tv_goods_no.setText("商品编号：" + goodsInfo.goods.goodsSerial + "");
        number = 1;
        et_number.setText("1");//设置默认购买数量为1
        if (goodsInfo.goods.qty > 1) {//库存大于
            add.setSelected(true);
        }
        ToolViewUtils.glideImageList(goodsInfo.goods.imageUrl, goods_spec_icon, R.drawable.load_fail);
        space_close_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        Map<Integer, String> selectItem = setSelectItem();
        adapter = new GoodsSpecTypeAdapter(activity, R.layout.goods_spec_type_item, goodsInfo.spec, selectItem);
        type_lv.setLayoutManager(new LinearLayoutManager(activity));
        type_lv.setAdapter(adapter);
        adapter.setSelectSpecListener(new GoodsSpecTypeAdapter.OnSelectSpecListener() {
            @Override
            public void onSelectSpecListener(String spec) {
                //把选择的规格赋值给商品的规格
                getGoodsDetail(spec);
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
                        et_number.setText(number + "");
                        ToolViewUtils.setSelection(et_number);
                        add.setSelected(false);
                        if (number == 1) {
                            remove.setSelected(false);
                        } else {
                            remove.setSelected(true);
                        }
                    } else {
                        if (goodsInfo.goods.qty == 0) {
                            add.setSelected(false);
                            remove.setSelected(false);
                        } else {
                            if (number == goodsInfo.goods.qty) {
                                add.setSelected(false);
                            } else {
                                add.setSelected(true);
                            }
                            if (number == 1) {
                                remove.setSelected(false);
                            } else {
                                remove.setSelected(true);
                            }
                        }
                    }
                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number + 1 <= goodsInfo.goods.qty) {
                    et_number.setText((number + 1) + "");
                    ToolViewUtils.setSelection(et_number);
                }
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number - 1 >= 1) {
                    et_number.setText((number - 1) + "");
                    ToolViewUtils.setSelection(et_number);
                }
            }
        });
        //添加到购物车
        space_add_shopping_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolShopCartUtil.addShopCartInfo(activity, goodsInfo, Integer.parseInt(et_number.getText().toString()));
                dismiss();
            }
        });

        space_confirm_bid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmOrder();
            }
        });

    }


    //初始化 被选中的状态
    private Map<Integer, String> setSelectItem() {
        try {
            Map<Integer, String> select_id = new HashMap<>();
            for (int i = 0; i < goodsInfo.currGoodsSpecItemsIds.size(); i++) {
                String specItemId = goodsInfo.currGoodsSpecItemsIds.get(i);
                GoodsInfo.SpecInfo spec = goodsInfo.spec.get(i);
                List<GoodsInfo.SpecInfo.SpecItem> specItem = spec.specItem;
                for (int j = 0; j < specItem.size(); j++) {
                    //查找被选中的ID的下标
                    GoodsInfo.SpecInfo.SpecItem item = specItem.get(j);
                    if (specItemId.equals(item.specItemId)) {
                        //找到被选中的下标  设置为选中
                        item.isSelect = true;
                        select_id.put(i, item.specItemId);
                        break;
                    }
                }
            }
            return select_id;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置banner
     *
     * @param slideList
     */
    private void initBanner(List<BannerInfo> slideList) {
        if (slideList != null && slideList.size() > 0) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100 && data != null) {
            AddressInfo info = (AddressInfo) data.getSerializableExtra("info");
            setAddress(false, info);
        }
    }

    private void dismiss() {
        if (window != null) {
            window.dismiss();
            window = null;
            popView = null;
        }
    }


    private void initBroadcastReceiver() {
        localBroadcastManager = LocalBroadcastManager.getInstance(activity);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_GOODS_DETAIL_FRAGMENT);
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    int status = intent.getIntExtra("status", 0);
                    if (status == 1) {
                        dismiss();
                    }
                }
            }
        };
        localBroadcastManager.registerReceiver(br, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
