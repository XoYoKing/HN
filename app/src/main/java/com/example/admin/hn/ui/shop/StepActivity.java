package com.example.admin.hn.ui.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.model.StepInfo;
import com.example.admin.hn.ui.adapter.StepAdapter;
import com.example.admin.hn.ui.fragment.shop.bean.ShopOrderInfo;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolString;
import com.example.admin.hn.volley.RequestListener;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by hjy on 2016/11/5.
 * 选择地址
 */
public class StepActivity extends BaseActivity {

    @Bind(R.id.text_title_back)
    TextView textTitleBack;
    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.tv_orderNo)
    TextView tv_orderNo;
    @Bind(R.id.tv_expressName)
    TextView tv_expressName;
    @Bind(R.id.tv_expressNo)
    TextView tv_expressNo;

    private StepAdapter adapter;
    private List<StepInfo> list = new ArrayList<>();
    private String url = Api.SHOP_BASE_URL + Api.GET_LOGISTICS;
    private ShopOrderInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_stepview);
        ButterKnife.bind(this);
        initTitleBar();
        initView();
        initData();
    }

    /**
     *
     * @param context
     */
    public static void startActivity(Context context, ShopOrderInfo info) {
        Intent intent = new Intent(context, StepActivity.class);
        intent.putExtra("info", info);
        context.startActivity(intent);
    }


    @Override
    public void initTitleBar() {
        textTitle.setText("查看物流");
        textTitleBack.setBackgroundResource(R.drawable.btn_back);
    }

    @Override
    public void initView() {
        info = (ShopOrderInfo) getIntent().getSerializableExtra("info");
        if (info != null) {
            tv_orderNo.setText(info.orderNo + "");
            tv_expressName.setText(info.expressName + "");
            tv_expressNo.setText(info.expressNo + "");

        }
    }


    @Override
    public void initData() {
        adapter = new StepAdapter(this, R.layout.item_step_view, list);
        listView.setAdapter(adapter);
        sendHttp();
    }

    private void sendHttp() {
        params.put("no", info.expressNo+"");
        params.put("code", info.expressId + "");
        http.get(url, params, progressTitle, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.e("物流信息", json);
                if (GsonUtils.isShopSuccess(json)) {
                    TypeToken typeToken = new TypeToken<List<StepInfo>>(){};
                    List<StepInfo> data = (List<StepInfo>) GsonUtils.jsonToList(json, typeToken, "data");
                    if (ToolString.isEmptyList(data)) {
                        list.addAll(data);
                        adapter.notifyDataSetChanged();
                    }
                }else {
                    ToolAlert.showToast(context,GsonUtils.getError(json));
                }
            }

            @Override
            public void requestError(String message) {
                ToolAlert.showToast(context,message);
            }
        });
    }

    @OnClick({R.id.text_title_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_title_back:
                finish();
                break;
        }
    }

}
