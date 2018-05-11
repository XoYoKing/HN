package com.example.admin.hn.ui.shop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.model.SearchInfo;
import com.example.admin.hn.model.StepInfo;
import com.example.admin.hn.ui.adapter.SearchAdapter;
import com.example.admin.hn.ui.adapter.StepAdapter;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

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

    private List<SearchInfo> list = new ArrayList<>();
    private EditText search_edit_content;
    private RelativeLayout search_relative;
    private TagFlowLayout search_history;
    private TagFlowLayout search_hot;
    private ListView listView;
    private SearchAdapter adapter;

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
    public static void startActivity(Activity context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivityForResult(intent, 100);
    }


    @Override
    public void initTitleBar() {

    }

    @Override
    public void initView() {
        search_relative = (RelativeLayout) findViewById(R.id.search_relative);
        search_history = (TagFlowLayout) findViewById(R.id.search_history);
        search_hot = (TagFlowLayout) findViewById(R.id.search_hot);
        listView = (ListView) findViewById(R.id.listView);
        search_edit_content = (EditText) findViewById(R.id.search_edit_content);
        adapter = new SearchAdapter(context, R.layout.item_search_layout, list);
        listView.setAdapter(adapter);
    }


    @Override
    public void initData() {
        final ArrayList<String> history_data = new ArrayList();
        for (int i = 1; i < 10; i++) {
            history_data.add("手机" + (i * 100));
        }
        final ArrayList<String> hot_data = new ArrayList();
        for (int i = 1; i < 8; i++) {
            hot_data.add("笔记本" + (i * 50));
        }
        search_history.setAdapter(new TagAdapter<String>(history_data) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView bt = (TextView) View.inflate(context, R.layout.search_hos_item, null);
                bt.setText(s);
                return bt;
            }
        });
        search_hot.setAdapter(new TagAdapter<String>(hot_data) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView bt = (TextView) View.inflate(context, R.layout.search_hos_item, null);
                bt.setText(s);
                return bt;
            }
        });
        search_history.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                select(history_data.get(position));
                return true;
            }

        });
        search_hot.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                select(hot_data.get(position));
                return true;
            }

        });

        adapter.notifyDataSetChanged();
    }

    @OnClick({R.id.iv_back,R.id.tv_search})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_search:
                select(search_edit_content.getText().toString());
                break;
        }
    }

    private void select(String search) {
        Intent intent = new Intent();
        intent.putExtra("search",search);
        setResult(100,intent);
        finish();
    }

}
