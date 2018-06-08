package com.example.admin.hn;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.hn.base.HNApplication;
import com.example.admin.hn.model.ShipInfo;
import com.example.admin.hn.ui.account.ShipSelectActivity;
import com.example.admin.hn.ui.fragment.FourFragment;
import com.example.admin.hn.ui.fragment.FourFragment2;
import com.example.admin.hn.ui.fragment.MapFragment;
import com.example.admin.hn.ui.fragment.MoreFragment;
import com.example.admin.hn.ui.fragment.ThreeFragment;
import com.example.admin.hn.ui.fragment.TypeFragment;
import com.example.admin.hn.utils.StateBarUtil;
import com.example.admin.hn.utils.ToolAlert;
import com.orhanobut.logger.Logger;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @Bind(R.id.id_tab_four_img2)
    ImageButton mIdTabFourImg2;
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
    @Bind(R.id.tv_tab_four2)
    TextView mTvTabFour2;
    @Bind(R.id.tv_tab_five)
    TextView mTvTabFive;
    @Bind(R.id.tv_prompt)
    ImageView prompt;

    @Bind(R.id.id_tab_one)
    LinearLayout id_tab_one;
    @Bind(R.id.id_tab_tow)
    LinearLayout id_tab_tow;
    @Bind(R.id.id_tab_three)
    LinearLayout id_tab_three;
    @Bind(R.id.id_tab_four)
    LinearLayout id_tab_four;
    @Bind(R.id.id_tab_four2)
    LinearLayout id_tab_four2;
    @Bind(R.id.id_tab_five)
    LinearLayout id_tab_five;

    private Fragment mTabOne;
    private Fragment mTabTow;
    private Fragment mTabThree;
    private Fragment mTabFour;
    private Fragment mTabFour2;
    private Fragment mTabFive;
    //
    public static final int SWITCH_TO_ONE = 1;
    public static final int SWITCH_TO_TOW = 2;
    public static final int SWITCH_TO_THREE = 3;
    public static final int SWITCH_TO_FOUR = 4;
    public static final int SWITCH_TO_FOUR2 = 42;
    public static final int SWITCH_TO_FIVE = 5;

    public static boolean isForeground = false;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.admin.hn.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public static List<ShipInfo.Ship> list = new ArrayList<>();
    public int level;//用户级别  0：其他，1：船舶用户，2：海务主管 ;3，代表岸端海宁用户
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initStateBar();
        init();
    }

    private void init() {
        Intent intent = getIntent();
        list = (ArrayList<ShipInfo.Ship>) intent.getSerializableExtra("list");
        //根据用户权限等级显示tab
        level = HNApplication.mApp.getUserType();
        if (level == 1 || level == 2 || level == 3) {
            // 船舶用户/海务主管显示界面：船位、文库、纸质海图、电子海图、更多；
            id_tab_tow.setVisibility(View.GONE);
        } else {
            // 其他用户显示界面：船位、商城、文库、更多
            id_tab_four.setVisibility(View.GONE);
            id_tab_four2.setVisibility(View.GONE);
        }
        resetImgs();
        setSelect(SWITCH_TO_ONE);
        //设置推送别名 以用户ID
        setAlias(HNApplication.mApp.getUserId());
        //创建本地购物车数据库
        createShopCartDb();
    }

    private void createShopCartDb() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                LitePal.deleteDatabase("hn_shop_cart");
                  Connector.getDatabase();
            }
        }).run();
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


    @OnClick({R.id.id_tab_one,R.id.id_tab_tow, R.id.id_tab_three, R.id.id_tab_four, R.id.id_tab_four2, R.id.id_tab_five})
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
            case R.id.id_tab_four2:
                setSelect(SWITCH_TO_FOUR2);
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
        if (!isShipSelect(i)) {
           return;
        }
        switch (i) {
            case SWITCH_TO_ONE:
                if (mTabOne == null) {
                    mTabOne = new MapFragment();
                    transaction.add(R.id.frame_tobe_repalce, mTabOne);
                } else {
                    transaction.show(mTabOne);
                }
                mIdTabOneImg.setSelected(true);
                mTvTabOne.setSelected(true);
                break;
            case SWITCH_TO_TOW:
                if (mTabTow == null) {
                    mTabTow = new TypeFragment();
                    transaction.add(R.id.frame_tobe_repalce, mTabTow);
                } else {
                    transaction.show(mTabTow);
                }
                mIdTabTowImg.setSelected(true);
                mTvTabTow.setSelected(true);
                break;
            case SWITCH_TO_THREE:
                if (mTabThree == null) {
                    mTabThree = new ThreeFragment();
                    transaction.add(R.id.frame_tobe_repalce, mTabThree);
                } else {
                    transaction.show(mTabThree);
                }
                mIdTabThreeImg.setSelected(true);
                mTvTabThree.setSelected(true);
                break;
            case SWITCH_TO_FOUR:
                if (mTabFour == null) {
                    mTabFour = new FourFragment();
                    transaction.add(R.id.frame_tobe_repalce, mTabFour);
                } else {
                    transaction.show(mTabFour);
                }
                mIdTabFourImg.setSelected(true);
                mTvTabFour.setSelected(true);
                break;
            case SWITCH_TO_FOUR2:
                if (mTabFour2 == null) {
                    mTabFour2 = new FourFragment2();
                    transaction.add(R.id.frame_tobe_repalce, mTabFour2);
                } else {
                    transaction.show(mTabFour2);
                }
                mIdTabFourImg2.setSelected(true);
                mTvTabFour2.setSelected(true);
                break;
            case SWITCH_TO_FIVE:
                if (mTabFive == null) {
                    mTabFive = new MoreFragment();
                    transaction.add(R.id.frame_tobe_repalce, mTabFive);
                } else {
                    transaction.show(mTabFive);
                }
                mIdTabFiveImg.setSelected(true);
                mTvTabFive.setSelected(true);
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private boolean isShipSelect(int index) {
        if (list.size() == 0) {
            //如果沒有选择船舶 就跳转到选择船舶界面
            ToolAlert.showToast(MainActivity.this, "请选择船舶", false);
            ShipSelectActivity.startActivity(this);
            if (index == SWITCH_TO_ONE) {
                return true;
            }
            return false;
        }
        return true;
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
        if (mTabFour2 != null) {
            transaction.hide(mTabFour2);
        }
        if (mTabFive != null) {
            transaction.hide(mTabFive);
        }

    }


    /**
     * 切换图片至暗色
     */
    private void resetImgs() {
        mIdTabOneImg.setSelected(false);
        mIdTabTowImg.setSelected(false);
        mIdTabThreeImg.setSelected(false);
        mIdTabFourImg.setSelected(false);
        mIdTabFourImg2.setSelected(false);
        mIdTabFiveImg.setSelected(false);

        //设置文字颜色为暗色
        mTvTabOne.setSelected(false);
        mTvTabTow.setSelected(false);
        mTvTabThree.setSelected(false);
        mTvTabFour.setSelected(false);
        mTvTabFour2.setSelected(false);
        mTvTabFive.setSelected(false);
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
        if (HNApplication.mApp.getMsgNumber() == 0) {
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
        super.onDestroy();
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        FragmentManager fragmentManager=getSupportFragmentManager();
        for(int index=0;index<fragmentManager.getFragments().size();index++){
            Fragment fragment=fragmentManager.getFragments().get(index); //找到第一层Fragment
            if(fragment!=null)
                handleResult(fragment,requestCode,resultCode,data);
        }
    }
    /**
     * 递归调用，对所有的子Fragment生效
     * @param fragment
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @SuppressLint("RestrictedApi")
    private void handleResult(Fragment fragment,int requestCode,int resultCode,Intent data) {
        fragment.onActivityResult(requestCode, resultCode, data);//调用每个Fragment的onActivityResult
        List<Fragment> childFragment = fragment.getChildFragmentManager().getFragments(); //找到第二层Fragment
        if(childFragment!=null)
            for(Fragment f:childFragment){
                if(f!=null) {
                    handleResult(f, requestCode, resultCode, data);
                }
            }
    }

    /**
     * 设置推送别名
     */
    private void setAlias(String alias) {
        Logger.e("alias", alias);
//        if (!ToolString.isValidTagAndAlias(alias)) {
//            ToolAlert.showToast(this, "alias格式不对");
//            return;
//        }
        //调用JPush API设置Alias
//        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

//    private static final int MSG_SET_ALIAS = 1001;
//    private static final int MSG_SET_TAGS = 1002;
//    private final Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(android.os.Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case MSG_SET_ALIAS:
//                    Log.d(TAG, "Set alias in handler.");
//                    JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
//                    break;
//
//                case MSG_SET_TAGS:
//                    Log.d(TAG, "Set tags in handler.");
//                    //JPushInterface.setAliasAndTags(getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
//                    break;
//
//                default:
//                    Log.i(TAG, "Unhandled msg - " + msg.what);
//            }
//        }
//    };

//    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
//
//        @Override
//        public void gotResult(int code, String alias, Set<String> tags) {
//            String logs;
//            switch (code) {
//                case 0:
//                    logs = "Set tag and alias success";
//                    Log.i(TAG, logs);
//                    break;
//
//                case 6002:
//                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
//                    Log.i(TAG, logs);
//                    if (ToolString.isConnected(getApplicationContext())) {
//                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
//                    } else {
//                        Log.i(TAG, "No network");
//                    }
//                    break;
//
//                default:
//                    logs = "Failed with errorCode = " + code;
//                    Log.e(TAG, logs);
//            }
//
//        }
//
//    };
}

