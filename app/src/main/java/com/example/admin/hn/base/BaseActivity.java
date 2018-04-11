package com.example.admin.hn.base;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.example.admin.hn.R;
import com.example.admin.hn.base.interf.Initialable;
import com.example.admin.hn.http.Constant;
import com.example.admin.hn.http.OkHttpUtil;
import com.example.admin.hn.utils.StateBarUtil;
import com.example.admin.hn.widget.ProgersssDialog;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.ButterKnife;


/**
*  @date on 2017/7/26 16:06
*  @author duantao
*  @describe 
*/
public class BaseActivity extends FragmentActivity implements Initialable{
    protected String progressTitle = Constant.LOADING;
    protected Context context;
    IntentFilter mFilter = new IntentFilter();
    public ProgersssDialog progersssDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initStateBar();
        this.context = this;
    }

    /**
     * 设置5.0以上系统的沉浸式顶栏效果
     */
    private void initStateBar() {
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#7D5F0D"));
        StateBarUtil.initSystemBar(BaseActivity.this);
    }

    /**
     * 获取物理设备IP地址
     *
     * @return
     */
//    public String getIpAddress() {
//        //获取wifi服务
//        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//        int ip = wifiInfo.getIpAddress();
//        String ipAddress = ToolNetwork.intToIp(ip);
//        MainActivity.ipAddress = ipAddress;
//        return ipAddress;
//    }

//    /**
//     * 获取手机IMEI号
//     *
//     * @return
//     */
//    public String getImei() {
//        String imei = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId();
//        MainActivity.imei = imei;
//        return imei;
//
//    }
//
//    private BroadcastReceiver mFinishReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            if ("finish".equals(intent.getAction())) {
//                Log.e("#########", "I am " + getLocalClassName() + ",now finishing myself...");
//                finish();
//            }
//        }
//    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭请求队列
        OkHttpUtil.mOkHttpClient.cancel(this);
        ButterKnife.unbind(this);
        //关闭网络广播监听
//        unregisterReceiver(mFinishReceiver);
    }



//    /**
//     * 设置ListView的高度
//     */
//    public void setListViewHeightBasedOnChildren(ListView listView) {
//        // 获取ListView对应的Adapter
//        ListAdapter listAdapter = listView.getAdapter();
//        if (listAdapter == null) {
//            return;
//        }
//
//        int totalHeight = 0;
//        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
//            View listItem = listAdapter.getView(i, null, listView);
//            //            int desiredWidth= View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
//            listItem.measure(0, 0); // 计算子项View 的宽高
//            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
//        }
//
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//        // params.height最后得到整个ListView完整显示需要的高度
//        listView.setLayoutParams(params);
//    }

    /**
     * 进入页面的动画效果
     *
     * @param intent
     */
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.anmi_in_right_left, R.anim.anmi_out_right_left);
    }

    /**
     * 退出页面的动画效果
     */
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_in_left_right, R.anim.anit_out_left_right);
    }

    /**
     * 回调时的动画效果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        overridePendingTransition(R.anim.anmi_in_right_left, R.anim.anmi_out_right_left);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initTitleBar() {

    }

//    @Override
//    public void onBackPressed() {
////        super.onBackPressed();
//    }
}
