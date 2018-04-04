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
    @Bind(R.id.top_type)
    TextView top_type;
    @Bind(R.id.recycleView)
    RecyclerView recycleView;

    private ArrayList<HomeTypeInfo> list = new ArrayList<>();
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
        sendHttp();
    }
    @Override
    public void initView() {
        Bundle bundle = getArguments();
        homeTypeInfo = (HomeTypeInfo) bundle.getSerializable("homeTypeInfo");

        top_type.setText(homeTypeInfo.getName());
        recycleView.setNestedScrollingEnabled(false);
        recycleView.setFocusable(false);
        recycleView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new ShopTypeListAdapter(activity, homeInfos);
        recycleView.setAdapter(adapter);
    }


    @OnClick({R.id.network_img,R.id.noData_img})
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
        }
    }
    //首页应用列表
    private List<HomeInfo> homeInfos = new ArrayList<>();
    private void initHomeData(){
        if (homeInfos.size() > 0) {
            homeInfos.clear();
        }
        for (int i = 1; i < 3; i++) {
            HomeInfo info = new HomeInfo();
            info.catalogData= new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                info.catalogData.add(new HomeItem());
            }
            info.type = i;
            homeInfos.add(info);
        }
        adapter.notifyDataSetChanged();
    }
    private void sendHttp() {
        initHomeData();
    }

}
