package com.example.admin.hn.ui.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
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
import com.example.admin.hn.utils.ToolViewUtils;
import com.example.admin.hn.volley.RequestListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by hjy on 2016/11/5.
 * 提交评价
 */
public class SubmitCommentActivity extends BaseActivity {

    @Bind(R.id.text_title_back)
    TextView textTitleBack;
    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.text_tile_right)
    TextView text_tile_right;
    @Bind(R.id.et_content)
    EditText et_content;
    @Bind(R.id.ratingBar)
    RatingBar ratingBar;
    @Bind(R.id.iv_img)
    ImageView iv_img;


    private String url = Api.SHOP_BASE_URL + Api.GET_SAVE_COMMENT;
    private String content;
    private ShopOrderInfo info;
    private ShopOrderInfo.OrderItems items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_comment);
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
        Intent intent = new Intent(context, SubmitCommentActivity.class);
        intent.putExtra("info", info);
        context.startActivity(intent);
    }


    @Override
    public void initTitleBar() {
        textTitle.setText("评价晒单");
        textTitleBack.setBackgroundResource(R.drawable.btn_back);
        text_tile_right.setText("提交");
    }

    @Override
    public void initView() {
        info = (ShopOrderInfo) getIntent().getSerializableExtra("info");
        if (ToolString.isEmptyList(info.orderItems)) {
            items = info.orderItems.get(0);
            ToolViewUtils.glideImageList(info.orderItems.get(0).imageUrl, iv_img, R.drawable.load_fail);
        }
    }


    @Override
    public void initData() {

    }

    @OnClick({R.id.text_title_back,R.id.text_tile_right})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_title_back:
                finish();
                break;
            case R.id.text_tile_right:
                content = et_content.getText().toString();
                if (ToolString.isEmpty(content)) {
                    submitComment();
                }else {
                    ToolAlert.showToast(context,"评价不能为空");
                }
                break;
        }
    }

    private void submitComment() {
        params.put("orederId", info.orderNo+"");
        params.put("spuId", items.spuId + "");
        params.put("memberName", info.memberName + "");
        params.put("goodsSpec", items.goodsSpec+"");
        params.put("goodsName", items.goodsName+"");
        params.put("score", (int)ratingBar.getRating() + "");
        params.put("content", content);
        http.post(url, params, "正在提交...", new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                if (GsonUtils.isShopSuccess(json)) {
                    finish();
                }
                ToolAlert.showToast(context,GsonUtils.getError(json));
            }

            @Override
            public void requestError(String message) {
                ToolAlert.showToast(context,message);
            }
        });
    }

}
