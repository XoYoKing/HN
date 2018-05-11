package com.example.admin.hn.ui.shop;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.model.GoodsListInfo;
import com.example.admin.hn.model.HomeItem;
import com.example.admin.hn.model.ScreenTypeInfo;
import com.example.admin.hn.ui.adapter.GoodsListAdapter;
import com.example.admin.hn.ui.adapter.ScreenTypeAdapter;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolAppUtils;
import com.example.admin.hn.utils.ToolRefreshView;
import com.example.admin.hn.utils.ToolString;
import com.example.admin.hn.volley.RequestListener;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by hjy on 2016/11/5.
 * 商品列表
 */
public class GoodsListActivity extends BaseActivity implements OnLoadmoreListener,OnRefreshListener{

    @Bind(R.id.goods_list_price_img)
    ImageView goods_list_price_img;
    @Bind(R.id.goods_list_most_img)
    ImageView goods_list_most_img;
    @Bind(R.id.goods_list_all_linear)
    LinearLayout goods_list_all_linear;
    @Bind(R.id.goods_list_screen_linear)
    LinearLayout goods_list_screen_linear;
    @Bind(R.id.goods_list_price_linear)
    LinearLayout goods_list_price_linear;
    @Bind(R.id.goods_list_most_linear)
    LinearLayout goods_list_most_linear;
    @Bind(R.id.goods_list_all_tv)
    TextView goods_list_all_tv;
    @Bind(R.id.goods_list_screen_tv)
    TextView goods_list_screen_tv;
    @Bind(R.id.goods_list_price_tv)
    TextView goods_list_price_tv;
    @Bind(R.id.goods_list_most_tv)
    TextView goods_list_most_tv;
    @Bind(R.id.network_disabled)
    RelativeLayout network;
    @Bind(R.id.network_img)
    ImageView network_img;
    @Bind(R.id.noData_img)
    ImageView noData_img;
    @Bind(R.id.recycleView)
    RecyclerView recycleView;
    @Bind(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @Bind(R.id.search_tv)
    TextView search_tv;
    @Bind(R.id.search_tv_content)
    TextView search_tv_content;

    private List<GoodsListInfo.Goods> list = new ArrayList<>();
    private GoodsListAdapter adapter;
    private int page;
    private int rows = 10;
    private HomeItem info;
    private String url = Api.SHOP_BASE_URL + Api.GET_GOODS_LIST;
    private String sort_price_asc = "goodsPrice,asc";//价格排序 升序asc
    private String sort_price_dsc = "goodsPrice,dsc";//价格排序 降序desc
    private String sort_count_asc = "orderCount,asc";//销量排序 升序asc
    private String sort_count_dsc = "orderCount,dsc";//销量排序 降序desc
    private String sort = "";//排序
    private int totalPages;//总页数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_list);
        ButterKnife.bind(this);
        initTitleBar();
        initView();
        initData();
    }

    /**
     *
     * @param context
     */
    public static void startActivity(Context context,HomeItem info) {
        Intent intent = new Intent(context, GoodsListActivity.class);
        intent.putExtra("info", info);
        context.startActivity(intent);
    }


    @Override
    public void initTitleBar() {
    }

    @Override
    public void initView() {
        ToolRefreshView.setRefreshLayout(context,refreshLayout,this,this);
        recycleView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new GoodsListAdapter(this, R.layout.item_goods_list, list);
        recycleView.setAdapter(adapter);
        info = (HomeItem) getIntent().getSerializableExtra("info");
    }


