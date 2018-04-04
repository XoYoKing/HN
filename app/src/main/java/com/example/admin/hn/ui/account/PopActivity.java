package com.example.admin.hn.ui.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.model.ScreenTypeInfo;
import com.example.admin.hn.ui.adapter.ScreenTypeAdapter;
import com.example.admin.hn.utils.ToolAlert;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by hjy on 2016/11/5.
 * 右侧筛选弹框
 */
public class PopActivity extends BaseActivity {

    @Bind(R.id.linear_top)
    FrameLayout linear_top;
    @Bind(R.id.linear_bottom)
    LinearLayout linear_bottom;
    private int requestCode;
    private int layoutId;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);
        ButterKnife.bind(this);
        initTitleBar();
        initView();
        initData();
    }

    /**
     * @param activity
     */
    public static void startActivity(Activity activity, int layoutId, int requestCode) {
        Intent intent = new Intent(activity, PopActivity.class);
        intent.putExtra("layoutId", layoutId);
        intent.putExtra("requestCode", requestCode);
        activity.startActivityForResult(intent, requestCode);
    }


    @Override
    public void initTitleBar() {

    }

    @Override
    public void initView() {
        Intent intent = getIntent();
        layoutId = intent.getIntExtra("layoutId", 0);
        requestCode = intent.getIntExtra("requestCode", 0);
        view = View.inflate(this, layoutId, null);
        linear_top.addView(view);
        if (requestCode == 100) {

        } else if (requestCode == 200) {

        } else if (requestCode == 300) {

        } else if (requestCode == 400) {

        } else if (requestCode == 500) {

        } else if (requestCode == 600) {

        } else if (requestCode == 700) {

        }

    }


    @Override
    public void initData() {


    }

    @OnClick({R.id.bt_reset, R.id.bt_sure})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_reset:
                finish();
                break;
            case R.id.bt_sure:
                setResult(requestCode);
                finish();
                break;
        }
    }


    private void initPopView(View view) {
        ExpandableListView screen_type_lv = (ExpandableListView) view.findViewById(R.id.screen_type_lv);
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
