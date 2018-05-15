package com.example.admin.hn.ui.shop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.model.SearchInfo;
import com.example.admin.hn.model.StepInfo;
import com.example.admin.hn.ui.adapter.SearchAdapter;
import com.example.admin.hn.ui.adapter.StepAdapter;
import com.example.admin.hn.ui.fragment.shop.bean.ShopCartInfo;
import com.example.admin.hn.utils.AbDateUtil;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolRefreshView;
import com.example.admin.hn.utils.ToolString;
import com.example.admin.hn.utils.ToolViewUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.litepal.crud.DataSupport;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by hjy on 2016/11/5.
 * 搜索
 */
public class SearchActivity extends BaseActivity {

    @Bind(R.id.network_disabled)
    RelativeLayout network;
    @Bind(R.id.network_img)
    ImageView network_img;
    @Bind(R.id.noData_img)
    ImageView noData_img;
    @Bind(R.id.iv_del)
    ImageView iv_del;
    @Bind(R.id.recycleView)
    RecyclerView recycleView;
    @Bind(R.id.search_edit_content)
    EditText search_edit_content;

    private List<SearchInfo> list = new ArrayList<>();
    private SearchAdapter adapter;

    private RelativeLayout search_relative;
    private TagFlowLayout search_history;
    private TagFlowLayout search_hot;
    private ListView listView;
    private String search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initTitleBar();
        initView();
        initData();
    }

    /**
     *
     * @param context
     */
    public static void startActivity(Activity context,String search) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra("search", search);
        context.startActivityForResult(intent, 100);
    }


    @Override
    public void initTitleBar() {

    }

    @Override
    public void initView() {
//        search_relative = (RelativeLayout) findViewById(R.id.search_relative);
//        search_history = (TagFlowLayout) findViewById(R.id.search_history);
//        search_hot = (TagFlowLayout) findViewById(R.id.search_hot);
//        listView = (ListView) findViewById(R.id.listView);

        search = getIntent().getStringExtra("search");
        if (ToolString.isEmpty(search)) {
            search_edit_content.setText(search);
            ToolViewUtils.setSelection(search_edit_content);
        }
        adapter = new SearchAdapter(context, R.layout.item_search_layout, list);
        recycleView.setLayoutManager(new LinearLayoutManager(context));
        recycleView.setAdapter(adapter);
    }


    @Override
    public void initData() {
//        final ArrayList<String> history_data = new ArrayList();
//        for (int i = 1; i < 10; i++) {
//            history_data.add("手机" + (i * 100));
//        }
//        final ArrayList<String> hot_data = new ArrayList();
//        for (int i = 1; i < 8; i++) {
//            hot_data.add("笔记本" + (i * 50));
//        }
//        search_history.setAdapter(new TagAdapter<String>(history_data) {
//            @Override
//            public View getView(FlowLayout parent, int position, String s) {
//                TextView bt = (TextView) View.inflate(context, R.layout.search_hos_item, null);
//                bt.setText(s);
//                return bt;
//            }
//        });
//        search_hot.setAdapter(new TagAdapter<String>(hot_data) {
//            @Override
//            public View getView(FlowLayout parent, int position, String s) {
//                TextView bt = (TextView) View.inflate(context, R.layout.search_hos_item, null);
//                bt.setText(s);
//                return bt;
//            }
//        });
//        search_history.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
//            @Override
//            public boolean onTagClick(View view, int position, FlowLayout parent) {
//                select(history_data.get(position));
//                return true;
//            }
//
//        });
//        search_hot.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
//            @Override
//            public boolean onTagClick(View view, int position, FlowLayout parent) {
//                select(hot_data.get(position));
//                return true;
//            }
//
//        });

        sendHttp();
    }

    private void sendHttp() {
        try {
            //时间倒叙排序
            List<SearchInfo> infoList = DataSupport.select("*").order("date desc").find(SearchInfo.class);
            list.clear();
            if (ToolString.isEmptyList(infoList)) {
                list.addAll(infoList);
                ToolRefreshView.hintView(adapter, false, network, noData_img, network_img);
            }else {
                ToolRefreshView.hintView(adapter, false, network, noData_img, network_img);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.iv_back,R.id.tv_search,R.id.iv_del})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_del:
                //删除历史记录
                try {
                    DataSupport.deleteAll(SearchInfo.class);
                    sendHttp();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_search:
                search = search_edit_content.getText().toString();
                if (ToolString.isEmpty(search)) {
                    select(search);
                }else {
                    ToolAlert.showToast(context, "请输入搜索内容");
                }
                break;
        }
    }

    /**
     * 设置选择的数据 保存到本地数据库
     * @param search
     */
    public void select(String search) {
        //保存到本地数据库
        try {
            List<SearchInfo> all = DataSupport.findAll(SearchInfo.class);
            if (ToolString.isEmptyList(all)) {
                for (int i = 0; i < all.size(); i++) {
                    SearchInfo info = all.get(i);
                    if (search.equals(info.getName())) {
                        //如果搜索内容相同就刷新搜索时间
                        info.setDate(System.currentTimeMillis());
                        info.update(info.getId());
                        break;
                    }
                    if (i == all.size() - 1) {
                        //如果沒有找到
                        save(search);
                    }
                }
            }else {
                save(search);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent = new Intent();
        intent.putExtra("search",search);
        setResult(100,intent);
        finish();
    }

    private void save(String search){
        SearchInfo info = new SearchInfo();
        info.setDate(System.currentTimeMillis());
        info.setName(search);
        info.save();
    }

}
