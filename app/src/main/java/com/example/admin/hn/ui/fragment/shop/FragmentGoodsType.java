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
        top_type.setVisibility(View.GONE);
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
        if (homeTypeInfo.getId() == 0) {
            for (int i = 0; i < 11; i++) {
                HomeInfo info = new HomeInfo();
                info.data= new ArrayList<>();
                if (i == 0) {
                    info.name = "今日推荐";
                    info.type = 1;
                } else if (i == 1) {
                    info.name = "中版海图";
                    info.type = 1;
                    for (int j = 0; j < 3; j++) {
                        HomeItem homeItem = new HomeItem();
                        if (j == 0) {
                            homeItem.goodsName = "航海部海图";
                        }else if (j == 1) {
                            homeItem.goodsName = "海事局海图";
                        }else if (j == 2) {
                            homeItem.goodsName = "专用海图";
                        }
                        info.data.add(homeItem);
                    }
                }else if (i == 2) {
                    info.name = "英版海图";
                    info.type = 1;
                    for (int j = 0; j < 1; j++) {
                        HomeItem homeItem = new HomeItem();
                        if (j == 0) {
                            homeItem.goodsName = "外版海图";
                        }
                        info.data.add(homeItem);
                    }
                }else if (i == 3) {
                    info.name = "中版书籍";
                    info.type = 1;
                    for (int j = 0; j < 2; j++) {
                        HomeItem homeItem = new HomeItem();
                        if (j == 0) {
                            homeItem.goodsName = "中版图书";
                        }else if (j == 1) {
                            homeItem.goodsName = "航海杂志";
                        }
                        info.data.add(homeItem);
                    }
                }else if (i == 4) {
                    info.name = "英版书籍";
                    info.type = 1;
                    for (int j = 0; j < 1; j++) {
                        HomeItem homeItem = new HomeItem();
                        if (j == 0) {
                            homeItem.goodsName = "外版图书";
                        }
                        info.data.add(homeItem);
                    }
                }else if (i == 5) {
                    info.name = "安全标志";
                    info.type = 1;
                    for (int j = 0; j < 3; j++) {
                        HomeItem homeItem = new HomeItem();
                        if (j == 0) {
                            homeItem.goodsName = "救生标志";
                        }else if (j == 1) {
                            homeItem.goodsName = "消防标志";
                        }else if (j == 2) {
                            homeItem.goodsName = "安全张贴";
                        }
                        info.data.add(homeItem);
                    }
                }else if (i == 6) {
                    info.name = "其他";
                    info.type = 1;
                    for (int j = 0; j < 6; j++) {
                        HomeItem homeItem = new HomeItem();
                        if (j == 0) {
                            homeItem.goodsName = "服务包";
                        }else if (j == 1) {
                            homeItem.goodsName = "中华表册";
                        }else if (j == 2) {
                            homeItem.goodsName = "油运表册";
                        }else if (j == 3) {
                            homeItem.goodsName = "国际表册";
                        }else if (j == 4) {
                            homeItem.goodsName = "航海通告";
                        }else if (j == 5) {
                            homeItem.goodsName = "其他材料";
                        }
                        info.data.add(homeItem);
                    }
                }else if (i == 7) {
                    info.name = "电子版海图";
                    info.type = 1;
                    for (int j = 0; j < 2; j++) {
                        HomeItem homeItem = new HomeItem();
                        if (j == 0) {
                            homeItem.goodsName = "电子版海图";
                        }else if (j == 1) {
                            homeItem.goodsName = "电子版海图";
                        }
                        info.data.add(homeItem);
                    }
                } else if (i == 8) {
                    info.name = "电子版书籍";
                    info.type = 1;
                    for (int j = 0; j < 2; j++) {
                        HomeItem homeItem = new HomeItem();
                        if (j == 0) {
                            homeItem.goodsName = "电子版书籍";
                        }else if (j == 1) {
                            homeItem.goodsName = "电子版书籍";
                        }
                        info.data.add(homeItem);
                    }
                }else if (i == 9) {
                    info.name = "丽佳通讯";
                    info.type = 1;
                    for (int j = 0; j < 1; j++) {
                        HomeItem homeItem = new HomeItem();
                        if (j == 0) {
                            homeItem.goodsName = "丽佳通讯";
                        }
                        info.data.add(homeItem);
                    }
                }else if (i == 10) {
                    info.name = "新书速递";
                    info.type = 1;
                    for (int j = 0; j < 5; j++) {
                        HomeItem homeItem = new HomeItem();
                        if (j == 0) {
                            homeItem.goodsName = "船舶使用低硫燃油指南2013";
                        }else if (j == 1) {
                            homeItem.goodsName = "IMO第92届海上安全委员会通过的决议(中英文光盘）";
                        }else if (j == 2) {
                            homeItem.goodsName = "IMO第65届海上环境保护委员会通过的决议（中英文光盘）";
                        }else if (j == 3) {
                            homeItem.goodsName = "国内航行海船缺陷处理操作指南";
                        }else if (j == 4) {
                            homeItem.goodsName = "船舶引航管理规定";
                        }
                        info.data.add(homeItem);
                    }
                }
                homeInfos.add(info);
            }
        } else if (homeTypeInfo.getId() == 1) {
            for (int i = 1; i < 7; i++) {
                HomeInfo info = new HomeInfo();
                info.data= new ArrayList<>();
                if (i == 1) {
                    info.name = "中版海图";
                    info.type = 1;
                    for (int j = 0; j < 3; j++) {
                        HomeItem homeItem = new HomeItem();
                        if (j == 0) {
                            homeItem.goodsName = "航海部海图";
                        }else if (j == 1) {
                            homeItem.goodsName = "海事局海图";
                        }else if (j == 2) {
                            homeItem.goodsName = "专用海图";
                        }
                        info.data.add(homeItem);
                    }
                }else if (i == 2) {
                    info.name = "英版海图";
                    info.type = 1;
                    for (int j = 0; j < 1; j++) {
                        HomeItem homeItem = new HomeItem();
                        if (j == 0) {
                            homeItem.goodsName = "外版海图";
                        }
                        info.data.add(homeItem);
                    }
                }else if (i == 3) {
                    info.name = "中版书籍";
                    info.type = 1;
                    for (int j = 0; j < 2; j++) {
                        HomeItem homeItem = new HomeItem();
                        if (j == 0) {
                            homeItem.goodsName = "中版图书";
                        }else if (j == 1) {
                            homeItem.goodsName = "航海杂志";
                        }
                        info.data.add(homeItem);
                    }
                }else if (i == 4) {
                    info.name = "英版书籍";
                    info.type = 1;
                    for (int j = 0; j < 1; j++) {
                        HomeItem homeItem = new HomeItem();
                        if (j == 0) {
                            homeItem.goodsName = "外版图书";
                        }
                        info.data.add(homeItem);
                    }
                }else if (i == 5) {
                    info.name = "安全标志";
                    info.type = 1;
                    for (int j = 0; j < 3; j++) {
                        HomeItem homeItem = new HomeItem();
                        if (j == 0) {
                            homeItem.goodsName = "救生标志";
                        }else if (j == 1) {
                            homeItem.goodsName = "消防标志";
                        }else if (j == 2) {
                            homeItem.goodsName = "安全张贴";
                        }
                        info.data.add(homeItem);
                    }
                }else if (i == 6) {
                    info.name = "其他";
                    info.type = 1;
                    for (int j = 0; j < 6; j++) {
                        HomeItem homeItem = new HomeItem();
                        if (j == 0) {
                            homeItem.goodsName = "服务包";
                        }else if (j == 1) {
                            homeItem.goodsName = "中华表册";
                        }else if (j == 2) {
                            homeItem.goodsName = "油运表册";
                        }else if (j == 1) {
                            homeItem.goodsName = "国际表册";
                        }else if (j == 2) {
                            homeItem.goodsName = "航海通告";
                        }else if (j == 1) {
                            homeItem.goodsName = "其他材料";
                        }
                        info.data.add(homeItem);
                    }
                }
                homeInfos.add(info);
            }
        }else if (homeTypeInfo.getId() == 2) {
            for (int i = 0; i < 2; i++) {
                HomeInfo info = new HomeInfo();
                info.data= new ArrayList<>();
                if (i == 0) {
                    info.name = "电子版海图";
                    info.type = 1;
                    for (int j = 0; j < 2; j++) {
                        HomeItem homeItem = new HomeItem();
                        if (j == 0) {
                            homeItem.goodsName = "电子版海图";
                        }else if (j == 1) {
                            homeItem.goodsName = "电子版海图";
                        }
                        info.data.add(homeItem);
                    }
                } else if (i == 1) {
                    info.name = "电子版书籍";
                    info.type = 1;
                    for (int j = 0; j < 2; j++) {
                        HomeItem homeItem = new HomeItem();
                        if (j == 0) {
                            homeItem.goodsName = "电子版书籍";
                        }else if (j == 1) {
                            homeItem.goodsName = "电子版书籍";
                        }
                        info.data.add(homeItem);
                    }
                }
                homeInfos.add(info);
            }
        }else if (homeTypeInfo.getId() == 3) {
            HomeInfo info = new HomeInfo();
            info.data= new ArrayList<>();
            info.name = "丽佳通讯";
            info.type = 1;
            for (int j = 0; j < 1; j++) {
                HomeItem homeItem = new HomeItem();
                if (j == 0) {
                    homeItem.goodsName = "丽佳通讯";
                }
                info.data.add(homeItem);
            }
            homeInfos.add(info);
        }else if (homeTypeInfo.getId() == 4) {
                HomeInfo info = new HomeInfo();
                info.data= new ArrayList<>();
                info.name = "新书速递";
                info.type = 1;
                for (int j = 0; j < 5; j++) {
                    HomeItem homeItem = new HomeItem();
                    if (j == 0) {
                        homeItem.goodsName = "船舶使用低硫燃油指南2013";
                    }else if (j == 1) {
                        homeItem.goodsName = "IMO第92届海上安全委员会通过的决议(中英文光盘）";
                    }else if (j == 2) {
                        homeItem.goodsName = "IMO第65届海上环境保护委员会通过的决议（中英文光盘）";
                    }else if (j == 3) {
                        homeItem.goodsName = "国内航行海船缺陷处理操作指南";
                    }else if (j == 4) {
                        homeItem.goodsName = "船舶引航管理规定";
                    }
                    info.data.add(homeItem);
                }
            homeInfos.add(info);
        }
        adapter.notifyDataSetChanged();
    }
    private void sendHttp() {
        initHomeData();
    }

}
