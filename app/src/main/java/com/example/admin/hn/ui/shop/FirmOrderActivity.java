package com.example.admin.hn.ui.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.model.AddressInfo;
import com.example.admin.hn.model.ShoppingCartInfo;
import com.example.admin.hn.ui.adapter.FirmOrderAdapter;

import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by hjy on 2016/11/5.
 * 确认订单
 */
public class FirmOrderActivity extends BaseActivity {

    @Bind(R.id.text_title_back)
    TextView textTitleBack;
    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.listView)
    ListView listView;

    private static List<ShoppingCartInfo> infos;
    private FirmOrderAdapter adapter;
    private AddressInfo addressInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firm_order);
        ButterKnife.bind(this);
        initTitleBar();
        initView();
        initData();
    }

    /**
     *
     * @param context
     */
    public static void startActivity(Context context, List<ShoppingCartInfo> orderInfo) {
        infos = orderInfo;
        Intent intent = new Intent(context, FirmOrderActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void initTitleBar() {
        textTitle.setText("确认订单");
        textTitleBack.setBackgroundResource(R.drawable.btn_back);
        Intent intent = getIntent();
        addressInfo = (AddressInfo) intent.getSerializableExtra("addressInfo");
    }

    @Override
    public void initView() {

    }


    @Override
    public void initData() {
        for (int i = 0; i < 2; i++) {
            infos.add(new ShoppingCartInfo());
        }
        adapter = new FirmOrderAdapter(this, R.layout.item_firm_order_layout, infos);
        listView.setAdapter(adapter);
    }

    private void setAddress(AddressInfo info) {
        if (info != null) {
//            goods_receipt_name.setText("收货人：" + info.getConsignee());
//            goods_receipt_phone.setText(info.getMobile());
        }
    }

    @OnClick({R.id.text_title_back,R.id.go_pay,R.id.address_linear})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_title_back:
                finish();
                break;
            case R.id.go_pay:
                payOrder();
                break;
            case R.id.address_linear:
                SelectAddressActivity.startActivity(context);
                break;
        }
    }

    private void payOrder() {
        PayActivity.startActivity(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            AddressInfo info = (AddressInfo) data.getSerializableExtra("info");
            setAddress(info);
        }
    }
}
