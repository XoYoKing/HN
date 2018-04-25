
package com.example.admin.hn.ui.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.base.BaseFragment;
import com.example.admin.hn.model.HomeTypeInfo;
import com.example.admin.hn.ui.adapter.GroupAdapter;
import com.example.admin.hn.ui.fragment.shop.FragmentGoodsType;
import com.example.admin.hn.ui.shop.OrderManagerActivity;
import com.example.admin.hn.ui.shop.ShopCartActivity;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 商品一级分类
 */
public class TypeFragment extends BaseFragment {

    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.text_tile_del)
    TextView text_tile_del;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.network_img)
    ImageView network_img;
    @Bind(R.id.noData_img)
    ImageView noData_img;
    @Bind(R.id.goods_pager)
    ViewPager goods_pager;
    @Bind(R.id.tools_scrollview)
    ScrollView scrollView;
    @Bind(R.id.tools_linear)
    LinearLayout tools_linear;

    private View view;
    private List<HomeTypeInfo> toolsList = new ArrayList<>();
    private TextView toolsTextViews[];
    private ImageView toolsImgViews[];
    private RelativeLayout toolsRelative[];
    private View views[];
    private int scrollViewWidth = 0, scrollViewMiddle = 0;
    private int currentItem = 0;
    private ShopAdapter shopAdapter;
    private LayoutInflater inflater;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shop_type, container, false);
        ButterKnife.bind(this, view);
        initTitleBar();
        initView();
        initData();
        return view;
    }

    @Override
    public void initData() {
        sendHttp();
    }
    @Override
    public void initTitleBar() {
        textTitle.setText("商城");
        text_tile_del.setVisibility(View.VISIBLE);
        text_tile_del.setBackgroundResource(R.drawable.shop_type_in);
    }

    @Override
    public void initView() {
        shopAdapter = new ShopAdapter(getChildFragmentManager());
        inflater = LayoutInflater.from(activity);
        goods_pager = (ViewPager) view.findViewById(R.id.goods_pager);
        goods_pager.setAdapter(shopAdapter);
        goods_pager.setOnPageChangeListener(onPageChangeListener);
    }


    @OnClick({R.id.network_img,R.id.noData_img,R.id.text_tile_del})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.network_img:
                progressBar.setVisibility(View.VISIBLE);
                network_img.setVisibility(View.GONE);
                sendHttp();
            break;
            case R.id.noData_img:
                noData_img.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                sendHttp();
                break;
            case R.id.text_tile_del:
                showPopwindow(v);
                break;
        }
    }


    /**
     * 动态生成显示items中的textview
     */
    private void showToolsView(List<HomeTypeInfo> list) {
        if (toolsList.size() > 0) {
            toolsList.clear();
        }
        if (list != null) {
            toolsList.addAll(list);
        } else {
            initHomeData();
        }
        shopAdapter.notifyDataSetChanged();
        LinearLayout toolsLayout = (LinearLayout) view.findViewById(R.id.tools);
        toolsTextViews = new TextView[toolsList.size()];
        toolsImgViews = new ImageView[toolsList.size()];
        toolsRelative = new RelativeLayout[toolsList.size()];
        views = new View[toolsList.size()];

        for (int i = 0; i < toolsList.size(); i++) {
            View view = inflater.inflate(R.layout.goods_type_title_item_layout, null);
            view.setId(i);
            view.setOnClickListener(toolsItemListener);
            TextView textView = (TextView) view.findViewById(R.id.text);
            ImageView line_right = (ImageView) view.findViewById(R.id.line_right);
            RelativeLayout type_item_bg = (RelativeLayout) view.findViewById(R.id.type_item_bg);
            textView.setText(toolsList.get(i).getName() + "");
            toolsLayout.addView(view);
            toolsTextViews[i] = textView;
            views[i] = view;
            toolsRelative[i] = type_item_bg;
            toolsImgViews[i] = line_right;
        }
        changeTextColor(0);
    }

    private void initHomeData() {
        for (int i = 0; i < 5; i++) {
            HomeTypeInfo homeTypeInfo = new HomeTypeInfo();
            if (i == 0) {
                homeTypeInfo.setName("今日推荐");
            } else if (i == 1) {
                homeTypeInfo.setName("纸版图书");
            } else if (i == 2) {
                homeTypeInfo.setName("电子版图书");
            } else if (i == 3) {
                homeTypeInfo.setName("丽佳通讯");
            } else if (i == 4) {
                homeTypeInfo.setName("新书速递");
            }
            homeTypeInfo.setId(i);
            toolsList.add(homeTypeInfo);
        }
    }

    private View.OnClickListener toolsItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            goods_pager.setCurrentItem(v.getId(), false);
        }
    };

    /**
     * OnPageChangeListener<br/>
     * 监听ViewPager选项卡变化事的事件
     */

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int arg0) {
            if (goods_pager.getCurrentItem() != arg0) goods_pager.setCurrentItem(arg0);
            if (currentItem != arg0) {
                changeTextColor(arg0);
                changeTextLocation(arg0);
            }
            currentItem = arg0;
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };


    /**
     * ViewPager 加载选项卡
     *
     * @author Administrator
     */
    private class ShopAdapter extends FragmentPagerAdapter {
        public ShopAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            Fragment fragment = new FragmentGoodsType();
            Bundle bundle = new Bundle();
            HomeTypeInfo homeTypeInfo = toolsList.get(arg0);
            bundle.putSerializable("homeTypeInfo", homeTypeInfo);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return toolsList.size();
        }
    }


    /**
     * 改变textView的颜色
     * 隐藏或显示被选中的title 右边的线条
     *
     * @param id
     */
    private void changeTextColor(int id) {
        for (int i = 0; i < toolsTextViews.length; i++) {
            if (i != id) {
                toolsTextViews[i].setTextColor(0xff000000);
                toolsImgViews[i].setVisibility(View.VISIBLE);
                toolsRelative[i].setSelected(false);
            }
        }
        if (toolsTextViews.length > 0) {
            toolsTextViews[id].setTextColor(0xffFF4081);
        }
        if (toolsImgViews.length > 0) {
            toolsImgViews[id].setVisibility(View.GONE);
        }
        if (toolsRelative.length > 0) {
            toolsRelative[id].setSelected(true);
        }

    }


    /**
     * 改变栏目位置
     *
     * @param clickPosition
     */
    private void changeTextLocation(int clickPosition) {
        int x = (views[clickPosition].getTop() - getScrollViewMiddle() + (getViewHeight(views[clickPosition]) / 2));
        scrollView.smoothScrollTo(0, x);
    }

    /**
     * 返回scrollview的中间位置
     *
     * @return
     */
    private int getScrollViewMiddle() {
        if (scrollViewMiddle == 0)
            scrollViewMiddle = getScrollViewHeight() / 2;
        return scrollViewMiddle;
    }

    /**
     * 返回ScrollView的宽度
     *
     * @return
     */
    private int getScrollViewHeight() {
        if (scrollViewWidth == 0)
            scrollViewWidth = scrollView.getBottom() - scrollView.getTop();
        return scrollViewWidth;
    }

    /**
     * 返回view的宽度
     *
     * @param view
     * @return
     */
    private int getViewHeight(View view) {
        return view.getBottom() - view.getTop();
    }


    private void sendHttp() {
        tools_linear.setVisibility(View.VISIBLE);
        showToolsView(null);
    }
    private PopupWindow mPopupWindow;
    private View contentView;
    private ListView listview;
    private ArrayList<String> group;
    private GroupAdapter groupAdapter;

    private void showPopwindow(View parent) {
        if (mPopupWindow == null) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(getActivity());
            contentView = mLayoutInflater.inflate(R.layout.group_list, null);
            listview = (ListView) contentView.findViewById(R.id.lv_group);
            // 加载数据
            group = new ArrayList<>();
            group.add("我的订单");
            group.add("购物车");
            groupAdapter = new GroupAdapter(getActivity(),R.layout.group_item, group);
            listview.setAdapter(groupAdapter);

            mPopupWindow = new PopupWindow(contentView, getActivity().getWindowManager().getDefaultDisplay().getWidth() / 3,
                    getActivity().getWindowManager().getDefaultDisplay().getHeight() / 5);
        }
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);

        // 显示的位置为:屏幕的宽度的1/16
        int xPos = getActivity().getWindowManager().getDefaultDisplay().getWidth() / 16;

        mPopupWindow.showAsDropDown(parent, xPos, 0);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
                switch (position) {
                    case 0:
                        OrderManagerActivity.startActivity(activity);
                        break;
                    case 1:
                        ShopCartActivity.startActivity(activity);
                        break;
                }
            }
        });

    }


}
