package com.example.admin.hn.ui.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.model.AddressInfo;
import com.example.admin.hn.model.ShoppingCartInfo;
import com.example.admin.hn.ui.adapter.FirmOrderAdapter;
import com.example.admin.hn.ui.adapter.ManageAddressAdapter;
import com.example.admin.hn.ui.adapter.SelectAddressAdapter;
import com.example.admin.hn.utils.ToolAlert;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by hjy on 2016/11/5.
 * 选择地址
 */
public class SelectAddressActivity extends BaseActivity {

    @Bind(R.id.text_title_back)
    TextView textTitleBack;
    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.text_tile_del)
    TextView text_tile_del;
    @Bind(R.id.listView)
    ListView listView;
    private List<AddressInfo> list=new ArrayList<>();
    private SelectAddressAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
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
        Intent intent = new Intent(context, SelectAddressActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void initTitleBar() {
        textTitle.setText("选择收货地址");
        textTitleBack.setBackgroundResource(R.drawable.btn_back);
        text_tile_del.setVisibility(View.VISIBLE);
        text_tile_del.setText("管理");
    }

    @Override
    public void initView() {


        for (int i = 0; i < 5; i++) {
            list.add(new AddressInfo());
        }
        adapter = new SelectAddressAdapter(this, R.layout.item_select_address, list);
        listView.setAdapter(adapter);

    }


    @Override
    public void initData() {

    }

    @OnClick({R.id.text_title_back,R.id.text_tile_del,R.id.add_address})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_title_back:
                finish();
                break;
            case R.id.text_tile_del:
                ManagerAddressActivity.startActivity(context);
                break;
            case R.id.add_address:
                CreateAddressActivity.startActivity(context);
                break;
        }
    }

}
