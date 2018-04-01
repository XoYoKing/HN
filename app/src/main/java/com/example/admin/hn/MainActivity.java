package com.example.admin.hn;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.hn.api.Api;
import com.example.admin.hn.http.OkHttpUtil;
import com.example.admin.hn.model.ServerResponse;
import com.example.admin.hn.model.ShipInfo;
import com.example.admin.hn.ui.account.ShipActivity;
import com.example.admin.hn.ui.fragment.FourFragment;
import com.example.admin.hn.ui.fragment.MapFragment;
import com.example.admin.hn.ui.fragment.MoreFragment;
import com.example.admin.hn.ui.fragment.OneFragment;
import com.example.admin.hn.ui.fragment.ThreeFragment;
import com.example.admin.hn.ui.fragment.TowFragment;
import com.example.admin.hn.utils.ExampleUtil;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.StateBarUtil;
import com.example.admin.hn.utils.ToolAlert;
import com.orhanobut.logger.Logger;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * @author duantao
 * @date on 2017/7/26 16:06
 * @describe
 */
public class MainActivity extends FragmentActivity {

    private static final String TAG = "MainActivity";
    private boolean isExit;

    //底部按钮和布局
    @Bind(R.id.id_tab_one_img)
    ImageButton mIdTabOneImg;
    @Bind(R.id.id_tab_tow_img)
    ImageButton mIdTabTowImg;
    @Bind(R.id.id_tab_three_img)
    ImageButton mIdTabThreeImg;
    @Bind(R.id.id_tab_four_img)
    ImageButton mIdTabFourImg;
    @Bind(R.id.id_tab_five_img)
    ImageButton mIdTabFiveImg;

    //底部Textview
    @Bind(R.id.tv_tab_one)
    TextView mTvTabOne;
    @Bind(R.id.tv_tab_tow)
    TextView mTvTabTow;
    @Bind(R.id.tv_tab_three)
    TextView mTvTabThree;
    @Bind(R.id.tv_tab_four)
    TextView mTvTabFour;
    @Bind(R.id.tv_tab_five)
    TextView mTvTabFive;
    @Bind(R.id.tv_prompt)
    ImageView prompt;