    @Override
    public void initData() {
        goods_list_all_tv.setSelected(true);//默认选中全部
        goods_list_all_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectDefault();
                goods_list_price_img.setSelected(false);
                goods_list_most_img.setSelected(false);
                goods_list_all_tv.setSelected(true);
                sort = "";
                sendHttp();
            }
        });

        goods_list_price_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectDefault();
                goods_list_most_img.setSelected(false);
                goods_list_price_tv.setSelected(true);
                setSelect(goods_list_price_img);
                if (goods_list_price_img.isSelected()) {
                    sort = sort_price_asc;
                }else {
                    sort = sort_price_dsc;
                }
                sendHttp();
            }
        });
        goods_list_most_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectDefault();
                goods_list_price_img.setSelected(false);
                goods_list_most_tv.setSelected(true);
                setSelect(goods_list_most_img);
                if (goods_list_most_img.isSelected()) {
                    sort = sort_count_asc;
                }else {
                    sort = sort_count_dsc;
                }
                sendHttp();
            }
        });
        goods_list_screen_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showPopWindow(v);
            }
        });
        sendHttp();
    }
    @OnClick({R.id.iv_back,R.id.noData_img,R.id.network_img,R.id.search_linear})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.search_linear:
                SearchActivity.startActivity(this);
                break;
            case R.id.noData_img:
                sendHttp();
                noData_img.setVisibility(View.GONE);
                break;
            case R.id.network_img:
                sendHttp();
                network_img.setVisibility(View.GONE);
                break;
        }
    }



    private void sendHttp() {
        params.put("page", page+"");
        params.put("rows", rows+"");
        params.put("sort", sort);
        params.put("columnName", info.name + "");
        http.get(url, params, progressTitle, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.e("商品列表", json);
                if (GsonUtils.isShopSuccess(json)) {
                    GoodsListInfo goodsListInfo = GsonUtils.jsonToBean2(json, GoodsListInfo.class);
                    if (goodsListInfo != null) {
                        totalPages = goodsListInfo.totalPages;
                        if (isRefresh) {
                            list.clear();
                        }
                        if (ToolString.isEmptyList(goodsListInfo.content)) {
                            list.addAll(goodsListInfo.content);
                        }
                    }
                }
                ToolRefreshView.hintView(adapter, refreshLayout, false, network, noData_img, network_img);
            }

            @Override
            public void requestError(String message) {
                ToolAlert.showToast(context,message);
                ToolRefreshView.hintView(adapter, refreshLayout, true, network, noData_img, network_img);
            }
        });
    }

    private void setSelect(ImageView imageView) {
        if (imageView.isSelected()) {
            imageView.setSelected(false);
            imageView.setImageResource(R.drawable.jiage_down);
        } else {
            imageView.setSelected(true);
            imageView.setImageResource(R.drawable.jiage_up);
        }
    }
    private void setSelectDefault() {
        goods_list_all_tv.setSelected(false);
        goods_list_screen_tv.setSelected(false);
        goods_list_price_tv.setSelected(false);
        goods_list_most_tv.setSelected(false);
        goods_list_price_img.setImageResource(R.drawable.jiage_normal);
        goods_list_most_img.setImageResource(R.drawable.jiage_normal);
    }


    private void showPopWindow(View v) {
        // TODO Auto-generated method stub
        View  view = LayoutInflater.from(this).inflate(R.layout.screen, null);
        initPopView(view);
        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        PopupWindow   window = new PopupWindow(view,
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00808080);
        window.setBackgroundDrawable(dw);

        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.my_popshow_right_anim_style);
        //设置在右侧显示
        window.showAtLocation(v, Gravity.RIGHT, 0, 100);
        //popWindow消失监听方法
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                ToolAppUtils.backgroundAlpha(GoodsListActivity.this, 1f);
            }
        });
        ToolAppUtils.backgroundAlpha(GoodsListActivity.this, 0.5f);
    }

    private void initPopView(View view) {
        ExpandableListView     screen_type_lv = (ExpandableListView) view.findViewById(R.id.screen_type_lv);
        List<ScreenTypeInfo> data = new ArrayList();

        List<String> str1 = new ArrayList();
        str1.add("蓝色");
        str1.add("黄色");
        str1.add("紫色");
        str1.add("黑色");
        List<String> str2 = new ArrayList();
        str2.add("第一");
        str2.add("第二");
        str2.add("第三");
        str2.add("第四");
        List<String> str3 = new ArrayList();
        str3.add("法国");
        str3.add("加拿大");
        str3.add("中国");
        str3.add("德国");
        List<String> str4 = new ArrayList();
        str4.add("80x100");
        str4.add("100x120");
        str4.add("120x140");
        str4.add("150x180");

        ScreenTypeInfo info1 = new ScreenTypeInfo("颜色", str1);
        ScreenTypeInfo info2 = new ScreenTypeInfo("类型", str2);
        ScreenTypeInfo info3 = new ScreenTypeInfo("国家", str3);
        ScreenTypeInfo info4 = new ScreenTypeInfo("尺寸", str4);
        data.add(info1);
        data.add(info2);
        data.add(info3);
        data.add(info4);
        final ScreenTypeAdapter mAdapter = new ScreenTypeAdapter(context, data);

        screen_type_lv.setAdapter(mAdapter);

        screen_type_lv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                mAdapter.update(groupPosition);
                return false;
            }
        });
    }


    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        page = page + 1;
        isRefresh = false;
        if (ToolRefreshView.isLoadMore(refreshlayout,page, totalPages)) {
            sendHttp();
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 0;
        isRefresh = true;
        sendHttp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100 && data != null) {
            String search = data.getStringExtra("search");
            if (ToolString.isEmpty(search)) {
                info.name = search;//搜索内容修改
                search_tv.setVisibility(View.GONE);
                search_tv_content.setText(search);
                search_tv_content.setVisibility(View.VISIBLE);
                sendHttp();
            } else {
                search_tv.setVisibility(View.VISIBLE);
                search_tv_content.setVisibility(View.GONE);
            }
        }
    }
}
