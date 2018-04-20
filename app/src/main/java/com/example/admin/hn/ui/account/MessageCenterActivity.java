package com.example.admin.hn.ui.account;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.base.HNApplication;
import com.example.admin.hn.http.Constant;
import com.example.admin.hn.model.EightEntity;
import com.example.admin.hn.model.MessageInfo;
import com.example.admin.hn.model.Messagedel;
import com.example.admin.hn.ui.adapter.GroupAdapter;
import com.example.admin.hn.ui.adapter.MessageAdapter;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolRefreshView;
import com.example.admin.hn.utils.ToolString;
import com.example.admin.hn.volley.RequestListener;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * @author duantao
 * @date on 2017/8/7 15:42
 * @describe 消息通知
 */
public class MessageCenterActivity extends BaseActivity implements AdapterView.OnItemClickListener ,OnRefreshListener,OnLoadmoreListener{
    private static final String TAG = "MessageCenterActivity";
    @Bind(R.id.lv_message_center)
    ListView mLvMessageCenter;
    @Bind(R.id.text_title_back)
    TextView mTextTitleBack;
    @Bind(R.id.text_title)
    TextView mTextTitle;
    @Bind(R.id.text_tile_right)
    TextView mTextright;
    @Bind(R.id.allCb)
    public CheckBox allCb;
    @Bind(R.id.bSubmit)
    public Button bSubmit;
    @Bind(R.id.rlBottom)
    RelativeLayout rlBottom;
    @Bind(R.id.text_tile_del)
    TextView text_tile_del;

    @Bind(R.id.network_disabled)
    RelativeLayout network;
    @Bind(R.id.network_img)
    ImageView network_img;
    @Bind(R.id.noData_img)
    ImageView noData_img;

