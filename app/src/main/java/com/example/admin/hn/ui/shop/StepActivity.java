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
import com.example.admin.hn.model.StepInfo;
import com.example.admin.hn.ui.adapter.ManageAddressAdapter;
import com.example.admin.hn.ui.adapter.StepAdapter;
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
public class StepActivity extends BaseActivity {

    @Bind(R.id.text_title_back)
    TextView textTitleBack;
    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.listView)
    ListView listView;
    private StepAdapter adapter;

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
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, StepActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void initTitleBar() {
        textTitle.setText("查看物流");
        textTitleBack.setBackgroundResource(R.drawable.btn_back);
    }

    @Override
    public void initView() {


    }


    @Override
    public void initData() {
        List<StepInfo> list0 = new ArrayList<>();
        list0.add(new StepInfo("2017-03-15 12:00", "感谢你在京东购物，欢迎你下次光临！"));
        list0.add(new StepInfo("2017-03-14 12:00", "配送员【包牙齿】已出发，联系电话【130-0000-0000】，感谢您的耐心等待，参加评价还能赢取好多礼物哦"));
        list0.add(new StepInfo("2017-03-13 12:00", "您的订单在京东【北京中关村大厦站】验货完成，正在分配配送员"));
        list0.add(new StepInfo("2017-03-12 12:00", "您的订单在京东【北京通州分拣中心】发货完成，准备送往京东【北京中关村大厦站】"));
        list0.add(new StepInfo("2017-03-11 12:00", "您的订单在京东【北京通州分拣中心】分拣完成"));
        list0.add(new StepInfo("2017-03-10 12:00", "您的订单在京东【华东外单分拣中心】发货完成，准备送往京东【北京通州分拣中心】"));
        list0.add(new StepInfo("2017-03-09 12:00", "打包成功"));
        list0.add(new StepInfo("2017-03-08 12:00", "扫描员已经扫描"));
        list0.add(new StepInfo("2017-03-07 12:00", "您的订单已拣货完成"));
        list0.add(new StepInfo("2017-03-06 12:00", "您的订单已打印完毕"));
        list0.add(new StepInfo("2017-03-05 12:00", "您的订单预计6月23日送达您的手中，618期间促销火爆，可能影响送货时间，请您谅解，我们会第一时间送到您的手中"));
        list0.add(new StepInfo("2017-03-04 12:00", "您的订单已经进入亚洲第一仓储中心1号库准备出库"));
        list0.add(new StepInfo("2017-03-03 12:00", "您的商品需要从外地调拨，我们会尽快处理，请耐心等待"));
        list0.add(new StepInfo("2017-03-02 12:00", "您已提交定单，等待系统确认"));

        adapter = new StepAdapter(this, R.layout.item_step_view, list0);
        listView.setAdapter(adapter);

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
