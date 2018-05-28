package com.example.admin.hn.ui.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.example.admin.hn.R;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.http.Constant;
import com.example.admin.hn.model.ScreenTypeInfo;
import com.example.admin.hn.ui.adapter.ScreenTypeAdapter;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolAppUtils;
import com.example.admin.hn.utils.ToolString;
import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    @Bind(R.id.tv_type_name)
    TextView tv_type_name;
    @Bind(R.id.pop_relative)
    RelativeLayout pop_relative;
    private int requestCode;
    private int layoutId;
    private View view;
    private TextView startDate1;
    private TextView endDate1;
    private EditText et_name1;
    private int childItem;//当前fragment的子fragment页面所在位置
    private TextView tv_date;
    private EditText et_dataNumber;
    private EditText et_chineseName;
    private EditText et_name;

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

    /**
     * @param activity
     */
    public static void startActivity(Activity activity,int childItem, int layoutId, int requestCode) {
        Intent intent = new Intent(activity, PopActivity.class);
        intent.putExtra("childItem", childItem);
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
        childItem = intent.getIntExtra("childItem", 0);

        int windowWith = ToolAppUtils.getWindowWith(this);
        ViewGroup.LayoutParams layoutParams = pop_relative.getLayoutParams();
        layoutParams.width = (int) (windowWith *0.8);
        pop_relative.setLayoutParams(layoutParams);
        view = View.inflate(this, layoutId, null);
        linear_top.addView(view);
        if (requestCode == Constant.POP_NOT_MATERIAL || requestCode == Constant.POP_NEW_MATERIAL ) {//船舶资料管理
            if (childItem == 0) {
                tv_type_name.setText("订单领用-待选");
            } else if (childItem == 1) {
                tv_type_name.setText("订单领用-新品推荐");
            }
            initNotMaterialView();
        } else if (requestCode == Constant.POP_ORDER_MANAGER) {//电子海图 订单管理
            tv_type_name.setText("订单管理");
            if (childItem == 0) {

            } else if (childItem == 1) {

            }
            initOrderManagerView(view);
        }else if (requestCode == Constant.POP_SHIP_AUDITING) {//船舶资料管理 审核管理
            if (childItem == 0) {
                tv_type_name.setText("审核管理-申请单");
            } else if (childItem == 1) {
                tv_type_name.setText("审核管理-领用单");
            }
            initOrderManagerView(view);
        }else if (requestCode == Constant.POP_LIB_TYPE) {//船舶资料管理
            tv_type_name.setText("文库搜索");
            initLibTypeView();
        }

    }

    /**
     * 初始化船舶资料管理待选搜索控件
     */
    private void initNotMaterialView() {
        et_dataNumber = (EditText) view.findViewById(R.id.et_dataNumber);
        et_chineseName = (EditText) view.findViewById(R.id.et_chineseName);
    }
    /**
     * 初始化船舶资料管理待选搜索控件
     */
    private void initLibTypeView() {
        et_name = (EditText) view.findViewById(R.id.et_name);
    }

    /**
     * 初始化电子海图订单管理搜索控件
     * @param view
     */
    private void initOrderManagerView(View view) {
        startDate1 = (TextView) view.findViewById(R.id.startdate);
        endDate1 = (TextView) view.findViewById(R.id.enddate);
        et_name1 = (EditText) view.findViewById(R.id.et_name);
        tv_date = (TextView) view.findViewById(R.id.tv_date);
        if (requestCode == Constant.POP_ORDER_MANAGER) {
            tv_date.setText("提交日期");
        }else if (requestCode == Constant.POP_SHIP_AUDITING){
            if (childItem == 0) {
                tv_date.setText("申请日期");
            }else {
                tv_date.setText("领用日期");
            }
        }
        //初始化选择器的时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        startDate1.setText(sdf.format(c.getTime()));
        endDate1.setText(date);

        startDate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //时间选择器
                TimePickerView pvTime = new TimePickerView.Builder(context, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        startDate1.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
                    }
                }).isCyclic(true).setBackgroundId(0x00FFFFFF).setContentSize(21).setType(new boolean[]{true, true, true, false, false, false}).build();
                pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
                pvTime.show();
            }
        });

        endDate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //时间选择器
                TimePickerView pTime = new TimePickerView.Builder(context, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        endDate1.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
                    }
                }).isCyclic(true).setBackgroundId(0x00FFFFFF).setContentSize(21).setType(new boolean[]{true, true, true, false, false, false}).build();
                pTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
                pTime.show();
            }
        });

    }


    @Override
    public void initData() {


    }

    @OnClick({R.id.bt_reset, R.id.bt_sure})
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.bt_reset:
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String curr_end = sdf.format(new Date());
                Calendar c = Calendar.getInstance();
                c.add(Calendar.MONTH, -1);
                String curr_start = sdf.format(c.getTime());
                if (requestCode == Constant.POP_ORDER_MANAGER) {//订单管理
                    intent.putExtra("start", curr_start);
                    intent.putExtra("end", curr_end);
                    intent.putExtra("childItem", childItem);
                }else if (requestCode == Constant.POP_SHIP_AUDITING) {//审核申领搜索
                    intent.putExtra("start", curr_start);
                    intent.putExtra("end", curr_end);
                } else if (requestCode == Constant.POP_NOT_MATERIAL) {//待选搜索
                    intent.putExtra("dataNumber", "");
                    intent.putExtra("chineseName", "");
                }else if (requestCode == Constant.POP_LIB_TYPE) {//文库搜索
                    intent.putExtra("name", "");
                    intent.putExtra("currentItem", childItem);
                }
                setResult(requestCode,intent);
                finish();
                break;
            case R.id.bt_sure:
                if (requestCode == Constant.POP_ORDER_MANAGER) {//订单管理
                    String start = startDate1.getText().toString();
                    String end = endDate1.getText().toString();
                    String name = et_name1.getText().toString();
                    intent.putExtra("start", start);
                    intent.putExtra("end", end);
                    intent.putExtra("childItem", childItem);
                    if (ToolString.isNoBlankAndNoNull(name)) {
                        intent.putExtra("name", name);
                    }
                }else if (requestCode == Constant.POP_SHIP_AUDITING) {//审核申领搜索
                    String start = startDate1.getText().toString();
                    String end = endDate1.getText().toString();
                    String name = et_name1.getText().toString();
                    intent.putExtra("start", start);
                    intent.putExtra("end", end);
                    if (ToolString.isNoBlankAndNoNull(name)) {
                        intent.putExtra("name", name);
                    }
                } else if (requestCode == Constant.POP_NOT_MATERIAL) {//待选搜索
                    String dataNumber = et_dataNumber.getText().toString();
                    String chineseName = et_chineseName.getText().toString();
                    intent.putExtra("dataNumber", dataNumber);
                    intent.putExtra("chineseName", chineseName);
                }else if (requestCode == Constant.POP_LIB_TYPE) {//文库搜索
                    String name = et_name.getText().toString();
                    intent.putExtra("name", name);
                    intent.putExtra("currentItem", childItem);
                }
                setResult(requestCode,intent);
                finish();
                break;
        }
    }

}
