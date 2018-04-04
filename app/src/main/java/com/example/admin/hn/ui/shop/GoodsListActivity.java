package com.example.admin.hn.ui.shop;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.model.GoodsInfo;
import com.example.admin.hn.model.ScreenTypeInfo;
import com.example.admin.hn.ui.adapter.GoodsListAdapter;
import com.example.admin.hn.ui.adapter.ScreenTypeAdapter;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolAppUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by hjy on 2016/11/5.
 * 商品列表
 */
public class GoodsListActivity extends BaseActivity {

    @Bind(R.id.text_title_back)
    TextView textTitleBack;
    @Bind(R.id.text_title)
    TextView textTitle;
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
    @Bind(R.id.network_img)
    ImageView network_img;
    @Bind(R.id.noData_img)
    ImageView nodata_img;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.listView)
    ListView listView;

    private List<GoodsInfo> list = new ArrayList<>();
    private GoodsListAdapter adapter;

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
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, GoodsListActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void initTitleBar() {
        textTitle.setText("商品列表");
        textTitleBack.setBackgroundResource(R.drawable.btn_back);
    }

    @Override
    public void initView() {

        for (int i = 0; i < 10; i++) {
            list.add(new GoodsInfo());
        }
        adapter = new GoodsListAdapter(this, R.layout.item_goods_list, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GoodsInfo info = list.get(position);
                GoodsDetailActivity.startActivity(context);
            }
        });
    }


    @Override
    public void initData() {

        goods_list_all_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goods_list_all_tv.setSelected(true);
            }
        });
        goods_list_screen_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showPopWindow(v);
            }
        });
        goods_list_price_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelect(goods_list_price_img);
                goods_list_price_tv.setSelected(true);
                goods_list_most_tv.setSelected(false);
            }
        });
        goods_list_most_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelect(goods_list_most_img);
                goods_list_most_tv.setSelected(true);
                goods_list_price_tv.setSelected(false);
            }
        });

    }
//,R.id.goods_list_most_linear,R.id.goods_list_most_linear,R.id.goods_list_price_linear,R.id.goods_list_screen_linear
    @OnClick({R.id.text_title_back,R.id.noData_img,R.id.network_img})
    public void onClick(View v) {
        setSelectDefault();
        switch (v.getId()) {
            case R.id.text_title_back:
                finish();
                break;
            case R.id.noData_img:
                clickHttp();
                nodata_img.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                break;
            case R.id.network_img:
                clickHttp();
                progressBar.setVisibility(View.VISIBLE);
                network_img.setVisibility(View.GONE);
                break;
            case R.id.goods_list_all_linear:
                clickHttp();
                goods_list_all_tv.setSelected(true);
                break;
            case R.id.goods_list_most_linear:
                clickHttp();
                setSelect(goods_list_most_img);
                goods_list_most_tv.setSelected(true);
                break;
            case R.id.goods_list_price_linear:
                clickHttp();
                setSelect(goods_list_price_img);
                goods_list_price_tv.setSelected(true);
                break;
            case R.id.goods_list_screen_linear:
                showPopWindow(v);
                break;
        }
    }

    private void clickHttp() {

    }

    private void setSelect(ImageView imageView) {
        if (imageView.isSelected()) {
            imageView.setSelected(false);
        } else {
            imageView.setSelected(true);
        }
    }
    private void setSelectDefault() {
        goods_list_all_tv.setSelected(false);
        goods_list_screen_tv.setSelected(false);
        goods_list_price_tv.setSelected(false);
        goods_list_most_tv.setSelected(false);
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


}