    private Fragment mTabOne;
    private Fragment mTabTow;
    private Fragment mTabThree;
    private Fragment mTabFour;
    private Fragment mTabFive;
    //
    public static final int SWITCH_TO_ONE = 1;
    public static final int SWITCH_TO_TOW = 2;
    public static final int SWITCH_TO_THREE = 3;
    public static final int SWITCH_TO_FOUR = 4;
    public static final int SWITCH_TO_FIVE = 5;
    //极光推送
    private MessageReceiver mMessageReceiver;
    public static boolean isForeground = false;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.admin.hn.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public static int number;
    public static String USER_ID;
    public static String phonenumber;
    public static String username;
    public static String email;
    public static String userid;
    public static String tuisongid;
    public static List<ShipInfo.ship> list = new ArrayList<ShipInfo.ship>();
    private String url_appid = Api.BASE_URL + Api.APPID;
    public static String ship = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initStateBar();
        init();
    }

    private void init() {
        Intent intent = getIntent();
        phonenumber = intent.getStringExtra("phonenumber");
        username = intent.getStringExtra("username");
        email = intent.getStringExtra("email");
        USER_ID = intent.getStringExtra("userid");
        list = (ArrayList<ShipInfo.ship>) getIntent().getSerializableExtra("list");
        if (list.size() == 0) {
            Intent intent1 = new Intent(MainActivity.this, ShipActivity.class);
            ToolAlert.showToast(MainActivity.this, "请选择船舶", false);
            startActivity(intent1);
        }
        resetImgs();
        setSelect(SWITCH_TO_ONE);
        //极光推送登陆
//        JPushInterface.init(getApplicationContext());
//        String JPush_id = JPushInterface.getRegistrationID(this);
//        setAlias();
////        ToolAlert.showToast(MainActivity.this, JPush_id, false);
//        Log.i(TAG, "推送id=" + JPush_id);
//        tuisongid=JPush_id;
    }


    private void initStateBar() {
        SystemBarTintManager tintManager = new SystemBarTintManager(MainActivity.this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#3084EC"));
        StateBarUtil.initSystemBar(MainActivity.this);
    }


    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        //解决fragment重叠的办法
        super.onSaveInstanceState(bundle);
    }


    @OnClick({R.id.id_tab_one,R.id.id_tab_tow, R.id.id_tab_three, R.id.id_tab_four, R.id.id_tab_five})
    public void onClick(View v) {
        resetImgs();
        switch (v.getId()) {
            case R.id.id_tab_one:
                setSelect(SWITCH_TO_ONE);
                break;
            case R.id.id_tab_tow:
                setSelect(SWITCH_TO_TOW);
                break;
            case R.id.id_tab_three:
                setSelect(SWITCH_TO_THREE);
                break;
            case R.id.id_tab_four:
                setSelect(SWITCH_TO_FOUR);
                break;
            case R.id.id_tab_five:
                setSelect(SWITCH_TO_FIVE);
                break;
            default:
                break;
        }

    }

    private void setSelect(int i) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
        // 把图片设置为亮的
        // 设置内容区域
        switch (i) {
            case SWITCH_TO_ONE:
                if (mTabOne == null) {
                    mTabOne = new MapFragment();
                    transaction.add(R.id.frame_tobe_repalce, mTabOne);
                } else {
                    transaction.show(mTabOne);
                }
                mIdTabOneImg.setImageResource(R.drawable.tab_icon_1_pressed);
                mTvTabOne.setTextColor(getResources().getColor(R.color.yukon_gold));
                break;
            case SWITCH_TO_TOW:
                if (mTabTow == null) {
                    mTabTow = new OneFragment();
                    transaction.add(R.id.frame_tobe_repalce, mTabTow);
                } else {
                    transaction.show(mTabTow);
                }
                mIdTabTowImg.setImageResource(R.drawable.tab_icon_2_pressed);
                mTvTabTow.setTextColor(getResources().getColor(R.color.yukon_gold));
                break;
            case SWITCH_TO_THREE:
                if (mTabThree == null) {
                    mTabThree = new ThreeFragment();
                    transaction.add(R.id.frame_tobe_repalce, mTabThree);
                } else {
                    transaction.show(mTabThree);
                }
                mIdTabThreeImg.setImageResource(R.drawable.tab_icon_3_pressed);
                mTvTabThree.setTextColor(getResources().getColor(R.color.yukon_gold));
                break;
            case SWITCH_TO_FOUR:
                if (mTabFour == null) {
                    mTabFour = new FourFragment();
                    transaction.add(R.id.frame_tobe_repalce, mTabFour);
                } else {
                    transaction.show(mTabFour);
                }
                mIdTabFourImg.setImageResource(R.drawable.tab_icon_4_pressed);
                mTvTabFour.setTextColor(getResources().getColor(R.color.yukon_gold));
                break;
            case SWITCH_TO_FIVE:
                if (mTabFive == null) {
                    mTabFive = new MoreFragment();
                    transaction.add(R.id.frame_tobe_repalce, mTabFive);
                } else {
                    transaction.show(mTabFive);
                }
                mIdTabFiveImg.setImageResource(R.drawable.tab_icon_4_pressed);
                mTvTabFive.setTextColor(getResources().getColor(R.color.yukon_gold));
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (mTabOne != null) {
            transaction.hide(mTabOne);
        }
        if (mTabTow != null) {
            transaction.hide(mTabTow);
        }
        if (mTabThree != null) {
            transaction.hide(mTabThree);
        }
        if (mTabFour != null) {
            transaction.hide(mTabFour);
        }
        if (mTabFive != null) {
            transaction.hide(mTabFive);
        }

    }


    /**
     * 切换图片至暗色
     */
    private void resetImgs() {
        mIdTabOneImg.setImageResource(R.drawable.tab_icon_1_normal);
        mIdTabTowImg.setImageResource(R.drawable.tab_icon_2_normal);
        mIdTabThreeImg.setImageResource(R.drawable.tab_icon_3_normal);
        mIdTabFourImg.setImageResource(R.drawable.tab_icon_4_normal);
        mIdTabFiveImg.setImageResource(R.drawable.tab_icon_4_normal);

        //设置文字颜色为暗色
        mTvTabOne.setTextColor(getResources().getColor(R.color.mountain_mist));
        mTvTabTow.setTextColor(getResources().getColor(R.color.mountain_mist));
        mTvTabThree.setTextColor(getResources().getColor(R.color.mountain_mist));
        mTvTabFour.setTextColor(getResources().getColor(R.color.mountain_mist));
        mTvTabFive.setTextColor(getResources().getColor(R.color.mountain_mist));
    }


    /**
     * 单击不退出,双击退出
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 监控返回键
            exitBy2Click();
            return false;
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            // 监控菜单键
            Toast.makeText(MainActivity.this, "Menu", Toast.LENGTH_SHORT).show();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 双击退出
     */
    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }


    @Override
    protected void onResume() {
        isForeground = true;
        if (number == 0) {
            prompt.setVisibility(View.GONE);
        } else {
            prompt.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }


    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //消除极光推送标记
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    //极光推送
    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!ExampleUtil.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                }
            } catch (Exception e) {
            }
        }
    }


    // 这是来自 JPush Example 的设置别名的 Activity 里的代码。一般 App 的设置的调用入口，在任何方便的地方调用都可以。
    private void setAlias() {
        // 调用 Handler 来异步设置别名
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, MainActivity.USER_ID));
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, MainActivity.USER_ID), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }
//            ExampleUtil.showToast(logs, getApplicationContext());
        }
    };
    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d(TAG, "Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    String JPush_id = JPushInterface.getRegistrationID(MainActivity.this);
                    Map map = new HashMap();
                    map.put("Appid", JPush_id);
                    map.put("Userid", MainActivity.USER_ID);

                    String jsonStr = GsonUtils.mapToJson(map);
                    Logger.i(TAG, jsonStr);
                    try {
                        OkHttpUtil.postJsonData2Server(MainActivity.this,
                                url_appid,
                                jsonStr,
                                new OkHttpUtil.MyCallBack() {
                                    @Override
                                    public void onFailure(Request request, IOException e) {
                                        ToolAlert.showToast(MainActivity.this, "服务器异常,请稍后再试", false);

                                    }

                                    @Override
                                    public void onResponse(String json) {
                                        Logger.i(TAG, json);
                                        ServerResponse serverResponse = GsonUtils
                                                .jsonToBean(json,
                                                        ServerResponse.class
                                                );
                                    }
                                });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };


}