    public ArrayList<EightEntity> list = new ArrayList<>();
    private GroupAdapter groupAdapter;
    private PopupWindow mPopupWindow;
    private View contentView;
    private MessageAdapter adapter;
    private ArrayList<String> group;
    private ListView listview;
    private ArrayList<MessageInfo.Messageinfo> groups = new ArrayList<>();
    private String url_message = Api.BASE_URL + Api.MESSAGE;
    private String url_del = Api.BASE_URL + Api.MESSAGEDEL;
    private int tradeType = 100;
    private int page = 1;
    private int status = 1;
    public List<Messagedel.Message> listdel = new ArrayList<>();
    private RefreshLayout refreshLayout;
    ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center);
        ButterKnife.bind(this);
        initTitleBar();
        initView();
        data(0);
        initDatas();
    }

    @Override
    public void initTitleBar() {
        super.initTitleBar();
        mTextTitle.setText(R.string.title_message_center);
        mTextTitleBack.setBackgroundResource(R.drawable.btn_back);
        mTextright.setBackgroundResource(R.drawable.sort);
        text_tile_del.setVisibility(View.VISIBLE);
        text_tile_del.setText("清理");
    }

    @Override
    public void initView() {
        super.initView();
        //消除角标
        ShortcutBadger.removeCount(this);
        HNApplication.mApp.setMsgNumber(0);
        //下拉刷新
        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        ToolRefreshView.setRefreshLayout(context,refreshLayout);

        mLvMessageCenter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent_order = new Intent(MessageCenterActivity.this, OrderActivity.class);
                intent_order.putExtra("number", list.get(position).getOrdernumber());
                startActivity(intent_order);
            }
        });
        adapter = new MessageAdapter(MessageCenterActivity.this, R.layout.eight_item, list);
        mLvMessageCenter.setAdapter(adapter);
    }


    @OnClick({R.id.text_title_back, R.id.text_tile_right, R.id.text_tile_del})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_title_back:
                finish();
                break;
            case R.id.text_tile_del:
                if (allCb.getVisibility() == View.GONE) {
                    rlBottom.setVisibility(View.VISIBLE);
                    allCb.setVisibility(View.VISIBLE);
                    text_tile_del.setText("完成");
                    for (EightEntity entity : list) {
                        entity.setChecked(true);
                    }
                } else {
                    text_tile_del.setText("清理");
                    rlBottom.setVisibility(View.GONE);
                    allCb.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.text_tile_right:
                showPopwindow(v);
                break;
        }
    }


    private void showPopwindow(View parent) {
        if (mPopupWindow == null) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(this);
            contentView = mLayoutInflater.inflate(R.layout.group_list, null);
            listview = (ListView) contentView.findViewById(R.id.lv_group);
            // 加载数据
            group = new ArrayList<>();
            group.add("全部");
            group.add("成功");
            group.add("失败");
            group.add("已删除");
            groupAdapter = new GroupAdapter(this,R.layout.group_item, group);
            listview.setAdapter(groupAdapter);
            mPopupWindow = new PopupWindow(contentView, getWindowManager()
                    .getDefaultDisplay().getWidth() / 3, getWindowManager()
                    .getDefaultDisplay().getHeight() / 3);
        }
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);

        // 显示的位置为:屏幕的宽度的1/16
        int xPos = getWindowManager().getDefaultDisplay().getWidth() / 16;

        mPopupWindow.showAsDropDown(parent, xPos, 0);

        listview.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
        switch (position) {
            case 0:
                status = 1;
                page=1;
                data(0);
                break;
            case 1:
                status = 2;
                page=1;
                data(0);
                break;
            case 2:
                status = 3;
                page=1;
                data(0);
                break;
            case 3:
                status = 4;
                page=1;
                data(0);
                break;
        }
    }


    public void initDatas() {
        allCb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!allCb.isChecked()) {
                    for (EightEntity entity : list) {
                        entity.setChecked(false);
                    }
                    adapter.notifyDataSetChanged();
                }

            }
        });

        allCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {
                    for (EightEntity entity : list) {
                        entity.setChecked(true);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                boolean flag = false;
                deleteMsg();
                if (!flag) {
                    return;
                }
                allCb.setVisibility(View.GONE);
                rlBottom.setVisibility(View.GONE);
            }
        });
    }

    public void deleteMsg() {
        List<EightEntity> deleteList = new ArrayList<>();
        for (EightEntity entity : list) {
            // (1-已读 0-未读)
            if (entity.isChecked()) {
                deleteList.add(entity);
                listdel.add(new Messagedel.Message(entity.getMessageid()));
            }
        }
        Messagedel messagedel = new Messagedel(listdel, HNApplication.mApp.getUserId());
        delData(messagedel);
        list.removeAll(deleteList);
        adapter.setList(list);
        adapter.notifyDataSetChanged();
    }

    public void delData(Messagedel messagedel) {
        http.postJson(url_del, messagedel, "正在删除...", new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.i(TAG, json);
                if (GsonUtils.isSuccess(json)) {
                    MessageInfo message = GsonUtils.jsonToBean(json, MessageInfo.class);
                    list.clear();
                    if (ToolString.isEmptyList(message.getDocuments())) {
                        for (int i=0;i<message.getDocuments().size();i++){
                            list.add(new EightEntity(false,message.getDocuments().get(i).getLinename(),message.getDocuments().get(i).getOrderdetails(),message.getDocuments().get(i).getNoticetime(),message.getDocuments().get(i).getShipname(),message.getDocuments().get(i).getMessageid(),message.getDocuments().get(i).getOrdernumber(), message.getDocuments().get(i).getMessagestate(), message.getDocuments().get(i).getNoticecontent()));
                        }
                    }
                }else {
                    if (page != 1) {
                        ToolAlert.showToast(context, Constant.LOADED);
                    }
                }
                ToolRefreshView.hintView(adapter,refreshLayout,false,network,noData_img,network_img);
            }

            @Override
            public void requestError(String message) {
                ToolAlert.showToast(context,message);
            }
        });
    }

    public void data(final int Loadmore) {
        Map map = new HashMap();
        map.put("screen", status);
        if (Loadmore==0){
            map.put("page", "1");
        }else {
            map.put("page", page);
        }
        http.postJson(url_message, map, progressTitle, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.i(TAG, json);
                if (GsonUtils.isSuccess(json)) {
                    MessageInfo message = GsonUtils.jsonToBean(json, MessageInfo.class);
                    if (Loadmore == 0) {
                        list.clear();
                    }
                    for (int i = 0; i < message.getDocuments().size(); i++) {
                        list.add(new EightEntity(false, message.getDocuments().get(i).getLinename(), message.getDocuments().get(i).getOrderdetails(), message.getDocuments().get(i).getNoticetime(), message.getDocuments().get(i).getShipname(), message.getDocuments().get(i).getMessageid(), message.getDocuments().get(i).getOrdernumber(), message.getDocuments().get(i).getMessagestate(), message.getDocuments().get(i).getNoticecontent()));
                    }
                }else {
                    ToolAlert.showToast(context,GsonUtils.getError(json));
                }
                ToolRefreshView.hintView(adapter,refreshLayout,false,network,noData_img,network_img);
            }

            @Override
            public void requestError(String message) {
                ToolAlert.showToast(context,message);
                ToolRefreshView.hintView(adapter,refreshLayout,true,network,noData_img,network_img);
            }
        });

    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        page = page+1;
        data(1);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 1;
        data(0);
    }
}
