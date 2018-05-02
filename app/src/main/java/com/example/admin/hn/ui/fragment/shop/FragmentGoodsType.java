package com.example.admin.hn.ui.fragment.shop;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.base.BaseFragment;
import com.example.admin.hn.model.HomeInfo;
import com.example.admin.hn.model.HomeItem;
import com.example.admin.hn.model.HomeTypeInfo;
import com.example.admin.hn.ui.adapter.ShopTypeListAdapter;


import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 商品二级分类
 */
public class FragmentGoodsType extends BaseFragment {

    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.network_img)
    ImageView network_img;
    @Bind(R.id.noData_img)
    ImageView noData_img;
    @Bind(R.id.recycleView)
    RecyclerView recycleView;
    private ShopTypeListAdapter adapter;
    private View view;
    private HomeTypeInfo homeTypeInfo;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_goods_type, container, false);
        ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }

    @Override
    public void initData() {
    }
    @Override
    public void initView() {
        Bundle bundle = getArguments();
        homeTypeInfo = (HomeTypeInfo) bundle.getSerializable("homeTypeInfo");
        recycleView.setNestedScrollingEnabled(false);
        recycleView.setFocusable(false);
        recycleView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new ShopTypeListAdapter(activity, homeTypeInfo.children);
        recycleView.setAdapter(adapter);
    }


    @OnClick({R.id.noData_img})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.noData_img:
                noData_img.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                break;
        }
    }

}
