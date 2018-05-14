package com.example.admin.hn.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.admin.hn.R;
import com.example.admin.hn.base.interf.Initialable;
import com.example.admin.hn.http.Constant;
import com.example.admin.hn.utils.StateBarUtil;
import com.example.admin.hn.volley.IRequest;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;

/**
*  @date on 2017/7/26 16:06
*  @author duantao
*  @describe 
*/
public class BaseFragment extends Fragment implements Initialable {

    protected FragmentActivity activity;
    protected String progressTitle = Constant.LOADING;
    protected boolean isFirstHttp = true;//第一次请求
    protected boolean isRefresh = true;
    protected IRequest http;
    protected HashMap params;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        http = new IRequest(activity);
        params = new HashMap();
        initStateBar();
    }

    /**
     * 设置ListView的高度
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            //            int desiredWidth= View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void initStateBar() {
        SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#3084EC"));
        StateBarUtil.initSystemBar(getActivity());
    }

    @Override
    public void startActivity(Intent intent) {
        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.anmi_in_right_left, R.anim.anmi_out_right_left);

    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        getActivity().startActivityForResult(intent, requestCode);
        getActivity().overridePendingTransition(R.anim.anmi_in_right_left, R.anim.anmi_out_right_left);
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
    private PermissionsListener mListener;

    /**
     * 请求权限封装
     *
     * @param permissions
     * @param listener
     */
    public void requestPermissions(String[] permissions, PermissionsListener listener) {
        this.mListener = listener;
        List<String> requestPermissions = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED) {
                requestPermissions.add(permission);
            }
        }
        if (!requestPermissions.isEmpty() && Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(activity, requestPermissions.toArray(new String[requestPermissions.size()]), 1);
        } else {
            this.mListener.onGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                List<String> deniedPermissions = new ArrayList<>();
                //当所有拒绝的权限都勾选不再询问，这个值为true,这个时候可以引导用户手动去授权。
                boolean isNeverAsk = true;
                for (int i = 0; i < grantResults.length; i++) {
                    int grantResult = grantResults[i];
                    String permission = permissions[i];
                    if (grantResult == PackageManager.PERMISSION_DENIED) {
                        deniedPermissions.add(permissions[i]);
                        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) { // 点击拒绝但没有勾选不再询问
                            isNeverAsk = false;
                        }
                    }
                }
                if (deniedPermissions.isEmpty()) {
                    try {
                        mListener.onGranted();
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                        mListener.onDenied(Arrays.asList(permissions), true);
                    }
                } else {
                    //被拒绝权限
//                    for (String p:deniedPermissions){
//                        LogUtils.loge("Permissions  "+p);
//                    }
                    mListener.onDenied(deniedPermissions, isNeverAsk);
                }
                break;
            default:
                break;
        }
    }


}
